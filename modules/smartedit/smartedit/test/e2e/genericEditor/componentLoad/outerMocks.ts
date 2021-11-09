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
import { OuterLanguagesMocks } from '../../utils/commonMockedModules/outerLanguagesMock';

@SeEntryModule('ConfigurationMocksModule')
@NgModule({
    imports: [
        OuterWhoAmIMocks,
        i18nMocks,
        OuterLanguagesMocks,
        OuterSitesMocks,
        OuterPermissionMocks
    ],
    providers: [
        moduleUtils.bootstrap(
            (httpBackendService: HttpBackendService) => {
                httpBackendService.matchLatestDefinitionEnabled(true);

                httpBackendService.whenGET(/test\/e2e/).passThrough();
                httpBackendService.whenGET(/static-resources/).passThrough();

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/types\/thesmarteditComponentType/)
                    .respond(function() {
                        const structure = {
                            attributes: [
                                {
                                    cmsStructureType: 'ShortString',
                                    qualifier: 'id',
                                    i18nKey: 'type.thesmarteditcomponenttype.id.name',
                                    localized: false,
                                    required: true
                                },
                                {
                                    cmsStructureType: 'LongString',
                                    qualifier: 'headline',
                                    i18nKey: 'type.thesmarteditcomponenttype.headline.name',
                                    localized: false
                                },
                                {
                                    cmsStructureType: 'Boolean',
                                    qualifier: 'active',
                                    i18nKey: 'type.thesmarteditcomponenttype.active.name',
                                    localized: false
                                },
                                {
                                    cmsStructureType: 'Boolean',
                                    qualifier: 'enabled',
                                    i18nKey: 'type.thesmarteditcomponenttype.enabled.name',
                                    localized: false
                                },
                                {
                                    type: 'Date',
                                    cmsStructureType: 'DateTime',
                                    qualifier: 'created',
                                    i18nKey: 'type.thesmarteditcomponenttype.created.name',
                                    localized: false
                                },
                                {
                                    cmsStructureType: 'RichText',
                                    qualifier: 'content',
                                    i18nKey: 'type.thesmarteditcomponenttype.content.name',
                                    localized: true,
                                    required: true
                                },
                                {
                                    cmsStructureType: 'Enum',
                                    cmsStructureEnumType: 'de.mypackage.Orientation',
                                    qualifier: 'orientation',
                                    i18nKey: 'type.thesmarteditcomponenttype.orientation.name',
                                    localized: false,
                                    required: true
                                },
                                {
                                    cmsStructureType: 'Dropdown',
                                    qualifier: 'simpleDropdown',
                                    i18nKey: 'type.thesmarteditcomponenttype.simpledropdown.name',
                                    options: [
                                        {
                                            id: '1',
                                            label: 'Option 1'
                                        },
                                        {
                                            id: '2',
                                            label: 'Option 2'
                                        },
                                        {
                                            id: '3',
                                            label: 'Option 3'
                                        }
                                    ]
                                },
                                {
                                    cmsStructureType: 'Boolean',
                                    qualifier: 'external',
                                    i18nKey: 'type.thesmarteditcomponenttype.external.name',
                                    localized: false
                                },
                                {
                                    cmsStructureType: 'ShortString',
                                    qualifier: 'urlLink',
                                    i18nKey: 'type.thesmarteditcomponenttype.urlLink.name',
                                    localized: false
                                }
                            ]
                        };

                        return [200, structure];
                    });

                let component = {
                    id: 'Component ID',
                    headline: 'The Headline',
                    active: true,
                    content: {
                        en: 'the content to edit',
                        fr: 'le contenu a editer',
                        pl: 'tresc edytowac',
                        it: 'il contenuto da modificare',
                        hi: 'Sampaadit karanee kee liee saamagree'
                    },
                    created: new Date().getTime(),
                    enabled: false,
                    orientation: 'vertical',
                    simpleDropdown: '1',
                    external: false,
                    urlLink: 'myPageUrl'
                };

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/sites\/.*\/languages/)
                    .respond(languages);

                httpBackendService.whenGET(URL_FOR_ITEM).respond(component);
                httpBackendService.whenPUT(URL_FOR_ITEM).respond((method, url, data) => {
                    component = JSON.parse(data);
                    return [200, component];
                });

                const orientationEnums = {
                    enums: [
                        {
                            code: 'vertical',
                            label: 'Vertical'
                        },
                        {
                            code: 'horizontal',
                            label: 'Horizontal'
                        }
                    ]
                };

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/enums/)
                    .respond((method, url, data) => {
                        const enumClass = (urlUtils.parseQuery(data) as any).enumClass;
                        if (enumClass === 'de.mypackage.Orientation') {
                            return [200, orientationEnums];
                        } else {
                            return [404, null];
                        }
                    });

                httpBackendService.whenGET(/i18n/).passThrough();
                httpBackendService.whenGET(/view/).passThrough(); // calls to storefront render API
                httpBackendService.whenPUT(/contentslots/).passThrough();
                httpBackendService.whenGET(/\.html/).passThrough();

                const userId = 'cmsmanager';

                httpBackendService
                    .whenGET(/authorizationserver\/oauth\/whoami/)
                    .respond(function() {
                        return [
                            200,
                            {
                                displayName: 'CMS Manager',
                                uid: userId
                            }
                        ];
                    });

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/users\/*/)
                    .respond(function(method, url) {
                        const userUid = url.substring(url.lastIndexOf('/') + 1);

                        return [
                            200,
                            {
                                uid: userUid,
                                readableLanguages: ['en', 'it', 'fr', 'pl', 'hi'],
                                writeableLanguages: ['en', 'it', 'fr', 'pl', 'hi']
                            }
                        ];
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
