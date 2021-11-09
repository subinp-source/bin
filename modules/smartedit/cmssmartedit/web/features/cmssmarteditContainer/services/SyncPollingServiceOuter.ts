/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    CrossFrameEventService,
    GatewayProxied,
    InvalidateCache,
    ICatalogService,
    IExperienceService,
    IPageInfoService,
    IUriContext,
    SeInjectable,
    StringUtils,
    SystemEventService,
    TimerService,
    TypedMap,
    WindowUtils
} from 'smarteditcommons';
import { ISyncStatus } from 'cmscommons/dtos/ISyncStatus';
import { cmsitemsEvictionTag } from 'cmscommons/dao/cmswebservices/sites/CmsitemsRestService';
import * as lo from 'lodash';
import { from, Observable } from 'rxjs';
import { share } from 'rxjs/internal/operators';

@GatewayProxied(
    'getSyncStatus',
    '_fetchSyncStatus',
    'changePollingSpeed',
    'registerSyncPollingEvents',
    'performSync'
)
@SeInjectable()
export class SyncPollingService {
    public SYNC_POLLING_THROTTLE: number = 500;

    private syncStatus: TypedMap<ISyncStatus> = {};
    private triggers: any[] = [];
    private syncPollingTimer: any = null;
    private refreshInterval: number = null;
    private syncPageObservableMap = new Map<string, Observable<any>>();

    constructor(
        private $q: angular.IQService,
        private $log: angular.ILogService,
        private isBlank: any,
        private pageInfoService: IPageInfoService,
        private experienceService: IExperienceService,
        private catalogService: ICatalogService,
        private synchronizationResource: any,
        private crossFrameEventService: CrossFrameEventService,
        private SYNCHRONIZATION_POLLING: TypedMap<any>,
        private systemEventService: SystemEventService,
        private OVERLAY_RERENDERED_EVENT: string,
        private lodash: lo.LoDashStatic,
        private EVENTS: TypedMap<string>,
        private SYNCHRONIZATION_EVENT: TypedMap<string>,
        private timerService: TimerService,
        private windowUtils: WindowUtils,
        private stringUtils: StringUtils
    ) {
        this.initSyncPolling();
    }

    getSyncStatus(
        pageUUID?: string,
        uriContext?: IUriContext,
        forceGetSynchronization?: boolean
    ): angular.IPromise<ISyncStatus> {
        forceGetSynchronization =
            forceGetSynchronization === true ? forceGetSynchronization : false;

        if (forceGetSynchronization) {
            this.clearSyncPageObservableMap();
        }

        if (
            this.syncStatus[pageUUID] &&
            pageUUID === this.syncStatus[pageUUID].itemId &&
            !forceGetSynchronization
        ) {
            return this.$q.when(this.syncStatus[pageUUID]);
        } else {
            return this.getPageUUID(pageUUID).then(
                (_pageUUID: string) => {
                    return this._fetchSyncStatus(_pageUUID, uriContext).then(
                        (syncStatus: ISyncStatus) => {
                            const syncPollingType = this.getSyncPollingTypeFromInterval(
                                this.refreshInterval
                            );
                            this.startSync(syncPollingType);
                            return syncStatus;
                        },
                        () => {
                            this.$log.error(
                                'syncPollingService::getSyncStatus - failed call to _fetchSyncStatus'
                            );
                            return this.$q.reject();
                        }
                    );
                },
                (e: any) => {
                    this.$log.error(
                        'syncPollingService::getSyncStatus - failed call to getPageUUID'
                    );
                    this.syncPollingTimer.stop();
                    return this._fetchSyncStatus(pageUUID, uriContext);
                }
            );
        }
    }

