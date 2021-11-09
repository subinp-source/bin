/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import 'jasmine';
import { TranslateService } from '@ngx-translate/core';

import {
    AngularJSLazyDependenciesService,
    CrossFrameEventService,
    GatewayFactory,
    ISharedDataService,
    MessageGateway,
    WindowUtils
} from 'smarteditcommons';

import { HeartBeatService } from 'smarteditcontainer/services/HeartBeatService';
import { AlertFactory } from 'smarteditcontainer/services/alerts/AlertFactory';
import { getSpyMock, AlertMock } from './mocks/AlertMock';
import { GenericEventer } from './mocks/GenericEventer';
import { mockAngularJSLazyDependenciesService } from '../../../../smartedit-build/test/unit';

describe('Storefront <-> SmartEdit Heart beat service', () => {
    const mockMessageGateway = new GenericEventer();
    const mockRootScopeService = new GenericEventer();
    const mockCrossFrameEventService = new GenericEventer();
    let mockLocation: jasmine.SpyObj<angular.ILocationService>;
    let mockAlertFactory: jasmine.SpyObj<AlertFactory>;
    const storefrontPath = '/storefront';
    let mockDisconnectedAlert: AlertMock;
    let mockReconnectedAlert: AlertMock;
    const MOCK_EVENTS = {
        PAGE_CHANGE: 'PAGE_CHANGE',
        EVENT_STRICT_PREVIEW_MODE_REQUESTED: 'EVENT_STRICT_PREVIEW_MODE_REQUESTED'
    };
    let windowUtils: WindowUtils;
    let heartBeatService: HeartBeatService;
    let angularJSLazyDependenciesService: jasmine.SpyObj<AngularJSLazyDependenciesService>;
    let sharedDataService: jasmine.SpyObj<ISharedDataService>;

    function createHeartBeatServiceInstance(heartbeatTimeout: number = 10000) {
        windowUtils = new WindowUtils();
        spyOn(windowUtils, 'runTimeoutOutsideAngular').and.callFake(
            (callback: () => void, timeout: number) => {
                return setTimeout(callback, timeout);
            }
        );

        mockLocation = jasmine.createSpyObj('$location', ['path']);
        mockLocation.path.and.callFake(() => 'dummypath');

        angularJSLazyDependenciesService = mockAngularJSLazyDependenciesService();
        angularJSLazyDependenciesService.$location.and.returnValue(mockLocation);
        angularJSLazyDependenciesService.$rootScope.and.returnValue(mockRootScopeService);

        // $location

        // Alerts
        mockDisconnectedAlert = getSpyMock();
        mockReconnectedAlert = getSpyMock();
        mockAlertFactory = jasmine.createSpyObj('alertFactory', ['createInfo']);

        mockAlertFactory.createInfo.and.returnValues(mockDisconnectedAlert, mockReconnectedAlert);

        // Translate
        const translateMock: jasmine.SpyObj<TranslateService> = jasmine.createSpyObj('$translate', [
            'instant'
        ]);
        translateMock.instant.and.callFake(() => 'some string');

        // Gateway
        const gatewayFactoryMock: jasmine.SpyObj<GatewayFactory> = jasmine.createSpyObj(
            'gatewayFactory',
            ['createGateway']
        );
        gatewayFactoryMock.createGateway.and.returnValue(
            (mockMessageGateway as any) as MessageGateway
        );

        mockCrossFrameEventService.subscribe(
            MOCK_EVENTS.EVENT_STRICT_PREVIEW_MODE_REQUESTED,
            angular.noop
        );

        sharedDataService = jasmine.createSpyObj<ISharedDataService>('sharedDataService', ['get']);

        sharedDataService.get.and.returnValue(Promise.resolve({}));

        heartBeatService = new HeartBeatService(
            heartbeatTimeout,
            translateMock,
            angularJSLazyDependenciesService,
            windowUtils,
            mockAlertFactory,
            (mockCrossFrameEventService as any) as CrossFrameEventService,
            gatewayFactoryMock,
            sharedDataService
        );
    }

    beforeEach(() => {
        jasmine.clock().install();
    });

    afterEach(() => {
        // just to make sure its all cleaned up on window
        heartBeatService.resetAndStop();
    });

    it('On $routeChangeStart will reset and hide all alerts', () => {
        // Given
        createHeartBeatServiceInstance();

        // When
        mockRootScopeService.publish('$routeChangeStart');

        // Then
        expect(mockDisconnectedAlert.shown).toBe(false);
        expect(mockReconnectedAlert.shown).toBe(false);
    });

    it('On timerElapsed reconnecting is shown, then heartBeat event will show reconnected', async () => {
        // Given
        createHeartBeatServiceInstance(20);
        spyOn(window, 'setTimeout').and.callThrough();
        spyOn(mockCrossFrameEventService, 'publish').and.callThrough();
        spyOn(mockCrossFrameEventService, 'subscribe');

        // Then
        expect(mockDisconnectedAlert.shown).toBe(false);
        expect(mockReconnectedAlert.shown).toBe(false);

        // When
        mockCrossFrameEventService.publish(MOCK_EVENTS.PAGE_CHANGE); // trigger very short timer
        await (mockCrossFrameEventService as any).subscribe.calls.argsFor(0)[1];

        jasmine.clock().tick(20);

        // Then disconnected will be shown
        expect(window.setTimeout).toHaveBeenCalled();
        expect(mockDisconnectedAlert.shown).toBe(true);
        expect(mockReconnectedAlert.shown).toBe(false);

        // When - finaly we receive a heartbeat...
        mockMessageGateway.publish(HeartBeatService.HEART_BEAT_MSG_ID);
        await (mockCrossFrameEventService as any).subscribe.calls.argsFor(1)[1];

        // Then disconnected should be hidden and reconnected displayed
        expect(mockDisconnectedAlert.shown).toBe(false);
        expect(mockReconnectedAlert.shown).toBe(true);
        expect(mockCrossFrameEventService.publish).toHaveBeenCalledWith(
            MOCK_EVENTS.EVENT_STRICT_PREVIEW_MODE_REQUESTED,
            false
        );

        // When - reconnected time elapses...
        jasmine.clock().tick(3000);

        // Then
        expect(mockReconnectedAlert.shown).toBe(false);
        expect(window.setTimeout).toHaveBeenCalled();
    });

    it('On $routeChangeSuccess will not start heartbeat timer for non-storefront path', () => {
        // Given
        // note: mockLocation will return default test value of "dummy path"
        createHeartBeatServiceInstance();
        spyOn(window, 'setTimeout');

        // When
        mockRootScopeService.publish('$routeChangeSuccess');

        // Then
        expect(window.setTimeout).not.toHaveBeenCalled();
    });

    it('On $routeChangeSuccess will start heartbeat timer for storefront path', async () => {
        // Given
        createHeartBeatServiceInstance();
        mockLocation.path.and.callFake(() => storefrontPath);
        spyOn(window, 'setTimeout');
        spyOn(mockCrossFrameEventService, 'subscribe');

        // When
        mockRootScopeService.publish('$routeChangeSuccess');
        await (mockCrossFrameEventService as any).subscribe.calls.argsFor(0)[1];

        // Then
        expect(mockDisconnectedAlert.shown).toBe(false);
        expect(mockReconnectedAlert.shown).toBe(false);
        expect(window.setTimeout).toHaveBeenCalled();
    });

    it('On PAGE_CHANGE it will trigger a restart of the heartbeat timer', async () => {
        // Given
        createHeartBeatServiceInstance();
        spyOn(window, 'setTimeout');
        spyOn(mockCrossFrameEventService, 'subscribe');

        // When
        mockCrossFrameEventService.publish(MOCK_EVENTS.PAGE_CHANGE);
        await (mockCrossFrameEventService as any).subscribe.calls.argsFor(0)[1];

        // Then
        expect(mockDisconnectedAlert.shown).toBe(false);
        expect(mockReconnectedAlert.shown).toBe(false);
        expect(window.setTimeout).toHaveBeenCalled();
    });

    it('On Page_Change it will trigger the heartbeat timer with a custom heartBeatTimeoutThreshold provided by the configuration', async () => {
        // Given
        createHeartBeatServiceInstance();
        spyOn(window, 'setTimeout');
        spyOn(mockCrossFrameEventService, 'subscribe');
        sharedDataService.get.and.returnValue(
            Promise.resolve({
                heartBeatTimeoutThreshold: 12000
            })
        );

        // When
        mockCrossFrameEventService.publish(MOCK_EVENTS.PAGE_CHANGE);
        await (mockCrossFrameEventService as any).subscribe.calls.argsFor(0)[1];

        // Then
        expect(window.setTimeout).toHaveBeenCalled();
        expect((window.setTimeout as any).calls.argsFor(0)[1]).toBe(12000);
    });
});
