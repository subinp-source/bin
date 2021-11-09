/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TranslateService } from '@ngx-translate/core';

import {
    CrossFrameEventService,
    IAlertService,
    IAuthenticationService,
    IRestService,
    OperationContextRegistered,
    OPERATION_CONTEXT,
    RestServiceFactory,
    SeDowngradeService,
    Timer,
    TimerService,
    TypedMap
} from 'smarteditcommons';

import { DEFAULT_SYNCHRONIZATION_EVENT } from '../components/synchronize/synchronizationConstants';
import { ISyncJob } from '../dtos';

export const CATALOG_SYNC_INTERVAL_IN_MILLISECONDS = 5000;

export interface DiscardableTimer extends Timer {
    discardWhenNextSynced?: boolean;
}

@SeDowngradeService()
@OperationContextRegistered(
    '/cmswebservices/v1/catalogs/:catalog/synchronizations/targetversions/:target',
    OPERATION_CONTEXT.CMS
)
@OperationContextRegistered(
    '/cmswebservices/v1/catalogs/:catalog/versions/:source/synchronizations/versions/:target',
    OPERATION_CONTEXT.CMS
)
export class SynchronizationService {
    // Constants
    private readonly BASE_URL = '/cmswebservices';
    private readonly SYNC_JOB_INFO_BY_TARGET_URI =
        '/cmswebservices/v1/catalogs/:catalog/synchronizations/targetversions/:target';
    private readonly SYNC_JOB_INFO_BY_SOURCE_AND_TARGET_URI =
        '/cmswebservices/v1/catalogs/:catalog/versions/:source/synchronizations/versions/:target';

    private intervalHandle: TypedMap<DiscardableTimer> = {};
    private syncRequested: string[] = [];
    private syncJobInfoByTargetRestService: IRestService<ISyncJob> = this.restServiceFactory.get(
        this.SYNC_JOB_INFO_BY_TARGET_URI
    );
    private syncJobInfoBySourceAndTargetRestService: IRestService<
        ISyncJob
    > = this.restServiceFactory.get(this.SYNC_JOB_INFO_BY_SOURCE_AND_TARGET_URI, 'catalog');

    constructor(
        private restServiceFactory: RestServiceFactory,
        private timerService: TimerService,
        private translateService: TranslateService,
        private alertService: IAlertService,
        private authenticationService: IAuthenticationService,
        private crossFrameEventService: CrossFrameEventService
    ) {}

    /**
     * This method is used to synchronize a catalog between two catalog versions.
     * It sends the SYNCHRONIZATION_EVENT.CATALOG_SYNCHRONIZED event if successful.
     */

    public async updateCatalogSync(catalog: ISyncJob): Promise<ISyncJob> {
        const jobKey = this._getJobKey(
            catalog.catalogId,
            catalog.sourceCatalogVersion,
            catalog.targetCatalogVersion
        );
        this.addCatalogSyncRequest(jobKey);

        try {
            const response = await this.syncJobInfoBySourceAndTargetRestService.save({
                catalog: catalog.catalogId,
                source: catalog.sourceCatalogVersion,
                target: catalog.targetCatalogVersion
            });

            return response;
        } catch (reason) {
            const translationErrorMsg = this.translateService.instant('sync.running.error.msg', {
                catalogName: catalog.name
            });
            if (reason.statusText === 'Conflict') {
                this.alertService.showDanger({
                    message: translationErrorMsg
                });
            }

            return null;
        }
    }

    /**
     * This method is used to get the status of the last synchronization job between two catalog versions.
     */

    public getCatalogSyncStatus(catalog: ISyncJob): Promise<ISyncJob> {
        if (catalog.sourceCatalogVersion) {
            return this.getSyncJobInfoBySourceAndTarget(catalog);
        } else {
            return this.getLastSyncJobInfoByTarget(catalog);
        }
    }

    /**
     * This method is used to get the status of the last synchronization job between two catalog versions.
     */

    public getSyncJobInfoBySourceAndTarget(catalog: ISyncJob): Promise<ISyncJob> {
        return this.syncJobInfoBySourceAndTargetRestService.get({
            catalog: catalog.catalogId,
            source: catalog.sourceCatalogVersion,
            target: catalog.targetCatalogVersion
        });
    }

    /**
     * This method is used to get the status of the last synchronization job.
     */

    public getLastSyncJobInfoByTarget(catalog: ISyncJob): Promise<ISyncJob> {
        return this.syncJobInfoByTargetRestService.get({
            catalog: catalog.catalogId,
            target: catalog.targetCatalogVersion
        });
    }