    _fetchSyncStatus(_pageUUID?: string, uriContext?: IUriContext): angular.IPromise<ISyncStatus> {
        return this.getPageUUID(_pageUUID).then(
            (pageUUID: string) => {
                if (pageUUID) {
                    return this._isCurrentPageFromActiveCatalog().then(
                        (currentPageFromActiveCatalog: boolean) => {
                            if (!currentPageFromActiveCatalog) {
                                const promise = this.catalogService
                                    .getContentCatalogActiveVersion(uriContext)
                                    .then((activeVersion: string) => {
                                        const uniqueKeyForPage = this.stringUtils.encode(pageUUID);
                                        const syncPageObservable = this.syncPageObservableMap.get(
                                            uniqueKeyForPage
                                        );
                                        // Re-use pre-existing Observable to avoid concurrent HTTP call to the same synchronization url.
                                        return syncPageObservable
                                            ? syncPageObservable.toPromise<any>()
                                            : this._fetchPageSynchronization(
                                                  activeVersion,
                                                  pageUUID,
                                                  uriContext
                                              ).toPromise<any>();
                                    });
                                return this.$q.when(promise);
                            } else {
                                return this.$q.reject();
                            }
                        }
                    );
                } else {
                    return this.$q.when({});
                }
            },
            () => {
                this.stopSync();
                return this.$q.reject();
            }
        );
    }

    changePollingSpeed(eventId: string, itemId?: string): void {
        if (eventId === this.SYNCHRONIZATION_POLLING.SPEED_UP) {
            this.syncStatus = {};
            if (itemId && this.triggers.indexOf(itemId) === -1) {
                this.triggers.push(itemId);
            }

            this.refreshInterval = this.SYNCHRONIZATION_POLLING.FAST_POLLING_TIME;
        } else {
            if (itemId) {
                this.triggers.splice(this.triggers.indexOf(itemId), 1);
            }
            if (this.triggers.length === 0) {
                this.refreshInterval = this.SYNCHRONIZATION_POLLING.SLOW_POLLING_TIME;
            }
        }

        this.syncPollingTimer.restart(this.refreshInterval);
    }

    @InvalidateCache(cmsitemsEvictionTag)
    performSync(array: any[], uriContext: IUriContext): angular.IPromise<any> {
        return this._isCurrentPageFromActiveCatalog().then(
            (currentPageFromActiveCatalog: boolean) => {
                if (!currentPageFromActiveCatalog) {
                    return this.catalogService
                        .getContentCatalogActiveVersion(uriContext)
                        .then((activeVersion: string) => {
                            return this.synchronizationResource
                                .getPageSynchronizationPostRestService(uriContext)
                                .save({
                                    target: activeVersion,
                                    items: array
                                });
                        });
                } else {
                    return this.$q.reject();
                }
            }
        );
    }

    private _fetchPageSynchronization(
        activeVersion: string,
        pageUUID: string,
        uriContext: IUriContext
    ): Observable<any> {
        const syncObservable = from(
            this.synchronizationResource
                .getPageSynchronizationGetRestService(uriContext)
                .get({
                    pageUid: pageUUID,
                    target: activeVersion
                })
                .then((syncStatus: ISyncStatus) => {
                    const lastSyncStatus: ISyncStatus = this.syncStatus[syncStatus.itemId];
                    if (JSON.stringify(syncStatus) !== JSON.stringify(lastSyncStatus)) {
                        this.crossFrameEventService.publish(
                            this.SYNCHRONIZATION_POLLING.FAST_FETCH,
                            syncStatus
                        );

                        if (
                            !lastSyncStatus ||
                            syncStatus.lastModifiedDate !== lastSyncStatus.lastModifiedDate ||
                            syncStatus.status !== lastSyncStatus.status
                        ) {
                            this.crossFrameEventService.publish('CMSITEMS_UPDATE');
                            this.crossFrameEventService.publish(this.EVENTS.PAGE_UPDATED, {
                                uuid: pageUUID
                            });
                        }
                    }
                    this.syncStatus[syncStatus.itemId] = syncStatus;

                    this.clearSyncPageObservableMap();

                    return syncStatus;
                })
        );

        const uniqueKeyForPage = this.stringUtils.encode(pageUUID);
        this.syncPageObservableMap.set(uniqueKeyForPage, syncObservable);

        return syncObservable.pipe(share());
    }

