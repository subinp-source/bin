/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { NgModule } from '@angular/core';

import { moduleUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';
import { OuterAuthorizationMocks } from '../utils/commonMockedModules/outerAuthorizationMock';
import { OuterWhoAmIMocks } from '../utils/commonMockedModules/outerWhoAmIMocks';
import { OuterConfigurationMocks } from '../utils/commonMockedModules/outerConfigurationMock';
import { i18nMocks } from '../utils/commonMockedModules/outerI18nMock';
import { OuterLanguagesMocks } from '../utils/commonMockedModules/outerLanguagesMock';
import { OuterOtherMocks } from '../utils/commonMockedModules/outerOtherMock';
import { OuterPreviewMocks } from '../utils/commonMockedModules/outerPreviewMock';
import { OuterSitesMocks } from '../utils/commonMockedModules/outerSitesMock';
import { E2eOnLoadingSetupModule } from '../outerE2eOnLoadingSetup';
import { OuterPermissionMocks } from '../utils/commonMockedModules/outerPermissionMocks';
import { urlUtils } from '@smart/utils';

@SeEntryModule('OuterMocksModule')
@NgModule({
    imports: [
        OuterAuthorizationMocks,
        OuterWhoAmIMocks,
        OuterConfigurationMocks,
        i18nMocks,
        OuterLanguagesMocks,
        OuterOtherMocks,
        OuterPreviewMocks,
        OuterSitesMocks,
        OuterPreviewMocks,
        E2eOnLoadingSetupModule,
        OuterPermissionMocks
    ],

    providers: [
        moduleUtils.provideValues({
            SMARTEDIT_ROOT: 'web/webroot',
            SMARTEDIT_RESOURCE_URI_REGEXP: /^(.*)\/test\/e2e/
        }),
        moduleUtils.bootstrap(
            (httpBackendService: HttpBackendService) => {
                httpBackendService.matchLatestDefinitionEnabled(true);

                httpBackendService.whenGET(/cmswebservices\/sites\/.*\/languages/).respond({
                    languages: [
                        {
                            nativeName: 'English',
                            isocode: 'en',
                            name: 'English',
                            required: true
                        }
                    ]
                });

                const oauthToken0 = {
                    access_token: 'access-token0',
                    token_type: 'bearer'
                };

                const oauthToken1 = {
                    access_token: 'access-token1',
                    token_type: 'bearer'
                };

                const oauthToken2 = {
                    access_token: 'access-token2',
                    token_type: 'bearer'
                };
                httpBackendService
                    .whenPOST(/authorizationserver\/oauth\/token/)
                    .respond(function(method, url, data) {
                        const query = urlUtils.parseQuery(data) as any;
                        if (
                            query.client_id === 'smartedit' &&
                            query.client_secret === undefined &&
                            query.grant_type === 'password' &&
                            query.username === 'cmsmanager' &&
                            query.password === '1234'
                        ) {
                            return [200, oauthToken0];
                        } else {
                            return [401, null];
                        }
                    });

                httpBackendService
                    .whenGET(/configuration/)
                    .respond(function(method, url, data, headers) {
                        if (headers.Authorization === 'bearer ' + oauthToken0.access_token) {
                            return [
                                200,
                                [
                                    {
                                        value: '"/thepreviewTicketURI"',
                                        key: 'previewTicketURI'
                                    },
                                    {
                                        value:
                                            '{"/authEntryPoint1":{"client_id":"client_id_1","client_secret":"client_secret_1"},"/authEntryPoint2":{"client_id":"client_id_2","client_secret":"client_secret_2"}}',
                                        key: 'authentication.credentials'
                                    },
                                    {
                                        value: '{ "api2":"/authEntryPoint2"}',
                                        key: 'authenticationMap'
                                    }
                                ]
                            ];
                        } else {
                            return [401, null];
                        }
                    });

                httpBackendService
                    .whenGET(/api1\/somepath/)
                    .respond(function(method, url, data, headers) {
                        if (headers.Authorization === 'bearer ' + oauthToken1.access_token) {
                            return [
                                200,
                                {
                                    status: 'OK'
                                }
                            ];
                        } else {
                            return [401, null];
                        }
                    });

                httpBackendService
                    .whenGET(/api2\/someotherpath/)
                    .respond(function(method, url, data, headers) {
                        if (headers.Authorization === 'bearer ' + oauthToken2.access_token) {
                            return [
                                200,
                                {
                                    status: 'OK'
                                }
                            ];
                        } else {
                            return [401, null];
                        }
                    });

                httpBackendService.whenGET(/fragments/).passThrough();
            },
            [HttpBackendService]
        )
    ]
})
export class OuterMocksModule {}

window.pushModules(OuterMocksModule);
