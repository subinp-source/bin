/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { annotationService, GatewayProxied, IBound, WindowUtils } from 'smarteditcommons';
import { NotificationMouseLeaveDetectionService } from 'smarteditcontainer/services';
import { promiseHelper, PromiseType } from 'testhelpers';

describe('notificationMouseLeaveDetectionService', () => {
    const DUMMY_X = 0;
    const DUMMY_Y = 0;
    const DUMMY_WIDTH = 100;
    const DUMMY_HEIGHT = 100;
    const DUMMY_OUTER_BOUNDS: IBound = {
        x: DUMMY_X,
        y: DUMMY_Y,
        width: DUMMY_WIDTH,
        height: DUMMY_HEIGHT
    };
    const DUMMY_INNER_BOUNDS = DUMMY_OUTER_BOUNDS;
    const DUMMY_BOUNDS = DUMMY_OUTER_BOUNDS;

    const MOUSE_MOVE_EVENT = 'mousemove';

    const _document = document.implementation.createHTMLDocument();
    const windowUtils: jasmine.SpyObj<WindowUtils> = jasmine.createSpyObj<WindowUtils>(
        'windowUtils',
        ['getGatewayTargetFrame']
    );

    let notificationMouseLeaveDetectionService: NotificationMouseLeaveDetectionService;

    /*
     * This method prepares a mock for the Gateway Proxy. It is used to test that
     * the service properly initializes itself for proxying across the gateay.
     */

    beforeEach(() => {
        notificationMouseLeaveDetectionService = new NotificationMouseLeaveDetectionService(
            _document,
            windowUtils
        );
        windowUtils.getGatewayTargetFrame.and.returnValue({});
    });

    describe('initialization', () => {
        it('extends the INotificationMouseLeaveDetectionService', () => {
            expect(
                (notificationMouseLeaveDetectionService as any)._remoteStartDetection
            ).toBeEmptyFunction();
            expect(
                (notificationMouseLeaveDetectionService as any)._remoteStopDetection
            ).toBeEmptyFunction();
        });

        it('checks GatewayProxied', () => {
            expect(
                annotationService.getClassAnnotation(
                    NotificationMouseLeaveDetectionService,
                    GatewayProxied
                )
            ).toEqual([
                'stopDetection',
                '_remoteStartDetection',
                '_remoteStopDetection',
                '_callCallback'
            ]);
        });
    });

    describe('validateBounds', () => {
        it('throws an error when the bounds object is not provided', () => {
            expect(() => {
                notificationMouseLeaveDetectionService.startDetection(null, null, null);
            }).toThrowError('Bounds are required for mouse leave detection');
        });

        it('throws an error if the bounds object does not contain an x coordinate', () => {
            expect(() => {
                notificationMouseLeaveDetectionService.startDetection(
                    {
                        y: DUMMY_Y,
                        width: DUMMY_WIDTH,
                        height: DUMMY_HEIGHT
                    } as IBound,
                    DUMMY_INNER_BOUNDS,
                    function() {
                        /**/
                    }
                );
            }).toThrowError('Bounds must contain the x coordinate');
        });

        it('throws an error if the bounds object does not contain a y coordinate', () => {
            expect(() => {
                notificationMouseLeaveDetectionService.startDetection(
                    {
                        x: DUMMY_X,
                        width: DUMMY_WIDTH,
                        height: DUMMY_HEIGHT
                    } as IBound,
                    DUMMY_INNER_BOUNDS,
                    function() {
                        /**/
                    }
                );
            }).toThrowError('Bounds must contain the y coordinate');
        });

        it('throws an error if the bounds object does not contain the width dimension', () => {
            expect(() => {
                notificationMouseLeaveDetectionService.startDetection(
                    {
                        x: DUMMY_X,
                        y: DUMMY_Y,
                        height: DUMMY_HEIGHT
                    } as IBound,
                    DUMMY_INNER_BOUNDS,
                    function() {
                        /**/
                    }
                );
            }).toThrowError('Bounds must contain the width dimension');
        });

        it('throws an error if the bounds object does not contain the height dimension', () => {
            expect(() => {
                notificationMouseLeaveDetectionService.startDetection(
                    {
                        x: DUMMY_X,
                        y: DUMMY_Y,
                        width: DUMMY_WIDTH
                    } as IBound,
                    DUMMY_INNER_BOUNDS,
                    function() {
                        /**/
                    }
                );
            }).toThrowError('Bounds must contain the height dimension');
        });
    });

    describe('startDetection', () => {
        it('throws an error if the callback function is not provided', () => {
            expect(() => {
                notificationMouseLeaveDetectionService.startDetection(
                    DUMMY_OUTER_BOUNDS,
                    null,
                    null
                );
            }).toThrowError('Callback function is required');
        });

        it('registers a mouse move event listener on the local frame', async () => {
            const addEventListenerSpy = spyOn(_document, 'addEventListener');

            // When
            await notificationMouseLeaveDetectionService.startDetection(
                DUMMY_OUTER_BOUNDS,
                DUMMY_INNER_BOUNDS,
                jasmine.any(Function)
            );

            // Then
            expect(addEventListenerSpy).toHaveBeenCalledWith(
                MOUSE_MOVE_EVENT,
                (notificationMouseLeaveDetectionService as any)._onMouseMove
            );
        });

        it('starts detection in the remote frame if the inner bounds are given', () => {
            // Given
            spyOn(notificationMouseLeaveDetectionService as any, '_remoteStartDetection');

            // When
            notificationMouseLeaveDetectionService.startDetection(
                DUMMY_OUTER_BOUNDS,
                DUMMY_INNER_BOUNDS,
                jasmine.any(Function)
            );

            // Then
            expect(
                (notificationMouseLeaveDetectionService as any)._remoteStartDetection
            ).toHaveBeenCalledWith(DUMMY_INNER_BOUNDS);
        });

        it('never starts detection in the remote frame if the inner bounds are not given', () => {
            // Given
            spyOn(notificationMouseLeaveDetectionService as any, '_remoteStartDetection');

            // When
            notificationMouseLeaveDetectionService.startDetection(
                DUMMY_OUTER_BOUNDS,
                null,
                jasmine.any(Function)
            );

            // Then
            expect(
                (notificationMouseLeaveDetectionService as any)._remoteStartDetection
            ).not.toHaveBeenCalled();
        });
    });

    describe('stopDetection', () => {
        it('un-registers the mouse move event listener on the local frame', () => {
            const removeEventListenerSpy = spyOn(_document, 'removeEventListener');

            // When
            notificationMouseLeaveDetectionService.stopDetection();

            // Then
            expect(removeEventListenerSpy).toHaveBeenCalledWith(
                MOUSE_MOVE_EVENT,
                (notificationMouseLeaveDetectionService as any)._onMouseMove
            );
        });

        it('stops detection in the remote frame', () => {
            // Given
            spyOn(notificationMouseLeaveDetectionService as any, '_remoteStopDetection');

            // When
            notificationMouseLeaveDetectionService.stopDetection();

            // Then
            expect(
                (notificationMouseLeaveDetectionService as any)._remoteStopDetection
            ).toHaveBeenCalled();
        });

        it('resets the notification panel bounds that were stored', async () => {
            // Given
            await notificationMouseLeaveDetectionService.startDetection(
                DUMMY_OUTER_BOUNDS,
                DUMMY_INNER_BOUNDS,
                jasmine.any(Function)
            );

            // When
            await notificationMouseLeaveDetectionService.stopDetection();

            // Then
            const bounds = (await (notificationMouseLeaveDetectionService as any)._getBounds()) as IBound;
            expect(bounds).toBeFalsy();
        });

        it('resets the mouse leave callback that was stored', async () => {
            // Given
            await notificationMouseLeaveDetectionService.startDetection(
                DUMMY_OUTER_BOUNDS,
                DUMMY_INNER_BOUNDS,
                jasmine.any(Function)
            );

            // When
            await notificationMouseLeaveDetectionService.stopDetection();

            // Then
            const callback = await (notificationMouseLeaveDetectionService as any)._getCallback();
            expect(callback).toBeFalsy();
        });
    });

    describe('_callCallback', () => {
        it('calls the mouse leave callback', () => {
            // Given
            const callBackFnSpy = jasmine.createSpy('callBackFn');
            const cbPromise = promiseHelper.buildPromise<any>(
                'cbPromise',
                PromiseType.RESOLVES,
                callBackFnSpy
            );

            spyOn(notificationMouseLeaveDetectionService as any, '_getCallback').and.returnValue(
                cbPromise
            );

            // When
            (notificationMouseLeaveDetectionService as any)._callCallback();

            // Then
            expect((notificationMouseLeaveDetectionService as any)._getCallback).toHaveBeenCalled();
            expect(callBackFnSpy).toHaveBeenCalled();
        });
    });

    describe('_getBounds', () => {
        it('returns the bounds that were given when detection was started', async () => {
            // Given
            await notificationMouseLeaveDetectionService.startDetection(
                DUMMY_OUTER_BOUNDS,
                DUMMY_INNER_BOUNDS,
                jasmine.any(Function)
            );

            // When
            const bounds = (await (notificationMouseLeaveDetectionService as any)._getBounds()) as IBound;

            // Then
            expect(bounds).toEqual(DUMMY_OUTER_BOUNDS);
        });
    });

    describe('_getCallback', () => {
        it('returns the callback that was given when detection was started', async () => {
            // Given
            const expectedCallback = () => {
                /**/
            };

            await notificationMouseLeaveDetectionService.startDetection(
                DUMMY_OUTER_BOUNDS,
                DUMMY_INNER_BOUNDS,
                expectedCallback
            );

            // When
            const actualCallback = await (notificationMouseLeaveDetectionService as any)._getCallback();

            // Then
            expect(expectedCallback).toEqual(actualCallback);
        });
    });

    describe('_onMouseLeave', () => {
        it('calls the callback function if it is defined locally', () => {
            // Given
            const callBackFnSpy = jasmine.createSpy('callBackFn');

            const callback = promiseHelper.buildPromise<any>(
                'cbPromise',
                PromiseType.RESOLVES,
                callBackFnSpy
            );

            spyOn(notificationMouseLeaveDetectionService as any, '_getCallback').and.returnValue(
                callback
            );
            spyOn(notificationMouseLeaveDetectionService as any, '_callCallback');

            // When
            (notificationMouseLeaveDetectionService as any)._onMouseLeave();

            // Then
            expect((notificationMouseLeaveDetectionService as any)._getCallback).toHaveBeenCalled();
            expect(
                (notificationMouseLeaveDetectionService as any)._callCallback
            ).not.toHaveBeenCalled();
            expect(callBackFnSpy).toHaveBeenCalled();
        });

        it('calls the callback function remotely if it is not defined locally', () => {
            // Given
            const callback = promiseHelper.buildPromise<any>(
                'cbPromise',
                PromiseType.RESOLVES,
                null
            );
            const callcallback = promiseHelper.buildPromise<any>(
                'ccbPromise',
                PromiseType.RESOLVES,
                undefined
            );
            spyOn(notificationMouseLeaveDetectionService as any, '_getCallback').and.returnValue(
                callback
            );
            spyOn(notificationMouseLeaveDetectionService as any, '_callCallback').and.returnValue(
                callcallback
            );

            // When
            (notificationMouseLeaveDetectionService as any)._onMouseLeave();

            // Then
            expect((notificationMouseLeaveDetectionService as any)._getCallback).toHaveBeenCalled();
            expect(
                (notificationMouseLeaveDetectionService as any)._callCallback
            ).toHaveBeenCalled();
        });

        it('stops detection', () => {
            // Given
            const callback = promiseHelper.buildPromise<any>(
                'cbPromise',
                PromiseType.RESOLVES,
                function() {
                    /**/
                }
            );
            spyOn(notificationMouseLeaveDetectionService as any, '_getCallback').and.returnValue(
                callback
            );
            spyOn(notificationMouseLeaveDetectionService, 'stopDetection');

            // When
            (notificationMouseLeaveDetectionService as any)._onMouseLeave();

            // Then
            expect(notificationMouseLeaveDetectionService.stopDetection).toHaveBeenCalled();
        });
    });

    describe('_onMouseMove', () => {
        describe('', () => {
            beforeEach(() => {
                const nullBoundsPromise = promiseHelper.buildPromise<IBound>(
                    'boundsPromise',
                    PromiseType.RESOLVES,
                    null
                );
                spyOn(notificationMouseLeaveDetectionService as any, '_getBounds').and.returnValue(
                    nullBoundsPromise
                );
                spyOn(notificationMouseLeaveDetectionService as any, '_onMouseLeave');
            });

            it('never calls _onMouseLeave if bounds are undefined', async () => {
                // Given
                const event = {
                    clientX: DUMMY_BOUNDS.x,
                    clientY: DUMMY_BOUNDS.y
                } as MouseEvent;

                // When
                await (notificationMouseLeaveDetectionService as any)._onMouseMove(event);

                // Then
                expect(
                    (notificationMouseLeaveDetectionService as any)._onMouseLeave
                ).not.toHaveBeenCalled();
            });
        });

        describe('', () => {
            /*
             * This setup method returns creates a spy on the interface's _getBounds method that returns
             * a dummy bounds object and a spy on the interface's _onMouseLeave method to avoid having to
             * create them in each individual test case.
             */
            beforeEach(() => {
                const boundsPromise: angular.IPromise<IBound> = promiseHelper.buildPromise<any>(
                    'boundsPromise',
                    PromiseType.RESOLVES,
                    DUMMY_BOUNDS
                );
                spyOn(notificationMouseLeaveDetectionService as any, '_getBounds').and.returnValue(
                    boundsPromise
                );
                spyOn(notificationMouseLeaveDetectionService as any, '_onMouseLeave');
            });

            it('never calls _onMouseLeave when the mouse pointer is within the notification panel', async () => {
                // Given
                const event = {
                    clientX: DUMMY_BOUNDS.x,
                    clientY: DUMMY_BOUNDS.y
                } as MouseEvent;

                // When
                await (notificationMouseLeaveDetectionService as any)._onMouseMove(event);

                // Then
                expect(
                    (notificationMouseLeaveDetectionService as any)._onMouseLeave
                ).not.toHaveBeenCalled();
            });

            it('calls _onMouseLeave when the mouse pointer is outside the notification panel on both the X and Y axes', async () => {
                // Given
                const event = {
                    clientX: DUMMY_BOUNDS.x + DUMMY_BOUNDS.width + 1,
                    clientY: DUMMY_BOUNDS.y + DUMMY_BOUNDS.height + 1
                } as MouseEvent;

                // When
                await (notificationMouseLeaveDetectionService as any)._onMouseMove(event);

                // Then
                expect(
                    (notificationMouseLeaveDetectionService as any)._onMouseLeave
                ).toHaveBeenCalled();
            });

            it('calls _onMouseLeave when the mouse pointer is outside the notification panel to the left but within it on the Y axis', async () => {
                // Given
                const event = {
                    clientX: DUMMY_BOUNDS.x - 1,
                    clientY: DUMMY_BOUNDS.y
                } as MouseEvent;

                // When
                await (notificationMouseLeaveDetectionService as any)._onMouseMove(event);

                // Then
                expect(
                    (notificationMouseLeaveDetectionService as any)._onMouseLeave
                ).toHaveBeenCalled();
            });

            it('calls _onMouseLeave when the mouse pointer is outside the notification panel to the right but within it on the Y axis', async () => {
                // Given
                const event = {
                    clientX: DUMMY_BOUNDS.x + DUMMY_BOUNDS.width + 1,
                    clientY: DUMMY_BOUNDS.y
                } as MouseEvent;

                // When
                await (notificationMouseLeaveDetectionService as any)._onMouseMove(event);

                // Then
                expect(
                    (notificationMouseLeaveDetectionService as any)._onMouseLeave
                ).toHaveBeenCalled();
            });

            it('calls _onMouseLeave when the mouse pointer is outside the notification panel above it but within it on the X axis', async () => {
                // Given
                const event = {
                    clientX: DUMMY_BOUNDS.x,
                    clientY: DUMMY_BOUNDS.y - 1
                } as MouseEvent;

                // When
                await (notificationMouseLeaveDetectionService as any)._onMouseMove(event);

                // Then
                expect(
                    (notificationMouseLeaveDetectionService as any)._onMouseLeave
                ).toHaveBeenCalled();
            });

            it('calls _onMouseLeave when the mouse pointer is outside the notification panel below it but within it on the X axis', async () => {
                // Given
                const event = {
                    clientX: DUMMY_BOUNDS.x,
                    clientY: DUMMY_BOUNDS.y + DUMMY_BOUNDS.height + 1
                } as MouseEvent;

                // When
                await (notificationMouseLeaveDetectionService as any)._onMouseMove(event);

                // Then
                expect(
                    (notificationMouseLeaveDetectionService as any)._onMouseLeave
                ).toHaveBeenCalled();
            });
        });
    });
});
