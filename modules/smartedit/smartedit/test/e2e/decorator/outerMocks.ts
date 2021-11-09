/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { moduleUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';
import { OuterWhoAmIMocks } from '../utils/commonMockedModules/outerWhoAmIMocks';
import { i18nMocks } from '../utils/commonMockedModules/outerI18nMock';
import { OuterSitesMocks } from '../utils/commonMockedModules/outerSitesMock';
import { OuterPermissionMocks } from '../utils/commonMockedModules/outerPermissionMocks';
import { E2eOnLoadingSetupModule } from '../outerE2eOnLoadingSetup';
import { OuterPreviewMocks } from '../utils/commonMockedModules/outerPreviewMock';
import { OuterLanguagesMocks } from '../utils/commonMockedModules/outerLanguagesMock';
import { OuterAuthorizationMocks } from '../utils/commonMockedModules/outerAuthorizationMock';
import { OuterConfigurationMocks } from '../utils/commonMockedModules/outerConfigurationMock';
import { OuterOtherMocks } from '../utils/commonMockedModules/outerOtherMock';

@SeEntryModule('ConfigurationMocksModule')
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

                httpBackendService.whenGET(/test\/e2e/).passThrough();
                httpBackendService.whenGET(/static-resources/).passThrough();

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
                            '{"smartEditLocation":"/test/e2e/utils/decorators/generated_outerDummyDecorators.js"}',
                        key: 'applications.DummyDecoratorsModule'
                    }
                ];

                httpBackendService.whenGET(/smartedit\/configuration/).respond(() => {
                    return [200, map];
                });
            },
            [HttpBackendService]
        )
    ]
})
export class ConfigurationMocksModule {}

window.pushModules(ConfigurationMocksModule);
