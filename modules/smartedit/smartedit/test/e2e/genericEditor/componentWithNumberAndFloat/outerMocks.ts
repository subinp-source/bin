/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { moduleUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';
import { OuterWhoAmIMocks } from '../../utils/commonMockedModules/outerWhoAmIMocks';
import { i18nMocks } from '../../utils/commonMockedModules/outerI18nMock';
import { OuterSitesMocks } from '../../utils/commonMockedModules/outerSitesMock';
import { OuterPermissionMocks } from '../../utils/commonMockedModules/outerPermissionMocks';
import { URL_FOR_ITEM } from '../common/outerCommonConstants';

@SeEntryModule('ConfigurationMocksModule')
@NgModule({
    imports: [OuterWhoAmIMocks, i18nMocks, OuterSitesMocks, OuterPermissionMocks],
    providers: [
        moduleUtils.bootstrap(
            (httpBackendService: HttpBackendService) => {
                httpBackendService.matchLatestDefinitionEnabled(true);
                httpBackendService
                    .whenGET(/cmswebservices\/v1\/types\/thesmarteditComponentType/)
                    .respond(function() {
                        const structure = {
                            attributes: [
                                {
                                    cmsStructureType: 'ShortString',
                                    qualifier: 'id',
                                    i18nKey: 'type.thesmarteditComponentType.id.name'
                                },
                                {
                                    cmsStructureType: 'Number',
                                    qualifier: 'quantity',
                                    i18nKey: 'type.thesmarteditComponentType.quantity.name'
                                },
                                {
                                    cmsStructureType: 'Float',
                                    qualifier: 'price',
                                    i18nKey: 'type.thesmarteditComponentType.price.name'
                                }
                            ]
                        };

                        return [200, structure];
                    });

                let component = {
                    id: 'Component ID',
                    quantity: 10,
                    price: 100.15
                };

                httpBackendService.whenGET(URL_FOR_ITEM).respond(component);
                httpBackendService.whenPUT(URL_FOR_ITEM).respond(function(method, url, data) {
                    component = JSON.parse(data);
                    return [200, component];
                });

                httpBackendService.whenGET(/i18n/).passThrough();
                httpBackendService.whenGET(/view/).passThrough(); // calls to storefront render API
                httpBackendService.whenPUT(/contentslots/).passThrough();
                httpBackendService.whenGET(/\.html/).passThrough();

                httpBackendService.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
                    languages: [
                        {
                            nativeName: 'English',
                            isocode: 'en',
                            required: true
                        },
                        {
                            nativeName: 'Polish',
                            isocode: 'pl'
                        },
                        {
                            nativeName: 'Italian',
                            isocode: 'it'
                        }
                    ]
                });

                const map = [
                    {
                        value: '"previewwebservices/v1/preview"',
                        key: 'previewTicketURI'
                    },
                    {
                        value:
                            '{"smartEditContainerLocation":"/test/e2e/genericEditor/generated_outerGenericEditorApp.js"}',
                        key: 'applications.GenericEditorApp'
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
            [HttpBackendService]
        )
    ]
})
export class ConfigurationMocksModule {}

window.pushModules(ConfigurationMocksModule);
