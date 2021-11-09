/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injector, ModuleWithProviders, NgModule, Provider } from '@angular/core';

import {
    moduleUtils,
    HttpBackendService,
    IExperienceService,
    SeEntryModule,
    TestModeService
} from 'smarteditcommons';
import { STOREFRONT_URI, STOREFRONT_URI_TOKEN } from './utils/outerConstants';

@SeEntryModule('E2eOnLoadingSetupModule')
@NgModule({
    providers: [
        {
            provide: TestModeService.TEST_TOKEN,
            useValue: true
        },
        { provide: STOREFRONT_URI_TOKEN, useValue: STOREFRONT_URI },
        moduleUtils.bootstrap(
            (injector: Injector, httpBackendService: HttpBackendService, storefrontUri: string) => {
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

                const allSites = [
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
                        const params = JSON.parse(data);
                        if (params.catalogIds) {
                            const filteredItems = allSites.filter(function(site) {
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

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/sites\/apparel-uk\/languages/)
                    .respond({
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

                httpBackendService
                    .whenPOST(/thepreviewTicketURI/)
                    .respond(function(method, url, data) {
                        const returnedPayload = {
                            ...data,
                            ticketId: 'dasdfasdfasdfa',
                            resourcePath: storefrontUri
                        };

                        return [200, returnedPayload];
                    });

                // Pushed modules are delivered both to smarteditloader and smarteditcontainer
                // IExperienceService is not accessible from smarteditloader.

                try {
                    injector.get(IExperienceService).loadExperience({
                        siteId: 'apparel-uk',
                        catalogId: 'apparel-ukContentCatalog',
                        catalogVersion: 'Staged'
                    });
                } catch (e) {
                    //
                }
            },
            [Injector, HttpBackendService, STOREFRONT_URI_TOKEN]
        )
    ]
})
export class E2eOnLoadingSetupModule {
    public static provide(providers: Provider[]): ModuleWithProviders {
        return {
            ngModule: E2eOnLoadingSetupModule,
            providers: [...providers]
        };
    }
}
