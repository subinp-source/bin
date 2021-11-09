/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { TranslateService } from '@ngx-translate/core';

import {
    stringUtils,
    CrossFrameEventService,
    EVENT_CONTENT_CATALOG_UPDATE,
    IAlertService,
    ICatalogService,
    IExperience,
    ISharedDataService,
    IWaitDialogService,
    L10nFilterFn,
    LogService,
    SeComponent,
    SystemEventService,
    Timer,
    TimerService,
    TypedMap
} from 'smarteditcommons';

import { DEFAULT_SYNCHRONIZATION_POLLING, SynchronizationStatus } from './synchronizationConstants';
import { ISynchronizationPanelApi, ISyncStatusItem } from './interfaces';

@SeComponent({
    templateUrl: 'synchronizationPanelTemplate.html',
    inputs: [
        'itemId',
        'getSyncStatus',
        'performSync',
        'headerTemplateUrl',
        'syncItems:=?',
        'onSelectedItemsUpdate',
        'onSyncStatusReady:&?',
        'getApi:&?'
    ]
})
export class SynchronizationPanelComponent {
    public isLoading = false;
    public syncItems: () => void;
    public headerTemplateUrl: string;
    public message: { type: string; description: string } = null;
    public showItemList = true;
    public showSyncButton: boolean;

    private disableList = false;
    private itemId: string;
    private onSelectedItemsUpdate: (items: ISyncStatusItem[]) => void;
    private onSyncStatusReady: (event: { $syncStatus: ISyncStatusItem }) => void;
    private getApi: (api: { $api: ISynchronizationPanelApi }) => void;
    private performSync: (payload: { itemId: string; itemType: string }[]) => Promise<any>;
    private resynchTimer: Timer;
    private syncPendingItems: string[] = [];
    private selectedItems: ISyncStatusItem[] = [];
    private refreshInterval = DEFAULT_SYNCHRONIZATION_POLLING.SLOW_POLLING_TIME;
    private syncStatus: any;
    private getSyncStatus: (id: string) => Promise<ISyncStatusItem>;
    private unsubscribeFastFetch: () => void;

    /**
     * @ngdoc object
     * @name synchronizationPanelModule.object:api
     * @description
     * The synchronization panel api object exposing public functionality
     */
    private api: ISynchronizationPanelApi = {
        /**
         * @ngdoc method
         * @name selectAll
         * @methodOf synchronizationPanelModule.object:api
         * @description
         * Function that selects all items on synchronization panel. Should be used with onSyncStatusReady event.
         */
        selectAll: () => {
            if (this.syncStatus) {
                this.syncStatus.selected = true;
                this.selectionChange(0);
            } else {
                throw new Error(
                    "Synchronization status is not available. The 'selectAll' function should be used with 'onSyncStatusReady' event."
                );
            }
        },

        /**
         * @ngdoc method
         * @name displayItemList
         * @methodOf synchronizationPanelModule.object:api
         * @description
         * Function that changes the visibility of the item list.
         */
        displayItemList: (visible: boolean) => {
            this.showItemList = visible;
        },

        /**
         * @ngdoc method
         * @name disableItemList
         * @methodOf synchronizationPanelModule.object:api
         * @description
         * Function that determines if the panel, as a whole, should be disabled.
         *
         */
        disableItemList: (disableList: boolean) => {
            this.disableList = disableList;
        },

        /**
         * @ngdoc method
         * @name setMessage
         * @methodOf synchronizationPanelModule.object:api
         * @description
         * Function that adds a message (yMessage) in the panel.
         *
         */
        setMessage: (msgConfig: { type: string; description: string }) => {
            this.message = msgConfig;
        },

        /**
         * @ngdoc method
         * @name disableItem
         * @methodOf synchronizationPanelModule.object:api
         * @description
         * Function that determines if an item should be disabled.
         */
        disableItem: null
    };

    constructor(
        private logService: LogService,
        private $attrs: angular.IAttributes,
        private translateService: TranslateService,
        private alertService: IAlertService,
        private systemEventService: SystemEventService,
        private timerService: TimerService,
        private crossFrameEventService: CrossFrameEventService,
        private catalogService: ICatalogService,
        private l10nFilter: L10nFilterFn,
        private sharedDataService: ISharedDataService,
        private waitDialogService: IWaitDialogService
    ) {}

