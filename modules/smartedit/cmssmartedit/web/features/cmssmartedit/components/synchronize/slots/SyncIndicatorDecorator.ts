/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject } from '@angular/core';
import {
    AbstractDecorator,
    CrossFrameEventService,
    ICatalogService,
    IPageInfoService,
    SeDecorator,
    TypedMap
} from 'smarteditcommons';

@SeDecorator()
@Component({
    selector: 'sync-indicator',
    templateUrl: './SyncIndicatorDecoratorTemplate.html'
})
export class SyncIndicatorDecorator extends AbstractDecorator {
    public pageUUID: string;
    public syncStatus: TypedMap<string>;
    public isVersionNonActive = false;

    private unRegisterSyncPolling: () => void;

    constructor(
        @Inject('$q') private $q: angular.IQService,
        private catalogService: ICatalogService,
        @Inject('slotSynchronizationService') private slotSynchronizationService: any,
        private crossFrameEventService: CrossFrameEventService,
        @Inject('pageInfoService') private pageInfoService: IPageInfoService,
        @Inject('SYNCHRONIZATION_STATUSES') private SYNCHRONIZATION_STATUSES: TypedMap<string>,
        @Inject('SYNCHRONIZATION_POLLING') private SYNCHRONIZATION_POLLING: TypedMap<string>
    ) {
        super();
    }

    ngOnInit() {
        // initial sync status is set to unavailable until the first fetch
        this.syncStatus = {
            status: this.SYNCHRONIZATION_STATUSES.UNAVAILABLE
        };

        this.pageInfoService.getPageUUID().then((pageUUID: string) => {
            this.pageUUID = pageUUID;
            this.unRegisterSyncPolling = this.crossFrameEventService.subscribe(
                this.SYNCHRONIZATION_POLLING.FAST_FETCH,
                this.fetchSyncStatus.bind(this)
            );

            this.catalogService.isContentCatalogVersionNonActive().then((isNonActive) => {
                this.isVersionNonActive = isNonActive;
                if (this.isVersionNonActive) {
                    this.fetchSyncStatus();
                }
            });
        });
    }

    ngOnDestroy() {
        if (this.unRegisterSyncPolling) {
            this.unRegisterSyncPolling();
        }
    }

    fetchSyncStatus() {
        return this.isVersionNonActive
            ? this.slotSynchronizationService
                  .getSyncStatus(this.pageUUID, this.componentAttributes.smarteditComponentId)
                  .then(
                      (response: TypedMap<string>) => {
                          this.syncStatus = response;
                      },
                      () => {
                          this.syncStatus.status = this.SYNCHRONIZATION_STATUSES.UNAVAILABLE;
                      }
                  )
            : this.$q.when();
    }
}