    /**
     * This method starts the auto synchronization status update in a catalog between two given catalog versions.
     */

    public startAutoGetSyncData(catalog: ISyncJob, callback: (job: ISyncJob) => void): void {
        const { catalogId, sourceCatalogVersion, targetCatalogVersion } = catalog;
        const jobKey = this._getJobKey(catalogId, sourceCatalogVersion, targetCatalogVersion);

        const syncJobTimer = this.timerService.createTimer(
            () => this._autoSyncCallback(catalog, callback, jobKey),
            CATALOG_SYNC_INTERVAL_IN_MILLISECONDS
        );

        syncJobTimer.start();

        this.intervalHandle[jobKey] = syncJobTimer;
    }

    /**
     * This method stops the auto synchronization status update in a catalog between two given catalog versions
     * or it marks the job with discardWhenNextSynced = true if there is a synchronization in progress. If the job is
     * marked with discardWhenNextSynced = true then it will be discarded when the synchronization process is finished or aborted.
     */
    public stopAutoGetSyncData(catalog: ISyncJob): void {
        const jobKey = this._getJobKey(
            catalog.catalogId,
            catalog.sourceCatalogVersion,
            catalog.targetCatalogVersion
        );
        if (this.intervalHandle[jobKey]) {
            if (this.syncRequested.indexOf(jobKey) > -1) {
                this.intervalHandle[jobKey].discardWhenNextSynced = true;
            } else {
                this.intervalHandle[jobKey].stop();
                this.intervalHandle[jobKey] = undefined;
            }
        }
    }

    private async _autoSyncCallback(
        catalog: ISyncJob,
        callback: (job: ISyncJob) => void,
        jobKey: string
    ): Promise<void> {
        const response = await this.authenticationService.isAuthenticated(this.BASE_URL);

        if (!response) {
            this.stopAutoGetSyncData(catalog);
        }

        const syncStatus = await this.getCatalogSyncStatus(catalog);
        const syncJob = this.syncRequestedCallback(catalog)(syncStatus);
        callback(syncJob);
        if (!this.intervalHandle[jobKey]) {
            this.startAutoGetSyncData(catalog, callback);
        }
    }
    /**
     * Method sends SYNCHRONIZATION_EVENT.CATALOG_SYNCHRONIZED event when synchronization process is finished.
     * It also stops polling if the job is not needed anymore (i.e. was marked with discardWhenNextSynced = true).
     */
    private syncRequestedCallback(catalog: ISyncJob): (job: ISyncJob) => ISyncJob {
        const jobKey = this._getJobKey(
            catalog.catalogId,
            catalog.sourceCatalogVersion,
            catalog.targetCatalogVersion
        );
        return (response: ISyncJob) => {
            if (this.catalogSyncInProgress(jobKey)) {
                if (this.catalogSyncFinished(response)) {
                    this.removeCatalogSyncRequest(jobKey);

                    this.crossFrameEventService.publish(
                        DEFAULT_SYNCHRONIZATION_EVENT.CATALOG_SYNCHRONIZED,
                        catalog
                    );
                }

                if (
                    (this.intervalHandle[jobKey].discardWhenNextSynced &&
                        this.catalogSyncFinished(response)) ||
                    this.catalogSyncAborted(response)
                ) {
                    this.intervalHandle[jobKey].stop();
                    this.intervalHandle[jobKey] = undefined;
                    this.removeCatalogSyncRequest(jobKey);
                }
            }
            return response;
        };
    }

    private catalogSyncInProgress(jobKey: string): boolean {
        return this.syncRequested.indexOf(jobKey) > -1;
    }

    private catalogSyncFinished(response: ISyncJob): boolean {
        return response.syncStatus === 'FINISHED';
    }

    private catalogSyncAborted(response: ISyncJob): boolean {
        return response.syncStatus === 'ABORTED';
    }

    private removeCatalogSyncRequest(jobKey: string): void {
        const index = this.syncRequested.indexOf(jobKey);

        if (index > -1) {
            this.syncRequested.splice(index, 1);
        }
    }

    private addCatalogSyncRequest(jobKey: string): void {
        if (this.syncRequested.indexOf(jobKey) === -1) {
            this.syncRequested.push(jobKey);
        }
    }

    private _getJobKey(
        catalogId: string,
        sourceCatalogVersion: string,
        targetCatalogVersion: string
    ): string {
        return catalogId + '_' + sourceCatalogVersion + '_' + targetCatalogVersion;
    }
}
