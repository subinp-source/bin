/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ModuleWithProviders, NgModule, Provider } from '@angular/core';
import {
    moduleUtils,
    stringUtils,
    urlUtils,
    DEFAULT_AUTHENTICATION_ENTRY_POINT,
    HttpBackendService,
    SeEntryModule,
    TypedMap
} from 'smarteditcommons';
import {
    ADMIN_AUTH,
    ADMIN_AUTH_TOKEN,
    CMSMANAGER_AUTH,
    CMSMANAGER_AUTH_TOKEN
} from '../outerConstants';

@SeEntryModule('OuterAuthorizationMocks')
@NgModule({
    providers: [
        moduleUtils.provideValues({
            ADMIN_AUTH_TOKEN: ADMIN_AUTH,
            CMSMANAGER_AUTH_TOKEN: CMSMANAGER_AUTH
        }),
        {
            provide: ADMIN_AUTH_TOKEN,
            useValue: ADMIN_AUTH
        },
        {
            provide: CMSMANAGER_AUTH_TOKEN,
            useValue: CMSMANAGER_AUTH
        },
        moduleUtils.initialize(
            (
                httpBackendService: HttpBackendService,
                adminAuth: TypedMap<string>,
                cmsManagerAuth: TypedMap<string>
            ) => {
                httpBackendService.whenGET(/smartedit\/settings/).respond({
                    'smartedit.sso.enabled': 'false'
                });

                httpBackendService
                    .whenPOST(
                        stringUtils.resourceLocationToRegex(DEFAULT_AUTHENTICATION_ENTRY_POINT)
                    )
                    .respond((method, url, _data) => {
                        const data = urlUtils.parseQuery(_data) as any;

                        if (
                            data.client_id === 'smartedit' &&
                            data.client_secret === undefined &&
                            data.grant_type === 'password' &&
                            data.username === 'admin' &&
                            data.password === '1234'
                        ) {
                            return [200, adminAuth];
                        } else if (
                            data.client_id === 'smartedit' &&
                            data.client_secret === undefined &&
                            data.grant_type === 'password' &&
                            data.username === 'cmsmanager' &&
                            data.password === '1234'
                        ) {
                            return [200, cmsManagerAuth];
                        } else {
                            return [401, null];
                        }
                    });
            },
            [HttpBackendService, ADMIN_AUTH_TOKEN, CMSMANAGER_AUTH_TOKEN]
        )
    ]
})
export class OuterAuthorizationMocks {
    public static provide(providers: Provider[]): ModuleWithProviders {
        return {
            ngModule: OuterAuthorizationMocks,
            providers: [...providers]
        };
    }
}

window.pushModules(OuterAuthorizationMocks);
