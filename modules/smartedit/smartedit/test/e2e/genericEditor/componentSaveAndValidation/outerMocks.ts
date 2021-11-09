/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { moduleUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';
import { OuterWhoAmIMocks } from '../../utils/commonMockedModules/outerWhoAmIMocks';
import { i18nMocks } from '../../utils/commonMockedModules/outerI18nMock';
import { OuterSitesMocks } from '../../utils/commonMockedModules/outerSitesMock';
import { OuterPermissionMocks } from '../../utils/commonMockedModules/outerPermissionMocks';
import { languages, URL_FOR_ITEM } from '../common/outerCommonConstants';
// tslint:disable:variable-name

@SeEntryModule('ConfigurationMocksModule')
@NgModule({
    imports: [OuterWhoAmIMocks, i18nMocks, OuterSitesMocks, OuterPermissionMocks],
    providers: [
        moduleUtils.bootstrap(
            (httpBackendService: HttpBackendService) => {
                httpBackendService.matchLatestDefinitionEnabled(true);

                const payload_headline_2_invalid = {
                    visible: false,
                    active: true,
                    content: {
                        en: 'the content to edit',
                        fr: 'le contenu a editer',
                        pl: 'tresc edytowac',
                        it: 'il contenuto da modificare',
                        hi: 'Sampaadit karanee kee liee saamagree'
                    },
                    external: false,
                    urlLink: '/url-link',
                    headline:
                        'I have changed to an invalid headline with two validation errors, % and lots of text',
                    id: 'thesmarteditComponentId',
                    identifier: 'thesmarteditComponentId'
                };

                const payload_headline_1_invalid = {
                    visible: false,
                    active: true,
                    content: {
                        en: 'the content to edit',
                        fr: 'le contenu a editer',
                        pl: 'tresc edytowac',
                        it: 'il contenuto da modificare',
                        hi: 'Sampaadit karanee kee liee saamagree'
                    },
                    external: false,
                    urlLink: '/url-link',
                    headline: 'I have changed to an invalid headline with one validation error, %',
                    id: 'thesmarteditComponentId',
                    identifier: 'thesmarteditComponentId'
                };

                const payload_urllink_invalid = {
                    visible: false,
                    id: 'thesmarteditComponentId',
                    headline: 'The Headline',
                    active: true,
                    content: {
                        en: 'the content to edit',
                        fr: 'le contenu a editer',
                        pl: 'tresc edytowac',
                        it: 'il contenuto da modificare',
                        hi: 'Sampaadit karanee kee liee saamagree'
                    },
                    external: false,
                    urlLink: '/url-link-invalid',
                    identifier: 'thesmarteditComponentId'
                };

                const payload_all_valid = {
                    visible: false,
                    active: true,
                    content: {
                        en: 'the content to edit',
                        fr: 'le contenu a editer',
                        pl: 'tresc edytowac',
                        it: 'il contenuto da modificare',
                        hi: 'Sampaadit karanee kee liee saamagree'
                    },
                    external: false,
                    urlLink: '/url-link',
                    headline: 'I have no errors',
                    id: 'thesmarteditComponentId',
                    identifier: 'thesmarteditComponentId'
                };

                const payload_content_invalid = {
                    visible: false,
                    active: true,
                    content: {
                        en:
                            'I have changed to an invalid content with one validation errorthe content to edit',
                        fr: 'le contenu a editer',
                        pl: 'tresc edytowac',
                        it:
                            'Ho cambiato ad un contenuto non valido con un errore di validazioneil contenuto da modificare',
                        hi: 'Sampaadit karanee kee liee saamagree'
                    },
                    external: false,
                    urlLink: '/url-link',
                    headline: 'The Headline',
                    id: 'thesmarteditComponentId',
                    identifier: 'thesmarteditComponentId'
                };

                const payload_for_unknown_type = {
                    visible: false,
                    active: true,
                    content: {
                        en: 'I have changed to an invalid content with one validation error',
                        fr: 'le contenu a editer',
                        pl: 'tresc edytowac',
                        it: 'Ho cambiato ad un contenuto non valido con un errore di validazione',
                        hi: 'Sampaadit karanee kee liee saamagree'
                    },
                    external: false,
                    urlLink: '/url-link',
                    headline: 'Checking unknown type',
                    id: 'thesmarteditComponentId',
                    identifier: 'thesmarteditComponentId'
                };

                const payload_all_valid_2 = {
                    visible: false,
                    active: true,
                    content: {
                        en: 'the content to edit',
                        fr: 'le contenu a editer',
                        pl: 'tresc edytowac',
                        it: 'il contenuto da modificare',
                        hi: 'Sampaadit karanee kee liee saamagree'
                    },
                    external: false,
                    urlLink: '/url-link',
                    headline: 'The Headline',
                    id: 'thesmarteditComponentId',
                    identifier: 'thesmarteditComponentId'
                };

                httpBackendService.whenPUT(URL_FOR_ITEM).respond(function(method, url, data) {
                    component = JSON.parse(data);
                    return [200, component];
                });

                httpBackendService
                    .whenPUT(URL_FOR_ITEM, payload_headline_2_invalid)
                    .respond(function() {
                        return [400, validationErrors_Headline];
                    });

                httpBackendService
                    .whenPUT(URL_FOR_ITEM, payload_urllink_invalid)
                    .respond(function() {
                        return [400, validationErrors_UrlLink];
                    });

                httpBackendService
                    .whenPUT(URL_FOR_ITEM, payload_content_invalid)
                    .respond(function() {
                        return [400, validationErrors_content_2tab];
                    });

                httpBackendService
                    .whenPUT(URL_FOR_ITEM, payload_headline_1_invalid)
                    .respond(function() {
                        return [400, validationErrors_1];
                    });

                httpBackendService
                    .whenPUT(URL_FOR_ITEM, payload_for_unknown_type)
                    .respond(function() {
                        return [400, validationErrors_Unknown_Type];
                    });

                httpBackendService.whenPUT(URL_FOR_ITEM, payload_all_valid).respond(function() {
                    return [400, validationErrors_1];
                });

                httpBackendService.whenPUT(URL_FOR_ITEM, payload_all_valid_2).respond(function() {
                    return [200, {}];
                });

                const validationErrors_Headline = {
                    errors: [
                        {
                            message:
                                'This field is required and must to be between 1 and 255 characters long.',
                            reason: 'missing',
                            subject: 'headline',
                            subjectType: 'parameter',
                            type: 'ValidationError'
                        },
                        {
                            message: 'This field cannot contain special characters',
                            reason: 'missing',
                            subject: 'headline',
                            subjectType: 'parameter',
                            type: 'ValidationError'
                        }
                    ]
                };

                const validationErrors_UrlLink = {
                    errors: [
                        {
                            message:
                                'This field is required and must to be between 1 and 255 characters long.',
                            reason: 'missing',
                            subject: 'urlLink',
                            subjectType: 'parameter',
                            type: 'ValidationError'
                        }
                    ]
                };

                const validationErrors_content_2tab = {
                    errors: [
                        {
                            message:
                                'This field is required and must to be between 1 and 255 characters long. Language: [en]',
                            reason: 'missing',
                            subject: 'content',
                            subjectType: 'parameter',
                            type: 'ValidationError'
                        },
                        {
                            message:
                                'This field is required and must to be between 1 and 255 characters long. Language: [it]',
                            reason: 'missing',
                            subject: 'content',
                            subjectType: 'parameter',
                            type: 'ValidationError'
                        }
                    ]
                };

                const validationErrors_1 = {
                    errors: [
                        {
                            message:
                                'This field is required and must to be between 1 and 255 characters long.',
                            reason: 'missing',
                            subject: 'headline',
                            subjectType: 'parameter',
                            type: 'ValidationError'
                        }
                    ]
                };

                const validationErrors_Unknown_Type = {
                    errors: [
                        {
                            message: 'The type is not valid',
                            reason: 'missing',
                            subject: 'unknownType',
                            subjectType: 'parameter',
                            type: 'ValidationError'
                        }
                    ]
                };

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/types\/thesmarteditComponentType/)
                    .respond(function() {
                        const structure = {
                            attributes: [
                                {
                                    cmsStructureType: 'ShortString',
                                    qualifier: 'id',
                                    i18nKey: 'type.thesmarteditComponentType.id.name',
                                    localized: false
                                },
                                {
                                    cmsStructureType: 'Boolean',
                                    qualifier: 'visible',
                                    i18nKey: 'type.thesmarteditComponentType.visible.name',
                                    localized: false
                                },
                                {
                                    cmsStructureType: 'LongString',
                                    qualifier: 'headline',
                                    i18nKey: 'type.thesmarteditComponentType.headline.name',
                                    localized: false
                                },
                                {
                                    cmsStructureType: 'Boolean',
                                    qualifier: 'active',
                                    i18nKey: 'type.thesmarteditComponentType.active.name',
                                    localized: false
                                },
                                {
                                    cmsStructureType: 'RichText',
                                    qualifier: 'content',
                                    i18nKey: 'type.thesmarteditComponentType.content.name',
                                    localized: true
                                }
                            ]
                        };

                        return [200, structure];
                    });

                let component = {
                    id: 'thesmarteditComponentId',
                    headline: 'The Headline',
                    active: true,
                    content: {
                        en: 'the content to edit',
                        fr: 'le contenu a editer',
                        pl: 'tresc edytowac',
                        it: 'il contenuto da modificare',
                        hi: 'Sampaadit karanee kee liee saamagree'
                    },
                    external: false,
                    urlLink: '/url-link'
                };

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/sites\/.*\/languages/)
                    .respond(languages);

                httpBackendService.whenGET(URL_FOR_ITEM).respond(component);

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

                httpBackendService.whenGET(/smartedit\/settings/).respond({
                    'cms.components.allowUnsafeJavaScript': 'false'
                });
            },
            [HttpBackendService]
        )
    ]
})
export class ConfigurationMocksModule {}

window.pushModules(ConfigurationMocksModule);
