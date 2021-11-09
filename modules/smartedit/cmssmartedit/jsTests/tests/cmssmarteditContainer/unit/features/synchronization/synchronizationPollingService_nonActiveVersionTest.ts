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
    IUriContext,
    StringUtils,
    SystemEventService,
    WindowUtils
} from 'smarteditcommons';
import { SyncPollingService } from 'cmssmarteditcontainer/services/SyncPollingServiceOuter';
import { promiseHelper } from 'testhelpers';
import { DEFAULT_SYNCHRONIZATION_POLLING } from 'cmscommons';

describe('Synchronization polling service with content catalog non active version - ', () => {
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
    let pageSynchronizationGetRestService: jasmine.SpyObj<IRestService<any>>;
    let pageSynchronizationPostRestService: jasmine.SpyObj<IRestService<any>>;
    const isBlank: jasmine.Spy = jasmine.createSpy('isBlank');
    let $log: jasmine.SpyObj<angular.ILogService>;
    let windowUtils: jasmine.SpyObj<WindowUtils>;
    let stringUtils: jasmine.SpyObj<StringUtils>;
    let targetFrame: Window;

    const EVENTS = {
        EXPERIENCE_UPDATE: 'EXPERIENCE_UPDATE',
        PAGE_CHANGE: 'PAGE_CHANGE',
        PAGE_UPDATED: 'PAGE_UPDATED'
    };

    const SYNCHRONIZATION_EVENT = {
        CATALOG_SYNCHRONIZED: 'CATALOG_SYNCHRONIZED_EVENT'
    };

    const MOCK_PAGE_ID_1 = 'pageId1';

    const OVERLAY_RERENDERED_EVENT = 'mockedOverlayRerenderedEvent';

    const SynchronizationMockData = window.test.unit.mockData.synchronization;
    const pageId1SyncStatus = new SynchronizationMockData().PAGE_ID1_SYNC_STATUS;
    const pageId2SyncStatus = new SynchronizationMockData().PAGE_ID2_SYNC_STATUS;

    const SYNCHRONIZATION_SLOW_POLLING_TIME = DEFAULT_SYNCHRONIZATION_POLLING.SLOW_POLLING_TIME;
    const SYNCHRONIZATION_FAST_POLLING_TIME = DEFAULT_SYNCHRONIZATION_POLLING.FAST_POLLING_TIME;
    const SYNC_POLLING_SPEED_UP = 'syncPollingSpeedUp';
    const SYNC_POLLING_SLOW_DOWN = 'syncPollingSlowDown';

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
                    active: false
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
        systemEventService.subscribe.calls.reset();

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
            'getPageSynchronizationGetRestService',
            'getPageSynchronizationPostRestService'
        ]);
        pageSynchronizationGetRestService = jasmine.createSpyObj<IRestService<any>>(
            'pageSynchronizationGetRestService',
            ['get']
        );
        pageSynchronizationPostRestService = jasmine.createSpyObj<IRestService<any>>(
            'pageSynchronizationPostRestService',
            ['save']
        );
        synchronizationResource.getPageSynchronizationGetRestService.and.returnValue(
            pageSynchronizationGetRestService
        );
        synchronizationResource.getPageSynchronizationPostRestService.and.returnValue(
            pageSynchronizationPostRestService
        );

        windowUtils = jasmine.createSpyObj<WindowUtils>('WindowUtils', ['getGatewayTargetFrame']);
        targetFrame = jasmine.createSpyObj<Window>('targetFrame', ['postMessage']);
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

    it('initSyncPolling will be called on service initialization and will set default values, register event handlers and start timer', () => {
        // GIVEN
        systemEventService.subscribe.and.returnValue({});

        // THEN
        expect((syncPollingService as any).refreshInterval).toBe(SYNCHRONIZATION_SLOW_POLLING_TIME);
        expect((syncPollingService as any).triggers).toEqual([]);
        expect((syncPollingService as any).syncStatus).toEqual({});

        expect(systemEventService.subscribe.calls.count()).toEqual(2);
        expect(systemEventService.subscribe.calls.argsFor(0)[0]).toEqual(SYNC_POLLING_SPEED_UP);
        expect(systemEventService.subscribe.calls.argsFor(1)[0]).toEqual(SYNC_POLLING_SLOW_DOWN);

        expect(timer.restart).toHaveBeenCalledWith(SYNCHRONIZATION_SLOW_POLLING_TIME);
    });

    it('when syncStatus in the scope is empty then getSyncStatus will fetch the sync status by making a rest call and set it to the scope object ', (done) => {
        // GIVEN
        pageInfoService.getPageUUID.and.returnValue($q.when(MOCK_PAGE_ID_1));
        pageSynchronizationGetRestService.get.and.returnValue($q.when(pageId1SyncStatus));

        // WHEN
        syncPollingService.getSyncStatus(MOCK_PAGE_ID_1).then((result) => {
            // THEN
            expect(result).toEqual(pageId1SyncStatus);
            expect((syncPollingService as any).syncStatus.pageId1).toEqual(pageId1SyncStatus);

            done();
        });
    });

    it('when syncStatus object is not empty syncStatus but has an unmatched name, then getSyncStatus will fetch the sync status by making a rest call and reset the syncStatus scope object', (done) => {
        // GIVEN
        pageInfoService.getPageUUID.and.returnValue($q.when(MOCK_PAGE_ID_1));
        pageSynchronizationGetRestService.get.and.returnValue($q.when(pageId1SyncStatus));
        (syncPollingService as any).syncStatus.pageId2 = pageId2SyncStatus;

        // WHEN
        syncPollingService.getSyncStatus(MOCK_PAGE_ID_1).then((result) => {
            // THEN
            expect(result).toEqual(pageId1SyncStatus);

            expect(pageSynchronizationGetRestService.get.calls.count()).toBe(1);
            expect(pageSynchronizationGetRestService.get).toHaveBeenCalledWith({
                target: 'Online',
                pageUid: MOCK_PAGE_ID_1
            });
            expect((syncPollingService as any).syncStatus.pageId1).toEqual(pageId1SyncStatus);

            done();
        });
    });

    it('when syncStatus object is not empty syncStatus and matches the name then getSyncStatus with directly return the promise of the syncStatus object', (done) => {
        // GIVEN
        pageInfoService.getPageUUID.and.returnValue($q.when(MOCK_PAGE_ID_1));
        pageSynchronizationGetRestService.get.and.returnValue($q.when(pageId1SyncStatus));
        (syncPollingService as any).syncStatus.pageId1 = pageId1SyncStatus;

        // WHEN
        syncPollingService.getSyncStatus(MOCK_PAGE_ID_1).then((result) => {
            // THEN
            expect(result).toEqual(pageId1SyncStatus);
            expect(pageSynchronizationGetRestService.get).not.toHaveBeenCalled();

            done();
        });
    });

    it('when getSyncStatus is called multiple times, timer does not restart more than once', function(done) {
        // GIVEN
        pageInfoService.getPageUUID.and.returnValue($q.when(MOCK_PAGE_ID_1));
        pageSynchronizationGetRestService.get.and.returnValue($q.when(pageId1SyncStatus));
        windowUtils.getGatewayTargetFrame.and.returnValue(null);
        timer.restart.calls.reset();

        // WHEN
        syncPollingService.getSyncStatus(MOCK_PAGE_ID_1).then((result1) => {
            syncPollingService.getSyncStatus(MOCK_PAGE_ID_1).then((result2) => {
                syncPollingService.getSyncStatus(MOCK_PAGE_ID_1).then((result3) => {
                    // THEN
                    expect(timer.restart).toHaveBeenCalledWith(SYNCHRONIZATION_SLOW_POLLING_TIME);
                    expect(timer.restart.calls.count()).toBe(1);

                    done();
                });
            });
        });
    });

    describe('fetchSyncStatus', () => {
        const PAGE_1_UUID = MOCK_PAGE_ID_1;

        beforeEach(() => {
            pageInfoService.getPageUUID.and.returnValue($q.when(PAGE_1_UUID));
            pageSynchronizationGetRestService.get.and.returnValue($q.when(pageId1SyncStatus));
        });

        it('fetchSyncStatus will fetch the sync status by making a rest call and reset the syncStatus scope object', () => {
            // WHEN
            const promise = syncPollingService._fetchSyncStatus();

            // THEN
            expect(promise).toBeResolvedWithData(pageId1SyncStatus);
            expect(pageSynchronizationGetRestService.get).toHaveBeenCalled();
            expect((syncPollingService as any).syncStatus.pageId1).toEqual(pageId1SyncStatus);
        });

        it('GIVEN new sync status is the same as before WHEN fetchSyncStatus is called THEN no event will be published', () => {
            // GIVEN
            (syncPollingService as any).syncStatus.pageId1 = pageId1SyncStatus;

            // WHEN
            syncPollingService._fetchSyncStatus();

            // THEN
            expect(crossFrameEventService.publish).not.toHaveBeenCalled();
        });

        it('GIVEN new sync status WHEN fetchSyncStatus is called THEN it will publish a FAST_FETCH event', () => {
            // GIVEN
            (syncPollingService as any).syncStatus.pageId1 = pageId2SyncStatus;

            // WHEN
            syncPollingService._fetchSyncStatus();

            // THEN
            expect(crossFrameEventService.publish).toHaveBeenCalledWith(
                DEFAULT_SYNCHRONIZATION_POLLING.FAST_FETCH,
                pageId1SyncStatus
            );
        });

        it('GIVEN first poll WHEN fetchSyncStatus is called THEN it will publish a PAGE_UPDATED event', () => {
            // WHEN
            syncPollingService._fetchSyncStatus();

            // THEN
            expect(crossFrameEventService.publish).toHaveBeenCalledWith(
                DEFAULT_SYNCHRONIZATION_POLLING.FAST_FETCH,
                pageId1SyncStatus
            );
            expect(crossFrameEventService.publish).toHaveBeenCalledWith(EVENTS.PAGE_UPDATED, {
                uuid: MOCK_PAGE_ID_1
            });
        });

        it('GIVEN page has changed from last time it was synched WHEN fetchSyncStatus is called THEN it will publish a PAGE_UPDATED event', () => {
            // GIVEN
            pageId1SyncStatus.lastModifiedDate = 'some date';
            pageId2SyncStatus.lastModifiedDate = 'other date';
            (syncPollingService as any).syncStatus.pageId1 = pageId2SyncStatus;

            // WHEN
            syncPollingService._fetchSyncStatus();

            // THEN
            expect(crossFrameEventService.publish).toHaveBeenCalledWith(
                DEFAULT_SYNCHRONIZATION_POLLING.FAST_FETCH,
                pageId1SyncStatus
            );
            expect(crossFrameEventService.publish).toHaveBeenCalledWith(EVENTS.PAGE_UPDATED, {
                uuid: MOCK_PAGE_ID_1
            });
        });

        it('GIVEN page sync status has changed from last time it was synched WHEN fetchSyncStatus is called THEN it will publish a PAGE_UPDATED event', () => {
            // GIVEN
            pageId1SyncStatus.status = 'some status';
            pageId2SyncStatus.status = 'some other status';
            (syncPollingService as any).syncStatus.pageId1 = pageId2SyncStatus;

            // WHEN
            syncPollingService._fetchSyncStatus();

            // THEN
            expect(crossFrameEventService.publish).toHaveBeenCalledWith(
                DEFAULT_SYNCHRONIZATION_POLLING.FAST_FETCH,
                pageId1SyncStatus
            );
            expect(crossFrameEventService.publish).toHaveBeenCalledWith(EVENTS.PAGE_UPDATED, {
                uuid: MOCK_PAGE_ID_1
            });
        });

        it('GIVEN page information did not change from last time it was synched WHEN fetchSyncStatus is called THEN no page updated event is triggered', () => {
            // GIVEN
            pageId1SyncStatus.lastModifiedDate = 'some date';
            pageId2SyncStatus.lastModifiedDate = 'some date';

            pageId1SyncStatus.status = 'some status';
            pageId2SyncStatus.status = 'some status';
            (syncPollingService as any).syncStatus.pageId1 = pageId2SyncStatus;

            // WHEN
            syncPollingService._fetchSyncStatus();

            // THEN
            expect(crossFrameEventService.publish).toHaveBeenCalledWith(
                DEFAULT_SYNCHRONIZATION_POLLING.FAST_FETCH,
                pageId1SyncStatus
            );
            expect(crossFrameEventService.publish).not.toHaveBeenCalledWith(EVENTS.PAGE_UPDATED, {
                uuid: MOCK_PAGE_ID_1
            });
        });

        it('when no page id is available then fetchSyncStatus will return an empty object', () => {
            // GIVEN
            pageInfoService.getPageUUID.and.returnValue($q.when(null));
            pageSynchronizationGetRestService.get.and.returnValue($q.when(pageId1SyncStatus));

            // WHEN
            const promise = syncPollingService._fetchSyncStatus();

            // THEN
            expect(promise).toBeResolvedWithData({});
            expect(pageSynchronizationGetRestService.get).not.toHaveBeenCalled();
            expect((syncPollingService as any).syncStatus).toEqual({});
        });
    });

    it('when changePollingSpeed is called with syncPollingSpeedUp then the item is added to the triggers array and refreshInterval is set to speed up interval', () => {
        // GIVEN
        pageSynchronizationGetRestService.get.and.returnValue($q.when(pageId1SyncStatus));

        // WHEN
        syncPollingService.changePollingSpeed(SYNC_POLLING_SPEED_UP, 'slot1');

        // THEN
        expect((syncPollingService as any).triggers).toEqual(['slot1']);
        expect((syncPollingService as any).refreshInterval).toBe(SYNCHRONIZATION_FAST_POLLING_TIME);
        expect(timer.restart).toHaveBeenCalledWith(SYNCHRONIZATION_FAST_POLLING_TIME);

        // WHEN
        syncPollingService.changePollingSpeed(SYNC_POLLING_SPEED_UP, 'slot2');

        // THEN
        expect((syncPollingService as any).triggers).toEqual(['slot1', 'slot2']);
        expect((syncPollingService as any).refreshInterval).toBe(SYNCHRONIZATION_FAST_POLLING_TIME);
        expect(timer.restart).toHaveBeenCalledWith(SYNCHRONIZATION_FAST_POLLING_TIME);
    });

    it('when changePollingSpeed is called with syncPollingSlowDown then the item is removed from the triggers array and refreshInterval is set to slow down interval if the array is empty', () => {
        // GIVEN
        pageSynchronizationGetRestService.get.and.returnValue($q.when(pageId1SyncStatus));

        syncPollingService.changePollingSpeed(SYNC_POLLING_SPEED_UP, 'slot1');
        expect((syncPollingService as any).triggers).toEqual(['slot1']);

        // WHEN
        syncPollingService.changePollingSpeed(SYNC_POLLING_SLOW_DOWN, 'slot1');

        // THEN
        expect((syncPollingService as any).triggers).toEqual([]);
        expect((syncPollingService as any).refreshInterval).toBe(SYNCHRONIZATION_SLOW_POLLING_TIME);
        expect(timer.restart).toHaveBeenCalledWith(SYNCHRONIZATION_SLOW_POLLING_TIME);
    });

    it('when changePollingSpeed is called with syncPollingSlowDown then the item is removed from the triggers array and refreshInterval is unaltered if the array is not empty', () => {
        // GIVEN
        pageSynchronizationGetRestService.get.and.returnValue($q.when(pageId1SyncStatus));

        syncPollingService.changePollingSpeed(SYNC_POLLING_SPEED_UP, 'slot1');
        syncPollingService.changePollingSpeed(SYNC_POLLING_SPEED_UP, 'slot2');
        expect((syncPollingService as any).triggers).toEqual(['slot1', 'slot2']);

        // WHEN
        syncPollingService.changePollingSpeed(SYNC_POLLING_SLOW_DOWN, 'slot1');

        // THEN
        expect((syncPollingService as any).triggers).toEqual(['slot2']);
        expect((syncPollingService as any).refreshInterval).toBe(SYNCHRONIZATION_FAST_POLLING_TIME);
        expect(timer.restart).toHaveBeenCalledWith(SYNCHRONIZATION_FAST_POLLING_TIME);
    });

    it('will listen to OVERLAY_RERENDERED_EVENT events and proceed to one fetch', () => {
        const status = {
            a: 'b'
        };
        spyOn(syncPollingService, '_fetchSyncStatus').and.returnValue($q.when(status));

        expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
            'mockedOverlayRerenderedEvent',
            jasmine.any(Function)
        );

        const callback = crossFrameEventService.subscribe.calls.argsFor(0)[1];

        callback();
        expect(syncPollingService._fetchSyncStatus).toHaveBeenCalled();
    });

    it('performSync will use activeVersion in REST call', () => {
        // GIVEN
        const uriContext = {} as IUriContext;
        const array = [
            {
                a: 'b'
            }
        ];
        catalogService.getContentCatalogActiveVersion.and.returnValue($q.when('mockedOnline'));

        // WHEN
        const promise = syncPollingService.performSync(array, uriContext);

        // THEN
        expect(promise).toBeResolved();

        expect(pageSynchronizationPostRestService.save).toHaveBeenCalledWith({
            target: 'mockedOnline',
            items: array
        });
        expect(catalogService.getContentCatalogActiveVersion).toHaveBeenCalledWith(uriContext);
    });

    it('will listen to EVENTS.PAGE_CHANGE events and clear syncStatus', () => {
        // WHEN
        expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
            'PAGE_CHANGE',
            jasmine.any(Function)
        );

        const callback = crossFrameEventService.subscribe.calls.argsFor(2)[1];

        callback();

        // THEN
        expect((syncPollingService as any).syncStatus).toEqual({});
    });

    it('will listen to SYNCHRONIZATION_EVENT.CATALOG_SYNCHRONIZED events and clear the syncStatus cache.', () => {
        // GIVEN
        const callback = crossFrameEventService.subscribe.calls.argsFor(3)[1];
        spyOn(syncPollingService, '_fetchSyncStatus');

        // WHEN
        callback();

        // THEN
        expect((syncPollingService as any).syncStatus).toEqual({});
        expect(syncPollingService._fetchSyncStatus).toHaveBeenCalled();
    });
});
