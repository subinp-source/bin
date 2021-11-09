/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { moduleUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';
import { OuterWhoAmIMocks } from '../../utils/commonMockedModules/outerWhoAmIMocks';
import { i18nMocks } from '../../utils/commonMockedModules/outerI18nMock';
import { OuterSitesMocks } from '../../utils/commonMockedModules/outerSitesMock';
import { OuterPermissionMocks } from '../../utils/commonMockedModules/outerPermissionMocks';
import { languages } from '../common/outerCommonConstants';

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
                                    qualifier: 'description',
                                    i18nKey: 'type.thesmarteditComponentType.description.name',
                                    localized: false
                                }
                            ]
                        };

                        return [200, structure];
                    });

                let component: { uid: string; uuid: string } = {} as { uid: string; uuid: string };

                httpBackendService.whenPOST(/previewApi/).respond((method, url, data) => {
                    component = JSON.parse(data);
                    component.uid = Math.random()
                        .toString(36)
                        .substring(7);
                    component.uuid = Math.random()
                        .toString(36)
                        .substring(7);
                    return [200, component];
                });

                httpBackendService.whenGET(/previewApi\/([\w]+)/).respond((method, url) => {
                    const id = /previewApi\/([\w]+)/.exec(url)[1];
                    if (id === component.uuid) {
                        return [200, component];
                    } else {
                        return [404, null];
                    }
                });

                httpBackendService.whenPUT(/previewApi\/([\w]+)/).respond((method, url, data) => {
                    const id = /previewApi\/([\w]+)/.exec(url)[1];
                    component = JSON.parse(data);
                    if (id === component.uuid) {
                        return [200, {}];
                    } else {
                        return [404, {}];
                    }
                });

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/sites\/.*\/languages/)
                    .respond(languages);

                httpBackendService.whenGET(/i18n/).passThrough();
                httpBackendService.whenGET(/view/).passThrough(); // calls to storefront render API
                httpBackendService.whenPUT(/contentslots/).passThrough();
                httpBackendService.whenGET(/\.html/).passThrough();

                const map = [
                    {
                        value: '"previewwebservices/v1/preview"',
                        key: 'previewTicketURI'
                    },
                    {
                        value:
                            '{"smartEditContainerLocation":"/test/e2e/genericEditor/componentPOSTPUT/generated_outerGenericEditorApp.js"}',
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
