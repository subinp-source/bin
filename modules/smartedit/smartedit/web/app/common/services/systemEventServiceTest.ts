/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { promiseUtils } from '@smart/utils';
import { LogService, SystemEventService } from 'smarteditcommons';

describe('systemEventService', () => {
    let systemEventService: SystemEventService;
    let handlerHolderMock: any;
    let logService: jasmine.SpyObj<LogService>;

    const id: string = 'myId';
    const data: string = 'myData';

    function pendingPromise() {
        return new Promise((resolve, reject) => {
            //
        });
    }

    function setPromiseMocks(
        promise1: Promise<any>,
        promise2: Promise<any>,
        promise3: Promise<any>
    ) {
        spyOn(handlerHolderMock, 'handler').and.returnValue(promise1);
        spyOn(handlerHolderMock, 'handler2').and.returnValue(promise2);
        spyOn(handlerHolderMock, 'handler3').and.returnValue(promise3);
    }

    beforeEach(() => {
        handlerHolderMock = {
            handler: angular.noop,
            handler2: angular.noop,
            handler3: angular.noop
        };

        logService = jasmine.createSpyObj<LogService>('logService', ['error', 'warn']);
        systemEventService = new SystemEventService(logService, promiseUtils);
    });

    describe('subscribe', () => {
        it('subscribe event handler with incorrect eventId', () => {
            setPromiseMocks(pendingPromise(), pendingPromise(), pendingPromise());

            systemEventService.subscribe(null, handlerHolderMock.handler);
            expect(logService.error).toHaveBeenCalledWith(
                'Failed to subscribe event handler for event: null'
            );
            expect(handlerHolderMock.handler).not.toHaveBeenCalledWith(id, data);
        });
        it('subscribe event handler with incorrect eventId', () => {
            setPromiseMocks(pendingPromise(), pendingPromise(), pendingPromise());

            systemEventService.subscribe(id, null);
            expect(logService.error).toHaveBeenCalledWith(
                'Failed to subscribe event handler for event: ' + id
            );
            expect(handlerHolderMock.handler).not.toHaveBeenCalledWith(id, data);
        });
        it('unsubscribe function', () => {
            setPromiseMocks(pendingPromise(), pendingPromise(), pendingPromise());

            const unsubscribeSpy = spyOn(
                systemEventService as any,
                '_unsubscribe'
            ).and.callThrough();
            const unsubscribeFn = systemEventService.subscribe(id, handlerHolderMock.handler);
            unsubscribeFn();
            expect(unsubscribeSpy).toHaveBeenCalledWith(id, handlerHolderMock.handler);
        });
    });

    describe('unsubscribe', () => {
        it('unsubscribe event handler with incorrect eventId', () => {
            setPromiseMocks(pendingPromise(), pendingPromise(), pendingPromise());

            (systemEventService as any)._unsubscribe(null, handlerHolderMock.handler);
            expect(logService.warn).toHaveBeenCalledWith(
                'Attempting to remove event handler for null but handler not found.'
            );
        });
    });

    describe('publishAsync', () => {
        beforeEach(() => {
            jasmine.clock().install();
        });
        afterEach(() => {
            jasmine.clock().uninstall();
        });
        it('When first handler out of 2 is successful Will call both my event handlers subscribed to the same eventId - with correct id and data - and resolves the promise chain to the resolved data of the last subscriber', (done) => {
            const publishSpy = spyOn(systemEventService, 'publish').and.callThrough();

            setPromiseMocks(
                Promise.resolve('firstValue'),
                Promise.resolve('secondValue'),
                pendingPromise()
            );

            systemEventService.subscribe(id, handlerHolderMock.handler);
            systemEventService.subscribe(id, handlerHolderMock.handler2);

            const promise = systemEventService.publishAsync(id, data);

            jasmine.clock().tick(0);

            promise.then(
                (resolvedData: string) => {
                    expect(resolvedData).toBe('secondValue');
                    done();
                },
                (rejectedData) => {
                    fail();
                }
            );

            expect(publishSpy).toHaveBeenCalledWith(id, data);
            expect(handlerHolderMock.handler).toHaveBeenCalledWith(id, data);
        });
    });

    // ----------------------------------------------------------
    // All the rest of the tests are using the Synchronized mode
    // ----------------------------------------------------------

    it('Will call my event handler with correct id and data', () => {
        setPromiseMocks(pendingPromise(), pendingPromise(), pendingPromise());

        systemEventService.subscribe(id, handlerHolderMock.handler);
        systemEventService.publish(id, data);
        expect(handlerHolderMock.handler).toHaveBeenCalledWith(id, data);
    });

    it('When first handler out of 2 is successful Will call both my event handlers subscribed to the same eventId - with correct id and data - and resolves the promise chain to the resolved data of the last subscriber', (done) => {
        setPromiseMocks(
            Promise.resolve('firstValue'),
            Promise.resolve('secondValue'),
            pendingPromise()
        );

        systemEventService.subscribe(id, handlerHolderMock.handler);
        systemEventService.subscribe(id, handlerHolderMock.handler2);
        const promise = systemEventService.publish(id, data);

        promise.then(
            (resolvedData: string) => {
                expect(resolvedData).toBe('secondValue');
                done();
            },
            (rejectedData) => {
                fail();
            }
        );

        expect(handlerHolderMock.handler).toHaveBeenCalledWith(id, data);
        expect(handlerHolderMock.handler2).toHaveBeenCalledWith(id, data);
    });

    it('When more than 2 handlers and all are successful , resolves the promise chain to the resolved data of the last subscriber', (done) => {
        setPromiseMocks(
            Promise.resolve('firstValue'),
            Promise.resolve('secondValue'),
            Promise.resolve('thirdValue')
        );

        systemEventService.subscribe(id, handlerHolderMock.handler);
        systemEventService.subscribe(id, handlerHolderMock.handler2);
        systemEventService.subscribe(id, handlerHolderMock.handler3);

        systemEventService.publish(id, data).then(
            (resolvedData: string) => {
                expect(resolvedData).toBe('thirdValue');
                done();
            },
            () => {
                fail();
            }
        );

        expect(handlerHolderMock.handler).toHaveBeenCalledWith(id, data);
        expect(handlerHolderMock.handler2).toHaveBeenCalledWith(id, data);
        expect(handlerHolderMock.handler3).toHaveBeenCalledWith(id, data);
    });

    it('When either of first or second handler is not successful Will send method promise rejects', (done) => {
        setPromiseMocks(
            Promise.resolve('firstValue'),
            Promise.reject('error reason'),
            new Promise((resolve, reject) => {
                //
            })
        );

        systemEventService.subscribe(id, handlerHolderMock.handler);
        systemEventService.subscribe(id, handlerHolderMock.handler2);
        systemEventService.publish(id, data).then(
            (resolvedData: string) => {
                fail();
            },
            (reason: string) => {
                expect(reason).toBe('error reason');
                done();
            }
        );

        expect(handlerHolderMock.handler).toHaveBeenCalledWith(id, data);
        expect(handlerHolderMock.handler2).toHaveBeenCalledWith(id, data);
    });

    it('Will NOT call my handler after it has been unsubscribed for the specific eventId and send method promise resolves', (done) => {
        setPromiseMocks(pendingPromise(), pendingPromise(), pendingPromise());

        const unsubscribeFn = systemEventService.subscribe(id, handlerHolderMock.handler);
        unsubscribeFn();
        systemEventService.publish(id, data).then(() => done(), () => fail());
        expect(handlerHolderMock.handler).not.toHaveBeenCalled();
    });
});
