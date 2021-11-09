/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { doImport } from './forcedImports';
doImport();

import * as angular from 'angular';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import {
    BootstrapService,
    LegacySmarteditServicesModule,
    LoadConfigManagerService
} from 'smarteditcontainer/services';
import {
    commonNgZone,
    BootstrapPayload,
    SeModule,
    SMARTEDITCONTAINER_COMPONENT_NAME,
    SMARTEDITLOADER_COMPONENT_NAME,
    SSOAuthenticationHelper
} from 'smarteditcommons';
import { ConfigurationObject } from 'smarteditcontainer/services/bootstrap/Configuration';
import { SmarteditContainerFactory } from 'smarteditcontainer/smarteditcontainer';

@SeModule({
    imports: [
        LegacySmarteditServicesModule,
        'templateCacheDecoratorModule',
        'coretemplates',
        'translationServiceModule'
    ],
    config: ($logProvider: angular.ILogProvider) => {
        'ngInject';
        $logProvider.debugEnabled(false);
    },
    providers: [BootstrapService],
    initialize: (
        ssoAuthenticationHelper: SSOAuthenticationHelper,
        loadConfigManagerService: LoadConfigManagerService,
        bootstrapService: BootstrapService
    ) => {
        'ngInject';

        if (ssoAuthenticationHelper.isSSODialog()) {
            ssoAuthenticationHelper.completeDialog();
        } else {
            loadConfigManagerService.loadAsObject().then((configurations: ConfigurationObject) => {
                bootstrapService
                    .bootstrapContainerModules(configurations)
                    .then((bootstrapPayload: BootstrapPayload) => {
                        const smarteditloaderNode = document.querySelector(
                            SMARTEDITLOADER_COMPONENT_NAME
                        );
                        smarteditloaderNode.parentNode.insertBefore(
                            document.createElement(SMARTEDITCONTAINER_COMPONENT_NAME),
                            smarteditloaderNode
                        );

                        platformBrowserDynamic()
                            .bootstrapModule(SmarteditContainerFactory(bootstrapPayload), {
                                ngZone: commonNgZone
                            })
                            .then((ref: any) => {
                                //
                            })
                            .catch((err) => console.log(err));
                    });
            });
        }
    }
})
export class Smarteditloader {}
