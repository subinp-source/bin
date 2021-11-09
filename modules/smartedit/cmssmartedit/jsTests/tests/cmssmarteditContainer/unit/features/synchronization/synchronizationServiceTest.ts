/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { DiscardableTimer, ISyncJob, SynchronizationService } from 'cmscommons';
import {
    CrossFrameEventService,
    IAlertService,
    IAuthenticationService,
    IRestService,
    RestServiceFactory,
    TimerService
} from 'smarteditcommons';
import { TranslateService } from '@ngx-translate/core';

describe('sync service  - unit test', () => {
    let synchronizationService: SynchronizationService;
    let crossFrameEventService: jasmine.SpyObj<CrossFrameEventService>;
    let authenticationService: jasmine.SpyObj<IAuthenticationService>;
    let timerService: jasmine.SpyObj<TimerService>;
    let restServiceFactory: jasmine.SpyObj<RestServiceFactory>;
    let restServiceForSync: jasmine.SpyObj<IRestService<ISyncJob>>;
    let alertService: jasmine.SpyObj<IAlertService>;
    let mockTimer: jasmine.SpyObj<DiscardableTimer>;
    let translateService: jasmine.SpyObj<TranslateService>;

    const theCatalog: ISyncJob = {
        catalogId: 'catalog',
        sourceCatalogVersion: 'sourceVersion',
        targetCatalogVersion: 'targetVersion'
    } as ISyncJob;

    const theCatalogGetStatus: ISyncJob = {
        startDate: '2016-02-12T16:08:29+0000',
        syncStatus: 'FINISHED'
    } as ISyncJob;
    const theCatalogUpdateStatus: ISyncJob = {
        startDate: '2016-02-12T17:09:29+0000',
        syncStatus: 'FINISHED'
    } as ISyncJob;

    const secondCatalog: ISyncJob = {
        catalogId: 'second catalog'
    } as ISyncJob;
    const secondCatalogGetStatus: ISyncJob = {
        startDate: '2016-04-01T12:00:00+0000',
        syncStatus: 'PENDING'
    } as ISyncJob;

    const theAbortedCatalog: ISyncJob = {
        catalogId: 'abortedCatalog',
        sourceCatalogVersion: 'sourceVersion',
        targetCatalogVersion: 'targetVersion'
    } as ISyncJob;

    const theAbortedCatalogStatus: ISyncJob = {
        startDate: '2016-02-12T16:08:29+0000',
        syncStatus: 'ABORTED'
    } as ISyncJob;

    const theAbortedCatalogUpdateStatus: ISyncJob = {
        startDate: '2016-02-12T17:09:29+0000',
        syncStatus: 'ABORTED'
    } as ISyncJob;

    beforeEach(() => {
        authenticationService = jasmine.createSpyObj('authenticationService', ['isAuthenticated']);
        authenticationService.isAuthenticated.and.callFake((url: string) => {
            const test = '/cmswebservices';
            if (url === test) {
                return Promise.resolve(true);
            } else {
                return Promise.resolve(false);
            }
        });

        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceForSync = jasmine.createSpyObj('searchRestService', ['get', 'save']);

        restServiceFactory.get.and.callFake(() => {
            return restServiceForSync;
        });

        restServiceForSync.save.and.callFake((value: { catalog: string; target: string }) => {
            if (value.catalog === 'catalog') {
                return Promise.resolve(theCatalogUpdateStatus);
            } else if (value.catalog === 'abortedCatalog') {
                return Promise.resolve(theAbortedCatalogUpdateStatus);
            }

            return Promise.resolve();
        });

        restServiceForSync.get.and.callFake((value: { catalog: string; target: string }) => {
            if (value.catalog === 'catalog') {
                return Promise.resolve(theCatalogGetStatus);
            } else if (value.catalog === 'second catalog') {
                return Promise.resolve(secondCatalogGetStatus);
            } else if (value.catalog === 'abortedCatalog') {
                return Promise.resolve(theAbortedCatalogStatus);
            }

            return Promise.resolve();
        });

        alertService = jasmine.createSpyObj('alertService', ['pushAlerts']);

        mockTimer = jasmine.createSpyObj('Timer', ['start', 'restart', 'stop']);

        timerService = jasmine.createSpyObj('timerService', ['createTimer']);
        timerService.createTimer.and.returnValue(mockTimer);

        crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', ['publish']);

        synchronizationService = new SynchronizationService(
            restServiceFactory,
            timerService,
            translateService,
            alertService,
            authenticationService,
            crossFrameEventService
        );

        translateService = jasmine.createSpyObj('translateService', ['instant']);
        translateService.instant.and.callFake((key: string) => `${key}_translated`);
    });

    it('should update sync status ', async () => {
        const response = await synchronizationService.updateCatalogSync(theCatalog);

        expect(response.startDate).toEqual('2016-02-12T17:09:29+0000');
        expect(response.syncStatus).toEqual('FINISHED');
    });

    it('should get catalog sync status', async () => {
        const response = await synchronizationService.getCatalogSyncStatus(theCatalog);

        expect(response.startDate).toEqual('2016-02-12T16:08:29+0000');
        expect(response.syncStatus).toEqual('FINISHED');
    });

    it('should call "get synchronization status" after interval.', async (done) => {
        const callback = jasmine.createSpy('callback');

        synchronizationService.startAutoGetSyncData(theCatalog, callback);
        expect(timerService.createTimer).toHaveBeenCalledWith(
            jasmine.any(Function),
            jasmine.any(Number)
        );

        const timerFn = timerService.createTimer.calls.argsFor(0)[0];
        await timerFn();

        expect(callback.calls.count()).toBe(1);

        await timerFn();

        expect(callback.calls.count()).toBe(2);

        done();
    });

    it('stopAutoGetSyncData should stop the timer', () => {
        // GIVEN
        const callback = jasmine.createSpy('callback');
        synchronizationService.startAutoGetSyncData(theCatalog, callback);
        expect(mockTimer.stop).not.toHaveBeenCalled();

        // WHEN
        synchronizationService.stopAutoGetSyncData(theCatalog);

        // THEN
        expect(mockTimer.stop).toHaveBeenCalled();
    });

    it('should stop calling "get sync update" on authentication failure', async () => {
        // GIVEN
        const callback = jasmine.createSpy('callback');

        authenticationService.isAuthenticated.and.callFake(() => {
            return Promise.resolve(false);
        });
        const stopAutoGetSyncData = spyOn(
            synchronizationService as any,
            'stopAutoGetSyncData'
        ).and.callThrough();

        synchronizationService.startAutoGetSyncData(secondCatalog, callback);
        const timerFn = timerService.createTimer.calls.argsFor(0)[0];

        await timerFn();

        expect(stopAutoGetSyncData).toHaveBeenCalled();
    });

    it('should continue calling "get sync update" on authentication success', async () => {
        const callback = jasmine.createSpy('callback').and.returnValue(Promise.resolve());

        const stopAutoGetSyncData = spyOn(
            synchronizationService as any,
            'stopAutoGetSyncData'
        ).and.callThrough();
        const getCatalogSyncStatus = spyOn(
            synchronizationService,
            'getCatalogSyncStatus'
        ).and.callThrough();

        synchronizationService.startAutoGetSyncData(theCatalog, callback);
        const timerFn = timerService.createTimer.calls.argsFor(0)[0];

        await timerFn();

        expect(stopAutoGetSyncData).not.toHaveBeenCalled();
        expect(getCatalogSyncStatus).toHaveBeenCalled();
    });

    it('updateCatalogSync should mark that synchronization is in progress', async () => {
        // GIVEN
        const jobKey = 'JOB_KEY';
        spyOn(synchronizationService as any, '_getJobKey').and.returnValue(jobKey);
        const addCatalogSyncRequest = spyOn(synchronizationService as any, 'addCatalogSyncRequest');

        // WHEN
        await synchronizationService.updateCatalogSync(theCatalog);

        // THEN
        expect(addCatalogSyncRequest).toHaveBeenCalledWith(jobKey);
    });

    it('GIVEN synchronization is in progress WHEN stopAutoGetSyncData is called THEN should only mark the timer as discardWhenNextSynced = true and the timer should not be stopped', async () => {
        // GIVEN
        const callback = jasmine.createSpy('callback');
        expect(mockTimer.discardWhenNextSynced).toEqual(undefined);

        await synchronizationService.updateCatalogSync(theCatalog);
        synchronizationService.startAutoGetSyncData(theCatalog, callback);

        // WHEN

        (synchronizationService as any).stopAutoGetSyncData(theCatalog);

        // THEN
        expect(mockTimer.stop).not.toHaveBeenCalled();
        expect(mockTimer.discardWhenNextSynced).toEqual(true);
    });

    it('GIVEN synchronization is in progress WHEN synchronization is finished THEN it sends SYNCHRONIZATION_EVENT.CATALOG_SYNCHRONIZED event AND removes job from "synchronization requested" array', async () => {
        // GIVEN
        const callback = jasmine.createSpy('callback').and.returnValue(Promise.resolve());
        const removeCatalogSyncRequest = spyOn(
            synchronizationService as any,
            'removeCatalogSyncRequest'
        );
        await synchronizationService.updateCatalogSync(theCatalog);
        synchronizationService.startAutoGetSyncData(theCatalog, callback);

        // WHEN
        const timerFn = timerService.createTimer.calls.argsFor(0)[0];
        await timerFn();

        // THEN
        expect(crossFrameEventService.publish).toHaveBeenCalledWith(
            'CATALOG_SYNCHRONIZED_EVENT',
            theCatalog
        );
        expect(removeCatalogSyncRequest).toHaveBeenCalled();
    });

    it('GIVEN synchronization is in progress WHEN synchronization is finished AND the job should be discarded THEN the timer is stopped AND the job is removed from "synchronization requested" array', async () => {
        // GIVEN
        const callback = jasmine.createSpy('callback').and.returnValue(Promise.resolve());
        const removeCatalogSyncRequest = spyOn(
            synchronizationService as any,
            'removeCatalogSyncRequest'
        );
        await synchronizationService.updateCatalogSync(theCatalog);
        synchronizationService.startAutoGetSyncData(theCatalog, callback);
        synchronizationService.stopAutoGetSyncData(theCatalog);

        // WHEN
        const timerFn = timerService.createTimer.calls.argsFor(0)[0];
        await timerFn();

        // THEN
        expect(mockTimer.stop).toHaveBeenCalled();
        expect(removeCatalogSyncRequest).toHaveBeenCalled();
    });

    it('GIVEN synchronization is in progress WHEN synchronization is aborted THEN the timer is stopped AND the job is removed from "synchronization requested" array', async () => {
        // GIVEN
        const callback = jasmine.createSpy('callback').and.returnValue(Promise.resolve());
        const removeCatalogSyncRequest = spyOn(
            synchronizationService as any,
            'removeCatalogSyncRequest'
        );
        await synchronizationService.updateCatalogSync(theAbortedCatalog);
        synchronizationService.startAutoGetSyncData(theAbortedCatalog, callback);
        synchronizationService.stopAutoGetSyncData(theAbortedCatalog);

        // WHEN
        const timerFn = timerService.createTimer.calls.argsFor(0)[0];
        await timerFn();

        // THEN
        expect(mockTimer.stop).toHaveBeenCalled();
        expect(removeCatalogSyncRequest).toHaveBeenCalled();
    });
});