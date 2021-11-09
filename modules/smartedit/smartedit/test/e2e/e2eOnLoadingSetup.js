/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
(function() {
    angular
        .module('e2eOnLoadingSetup', ['resourceLocationsModule'])
        //.constant('STOREFRONT_URI', 'http://127.0.0.1:9000/smartedit-build/test/e2e/dummystorefront/dummyAngularStorefront.html')
        .constant('STOREFRONT_URI', 'http://127.0.0.1:9000/test/utils/storefront.html')
        .run(function(experienceService, STOREFRONT_URI, httpBackendService) {
            httpBackendService.whenGET(/jsTarget\/e2e/).passThrough();
            httpBackendService.whenGET(/test\/e2e/).passThrough();
            httpBackendService.whenGET(/static-resources/).passThrough();

            httpBackendService
                .whenGET(/cmssmarteditwebservices\/v1\/sites\/electronics\/contentcatalogs/)
                .respond({
                    catalogs: [
                        {
                            catalogId: 'electronicsContentCatalog',
                            name: {
                                en: 'Electronics Content Catalog'
                            },
                            versions: [
                                {
                                    version: 'Online',
                                    active: true,
                                    uuid: 'electronicsContentCatalog/Online'
                                },
                                {
                                    version: 'Staged',
                                    active: false,
                                    uuid: 'electronicsContentCatalog/Staged'
                                }
                            ]
                        }
                    ]
                });

            httpBackendService
                .whenGET(/cmssmarteditwebservices\/v1\/sites\/apparel-uk\/contentcatalogs/)
                .respond({
                    catalogs: [
                        {
                            catalogId: 'apparel-ukContentCatalog',
                            name: {
                                en: 'Apparel UK Content Catalog'
                            },
                            versions: [
                                {
                                    version: 'Online',
                                    active: true,
                                    uuid: 'apparel-ukContentCatalog/Online'
                                },
                                {
                                    version: 'Staged',
                                    active: false,
                                    uuid: 'apparel-ukContentCatalog/Staged'
                                }
                            ]
                        }
                    ]
                });

            httpBackendService
                .whenGET(/cmssmarteditwebservices\/v1\/sites\/electronics\/productcatalogs/)
                .respond({
                    catalogs: [
                        {
                            catalogId: 'electronicsProductCatalog',
                            name: {
                                en: 'Electronics Product Catalog',
                                de: 'Produktkatalog Handys'
                            },
                            versions: [
                                {
                                    active: true,
                                    uuid: 'electronicsProductCatalog/Online',
                                    version: 'Online'
                                },
                                {
                                    active: false,
                                    uuid: 'electronicsProductCatalog/Staged',
                                    version: 'Staged'
                                }
                            ]
                        }
                    ]
                });

            httpBackendService
                .whenGET(/cmssmarteditwebservices\/v1\/sites\/apparel-uk\/productcatalogs/)
                .respond({
                    catalogs: [
                        {
                            catalogId: 'apparel-ukProductCatalog-clothing',
                            name: {
                                en: 'Clothing Product Catalog'
                            },
                            versions: [
                                {
                                    active: true,
                                    uuid: 'apparel-ukProductCatalog-clothing/Online',
                                    version: 'Online'
                                },
                                {
                                    active: false,
                                    uuid: 'apparel-ukProductCatalog-clothing/Staged',
                                    version: 'Staged'
                                }
                            ]
                        },
                        {
                            catalogId: 'apparel-ukProductCatalog-shoes',
                            name: {
                                en: 'Shoes Product Catalog'
                            },
                            versions: [
                                {
                                    active: true,
                                    uuid: 'apparel-ukProductCatalog-shoes/Online',
                                    version: 'Online'
                                },
                                {
                                    active: false,
                                    uuid: 'apparel-ukProductCatalog-shoes/Staged-1',
                                    version: 'Staged-1'
                                },
                                {
                                    active: false,
                                    uuid: 'apparel-ukProductCatalog-shoes/Staged-2',
                                    version: 'Staged-2'
                                }
                            ]
                        }
                    ]
                });

            var allSites = [
                {
                    previewUrl:
                        '/smartedit-build/test/e2e/dummystorefront/dummystorefrontElectronics.html',
                    name: {
                        en: 'Electronics'
                    },
                    redirectUrl: 'redirecturlElectronics',
                    uid: 'electronics',
                    contentCatalogs: ['electronicsContentCatalog']
                },
                {
                    previewUrl: '/test/utils/storefront.html',
                    name: {
                        en: 'Apparels'
                    },
                    redirectUrl: 'redirecturlApparels',
                    uid: 'apparel-uk',
                    contentCatalogs: ['apparel-ukContentCatalog']
                }
            ];

            httpBackendService.whenGET(/cmswebservices\/v1\/sites$/).respond({
                sites: allSites
            });
            httpBackendService
                .whenPOST(/cmswebservices\/v1\/sites\/catalogs/)
                .respond(function(method, url, data) {
                    var params = JSON.parse(data);
                    if (params.catalogIds) {
                        var filteredItems = allSites.filter(function(site) {
                            return (
                                params.catalogIds.indexOf(
                                    site.contentCatalogs[site.contentCatalogs.length - 1]
                                ) > -1
                            );
                        });
                        return [
                            200,
                            {
                                sites: filteredItems
                            }
                        ];
                    }
                    return [
                        200,
                        {
                            sites: []
                        }
                    ];
                });

            httpBackendService
                .whenGET(/cmswebservices\/v1\/sites\/electronics\/languages/)
                .respond({
                    languages: [
                        {
                            nativeName: 'English',
                            isocode: 'en',
                            required: true
                        }
                    ]
                });

            httpBackendService.whenGET(/cmswebservices\/v1\/sites\/apparel-uk\/languages/).respond({
                languages: [
                    {
                        nativeName: 'English',
                        isocode: 'en',
                        required: true
                    }
                ]
            });

            httpBackendService
                .whenGET(/cmswebservices\/v1\/types\?code=PreviewData\&mode=DEFAULT/)
                .respond({
                    attributes: [
                        {
                            cmsStructureType: 'EditableDropdown',
                            qualifier: 'previewCatalog',
                            i18nKey: 'experience.selector.catalog'
                        },
                        {
                            cmsStructureType: 'EditableDropdown',
                            qualifier: 'language',
                            i18nKey: 'experience.selector.language',
                            dependsOn: 'previewCatalog'
                        },
                        {
                            cmsStructureType: 'DateTime',
                            qualifier: 'time',
                            i18nKey: 'experience.selector.date.and.time'
                        },
                        {
                            cmsStructureType: 'ProductCatalogVersionsSelector',
                            qualifier: 'productCatalogVersions',
                            i18nKey: 'experience.selector.catalogversions'
                        },
                        {
                            cmsStructureType: 'ShortString',
                            qualifier: 'newField',
                            i18nKey: 'experience.selector.newfield'
                        }
                    ]
                });

            httpBackendService.whenPOST(/thepreviewTicketURI/).respond(function(method, url, data) {
                var returnedPayload = angular.extend({}, data, {
                    ticketId: 'dasdfasdfasdfa',
                    resourcePath: STOREFRONT_URI
                });

                return [200, returnedPayload];
            });

            experienceService.loadExperience({
                siteId: 'apparel-uk',
                catalogId: 'apparel-ukContentCatalog',
                catalogVersion: 'Staged'
            });
        });

    angular.module('smarteditcontainer').constant('e2eMode', true);
    angular.module('smarteditcontainer').requires.push('e2eOnLoadingSetup');
})();