    private stopSync(): void {
        if (this.syncPollingTimer.isActive()) {
            this.syncPollingTimer.stop();
        }
        this.clearSyncStatus();
    }

    private startSync(syncPollingType?: string) {
        if (!this.syncPollingTimer.isActive()) {
            this.changePollingSpeed(syncPollingType || this.SYNCHRONIZATION_POLLING.SLOW_DOWN);
        }
    }

    private initSyncPolling() {
        this.refreshInterval = this.SYNCHRONIZATION_POLLING.SLOW_POLLING_TIME;
        /**
         * When multiple items needs sync polling at different paces (either slow or fast),
         * triggers array makes sure to set to fast polling even if any one of them needs fast polling.
         */
        this.triggers = [];
        this.syncStatus = {};

        const changePolling = this.changePollingSpeed.bind(this);

        this.systemEventService.subscribe(this.SYNCHRONIZATION_POLLING.SPEED_UP, changePolling);
        this.systemEventService.subscribe(this.SYNCHRONIZATION_POLLING.SLOW_DOWN, changePolling);

        this.crossFrameEventService.subscribe(
            this.SYNCHRONIZATION_POLLING.FETCH_SYNC_STATUS_ONCE,
            (eventId: string, pageUUID: string) => {
                this._fetchSyncStatus.bind(this)(pageUUID);
            }
        );

        this.crossFrameEventService.subscribe(
            this.OVERLAY_RERENDERED_EVENT,
            this.lodash.throttle(() => {
                if (this.syncPollingTimer.isActive()) {
                    this._fetchSyncStatus.bind(this)();
                }
            }, this.SYNC_POLLING_THROTTLE)
        );

        this.crossFrameEventService.subscribe(this.EVENTS.PAGE_CHANGE, () => {
            this.clearSyncStatus();
            this.clearSyncPageObservableMap();
        });

        this.crossFrameEventService.subscribe(
            this.SYNCHRONIZATION_EVENT.CATALOG_SYNCHRONIZED,
            () => {
                this.syncStatus = {};
                this._fetchSyncStatus.bind(this)();
            }
        );

        // _fetchSyncStatus callback uses current page uuid by default
        this.syncPollingTimer = this.timerService.createTimer(
            this._fetchSyncStatus.bind(this),
            this.refreshInterval
        );

        // start sync polling if it is a storefront page
        if (this.windowUtils.getGatewayTargetFrame()) {
            const syncPollingType = this.getSyncPollingTypeFromInterval(this.refreshInterval);
            this.startSync(syncPollingType);
        }
    }

    private clearSyncPageObservableMap() {
        this.syncPageObservableMap.clear();
    }

    private clearSyncStatus(): void {
        this.syncStatus = {};
    }

    private getPageUUID(_pageUUID: string): angular.IPromise<string> {
        return !this.isBlank(_pageUUID)
            ? this.$q.when(_pageUUID)
            : this.$q.when(this.pageInfoService.getPageUUID());
    }

    private _isCurrentPageFromActiveCatalog() {
        const promise = this.experienceService.getCurrentExperience().then((currentExperience) => {
            return currentExperience.pageContext
                ? currentExperience.pageContext.active
                : currentExperience.catalogDescriptor.active;
        });

        return this.$q.when(promise);
    }

    private getSyncPollingTypeFromInterval(interval: number): string {
        return interval === this.SYNCHRONIZATION_POLLING.FAST_POLLING_TIME
            ? this.SYNCHRONIZATION_POLLING.SPEED_UP
            : this.SYNCHRONIZATION_POLLING.SLOW_DOWN;
    }
}
