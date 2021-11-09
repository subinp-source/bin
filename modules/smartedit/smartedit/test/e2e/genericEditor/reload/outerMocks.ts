/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import {
    moduleUtils,
    HttpBackendService,
    ISharedDataService,
    SeEntryModule
} from 'smarteditcommons';
import { OuterWhoAmIMocks } from '../../utils/commonMockedModules/outerWhoAmIMocks';
import { i18nMocks } from '../../utils/commonMockedModules/outerI18nMock';
import { OuterPermissionMocks } from '../../utils/commonMockedModules/outerPermissionMocks';
import { OuterAuthorizationMocks } from '../../utils/commonMockedModules/outerAuthorizationMock';
import { OuterConfigurationMocks } from '../../utils/commonMockedModules/outerConfigurationMock';
import { OuterLanguagesMocks } from '../../utils/commonMockedModules/outerLanguagesMock';
import { OuterPreviewMocks } from '../../utils/commonMockedModules/outerPreviewMock';
import { OuterSitesMocks } from '../../utils/commonMockedModules/outerSitesMock';

@SeEntryModule('ConfigurationMocksModule')
@NgModule({
    imports: [
        OuterAuthorizationMocks,
        OuterWhoAmIMocks,
        OuterConfigurationMocks,
        OuterLanguagesMocks,
        OuterPreviewMocks,
        i18nMocks,
        OuterSitesMocks,
        OuterPermissionMocks
    ],
    providers: [
        moduleUtils.bootstrap(
            (httpBackendService: HttpBackendService, sharedDataService: ISharedDataService) => {
                httpBackendService.matchLatestDefinitionEnabled(true);
                const DEFAULT_STRUCTURE = {
                    attributes: [
                        {
                            cmsStructureType: 'ShortString',
                            qualifier: 'name',
                            i18nKey: 'type.anyComponentType.name.name'
                        }
                    ]
                };
                const ANY_STRUCTURE = {
                    attributes: [
                        {
                            cmsStructureType: 'ShortString',
                            qualifier: 'headline',
                            i18nKey: 'type.anyComponentType.headline.name'
                        },
                        {
                            cmsStructureType: 'Boolean',
                            qualifier: 'active',
                            i18nKey: 'type.anyComponentType.active.name'
                        },
                        {
                            cmsStructureType: 'LongString',
                            qualifier: 'comments',
                            i18nKey: 'type.anyComponentType.comments.name'
                        }
                    ]
                };

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/types\/defaultComponent/)
                    .respond(() => {
                        return [200, DEFAULT_STRUCTURE];
                    });

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/types\/anotherComponent/)
                    .respond(() => {
                        return [200, ANY_STRUCTURE];
                    });

                httpBackendService
                    .whenGET(
                        /cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/items\/anyComponentId/
                    )
                    .respond(() => {
                        return [
                            200,
                            {
                                type: 'anyComponentData',
                                name: 'Any name',
                                pk: '1234567890',
                                typeCode: 'AnyComponent',
                                uid: 'ApparelDEAnyComponent',
                                visible: true
                            }
                        ];
                    });

                httpBackendService
                    .whenPUT(
                        /cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/items\/anyComponentId/
                    )
                    .respond(() => {
                        return [
                            200,
                            {
                                type: 'anyComponentData',
                                name: 'some new name',
                                pk: '1234567890',
                                richtext: '<strong>Any rich text here...</strong>',
                                typeCode: 'AnyComponent',
                                uid: 'ApparelDEAnyComponent',
                                visible: true
                            }
                        ];
                    });

                httpBackendService
                    .whenPOST(
                        /cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/items/
                    )
                    .respond(() => {
                        return [
                            200,
                            {
                                type: 'anyComponentData',
                                name: 'new component name',
                                pk: '1234567890',
                                typeCode: 'AnyComponent',
                                uid: 'ApparelDEAnyComponent',
                                visible: true,
                                richtext: '',
                                componentCustomField: 'custom value'
                            }
                        ];
                    });

                sharedDataService.set('experience', {
                    siteDescriptor: {
                        uid: 'apparel-uk'
                    },
                    catalogDescriptor: {
                        catalogId: 'apparel-ukContentCatalog',
                        catalogVersion: 'Staged',
                        uuid: 'apparel-ukContentCatalog/Staged'
                    },
                    pageContext: {
                        catalogId: 'apparel-ukContentCatalog',
                        catalogVersion: 'Staged',
                        uuid: 'apparel-ukContentCatalog/Staged',
                        siteId: 'apparel-uk'
                    }
                });

                const map = [
                    {
                        value: '"previewwebservices/v1/preview"',
                        key: 'previewTicketURI'
                    },
                    {
                        value:
                            '{"smartEditContainerLocation":"/test/e2e/genericEditor/reload/generated_outerapp.js"}',
                        key: 'applications.Outerapp'
                    },

                    {
                        value: '"/cmswebservices/v1/i18n/languages"',
                        key: 'i18nAPIRoot'
                    }
                ];

                httpBackendService.whenGET(/smartedit\/configuration/).respond(() => {
                    return [200, map];
                });
            },
            [HttpBackendService, ISharedDataService]
        )
    ]
})
export class ConfigurationMocksModule {}

window.pushModules(ConfigurationMocksModule);
