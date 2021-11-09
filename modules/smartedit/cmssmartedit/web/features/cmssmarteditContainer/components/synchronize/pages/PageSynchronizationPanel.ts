/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import {
    CrossFrameEventService,
    IAlertServiceType,
    ISeComponent,
    IUriContext,
    SeComponent,
    TypedMap
} from 'smarteditcommons';
import { ICMSPage, IPageService, ISyncStatus } from 'cmscommons';
import { PageSynchronizationService } from 'cmssmarteditcontainer/dao/PageSynchronizationService';
import { HomepageService } from 'cmssmarteditcontainer/services';

interface PageSyncConditions {
    canSyncHomepage: boolean;
    pageHasUnavailableDependencies: boolean;
    pageHasSyncStatus: boolean;
    pageHasNoDepOrNoSyncStatus: boolean;
}

@SeComponent({
    templateUrl: 'pageSynchronizationPanelTemplate.html',
    inputs: [
        'syncItems:=?',
        'uriContext:?',
        'onSelectedItemsUpdate:?',
        'cmsPage:?',
        'showDetailedInfo:?'
    ]
})
export class PageSynchronizationPanel implements ISeComponent {
    public showSyncButton: boolean;
    public onSelectedItemsUpdate: any;
    public syncItems: any;
    public cmsPage: ICMSPage;
    public showDetailedInfo: any;

    private SYNCHRONIZATION_PAGE_SELECT_ALL_SLOTS_LABEL =
        'se.cms.synchronization.page.select.all.slots';
    private uriContext: IUriContext;

    private syncStatus: ISyncStatus = null;
    private synchronizationPanelApi: any;
    private pageSyncConditions: PageSyncConditions;

    private removeOnPageUpdateEvtHandler: () => any;

    constructor(
        private isBlank: (value: any) => boolean,
        private lodash: lo.LoDashStatic,
        private pageService: IPageService,
        private homepageService: HomepageService,
        private crossFrameEventService: CrossFrameEventService,
        private pageSynchronizationService: PageSynchronizationService,
        private PAGE_SYNC_STATUS_READY: string,
        private EVENTS: TypedMap<string>,
        private $q: angular.IQService,
        private $attrs: angular.IAttributes,
        private $translate: angular.translate.ITranslateService
    ) {
        this.pageSyncConditions = {
            canSyncHomepage: false,
            pageHasUnavailableDependencies: false,
            pageHasSyncStatus: false,
            pageHasNoDepOrNoSyncStatus: false
        };
    }

    public $onInit() {
        this.removeOnPageUpdateEvtHandler = this.crossFrameEventService.subscribe(
            this.EVENTS.PAGE_UPDATED,
            this.evaluateIfSyncIsApproved.bind(this)
        );
    }

    public $onDestroy() {
        this.removeOnPageUpdateEvtHandler();
    }

    public $postLink() {
        this.showSyncButton = this.isBlank(this.$attrs.syncItems);
    }

    public getApi($api: any): void {
        this.synchronizationPanelApi = $api;

        this.synchronizationPanelApi.disableItem = (item: any) => {
            return !this.pageSyncConditions.canSyncHomepage && item === this.syncStatus;
        };
    }

    public getSyncStatus = (): angular.IPromise<ISyncStatus> => {
        return this.$q
            .all([
                this.homepageService.canSyncHomepage(this.cmsPage, this.uriContext),
                this.pageSynchronizationService.getSyncStatus(this.cmsPage.uuid, this.uriContext),
                this.evaluateIfSyncIsApproved()
            ])
            .then((resolves: any[]) => {
                this.pageSyncConditions.canSyncHomepage = resolves[0];
                this.syncStatus = resolves[1];

                (this
                    .syncStatus as any).selectAll = this.SYNCHRONIZATION_PAGE_SELECT_ALL_SLOTS_LABEL;

                return this.syncStatus;
            });
    };

    public onSyncStatusReady = ($syncStatus: ISyncStatus): void => {
        this.pageSyncConditions.pageHasUnavailableDependencies =
            $syncStatus.unavailableDependencies.length > 0;
        this.pageSyncConditions.pageHasSyncStatus = !!$syncStatus.lastSyncStatus;
        this.pageSyncConditions.pageHasNoDepOrNoSyncStatus =
            this.pageSyncConditions.pageHasUnavailableDependencies ||
            !this.pageSyncConditions.pageHasSyncStatus;

        this.pageSyncConditions = this.lodash.cloneDeep(this.pageSyncConditions);

        if (this.pageSyncConditions.pageHasUnavailableDependencies) {
            this.hidePageSync();
        } else if (!this.pageSyncConditions.pageHasSyncStatus) {
            this.showPageSync();
        } else {
            this.enableSlotsSync();
        }

        this.crossFrameEventService.publish(this.PAGE_SYNC_STATUS_READY, this.pageSyncConditions);
    };

    public performSync = (items: TypedMap<string>[]): angular.IPromise<any> => {
        return this.pageSynchronizationService.performSync(items, this.uriContext);
    };

    private evaluateIfSyncIsApproved(): Promise<void> {
        return this.pageService
            .isPageApproved(this.cmsPage.uuid)
            .then((isPageApproved: boolean) => {
                if (!isPageApproved) {
                    this.disablePageSync();
                }
            });
    }

    private disablePageSync(): void {
        this.synchronizationPanelApi.setMessage({
            type: IAlertServiceType.WARNING,
            description: this.$translate.instant('se.cms.synchronization.slot.disabled.msg')
        });
        this.synchronizationPanelApi.disableItemList(true);
    }

    private hidePageSync(): void {
        this.synchronizationPanelApi.displayItemList(false);
    }

    // enable page sync only
    private showPageSync(): void {
        this.synchronizationPanelApi.selectAll();
        this.synchronizationPanelApi.displayItemList(false);
    }

    // enable slot/page sync
    private enableSlotsSync(): void {
        this.synchronizationPanelApi.displayItemList(true);
    }
}
