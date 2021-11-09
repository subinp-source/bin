/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { moduleUtils, urlUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';
import { OuterWhoAmIMocks } from '../../utils/commonMockedModules/outerWhoAmIMocks';
import { i18nMocks } from '../../utils/commonMockedModules/outerI18nMock';
import { OuterSitesMocks } from '../../utils/commonMockedModules/outerSitesMock';
import { OuterPermissionMocks } from '../../utils/commonMockedModules/outerPermissionMocks';
import { languages, URL_FOR_ITEM } from '../common/outerCommonConstants';

@SeEntryModule('OuterMocksModule')
@NgModule({
    imports: [OuterWhoAmIMocks, i18nMocks, OuterSitesMocks, OuterPermissionMocks],
    providers: [
        moduleUtils.bootstrap(
            (httpBackendService: HttpBackendService) => {
                httpBackendService.matchLatestDefinitionEnabled(true);

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/types\/thesmarteditComponentType/)
                    .respond(() => {
                        const structure = {
                            attributes: [
                                {
                                    cmsStructureType: 'EditableDropdown',
                                    qualifier: 'dropdownA',
                                    i18nKey: 'type.thesmarteditComponentType.dropdownA.name',
                                    uri: '/sampleOptionsAPI0',
                                    paged: 'true'
                                },
                                {
                                    cmsStructureType: 'EditableDropdown',
                                    qualifier: 'dropdownB',
                                    i18nKey: 'type.thesmarteditComponentType.dropdownB.name',
                                    uri: '/sampleOptionsAPI1',
                                    dependsOn: 'dropdownA'
                                },
                                {
                                    cmsStructureType: 'EditableDropdown',
                                    qualifier: 'dropdownC',
                                    i18nKey: 'type.thesmarteditComponentType.dropdownC.name',
                                    uri: '/sampleOptionsAPI2',
                                    dependsOn: 'dropdownA',
                                    collection: true
                                },
                                {
                                    cmsStructureType: 'EditableDropdown',
                                    qualifier: 'dropdownD',
                                    i18nKey: 'type.thesmarteditComponentType.dropdownD.name',
                                    options: [
                                        {
                                            id: '1',
                                            label: 'OptionD1-sample'
                                        },
                                        {
                                            id: '2',
                                            label: 'OptionD2-sample-element'
                                        },
                                        {
                                            id: '3',
                                            label: 'OptionD3-element'
                                        }
                                    ],
                                    collection: true
                                },
                                {
                                    cmsStructureType: 'EditableDropdown',
                                    qualifier: 'dropdownE',
                                    i18nKey: 'type.thesmarteditComponentType.dropdownE.name',
                                    uri: '/sampleOptionsAPI3',
                                    dependsOn: 'dropdownB'
                                }
                            ]
                        };

                        return [200, structure];
                    });

                const sampleOptionsAPI0Response = [
                    {
                        id: '1',
                        label: 'OptionA1'
                    },
                    {
                        id: '2',
                        label: 'OptionA2'
                    },
                    {
                        id: '3',
                        label: 'OptionA3'
                    }
                ];

                httpBackendService.whenGET(/sampleOptionsAPI0/).respond((_, __, data) => {
                    const query = urlUtils.parseQuery(data) as any;
                    const mask = query.mask as string;
                    // paging ignored in mock: just one page
                    // query.pageSize;
                    // query.currentPage;

                    const options =
                        sampleOptionsAPI0Response.filter(
                            (option) =>
                                !mask || option.label.toLowerCase().indexOf(mask.toLowerCase()) >= 0
                        ) || [];
                    const pagedResults = {
                        pagination: {
                            count: options.length,
                            page: query.currentPage,
                            totalCount: options.length,
                            totalPages: 1
                        },
                        products: options
                    };
                    return [200, pagedResults];
                });

                const sampleOptionsAPI1Response = [
                    {
                        id: '1',
                        parent: '1',
                        label: 'OptionB1-A1'
                    },
                    {
                        id: '2',
                        parent: '1',
                        label: 'OptionB2-A1'
                    },
                    {
                        id: '7',
                        parent: '1',
                        label: 'OptionB7-A1-A2'
                    },
                    {
                        id: '7',
                        parent: '2',
                        label: 'OptionB7-A1-A2'
                    },
                    {
                        id: '3',
                        parent: '2',
                        label: 'OptionB3-A2'
                    },
                    {
                        id: '4',
                        parent: '2',
                        label: 'OptionB4-A2'
                    },
                    {
                        id: '5',
                        parent: '3',
                        label: 'OptionB5-A3'
                    },
                    {
                        id: '6',
                        parent: '3',
                        label: 'OptionB6-A3'
                    }
                ];

                httpBackendService.whenGET(/sampleOptionsAPI0\/(.+)/).respond((_, url) => {
                    const id = /sampleOptionsAPI0\/(.+)/.exec(url)[1];
                    const item = sampleOptionsAPI0Response.find((option) => option.id === id);

                    return [200, item];
                });

                httpBackendService.whenGET(/sampleOptionsAPI1/).respond((_method, _url, data) => {
                    const query = urlUtils.parseQuery(data) as any;
                    const filterId = query.dropdownA as string;

                    const response: { options: typeof sampleOptionsAPI1Response[0][] } = {
                        options: undefined
                    };
                    response.options =
                        sampleOptionsAPI1Response.filter((option) => option.parent === filterId) ||
                        [];

                    return [200, response];
                });

                const sampleOptionsAPI2Response = [
                    {
                        id: '1',
                        parent: '1',
                        label: 'OptionC1-A1'
                    },
                    {
                        id: '2',
                        parent: '1',
                        label: 'OptionC2-A1'
                    },
                    {
                        id: '3',
                        parent: '2',
                        label: 'OptionC3-A2'
                    },
                    {
                        id: '4',
                        parent: '2',
                        label: 'OptionC4-A2'
                    },
                    {
                        id: '5',
                        parent: '3',
                        label: 'OptionC5-A3'
                    },
                    {
                        id: '6',
                        parent: '3',
                        label: 'OptionC6-A3'
                    }
                ];

                httpBackendService.whenGET(/sampleOptionsAPI2/).respond((_method, _url, data) => {
                    const query = urlUtils.parseQuery(data) as any;
                    const filterId = query.dropdownA as string;

                    const response: { options: typeof sampleOptionsAPI2Response[0][] } = {
                        options: undefined
                    };
                    response.options =
                        sampleOptionsAPI2Response.filter((option) => option.parent === filterId) ||
                        [];

                    return [200, response];
                });

                const sampleOptionsAPI3Response = [
                    {
                        id: '1',
                        parent: '1',
                        label: 'OptionE1-B1'
                    },
                    {
                        id: '2',
                        parent: '2',
                        label: 'OptionE2-B2'
                    },
                    {
                        id: '3',
                        parent: '3',
                        label: 'OptionE3-B3'
                    },
                    {
                        id: '4',
                        parent: '4',
                        label: 'OptionE4-B4'
                    },
                    {
                        id: '5',
                        parent: '5',
                        label: 'OptionE5-B5'
                    },
                    {
                        id: '6',
                        parent: '6',
                        label: 'OptionE6-B6'
                    },
                    {
                        id: '7',
                        parent: '7',
                        label: 'OptionE7-B7'
                    }
                ];

                httpBackendService.whenGET(/sampleOptionsAPI3/).respond((_method, _url, data) => {
                    const query = urlUtils.parseQuery(data) as any;
                    const filterId = query.dropdownB;

                    const response: { options: typeof sampleOptionsAPI3Response[0][] } = {
                        options: undefined
                    };
                    response.options =
                        sampleOptionsAPI3Response.filter((option) => option.parent === filterId) ||
                        [];

                    return [200, response];
                });

                let component = {
                    id: 'Component ID',
                    headline: 'The Headline',
                    dropdownA: '2',
                    dropdownB: '7',
                    dropdownC: ['3', '4'],
                    dropdownD: ['2'],
                    dropdownE: '1',
                    dropdownF: '',
                    active: true,
                    content: 'the content to edit',
                    create: new Date().getTime(),
                    external: false
                };

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/sites\/.*\/languages/)
                    .respond(languages);

                httpBackendService.whenGET(URL_FOR_ITEM).respond(component);
                httpBackendService.whenPUT(URL_FOR_ITEM).respond((_method, _url, data) => {
                    component = JSON.parse(data);
                    return [200, component];
                });

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
export class OuterMocksModule {}

window.pushModules(OuterMocksModule);
