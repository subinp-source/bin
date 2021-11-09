/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import {
    CrossFrameEventService,
    ICatalogService,
    IExperienceService,
    IPageInfoService,
    IRestService,
    StringUtils,
    SystemEventService,
    WindowUtils
} from 'smarteditcommons';
import { SyncPollingService } from 'cmssmarteditcontainer/services/SyncPollingServiceOuter';
import { promiseHelper } from 'testhelpers';
import { DEFAULT_SYNCHRONIZATION_POLLING } from 'cmscommons';

describe('Synchronization polling service with content catalog active version - ', () => {
    let syncPollingService: SyncPollingService;
    let $q: jasmine.SpyObj<angular.IQService>;
    let experienceService: jasmine.SpyObj<IExperienceService>;
    let crossFrameEventService: jasmine.SpyObj<CrossFrameEventService>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let pageInfoService: jasmine.SpyObj<IPageInfoService>;
    let catalogService: jasmine.SpyObj<ICatalogService>;
    let timerService: jasmine.SpyObj<any>;
    let timer: jasmine.SpyObj<any>;
    let synchronizationResource: jasmine.SpyObj<any>;
    let restService: jasmine.SpyObj<IRestService<any>>;
    const isBlank: jasmine.Spy = jasmine.createSpy('isBlank');
    let $log: jasmine.SpyObj<angular.ILogService>;
    let windowUtils: jasmine.SpyObj<WindowUtils>;
    let stringUtils: jasmine.SpyObj<StringUtils>;
    let targetFrame: Window;

    const EVENTS = {
        EXPERIENCE_UPDATE: 'EXPERIENCE_UPDATE',
        PAGE_CHANGE: 'PAGE_CHANGE'
    };

    const SYNCHRONIZATION_EVENT = {
        CATALOG_SYNCHRONIZED: 'CATALOG_SYNCHRONIZED_EVENT'
    };

    const OVERLAY_RERENDERED_EVENT = 'mockedOverlayRerenderedEvent';

    const SynchronizationMockData = window.test.unit.mockData.synchronization;
    const pageId1SyncStatus = new SynchronizationMockData().PAGE_ID1_SYNC_STATUS;
    const pageId2SyncStatus = new SynchronizationMockData().PAGE_ID2_SYNC_STATUS;

    beforeEach(() => {
        isBlank.and.callFake((value: string) => {
            return (
                typeof value === 'undefined' ||
                value === null ||
                value === 'null' ||
                value.toString().trim().length === 0
            );
        });

        $q = promiseHelper.$q();

        $log = jasmine.createSpyObj<angular.ILogService>('$log', ['info', 'error']);

        experienceService = jasmine.createSpyObj<IExperienceService>('experienceService', [
            'getCurrentExperience'
        ]);
        experienceService.getCurrentExperience.and.returnValue(
            $q.when({
                pageContext: {
                    active: true
                }
            })
        );
        crossFrameEventService = jasmine.createSpyObj<CrossFrameEventService>(
            'crossFrameEventService',
            ['publish', 'subscribe']
        );
        systemEventService = jasmine.createSpyObj<SystemEventService>('systemEventService', [
            'publish',
            'subscribe'
        ]);

        pageInfoService = jasmine.createSpyObj<IPageInfoService>('pageInfoService', [
            'getPageUUID'
        ]);

        catalogService = jasmine.createSpyObj<ICatalogService>('catalogService', [
            'getContentCatalogActiveVersion'
        ]);
        catalogService.getContentCatalogActiveVersion.and.returnValue($q.when('Online'));

        timerService = jasmine.createSpyObj('timerService', ['createTimer']);
        timer = jasmine.createSpyObj('Timer', ['start', 'restart', 'stop', 'isActive']);

        timerService.createTimer.and.returnValue(timer);

        synchronizationResource = jasmine.createSpyObj('synchronizationResource', [
            'getPageSynchronizationGetRestService'
        ]);
        restService = jasmine.createSpyObj<IRestService<any>>('restService', ['get']);
        synchronizationResource.getPageSynchronizationGetRestService.and.returnValue(restService);

        targetFrame = jasmine.createSpyObj<Window>('targetFrame', ['postMessage']);
        windowUtils = jasmine.createSpyObj<WindowUtils>('WindowUtils', ['getGatewayTargetFrame']);
        windowUtils.getGatewayTargetFrame.and.returnValue(targetFrame);

        stringUtils = jasmine.createSpyObj<StringUtils>('StringUtils', ['encode']);

        syncPollingService = new SyncPollingService(
            $q,
            $log,
            isBlank,
            pageInfoService,
            experienceService,
            catalogService,
            synchronizationResource,
            crossFrameEventService,
            DEFAULT_SYNCHRONIZATION_POLLING,
            systemEventService,
            OVERLAY_RERENDERED_EVENT,
            lo,
            EVENTS,
            SYNCHRONIZATION_EVENT,
            timerService,
            windowUtils,
            stringUtils
        );
    });

    it('getSyncStatus will reject, not proceed to rest call and leave the syncStatus unchanged', () => {
        // GIVEN
        (syncPollingService as any).syncStatus = pageId2SyncStatus;
        pageInfoService.getPageUUID.and.returnValue($q.when('pageId1'));
        restService.get.and.returnValue($q.when(pageId1SyncStatus));
        // WHEN
        const promise = syncPollingService.getSyncStatus('pageId1');

        // THEN
        expect(promise).toBeRejected();
        expect(restService.get).not.toHaveBeenCalled();
        expect((syncPollingService as any).syncStatus).toBe(pageId2SyncStatus);
    });

    it('fetchSyncStatus will reject, not proceed to rest call and leave the syncStatus unchanged', () => {
        // GIVEN
        (syncPollingService as any).syncStatus = pageId2SyncStatus;
        pageInfoService.getPageUUID.and.returnValue($q.when('pageId1'));
        restService.get.and.returnValue($q.when(pageId1SyncStatus));

        // WHEN
        const promise = syncPollingService._fetchSyncStatus();

        // THEN
        expect(promise).toBeRejected();
        expect(restService.get).not.toHaveBeenCalled();
        expect((syncPollingService as any).syncStatus).toEqual(pageId2SyncStatus);
    });

    it('startSync call without pollingType should restart the timer with SLOW_POLLING_TIME by default', () => {
        timer.isActive.and.returnValue(false);

        (syncPollingService as any).startSync();

        expect(timer.restart).toHaveBeenCalledWith(
            DEFAULT_SYNCHRONIZATION_POLLING.SLOW_POLLING_TIME
        );
    });

    it('startSync call with DEFAULT_SYNCHRONIZATION_POLLING.FAST_POLLING_TIME should restart the timer with FAST_POLLING_TIME', () => {
        timer.isActive.and.returnValue(false);

        (syncPollingService as any).startSync(DEFAULT_SYNCHRONIZATION_POLLING.SPEED_UP);

        expect(timer.restart).toHaveBeenCalledWith(
            DEFAULT_SYNCHRONIZATION_POLLING.FAST_POLLING_TIME
        );
    });

    it("startSync call should not restart the timer if it's active", () => {
        timer.isActive.and.returnValue(true);
        timer.restart.calls.reset();

        (syncPollingService as any).startSync();

        expect(timer.restart).not.toHaveBeenCalled();
    });

    it("stopSync should stop the timer when it's active", () => {
        timer.isActive.and.returnValue(true);

        (syncPollingService as any).stopSync();

        expect(timer.stop).toHaveBeenCalled();
    });

    it("stopSync should not stop the timer when it's not active", () => {
        timer.isActive.and.returnValue(false);

        (syncPollingService as any).stopSync();

        expect(timer.stop).not.toHaveBeenCalled();
    });
});