    $onInit() {
        this.syncItems = () => {
            const syncPayload = this.getRows()
                .filter((item) => item.selected)
                .map(({ itemId, itemType }) => ({
                    itemId,
                    itemType
                }));

            Array.prototype.push.apply(
                this.syncPendingItems,
                syncPayload.map((el) => el.itemId)
            );

            if (this.selectedItems.length) {
                this.waitDialogService.showWaitModal('se.sync.synchronizing');
                return this.performSync(syncPayload).then(
                    () => {
                        this.refreshInterval = DEFAULT_SYNCHRONIZATION_POLLING.FAST_POLLING_TIME;
                        this.resynchTimer.restart(this.refreshInterval);
                        this.systemEventService.publishAsync(
                            DEFAULT_SYNCHRONIZATION_POLLING.SPEED_UP,
                            this.itemId
                        );
                    },
                    () => {
                        this.logService.warn(
                            '[synchronizationPanel] - Could not perform synchronization.'
                        );
                        this.waitDialogService.hideWaitModal();
                    }
                );
            } else {
                return Promise.resolve();
            }
        };

        if (typeof this.getApi === 'function') {
            this.getApi({
                $api: this.api
            });
        }

        this.unsubscribeFastFetch = this.crossFrameEventService.subscribe(
            DEFAULT_SYNCHRONIZATION_POLLING.FAST_FETCH,
            (id, data) => this.fetchSyncStatus(id, (data as unknown) as ISyncStatusItem)
        );
        this.fetchSyncStatus();

        // start timer polling
        this.resynchTimer = this.timerService.createTimer(
            () => this.fetchSyncStatus(),
            this.refreshInterval
        );

        this.resynchTimer.start();
    }

    $onDestroy() {
        this.resynchTimer.teardown();
        this.unsubscribeFastFetch();
        this.systemEventService.publishAsync(
            DEFAULT_SYNCHRONIZATION_POLLING.SLOW_DOWN,
            this.itemId
        );
    }

    $postLink() {
        this.showSyncButton = stringUtils.isBlank(this.$attrs.syncItems);
    }

    public getRows(): ISyncStatusItem[] {
        return this.syncStatus ? [this.syncStatus, ...this.syncStatus.selectedDependencies] : [];
    }

    public selectionChange(index?: number): void {
        this.syncStatus.selectedDependencies.forEach((item: ISyncStatusItem) => {
            item.selected =
                index === 0 && item.status !== SynchronizationStatus.InSync && !item.isExternal
                    ? true
                    : item.selected;
        });

        this.selectedItems = this.getRows().filter((item: ISyncStatusItem) => {
            return item.selected;
        });

        if (this.onSelectedItemsUpdate) {
            this.onSelectedItemsUpdate(this.selectedItems);
        }
    }

    public preserveSelection(selectedItems: ISyncStatusItem[]): void {
        const itemIds = (selectedItems || []).map((item) => {
            return item.itemId;
        }, []);

        this.getRows().forEach((item) => {
            item.selected =
                itemIds.indexOf(item.itemId) > -1 && item.status !== SynchronizationStatus.InSync;
        });
    }

    public isDisabled(item: ISyncStatusItem): boolean {
        if (this.disableList || (this.api.disableItem && this.api.disableItem(item))) {
            return true;
        }

        return (
            (item !== this.syncStatus && this.syncStatus.selected) ||
            item.status === SynchronizationStatus.InSync
        );
    }

    public isSyncButtonDisabled(): boolean {
        return (
            this.disableList ||
            this.selectedItems.length === 0 ||
            this.syncPendingItems.length !== 0
        );
    }

    public isInSync(dependency: ISyncStatusItem): boolean {
        return dependency.status === SynchronizationStatus.InSync;
    }
    public isOutOfSync(dependency: ISyncStatusItem): boolean {
        return dependency.status === SynchronizationStatus.NotSync;
    }
    public isInProgress(dependency: ISyncStatusItem): boolean {
        return dependency.status === SynchronizationStatus.InProgress;
    }
    public isSyncFailed(dependency: ISyncStatusItem): boolean {
        return dependency.status === SynchronizationStatus.SyncFailed;
    }
    public hasHelp(dependency: ISyncStatusItem): boolean {
        return (
            dependency.dependentItemTypesOutOfSync &&
            dependency.dependentItemTypesOutOfSync.length > 0
        );
    }

    public buildInfoTemplate(dependency: ISyncStatusItem): string {
        if (dependency && dependency.dependentItemTypesOutOfSync && !dependency.isExternal) {
            const infoTemplate = dependency.dependentItemTypesOutOfSync.reduce(
                (accumulator: string, item: TypedMap<string>) => {
                    accumulator += '<div>' + this.translateService.instant(item.i18nKey) + '</div>';
                    return accumulator;
                },
                ''
            );
            return infoTemplate ? '<div>' + infoTemplate + '</div>' : '';
        } else if (dependency.isExternal) {
            return dependency.catalogVersionNameTemplate;
        }

        return '';
    }

