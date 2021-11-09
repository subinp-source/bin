/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ModuleWithProviders, NgModule, Provider } from '@angular/core';
import {
    moduleUtils,
    stringUtils,
    CONFIGURATION_URI,
    HttpBackendService,
    SeEntryModule,
    TypedMap
} from 'smarteditcommons';
import {
    ADMIN_AUTH,
    ADMIN_AUTH_TOKEN,
    CMSMANAGER_AUTH,
    CMSMANAGER_AUTH_TOKEN,
    CONFIGURATION_AUTHORIZED_TOKEN,
    CONFIGURATION_FORBIDDEN_TOKEN,
    CONFIGURATION_MOCK_TOKEN
} from '../outerConstants';

@SeEntryModule('OuterConfigurationMocks')
@NgModule({
    providers: [
        moduleUtils.provideValues({
            SMARTEDIT_RESOURCE_URI_REGEXP: /^(.*)\/test\/e2e/,
            SMARTEDIT_ROOT: 'web/webroot',
            CONFIGURATION_MOCK: [],
            CONFIGURATION_AUTHORIZED: false,
            CONFIGURATION_FORBIDDEN: false
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
            provide: CONFIGURATION_AUTHORIZED_TOKEN,
            useValue: false
        },
        {
            provide: CONFIGURATION_MOCK_TOKEN,
            useValue: []
        },
        {
            provide: CONFIGURATION_FORBIDDEN_TOKEN,
            useValue: false
        },
        moduleUtils.initialize(
            (
                httpBackendService: HttpBackendService,
                CONFIGURATION_AUTHORIZED: boolean,
                CONFIGURATION_FORBIDDEN: boolean,
                ADMIN_AUTH_VAL: TypedMap<string>,
                CMSMANAGER_AUTH_VAL: TypedMap<string>,
                CONFIGURATION_MOCK: TypedMap<string>[]
            ) => {
                let CONFIGURATIONS = [...CONFIGURATION_MOCK];

                httpBackendService
                    .whenGET(stringUtils.resourceLocationToRegex(CONFIGURATION_URI))
                    .respond((method, url, data, headers) => {
                        if (
                            !CONFIGURATION_AUTHORIZED ||
                            headers.Authorization === 'bearer ' + ADMIN_AUTH_VAL.access_token ||
                            headers.Authorization === 'bearer ' + CMSMANAGER_AUTH_VAL.access_token
                        ) {
                            return [200, CONFIGURATIONS];
                        } else {
                            return [401, null];
                        }
                    });

                httpBackendService
                    .whenPUT(stringUtils.resourceLocationToRegex(CONFIGURATION_URI))
                    .respond((method, url, data) => {
                        if (CONFIGURATION_FORBIDDEN) {
                            return [403, null];
                        }
                        const key = getConfigurationKeyFromUrl(url);
                        data = JSON.parse(data);
                        const entry = CONFIGURATIONS.find((_entry) => {
                            return _entry.key === key;
                        });
                        entry.value = data.value;
                        return [200, data];
                    });

                httpBackendService
                    .whenPOST(stringUtils.resourceLocationToRegex(CONFIGURATION_URI))
                    .respond((method, url, data) => {
                        if (CONFIGURATION_FORBIDDEN) {
                            return [403, null];
                        }
                        data = JSON.parse(data);
                        data.id = Math.random();
                        CONFIGURATIONS.push(data);
                        return [200, data];
                    });

                httpBackendService
                    .whenDELETE(stringUtils.resourceLocationToRegex(CONFIGURATION_URI))
                    .respond((method, url) => {
                        if (CONFIGURATION_FORBIDDEN) {
                            return [403, null];
                        }
                        const key = getConfigurationKeyFromUrl(url);
                        CONFIGURATIONS = CONFIGURATIONS.filter(function(entry) {
                            return entry.key !== key;
                        });
                        return [200, {}];
                    });

                function getConfigurationKeyFromUrl(url: string) {
                    return /configuration\/(.+)/.exec(url)[1];
                }
            },
            [
                HttpBackendService,
                CONFIGURATION_AUTHORIZED_TOKEN,
                CONFIGURATION_FORBIDDEN_TOKEN,
                ADMIN_AUTH_TOKEN,
                CMSMANAGER_AUTH_TOKEN,
                CONFIGURATION_MOCK_TOKEN
            ]
        )
    ]
})
export class OuterConfigurationMocks {
    static provide(providers: Provider[]): ModuleWithProviders {
        return {
            ngModule: OuterConfigurationMocks,
            providers: [...providers]
        };
    }
}

window.pushModules(OuterConfigurationMocks);
