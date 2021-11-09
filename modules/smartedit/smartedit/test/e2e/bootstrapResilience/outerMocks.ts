/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { InjectionToken, NgModule } from '@angular/core';

import { moduleUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';

import { OuterAuthorizationMocks } from '../utils/commonMockedModules/outerAuthorizationMock';
import { OuterWhoAmIMocks } from '../utils/commonMockedModules/outerWhoAmIMocks';
import { i18nMocks } from '../utils/commonMockedModules/outerI18nMock';
import { OuterLanguagesMocks } from '../utils/commonMockedModules/outerLanguagesMock';
import { OuterPreviewMocks } from '../utils/commonMockedModules/outerPreviewMock';
import { OuterSitesMocks } from '../utils/commonMockedModules/outerSitesMock';
import { OuterPermissionMocks } from '../utils/commonMockedModules/outerPermissionMocks';
import { E2eOnLoadingSetupModule } from '../outerE2eOnLoadingSetup';

export const DUMMY_SERVICE_CLASS_TOKEN = new InjectionToken('DUMMY_SERVICE_CLASS_TOKEN');

@SeEntryModule('OuterMocksModule')
@NgModule({
    imports: [
        OuterAuthorizationMocks,
        OuterWhoAmIMocks,
        i18nMocks,
        OuterLanguagesMocks,
        OuterPreviewMocks,
        OuterSitesMocks,
        OuterPermissionMocks,
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

                const map = [
                    {
                        value: '"thepreviewTicketURI"',
                        key: 'previewTicketURI'
                    },
                    {
                        value: '"somepath"',
                        key: 'i18nAPIRoot'
                    },

                    {
                        value:
                            '{"smartEditContainerLocation":"/test/e2e/bootstrapResilience/generated_outerExtendingModule.js", "extends":"OuterDummyToolbars"}',
                        key: 'applications.OuterExtendingModule'
                    },
                    {
                        value:
                            '{"smartEditContainerLocation":"/path/to/some/non/existent/container/script.js"}',
                        key: 'applications.nonExistentSmartEditContainerModule'
                    },
                    {
                        value:
                            '{"smartEditLocation":"/path/to/some/non/existent/application/script.js"}',
                        key: 'applications.nonExistentSmartEditModule'
                    },
                    {
                        value:
                            '{"smartEditLocation":"/test/e2e/bootstrapResilience/generated_innerDummyCmsDecorators.js"}',
                        key: 'applications.InnerDummyCmsDecoratorsModule'
                    },

                    {
                        value:
                            '{"smartEditContainerLocation":"/test/e2e/bootstrapResilience/generated_outerDummyToolbars.js"}',
                        key: 'applications.OuterDummyToolbars'
                    },
                    {
                        value:
                            '{"smartEditLocation":"/test/e2e/bootstrapResilience/generated_innerExtendingModule.js" , "extends":"InnerDummyCmsDecoratorsModule"}',
                        key: 'applications.InnerExtendingModule'
                    }
                ];

                httpBackendService.whenGET(/smartedit\/configuration/).respond(function() {
                    return [200, map];
                });

                httpBackendService.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
                    languages: [
                        {
                            nativeName: 'English',
                            isocode: 'en',
                            name: 'English',
                            required: true
                        }
                    ]
                });

                httpBackendService.whenGET(/^\w+.*/).passThrough();
            },
            [HttpBackendService]
        )
    ]
})
export class OuterMocksModule {}

window.pushModules(OuterMocksModule);
