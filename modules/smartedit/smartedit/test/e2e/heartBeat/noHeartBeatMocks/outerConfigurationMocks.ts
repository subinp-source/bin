/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { NgModule } from '@angular/core';

import { moduleUtils, SeEntryModule } from 'smarteditcommons';

import { OuterAuthorizationMocks } from '../../utils/commonMockedModules/outerAuthorizationMock';
import { OuterConfigurationMocks } from '../../utils/commonMockedModules/outerConfigurationMock';
import { OuterWhoAmIMocks } from '../../utils/commonMockedModules/outerWhoAmIMocks';
import { OuterPermissionMocks } from '../../utils/commonMockedModules/outerPermissionMocks';
import { OuterLanguagesMocks } from '../../utils/commonMockedModules/outerLanguagesMock';
import { E2eOnLoadingSetupModule } from '../../outerE2eOnLoadingSetup';
import { i18nMocks } from '../../utils/commonMockedModules/outerI18nMock';
import { OuterPreviewMocks } from '../../utils/commonMockedModules/outerPreviewMock';
import { CONFIGURATION_MOCK_TOKEN, STOREFRONT_URI_TOKEN } from '../../utils/outerConstants';

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
            }
        ]),
        OuterPermissionMocks,
        OuterLanguagesMocks,
        OuterPreviewMocks,
        i18nMocks,
        E2eOnLoadingSetupModule.provide([
            {
                provide: STOREFRONT_URI_TOKEN,
                useValue:
                    'http://127.0.0.1:9000/test/e2e/heartBeat/noHeartBeatMocks/storefront.html'
            }
        ])
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
