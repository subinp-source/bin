/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { HttpErrorResponse, HttpHeaders, HttpRequest } from '@angular/common/http';
import {
    httpUtils,
    IAlertService,
    LANGUAGE_RESOURCE_URI,
    ResourceNotFoundErrorInterceptor
} from 'smarteditcommons';

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('resource not found validation error interceptor', () => {
    let alertService: jasmine.SpyObj<IAlertService>;
    let resourceNotFoundErrorInterceptor: ResourceNotFoundErrorInterceptor;

    beforeEach(() => {
        alertService = jasmine.createSpyObj('alertService', ['showDanger']);

        resourceNotFoundErrorInterceptor = new ResourceNotFoundErrorInterceptor(
            alertService,
            httpUtils
        );
    });

    function assertPredicate(
        responseMockData: {
            method: string;
            url?: string;
            status: number;
            accept: string;
        },
        truthy: boolean
    ) {
        // GIVEN
        const request = {
            method: responseMockData.method,
            url: responseMockData.url || '/any_url',
            headers: new HttpHeaders().set('Accept', responseMockData.accept)
        } as HttpRequest<any>;

        const mockResponse = {
            url: responseMockData.url || '/any_url',
            status: responseMockData.status
        } as HttpErrorResponse;

        // WHEN
        const matchPredicate = resourceNotFoundErrorInterceptor.predicate(request, mockResponse);

        // THEN
        expect(matchPredicate).toBe(truthy);
    }

    it('should match predicate for a GET xhr request with a 404 response code and text/json as Accept header', () => {
        assertPredicate(
            {
                method: 'GET',
                status: 404,
                accept: 'text/json'
            },
            true
        );
    });

    it('should not match predicate for a GET xhr request with a 404 response code and text/html as Accept header', () => {
        assertPredicate(
            {
                method: 'GET',
                status: 404,
                accept: 'text/html'
            },
            false
        );
    });

    it('should not match predicate for a POST xhr request with a 404 response code and text/json as Accept header', () => {
        assertPredicate(
            {
                method: 'POST',
                status: 404,
                accept: 'text/json'
            },
            true
        );
    });

    it('should not match predicate for a PUT xhr request with a 404 response code and text/json as Accept header', () => {
        assertPredicate(
            {
                method: 'PUT',
                status: 404,
                accept: 'text/json'
            },
            true
        );
    });

    it('should not match predicate for a DELETE xhr request with a 404 response code and text/json as Accept header', () => {
        assertPredicate(
            {
                method: 'DELETE',
                status: 404,
                accept: 'text/json'
            },
            true
        );
    });

    it('should not match predicate for a GET xhr request with a 404 response code to a language resource uri', () => {
        assertPredicate(
            {
                method: 'GET',
                status: 404,
                accept: 'text/json',
                url: LANGUAGE_RESOURCE_URI
            },
            false
        );
    });

    it('should display error message in alert during 10 seconds and reject the promise', () => {
        // GIVEN

        const request = {
            method: 'GET',
            url: '/any_url',
            headers: new HttpHeaders().set('Accept', 'text/json')
        } as HttpRequest<any>;

        const mockResponse = {
            url: '/any_url',
            status: 404,
            message: 'any error message'
        } as HttpErrorResponse;

        // WHEN
        const promise = resourceNotFoundErrorInterceptor.responseError(request, mockResponse);

        // THEN
        expect(alertService.showDanger).toHaveBeenCalledWith({
            message: mockResponse.message,
            timeout: 10000
        });

        expect(promise).toBeRejectedWithData(mockResponse);
    });
});
