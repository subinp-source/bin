/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { HttpErrorResponse, HttpRequest } from '@angular/common/http';
import {
    GenericEditorStackService,
    IAlertService,
    NonValidationErrorInterceptor
} from 'smarteditcommons';

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('non validation error interceptor', () => {
    let alertService: jasmine.SpyObj<IAlertService>;
    let genericEditorStackService: jasmine.SpyObj<GenericEditorStackService>;
    let nonValidationErrorInterceptor: NonValidationErrorInterceptor;

    beforeEach(() => {
        alertService = jasmine.createSpyObj<IAlertService>('alertService', ['showDanger']);
        genericEditorStackService = jasmine.createSpyObj<GenericEditorStackService>(
            'genericEditorStackService',
            ['isAnyGenericEditorOpened']
        );

        nonValidationErrorInterceptor = new NonValidationErrorInterceptor(
            alertService,
            genericEditorStackService
        );
    });

    it('should match predicate for a GET xhr request with a HTTP Error 400', () => {
        // GIVEN
        const matchMockResponse = ({
            status: 400
        } as any) as HttpErrorResponse;

        // WHEN
        const matchPredicate = nonValidationErrorInterceptor.predicate(
            {} as HttpRequest<any>,
            matchMockResponse
        );

        // THEN
        expect(matchPredicate).toBe(true);
    });

    it('should not match predicate for a GET xhr request with a HTTP Error 401 or 404', () => {
        // GIVEN
        let predicate;
        [401, 404].forEach(function(status) {
            // WHEN
            predicate = nonValidationErrorInterceptor.predicate(
                {} as HttpRequest<any>,
                ({
                    status
                } as any) as HttpErrorResponse
            );

            // THEN
            expect(predicate).toBe(false);
        });
    });

    it('GIVEN no generic editor is opened AND there are validation and non-validation errors WHEN errors are intercepted THEN it should display all errors and reject the promise', () => {
        // GIVEN
        genericEditorStackService.isAnyGenericEditorOpened.and.returnValue(false);
        const mockResponse = {
            status: 400,
            error: {
                errors: [
                    {
                        message: 'This field cannot contain special characters',
                        type: 'ValidationError'
                    },
                    {
                        message: 'This is the second validation error',
                        type: 'NonValidationError'
                    }
                ]
            }
        } as HttpErrorResponse;

        // WHEN
        const promise = nonValidationErrorInterceptor.responseError(
            {} as HttpRequest<any>,
            mockResponse
        );

        // THEN
        expect(alertService.showDanger.calls.count()).toEqual(2);
        expect(alertService.showDanger).toHaveBeenCalledWith(
            jasmine.objectContaining({
                message: 'This field cannot contain special characters'
            })
        );
        expect(alertService.showDanger).toHaveBeenCalledWith(
            jasmine.objectContaining({
                message: 'This is the second validation error'
            })
        );
        expect(promise).toBeRejectedWithData(mockResponse);
    });

    it('GIVEN generic editor is opened AND there are validation and non-validation errors WHEN errors are intercepted THEN it should only display non-validation errors and reject the promise', () => {
        // GIVEN
        genericEditorStackService.isAnyGenericEditorOpened.and.returnValue(true);
        const mockResponse = {
            status: 400,
            error: {
                errors: [
                    {
                        message: 'This field cannot contain special characters',
                        type: 'ValidationError'
                    },
                    {
                        message: 'This is the second validation error',
                        type: 'NonValidationError'
                    }
                ]
            }
        } as HttpErrorResponse;

        // WHEN
        const promise = nonValidationErrorInterceptor.responseError(
            {} as HttpRequest<any>,
            mockResponse
        );

        // THEN
        expect(alertService.showDanger.calls.count()).toEqual(1);
        expect(alertService.showDanger).toHaveBeenCalledWith(
            jasmine.objectContaining({
                message: 'This is the second validation error'
            })
        );
        expect(promise).toBeRejectedWithData(mockResponse);
    });

    it('GIVEN generic editor is opened AND there are only validation errors WHEN errors are intercepted THEN it should not display any alert messages and reject the promise', () => {
        // GIVEN
        genericEditorStackService.isAnyGenericEditorOpened.and.returnValue(true);
        const mockResponse = {
            status: 400,
            error: {
                errors: [
                    {
                        message: 'This field cannot contain special characters',
                        type: 'ValidationError'
                    },
                    {
                        message: 'This field is required',
                        type: 'ValidationError'
                    }
                ]
            }
        } as HttpErrorResponse;

        // WHEN
        const promise = nonValidationErrorInterceptor.responseError(
            {} as HttpRequest<any>,
            mockResponse
        );

        // THEN
        expect(alertService.showDanger).not.toHaveBeenCalled();
        expect(promise).toBeRejectedWithData(mockResponse);
    });
});
