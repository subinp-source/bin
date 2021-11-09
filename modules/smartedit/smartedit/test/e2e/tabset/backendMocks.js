/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('backendMocks', [
        'functionsModule',
        'translationServiceModule',
        'resourceLocationsModule',
        'smarteditServicesModule'
    ])
    .run(function(httpBackendService, filterFilter, parseQuery) {
        var map = [
            {
                value: '"somepath"',
                key: 'i18nAPIRoot'
            }
        ];

        httpBackendService.whenGET(/configuration/).respond(function() {
            return [200, map];
        });

        httpBackendService
            .whenGET(/cmswebservices\/v1\/types\/thesmarteditComponentType/)
            .respond(function() {
                var structure = {
                    structureProperties: [
                        {
                            type: 'ShortText',
                            qualifier: 'id',
                            i18nKey: 'type.thesmarteditcomponenttype.id.name'
                        },
                        {
                            type: 'LongText',
                            qualifier: 'headline',
                            i18nKey: 'type.thesmarteditcomponenttype.headline.name'
                        },
                        {
                            type: 'Boolean',
                            qualifier: 'active',
                            i18nKey: 'type.thesmarteditcomponenttype.active.name'
                        },
                        {
                            type: 'Date',
                            qualifier: 'activationDate',
                            i18nKey: 'type.thesmarteditcomponenttype.activationDate.name'
                        },
                        {
                            type: 'RichText',
                            qualifier: 'content',
                            i18nKey: 'type.thesmarteditcomponenttype.content.name'
                        },
                        {
                            type: 'Media',
                            qualifier: 'media',
                            i18nKey: 'type.thesmarteditcomponenttype.media.name'
                        },
                        {
                            type: 'Boolean',
                            qualifier: 'external',
                            i18nKey: 'thesmarteditComponentType_external'
                        },
                        {
                            type: 'ShortText',
                            qualifier: 'urlLink',
                            i18nKey: 'thesmarteditComponentType_urlLink'
                        }
                    ]
                };

                return [200, structure];
            });

        var component = {
            id: 'Component ID',
            pk: '123455667',
            uid: 'Comp_0006456345634',
            name: 'Custom Paragraph Component',
            headline: 'The Headline',
            active: true,
            content: 'the content to edit',
            activationDate: new Date().getTime(),
            creationtime: new Date().getTime(),
            modifiedtime: new Date().getTime(),
            media: '4',
            external: false,
            visible: true
        };

        httpBackendService
            .whenGET(/cmswebservices\/v1\/items\/thesmarteditComponentId/)
            .respond(component);
        httpBackendService
            .whenPUT(/cmswebservices\/v1\/items\/thesmarteditComponentId/)
            .respond(function(method, url, data) {
                component = JSON.parse(data);
                return [200, component];
            });

        var medias = [
            {
                id: '4',
                code: 'clone4',
                description: 'Clone background',
                altText: 'clone alttext',
                realFileName: 'clone_bckg.png',
                url: 'web/webroot/icons/clone_bckg.png'
            }
        ];

        httpBackendService
            .whenGET(/cmswebservices\/cmsxdata\/contentcatalog\/staged\/Media\/(.+)/)
            .respond(function(method, url) {
                var identifier = /Media\/(.+)/.exec(url)[1];
                console.info('get media', identifier);
                var filtered = medias.filter(function(media) {
                    return media.id === identifier;
                });
                if (filtered.length === 1) {
                    return [200, filtered[0]];
                } else {
                    return [404];
                }
            });

        httpBackendService
            .whenGET(/cmswebservices\/cmsxdata\/contentcatalog\/staged\/Media/)
            .respond(function(method, url) {
                console.info('query medias');
                var filtered = filterFilter(medias, parseQuery(url).search);
                return [
                    200,
                    {
                        medias: filtered
                    }
                ];
            });

        httpBackendService.whenGET(/i18n/).passThrough();
        httpBackendService.whenGET(/view$/).passThrough(); //calls to storefront render API
        httpBackendService.whenPUT(/contentslots/).passThrough();
    });
angular.module('tabsetApp').requires.push('backendMocks');
