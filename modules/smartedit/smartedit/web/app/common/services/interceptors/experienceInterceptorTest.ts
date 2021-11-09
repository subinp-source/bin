/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { HttpEvent, HttpHandler, HttpRequest } from '@angular/common/http';
import { of, Observable } from 'rxjs';
import {
    httpUtils,
    stringUtils,
    CONTEXT_CATALOG,
    CONTEXT_CATALOG_VERSION,
    CONTEXT_SITE_ID,
    ISharedDataService
} from 'smarteditcommons';
import { ExperienceInterceptor } from 'smarteditcommons/services/interceptors/ExperienceInterceptor';

describe('experience interceptor - unit test catalog version header interceptor', () => {
    let next: jasmine.SpyObj<HttpHandler>;
    let sharedDataService: jasmine.SpyObj<ISharedDataService>;
    let interceptor: ExperienceInterceptor;
    let observable: Observable<any>;
    let result: HttpEvent<any>;

    beforeEach(() => {
        result = jasmine.createSpyObj<HttpEvent<any>>('next', ['url']);
        observable = of(result);

        next = jasmine.createSpyObj<HttpHandler>('next', ['handle']);

        sharedDataService = jasmine.createSpyObj<ISharedDataService>('sharedDataService', [
            'get',
            'set'
        ]);

        sharedDataService.get.and.callFake(() => {
            return Promise.resolve({
                catalogDescriptor: {
                    catalogId: 'apparel-uk',
                    catalogVersion: 'Staged'
                },
                siteDescriptor: {
                    uid: 'apparel-uid'
                }
            });
        });

        interceptor = new ExperienceInterceptor(sharedDataService, stringUtils, httpUtils);
    });

    it('should replace the catalog version, site uid and catalog in the URI if they exist in the sharedDataService', (done) => {
        const request = new HttpRequest<any>(
            'GET',
            '/cmswebservices/v1/sites/' +
                CONTEXT_SITE_ID +
                '/catalogs/' +
                CONTEXT_CATALOG +
                '/versions/' +
                CONTEXT_CATALOG_VERSION +
                '/items'
        );

        next.handle.and.callFake((req: HttpRequest<any>) => {
            if (
                req.url ===
                '/cmswebservices/v1/sites/apparel-uid/catalogs/apparel-uk/versions/Staged/items'
            ) {
                return observable;
            }
            throw new Error(`unexpected request in ExperienceInterceptor ${req.url}`);
        });

        interceptor.intercept(request, next).subscribe((value) => {
            expect(value).toBe(result);
            done();
        });
    });

    it('should not replace catalog version and catalog to the URI if the url is different the cmsapi', (done) => {
        const request = new HttpRequest<any>('GET', '/abc');

        next.handle.and.callFake((req: HttpRequest<any>) => {
            if (req.url === '/abc') {
                return observable;
            }
            throw new Error(`unexpected request in ExperienceInterceptor ${req.url}`);
        });

        interceptor.intercept(request, next).subscribe((value) => {
            expect(value).toBe(result);
            done();
        });
    });
});
