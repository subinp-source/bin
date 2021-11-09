/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injector } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ISharedDataService, LogService, PREVIEW_RESOURCE_URI } from 'smarteditcommons';
import { PreviewErrorInterceptor } from './PreviewErrorInterceptor';

describe('preview resource error interceptor', () => {
    let previewErrorInterceptor: PreviewErrorInterceptor;
    let injector: jasmine.SpyObj<Injector>;
    let logService: jasmine.SpyObj<LogService>;
    let iframeManagerService: jasmine.SpyObj<{ setCurrentLocation: (location: string) => void }>;
    let sharedDataService: jasmine.SpyObj<ISharedDataService>;
    let httpClient: jasmine.SpyObj<HttpClient>;

    beforeEach(() => {
        iframeManagerService = jasmine.createSpyObj('iframeManagerService', ['setCurrentLocation']);
        httpClient = jasmine.createSpyObj<HttpClient>('httpClient', ['request']);
        injector = jasmine.createSpyObj<Injector>('injector', ['get']);
        injector.get.and.callFake((token: any) => {
            if (token === 'iframeManagerService') {
                return iframeManagerService;
            } else if (token === HttpClient) {
                return httpClient;
            }
            return null;
        });
        logService = jasmine.createSpyObj<LogService>('logService', ['info']);
        sharedDataService = jasmine.createSpyObj<ISharedDataService>('sharedDataService', [
            'update'
        ]);

        previewErrorInterceptor = new PreviewErrorInterceptor(
            injector,
            logService,
            sharedDataService
        );
    });

    it('should match predicate for a xhr request to preview resource uri that returns a 400 response code with a pageId and an unknow identifier error type', () => {
        // GIVEN
        const request = {
            method: 'GET',
            url: PREVIEW_RESOURCE_URI,
            body: {
                pageId: 1
            }
        } as HttpRequest<any>;

        const mockResponse = {
            url: PREVIEW_RESOURCE_URI,
            error: {
                errors: [
                    {
                        type: 'UnknownIdentifierError'
                    }
                ]
            },
            status: 400
        } as HttpErrorResponse;

        // WHEN
        const matchPredicate = previewErrorInterceptor.predicate(request, mockResponse);

        // THEN
        expect(matchPredicate).toBe(true);
    });

    it('should not match predicate for a xhr request to a non preview resource uri with a 400 response code', () => {
        // GIVEN
        const request = {
            method: 'GET',
            url: '/any_url',
            body: {}
        } as HttpRequest<any>;

        const mockResponse = {
            url: '/any_url',
            error: {},
            status: 400
        } as HttpErrorResponse;

        // WHEN
        const matchPredicate = previewErrorInterceptor.predicate(request, mockResponse);

        // THEN
        expect(matchPredicate).toBe(false);
    });

    it('should set iframeManagerService current location to null for a match predicate', () => {
        const request = {
            method: 'GET',
            url: PREVIEW_RESOURCE_URI,
            body: {
                pageId: 1
            }
        } as HttpRequest<any>;

        const mockResponse = {
            url: PREVIEW_RESOURCE_URI,
            error: {
                errors: [
                    {
                        type: 'UnknownIdentifierError'
                    }
                ]
            },
            status: 400
        } as HttpErrorResponse;

        const promise = jasmine.createSpyObj<Promise<any>>('observable', ['then']);
        const observable = jasmine.createSpyObj<Observable<any>>('observable', ['toPromise']);
        observable.toPromise.and.returnValue(promise);

        const experience = {
            pageId: 'pageId',
            bla: 'bli'
        };

        sharedDataService.update.and.returnValue(Promise.resolve());

        httpClient.request.and.returnValue(observable);

        expect(previewErrorInterceptor.responseError(request, mockResponse)).toBe(promise);

        const callback = sharedDataService.update.calls.argsFor(0)[1];

        callback(experience);
        expect(experience.pageId).toBeUndefined();

        expect(iframeManagerService.setCurrentLocation).toHaveBeenCalledWith(null);

        expect(httpClient.request).toHaveBeenCalledWith(request);
    });
});
