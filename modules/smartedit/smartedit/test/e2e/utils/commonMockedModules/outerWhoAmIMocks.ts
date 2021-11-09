/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ModuleWithProviders, NgModule, Provider } from '@angular/core';
import { moduleUtils, HttpBackendService, SeEntryModule, TypedMap } from 'smarteditcommons';
import {
    ADMIN_AUTH,
    ADMIN_AUTH_TOKEN,
    ADMIN_WHOAMI_DATA,
    ADMIN_WHOAMI_DATA_TOKEN,
    CMSMANAGER_AUTH,
    CMSMANAGER_AUTH_TOKEN,
    CMSMANAGER_WHOAMI_DATA,
    CMSMANAGER_WHOAMI_DATA_TOKEN
} from '../outerConstants';

@SeEntryModule('OuterWhoAmIMocks')
@NgModule({
    providers: [
        moduleUtils.provideValues({
            ADMIN_AUTH_TOKEN: ADMIN_AUTH,
            CMSMANAGER_AUTH_TOKEN: CMSMANAGER_AUTH,
            ADMIN_WHOAMI_DATA,
            CMSMANAGER_WHOAMI_DATA
        }),
        {
            provide: ADMIN_AUTH_TOKEN,
            useValue: ADMIN_AUTH
        },
        {
            provide: CMSMANAGER_AUTH_TOKEN,
            useValue: CMSMANAGER_AUTH
        },
        {
            provide: ADMIN_WHOAMI_DATA_TOKEN,
            useValue: ADMIN_WHOAMI_DATA
        },
        {
            provide: CMSMANAGER_WHOAMI_DATA_TOKEN,
            useValue: CMSMANAGER_WHOAMI_DATA
        },
        moduleUtils.initialize(
            (
                httpBackendService: HttpBackendService,
                adminAuth: TypedMap<string>,
                cmsManagerAuth: TypedMap<string>,
                adminWhoamiData: TypedMap<string>,
                cmsManagerWhoamiData: TypedMap<string>
            ) => {
                httpBackendService
                    .whenGET(/authorizationserver\/oauth\/whoami/)
                    .respond(function(method, url, data, headers) {
                        return [
                            200,
                            headers.Authorization === 'bearer ' + adminAuth.access_token
                                ? adminWhoamiData
                                : cmsManagerWhoamiData
                        ];
                    });

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/users\/*/)
                    .respond(function(method, url) {
                        const userUid = url.substring(url.lastIndexOf('/') + 1);

                        return [
                            200,
                            {
                                uid: userUid,
                                readableLanguages: ['de', 'ja', 'en', 'zh'],
                                writeableLanguages: ['de', 'ja', 'en', 'zh']
                            }
                        ];
                    });
            },
            [
                HttpBackendService,
                ADMIN_AUTH_TOKEN,
                CMSMANAGER_AUTH_TOKEN,
                ADMIN_WHOAMI_DATA_TOKEN,
                CMSMANAGER_WHOAMI_DATA_TOKEN
            ]
        )
    ]
})
export class OuterWhoAmIMocks {
    public static provide(providers: Provider[]): ModuleWithProviders {
        return {
            ngModule: OuterWhoAmIMocks,
            providers: [...providers]
        };
    }
}

window.pushModules(OuterWhoAmIMocks);
