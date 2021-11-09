/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { NgModule } from '@angular/core';

import { moduleUtils, SeEntryModule } from 'smarteditcommons';
import { CONFIGURATION_AUTHORIZED_TOKEN, CONFIGURATION_MOCK_TOKEN } from '../utils/outerConstants';

import { OuterAuthorizationMocks } from '../utils/commonMockedModules/outerAuthorizationMock';
import { OuterWhoAmIMocks } from '../utils/commonMockedModules/outerWhoAmIMocks';
import { OuterConfigurationMocks } from '../utils/commonMockedModules/outerConfigurationMock';
import { i18nMocks } from '../utils/commonMockedModules/outerI18nMock';
import { OuterLanguagesMocks } from '../utils/commonMockedModules/outerLanguagesMock';
import { OuterPermissionMocks } from '../utils/commonMockedModules/outerPermissionMocks';
import { OuterOtherMocks } from '../utils/commonMockedModules/outerOtherMock';
import { OuterPreviewMocks } from '../utils/commonMockedModules/outerPreviewMock';
import { E2eOnLoadingSetupModule } from '../outerE2eOnLoadingSetup';

@SeEntryModule('OuterMocksModule')
@NgModule({
    imports: [
        OuterAuthorizationMocks,
        OuterWhoAmIMocks,
        OuterConfigurationMocks.provide([
            {
                provide: CONFIGURATION_MOCK_TOKEN,
                useValue: [
                    {
                        value: '"thepreviewTicketURI"',
                        key: 'previewTicketURI'
                    },
                    {
                        value: '{malformed}',
                        key: 'i18nAPIRoot'
                    }
                ]
            },
            {
                provide: CONFIGURATION_AUTHORIZED_TOKEN,
                useValue: true
            }
        ]),
        i18nMocks,
        OuterLanguagesMocks,
        OuterPermissionMocks,
        OuterOtherMocks,
        OuterPreviewMocks,
        E2eOnLoadingSetupModule
    ],
    providers: [
        moduleUtils.provideValues({
            SMARTEDIT_ROOT: 'web/webroot',
            SMARTEDIT_RESOURCE_URI_REGEXP: /^(.*)\/test\/e2e/
        })
    ]
})
export class OuterMocksModule {}

window.pushModules(OuterMocksModule);
