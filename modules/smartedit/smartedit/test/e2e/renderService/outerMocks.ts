/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { NgModule } from '@angular/core';
import { UpgradeModule } from '@angular/upgrade/static';

import {
    moduleUtils,
    AngularJSBootstrapIndicatorService,
    HttpBackendService,
    IRenderService,
    IToolbarServiceFactory,
    SeEntryModule,
    ToolbarItemType
} from 'smarteditcommons';
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
        OuterOtherMocks,
        E2eOnLoadingSetupModule,
        OuterPermissionMocks
    ],

    providers: [
        moduleUtils.provideValues({
            SMARTEDIT_ROOT: 'web/webroot',
            SMARTEDIT_RESOURCE_URI_REGEXP: /^(.*)\/test\/e2e/
        }),
        moduleUtils.bootstrap(
            (
                httpBackendService: HttpBackendService,
                indicator: AngularJSBootstrapIndicatorService,
                upgrade: UpgradeModule
            ) => {
                httpBackendService.matchLatestDefinitionEnabled(true);

                const map = [
                    {
                        value: '"thepreviewTicketURI"',
                        key: 'previewTicketURI'
                    },
                    {
                        key: 'applications.InnerRenderDecorators',
                        value:
                            '{"smartEditLocation":"/test/e2e/utils/decorators/generated_innerRenderDecorators.js"}'
                    },
                    {
                        value: '"somepath"',
                        key: 'i18nAPIRoot'
                    }
                ];

                httpBackendService.whenGET(/smartedit\/configuration/).respond(function() {
                    return [200, map];
                });

                indicator.onSmarteditContainerReady().subscribe(() => {
                    const toolbarService = upgrade.injector
                        .get(IToolbarServiceFactory)
                        .getToolbarService('smartEditPerspectiveToolbar');
                    toolbarService.addItems([
                        {
                            key: 'toolbar.action.render.component',
                            type: ToolbarItemType.ACTION,
                            nameI18nKey: 'toolbar.action.render.component',
                            priority: 1,
                            callback() {
                                upgrade.injector
                                    .get(IRenderService)
                                    .renderComponent('component1', 'componentType1');
                            },
                            icons: ['render.png']
                        },
                        {
                            key: 'toolbar.action.render.slot',
                            type: ToolbarItemType.ACTION,
                            nameI18nKey: 'toolbar.action.render.slot',
                            priority: 2,
                            callback() {
                                upgrade.injector.get(IRenderService).renderSlots(['topHeaderSlot']);
                            },
                            icons: ['render.png']
                        }
                    ]);
                });
            },
            [HttpBackendService, AngularJSBootstrapIndicatorService, UpgradeModule]
        )
    ]
})
export class OuterMocksModule {}

window.pushModules(OuterMocksModule);
