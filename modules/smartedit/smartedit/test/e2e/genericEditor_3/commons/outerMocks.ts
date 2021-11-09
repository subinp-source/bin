/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { moduleUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';
import { ComponentMocks } from './mocks/outerComponentMocks';
import { LanguageMocks } from './mocks/outerLanguageMocks';
import { TempMocks } from './mocks/outerTempMocks';
import { UserMocks } from './mocks/outerUserMocks';
import { i18nMocks } from '../../utils/commonMockedModules/outerI18nMock';
import { OuterAuthorizationMocks } from '../../utils/commonMockedModules/outerAuthorizationMock';
import { OuterSitesMocks } from '../../utils/commonMockedModules/outerSitesMock';
import { OuterWhoAmIMocks } from '../../utils/commonMockedModules/outerWhoAmIMocks';
import { OuterPermissionMocks } from '../../utils/commonMockedModules/outerPermissionMocks';
import { OuterLanguagesMocks } from '../../utils/commonMockedModules/outerLanguagesMock';
import { OuterPreviewMocks } from '../../utils/commonMockedModules/outerPreviewMock';

@SeEntryModule('ConfigurationMocksModule')
@NgModule({
    imports: [
        ComponentMocks,
        LanguageMocks,
        TempMocks,
        UserMocks,
        i18nMocks,
        OuterLanguagesMocks,
        OuterAuthorizationMocks,
        OuterSitesMocks,
        OuterWhoAmIMocks,
        OuterPermissionMocks,
        OuterPreviewMocks
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
                            '{"smartEditContainerLocation":"/test/e2e/genericEditor_3/commons/generated_outerGenericEditorApp.js"}',
                        key: 'applications.GenericEditorApp'
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