    async fetchSyncStatus(eventId?: string, eventData?: ISyncStatusItem): Promise<void> {
        if (eventData && eventData.itemId !== this.itemId) {
            return;
        }

        this.isLoading = true;

        const syncStatus = await this.getSyncStatus(this.itemId);
        const experience = (await this.sharedDataService.get('experience')) as IExperience;

        const targetCatalogVersion = syncStatus.catalogVersionUuid;
        this.syncStatus = syncStatus;
        this.preserveSelection(this.selectedItems);
        this.selectionChange();
        this._updateStatus();
        this.markExternalComponents(targetCatalogVersion, this.getRows());
        this.setTemplateExternalCatalogVersionName(experience, this.getRows());

        if (this.syncPendingItems.length === 0) {
            this.refreshInterval = DEFAULT_SYNCHRONIZATION_POLLING.SLOW_POLLING_TIME;
            this.resynchTimer.restart(this.refreshInterval);
            this.systemEventService.publishAsync(
                DEFAULT_SYNCHRONIZATION_POLLING.SLOW_DOWN,
                this.itemId
            );
        }

        if (typeof this.onSyncStatusReady === 'function') {
            this.onSyncStatusReady({
                $syncStatus: syncStatus
            });
        }

        this.isLoading = false;
    }

    public markExternalComponents(
        targetCatalogVersion: string,
        syncStatuses: ISyncStatusItem[]
    ): void {
        syncStatuses.forEach((syncStatus) => {
            syncStatus.isExternal = syncStatus.catalogVersionUuid !== targetCatalogVersion;
        });
    }

    public getInfoTitle(dependency: ISyncStatusItem): string {
        if (!dependency.isExternal) {
            return 'se.cms.synchronization.panel.update.title';
        }

        return '';
    }

    public getTemplateInfoForExternalComponent(): string {
        return `<div>${this.translateService.instant(
            'se.cms.synchronization.slot.external.component'
        )}</div>`;
    }

    public async setTemplateExternalCatalogVersionName(
        experience: IExperience,
        syncStatuses: ISyncStatusItem[]
    ): Promise<void> {
        for (const syncStatus of syncStatuses) {
            syncStatus.catalogVersionNameTemplate = '<span></span>';

            const catalogVersion = await this.catalogService.getCatalogVersionByUuid(
                syncStatus.catalogVersionUuid,
                experience.siteDescriptor.uid
            );

            syncStatus.catalogVersionNameTemplate =
                '<div>' + this.l10nFilter(catalogVersion.catalogName) + '</div>';
        }
    }

    private _updateStatus(): void {
        let anyFailures = false;
        let itemsInErrors = '';
        let preNbSyncPendingItems = this.syncPendingItems.length;

        this.getRows().forEach((item) => {
            const idx = this.syncPendingItems.indexOf(item.itemId);

            if (idx > -1 && item.status === SynchronizationStatus.SyncFailed) {
                itemsInErrors = ' ' + itemsInErrors + item.itemId;
                anyFailures = true;
            }
            if (
                idx > -1 &&
                item.status !== SynchronizationStatus.InProgress &&
                item.status !== SynchronizationStatus.NotSync
            ) {
                this.syncPendingItems.splice(idx, 1);
            }

            // if there was at least one item in the sync queue, and the last item has been resolved
            // or if there wasn't any item in the sync queue
            const syncQueueCompleted = preNbSyncPendingItems && this.syncPendingItems.length === 0;
            if (syncQueueCompleted || preNbSyncPendingItems === 0) {
                if (syncQueueCompleted) {
                    this.systemEventService.publishAsync(EVENT_CONTENT_CATALOG_UPDATE);
                    preNbSyncPendingItems = 0;
                }
                this.waitDialogService.hideWaitModal();
            }
            if (
                item.status === SynchronizationStatus.InProgress &&
                this.syncPendingItems.indexOf(item.itemId) === -1
            ) {
                this.syncPendingItems.push(item.itemId);
            }
        });

        if (anyFailures) {
            this.alertService.showDanger({
                message: this.translateService.instant(
                    'se.cms.synchronization.panel.failure.message',
                    {
                        items: itemsInErrors
                    }
                )
            });
        }
    }
}
