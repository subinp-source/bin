/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { DiscardablePromiseUtils, LogService } from 'smarteditcommons';

describe('DiscardablePromiseUtilsTest', () => {
    const identifier1 = 'identifier1';
    const identifier2 = 'identifier2';
    class DTO {
        someKey: string;
    }

    let logService: jasmine.SpyObj<LogService>;
    let discardablePromiseUtils: DiscardablePromiseUtils;

    let values: string[];
    let errors: string[];

    let successcallback: (response: DTO) => void;
    let failurecallback: (response: Error) => void;

    let firstPromiseResolve: () => void;
    let firstPromiseReject: () => void;
    let secondPromiseResolve: () => void;
    let secondPromiseReject: () => void;

    beforeEach(() => {
        values = [];
        errors = [];

        successcallback = (response: DTO) => {
            values.push(response.someKey);
        };
        failurecallback = (response: Error) => {
            errors.push(response.message);
        };

        logService = jasmine.createSpyObj<LogService>('logService', ['debug']);

        discardablePromiseUtils = new DiscardablePromiseUtils(logService);
    });

    describe('Both promises resolve', () => {
        it('GIVEN first promise is faster than second, the second still takes precedence', async () => {
            const firstPromise = new Promise(function(resolve, reject) {
                firstPromiseResolve = () => resolve({ someKey: 'value1' });
            });
            const secondPromise = new Promise(function(resolve, reject) {
                secondPromiseResolve = () => resolve({ someKey: 'value2' });
            });

            discardablePromiseUtils.apply(
                identifier1,
                firstPromise,
                successcallback,
                failurecallback
            );
            discardablePromiseUtils.apply(
                identifier1,
                secondPromise,
                successcallback,
                failurecallback
            );

            await firstPromiseResolve();
            await secondPromiseResolve();

            expect(logService.debug.calls.count()).toBe(2);
            expect(logService.debug).toHaveBeenCalledWith(
                `competing promise for key ${identifier1}`
            );
            expect(logService.debug).toHaveBeenCalledWith(
                `aborted successCallback for promise identified by ${identifier1}`
            );

            expect(values).toEqual(['value2']);
            expect(errors).toEqual([]);
        });

        it('GIVEN first promise is slower than second, the second still takes precedence', async () => {
            const firstPromise = new Promise(function(resolve, reject) {
                firstPromiseResolve = () => resolve({ someKey: 'value1' });
            });

            const secondPromise = new Promise(function(resolve, reject) {
                secondPromiseResolve = () => resolve({ someKey: 'value2' });
            });

            discardablePromiseUtils.apply(
                identifier1,
                firstPromise,
                successcallback,
                failurecallback
            );
            discardablePromiseUtils.apply(
                identifier1,
                secondPromise,
                successcallback,
                failurecallback
            );

            await secondPromiseResolve();
            await firstPromiseResolve();

            expect(logService.debug).toHaveBeenCalledWith(
                `competing promise for key ${identifier1}`
            );
            expect(logService.debug).toHaveBeenCalledWith(
                `aborted successCallback for promise identified by ${identifier1}`
            );

            expect(values).toEqual(['value2']);
            expect(errors).toEqual([]);
        });

        it('GIVEN 2 promises on different identifiers, they do not interfere', async () => {
            const firstPromise = new Promise(function(resolve, reject) {
                firstPromiseResolve = () => resolve({ someKey: 'value1' });
            });

            const secondPromise = new Promise(function(resolve, reject) {
                secondPromiseResolve = () => resolve({ someKey: 'value2' });
            });

            discardablePromiseUtils.apply(
                identifier1,
                firstPromise,
                successcallback,
                failurecallback
            );
            discardablePromiseUtils.apply(
                identifier2,
                secondPromise,
                successcallback,
                failurecallback
            );

            await firstPromiseResolve();
            await secondPromiseResolve();

            expect(logService.debug).not.toHaveBeenCalled();

            expect(values).toEqual(['value1', 'value2']);
            expect(errors).toEqual([]);
        });
    });

    describe('Both promises reject', () => {
        it('GIVEN first promise is faster than second, the second still takes precedence', async () => {
            const firstPromise = new Promise(function(resolve, reject) {
                firstPromiseReject = () => reject(new Error('value1'));
            });

            const secondPromise = new Promise(function(resolve, reject) {
                secondPromiseReject = () => reject(new Error('value2'));
            });

            discardablePromiseUtils.apply(
                identifier1,
                firstPromise,
                successcallback,
                failurecallback
            );
            discardablePromiseUtils.apply(
                identifier1,
                secondPromise,
                successcallback,
                failurecallback
            );

            await firstPromiseReject();
            await secondPromiseReject();

            expect(logService.debug).toHaveBeenCalledWith(
                `competing promise for key ${identifier1}`
            );
            expect(logService.debug).toHaveBeenCalledWith(
                `aborted failureCallback for promise identified by ${identifier1}`
            );

            expect(values).toEqual([]);
            expect(errors).toEqual(['value2']);
        });

        it('GIVEN first promise is slower than second, the second still takes precedence', async () => {
            const firstPromise = new Promise(function(resolve, reject) {
                firstPromiseReject = () => reject(new Error('value1'));
            });

            const secondPromise = new Promise(function(resolve, reject) {
                secondPromiseReject = () => reject(new Error('value2'));
            });

            discardablePromiseUtils.apply(
                identifier1,
                firstPromise,
                successcallback,
                failurecallback
            );
            discardablePromiseUtils.apply(
                identifier1,
                secondPromise,
                successcallback,
                failurecallback
            );

            await secondPromiseReject();
            await firstPromiseReject();

            expect(logService.debug).toHaveBeenCalledWith(
                `competing promise for key ${identifier1}`
            );
            expect(logService.debug).toHaveBeenCalledWith(
                `aborted failureCallback for promise identified by ${identifier1}`
            );

            expect(values).toEqual([]);
            expect(errors).toEqual(['value2']);
        });

        it('GIVEN 2 promises on different identifiers, they do not interfere', async () => {
            const firstPromise = new Promise(function(resolve, reject) {
                firstPromiseReject = () => reject(new Error('value1'));
            });

            const secondPromise = new Promise(function(resolve, reject) {
                secondPromiseReject = () => reject(new Error('value2'));
            });

            discardablePromiseUtils.apply(
                identifier1,
                firstPromise,
                successcallback,
                failurecallback
            );
            discardablePromiseUtils.apply(
                identifier2,
                secondPromise,
                successcallback,
                failurecallback
            );

            await firstPromiseReject();
            await secondPromiseReject();

            expect(logService.debug).not.toHaveBeenCalled();

            expect(values).toEqual([]);
            expect(errors).toEqual(['value1', 'value2']);
        });
    });

    describe('First promise resolves and second rejects', () => {
        it('GIVEN first promise is faster than second, the second still takes precedence', async () => {
            const firstPromise = new Promise(function(resolve, reject) {
                firstPromiseResolve = () => resolve({ someKey: 'value1' });
            });

            const secondPromise = new Promise(function(resolve, reject) {
                secondPromiseReject = () => reject(new Error('value2'));
            });

            discardablePromiseUtils.apply(
                identifier1,
                firstPromise,
                successcallback,
                failurecallback
            );
            discardablePromiseUtils.apply(
                identifier1,
                secondPromise,
                successcallback,
                failurecallback
            );

            await firstPromiseResolve();
            await secondPromiseReject();

            expect(logService.debug).toHaveBeenCalledWith(
                `competing promise for key ${identifier1}`
            );
            expect(logService.debug).toHaveBeenCalledWith(
                `aborted successCallback for promise identified by ${identifier1}`
            );

            expect(values).toEqual([]);
            expect(errors).toEqual(['value2']);
        });

        it('GIVEN first promise is slower than second, the second still takes precedence', async () => {
            const firstPromise = new Promise(function(resolve, reject) {
                firstPromiseResolve = () => resolve({ someKey: 'value1' });
            });

            const secondPromise = new Promise(function(resolve, reject) {
                secondPromiseReject = () => reject(new Error('value2'));
            });

            discardablePromiseUtils.apply(
                identifier1,
                firstPromise,
                successcallback,
                failurecallback
            );
            discardablePromiseUtils.apply(
                identifier1,
                secondPromise,
                successcallback,
                failurecallback
            );

            await secondPromiseReject();
            await firstPromiseResolve();

            expect(logService.debug).toHaveBeenCalledWith(
                `competing promise for key ${identifier1}`
            );
            expect(logService.debug).toHaveBeenCalledWith(
                `aborted successCallback for promise identified by ${identifier1}`
            );

            expect(values).toEqual([]);
            expect(errors).toEqual(['value2']);
        });
    });
});
