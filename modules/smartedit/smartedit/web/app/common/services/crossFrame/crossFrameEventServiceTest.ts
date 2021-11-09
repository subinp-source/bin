/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    CrossFrameEventService,
    GatewayFactory,
    MessageGateway,
    SystemEventService,
    WindowUtils
} from 'smarteditcommons';

describe('crossFrameEventService', () => {
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let crossFrameEventService: CrossFrameEventService;
    let gatewayFactory: jasmine.SpyObj<GatewayFactory>;
    let gateway: jasmine.SpyObj<MessageGateway>;
    const eventId = 'eventId';
    const data = 'some data';
    const handler = () => {
        //
    };
    let windowUtils: jasmine.SpyObj<WindowUtils>;
    let targetIFrameValue = true;

    beforeEach(() => {
        systemEventService = jasmine.createSpyObj('systemEventService', [
            'publishAsync',
            'subscribe'
        ]);
        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['createGateway']);
        gateway = jasmine.createSpyObj('gateway', ['publish', 'subscribe']);
        gatewayFactory.createGateway.and.returnValue(gateway);

        windowUtils = jasmine.createSpyObj<WindowUtils>('windowUtils', ['getGatewayTargetFrame']);
        windowUtils.getGatewayTargetFrame.and.callFake(() => {
            return targetIFrameValue;
        });

        crossFrameEventService = new CrossFrameEventService(
            systemEventService,
            gateway,
            windowUtils
        );
    });

    it('publish will publish to the gatewayFactory and then send an event for a given event id and data', (done) => {
        targetIFrameValue = true;

        // GIVEN
        systemEventService.publishAsync.and.returnValue(Promise.resolve('systemEventService'));
        gateway.publish.and.returnValue(Promise.resolve('gateway'));

        // WHEN
        crossFrameEventService.publish(eventId, data).then((value) => {
            // THEN
            expect(systemEventService.publishAsync).toHaveBeenCalledWith(eventId, data);
            expect(gateway.publish).toHaveBeenCalledWith(eventId, data);
            expect(value).toEqual(['systemEventService', 'gateway']);
            done();
        });
    });

    it('publish with no targetIframe will send only resolve systemEventService', (done) => {
        targetIFrameValue = false;

        // GIVEN
        systemEventService.publishAsync.and.returnValue(Promise.resolve('systemEventService'));
        gateway.publish.and.returnValue(Promise.resolve('gateway'));

        // WHEN
        crossFrameEventService.publish(eventId, data).then((value) => {
            // THEN
            expect(systemEventService.publishAsync).toHaveBeenCalledWith(eventId, data);
            expect(gateway.publish).not.toHaveBeenCalledWith(eventId, data);
            expect(value).toEqual(['systemEventService']);
            done();
        });
    });

    it('subscribe will subscribe to the gatewayFactory and then register an event for the given event id and the provided handler', () => {
        targetIFrameValue = true;

        const unRegisterFnSpy = jasmine.createSpy('unRegisterFn');
        const gatewayUnsubscribeFn = jasmine.createSpy('gatewayUnsubscribeFn');

        // GIVEN
        systemEventService.subscribe.and.returnValue(unRegisterFnSpy);
        gateway.subscribe.and.returnValue(gatewayUnsubscribeFn);

        // WHEN
        const unSubscribeFn = crossFrameEventService.subscribe(eventId, handler);

        unSubscribeFn();

        // THEN
        expect(systemEventService.subscribe).toHaveBeenCalledWith(eventId, handler);
        expect(gateway.subscribe).toHaveBeenCalledWith(eventId, handler);
        expect(unRegisterFnSpy).toHaveBeenCalled();
        expect(gatewayUnsubscribeFn).toHaveBeenCalled();
    });
});
