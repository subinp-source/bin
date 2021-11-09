/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { annotationService, GatewayProxied, IBound } from 'smarteditcommons';
import { NotificationMouseLeaveDetectionService } from 'smartedit/services';

describe('notificationMouseLeaveDetectionService', () => {
    const DUMMY_X = 0;
    const DUMMY_Y = 0;
    const DUMMY_WIDTH = 100;
    const DUMMY_HEIGHT = 100;
    const DUMMY_BOUNDS: IBound = {
        x: DUMMY_X,
        y: DUMMY_Y,
        width: DUMMY_WIDTH,
        height: DUMMY_HEIGHT
    };
    const MOUSE_MOVE_EVENT = 'mousemove';

    const _document = document.implementation.createHTMLDocument();

    let notificationMouseLeaveDetectionService: NotificationMouseLeaveDetectionService;

    /*
     * This method prepares a mock for the Gateway Proxy. It is used to test that
     * the service properly initializes itself for proxying across the gateway.
     */
    beforeEach(() => {
        notificationMouseLeaveDetectionService = new NotificationMouseLeaveDetectionService(
            _document
        );
    });

    describe('initialization', () => {
        it('extends the INotificationMouseLeaveDetectionService', () => {
            expect(notificationMouseLeaveDetectionService.startDetection).toBeEmptyFunction();
            expect(notificationMouseLeaveDetectionService.stopDetection).toBeEmptyFunction();
            expect(
                (notificationMouseLeaveDetectionService as any)._callCallback
            ).toBeEmptyFunction();
        });

        it('checks GatewayProxied', () => {
            const decoratorObj = annotationService.getClassAnnotation(
                NotificationMouseLeaveDetectionService,
                GatewayProxied
            );
            expect(decoratorObj).toEqual([
                'stopDetection',
                '_remoteStartDetection',
                '_remoteStopDetection',
                '_callCallback'
            ]);
        });
    });

    describe('_remoteStartDetection', () => {
        it('registers a mouse move event listener on the local frame', async () => {
            const addEventListenerSpy = spyOn(_document, 'addEventListener');

            // When
            await (notificationMouseLeaveDetectionService as any)._remoteStartDetection(
                DUMMY_BOUNDS
            );

            // Then
            expect(addEventListenerSpy).toHaveBeenCalledWith(
                MOUSE_MOVE_EVENT,
                (notificationMouseLeaveDetectionService as any)._onMouseMove
            );
        });
    });

    describe('_remoteStopDetection', () => {
        it('un-registers the mouse move event listener on the local frame', async () => {
            const removeEventListenerSpy = spyOn(_document, 'removeEventListener');

            // When
            await (notificationMouseLeaveDetectionService as any)._remoteStopDetection();

            // Then
            expect(removeEventListenerSpy).toHaveBeenCalledWith(
                MOUSE_MOVE_EVENT,
                (notificationMouseLeaveDetectionService as any)._onMouseMove
            );
        });

        it('resets the notification panel bounds that were stored', async () => {
            // Given
            await notificationMouseLeaveDetectionService.startDetection(
                DUMMY_BOUNDS,
                null,
                function() {
                    /**/
                }
            );

            // When
            await notificationMouseLeaveDetectionService.stopDetection();
            const bounds = (await (notificationMouseLeaveDetectionService as any)._getBounds()) as IBound;

            // Then
            expect(bounds).toBeFalsy();
        });
    });

    describe('_getBounds', () => {
        it('returns the bounds that were given when detection was started', async () => {
            // Given
            await (notificationMouseLeaveDetectionService as any)._remoteStartDetection(
                DUMMY_BOUNDS
            );

            // When
            const bounds = (await (notificationMouseLeaveDetectionService as any)._getBounds()) as IBound;

            // Then
            expect(bounds).toEqual(DUMMY_BOUNDS);
        });
    });

    describe('_getCallback', () => {
        it('always returns null', async () => {
            // Given
            await (notificationMouseLeaveDetectionService as any)._remoteStartDetection(
                DUMMY_BOUNDS
            );

            // When
            const callback = (await (notificationMouseLeaveDetectionService as any)._getCallback()) as IBound;

            // Then
            expect(callback).toBeFalsy();
        });
    });
});
