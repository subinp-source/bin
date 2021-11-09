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
import { OuterPermissionMocks } from '../utils/commonMockedModules/outerPermissionMocks';
import { OuterPreviewMocks } from '../utils/commonMockedModules/outerPreviewMock';
import { E2eOnLoadingSetupModule } from '../outerE2eOnLoadingSetup';
import { OuterSitesMocks } from '../utils/commonMockedModules/outerSitesMock';

@SeEntryModule('OuterMocksModule')
@NgModule({
    imports: [
        OuterAuthorizationMocks,
        OuterWhoAmIMocks,
        OuterConfigurationMocks,
        i18nMocks,
        OuterLanguagesMocks,
        OuterOtherMocks,
        OuterPermissionMocks,
        OuterPreviewMocks,
        OuterSitesMocks,
        E2eOnLoadingSetupModule
    ],
    providers: [
        moduleUtils.provideValues({
            SMARTEDIT_ROOT: 'web/webroot',
            SMARTEDIT_RESOURCE_URI_REGEXP: /^(.*)\/test\/e2e/
        }),
        moduleUtils.bootstrap(
            (httpBackendService: HttpBackendService) => {
                httpBackendService.matchLatestDefinitionEnabled(true);

                httpBackendService.whenGET(/test\/e2e/).passThrough();
                httpBackendService.whenGET(/static-resources/).passThrough();

                const map = [
                    {
                        value:
                            '{"smartEditContainerLocation":"/test/e2e/a_httpErrorInterceptorService/generated_outerapp.js"}',
                        key: 'applications.Outerapp'
                    },
                    {
                        value: '"thepreviewTicketURI"',
                        key: 'previewTicketURI'
                    },
                    {
                        value: '"/cmswebservices/v1/i18n/languages"',
                        key: 'i18nAPIRoot'
                    }
                ];

                httpBackendService.whenGET(/smartedit\/configuration/).respond(() => {
                    return [200, map];
                });
                httpBackendService.whenGET(/error404_json/).respond(() => {
                    return [
                        404,
                        {
                            errors: []
                        }
                    ];
                });
                httpBackendService.whenGET(/error400_json/).respond(() => {
                    return [
                        400,
                        {
                            errors: [
                                {
                                    type: 'ValidationError',
                                    message: 'validation error'
                                },
                                {
                                    message: 'error: bad request'
                                }
                            ]
                        }
                    ];
                });
                httpBackendService.whenGET(/error404_html/).respond(() => {
                    return [404, null];
                });
                httpBackendService.whenGET(/error501_json/).respond(() => {
                    return [
                        501,
                        {
                            errors: [
                                {
                                    message: 'error: 501 bad request'
                                }
                            ]
                        }
                    ];
                });
                httpBackendService.whenGET(/error503\/.*\/v1\/.*/).respond(() => {
                    return [503, null];
                });

                let getError502Attempt = 0;
                httpBackendService.whenGET(/error502\/retry/).respond(() => {
                    getError502Attempt++;
                    return [getError502Attempt === 2 ? 200 : 502, null];
                });
            },
            [HttpBackendService]
        )
    ]
})
export class OuterMocksModule {}

window.pushModules(OuterMocksModule);
