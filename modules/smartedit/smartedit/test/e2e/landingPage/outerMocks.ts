/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { NgModule } from '@angular/core';

import { moduleUtils, HttpBackendService, LanguageService, SeEntryModule } from 'smarteditcommons';
import { OuterAuthorizationMocks } from '../utils/commonMockedModules/outerAuthorizationMock';
import { OuterWhoAmIMocks } from '../utils/commonMockedModules/outerWhoAmIMocks';
import { i18nMocks } from '../utils/commonMockedModules/outerI18nMock';
import { OuterLanguagesMocks } from '../utils/commonMockedModules/outerLanguagesMock';
import { OuterPreviewMocks } from '../utils/commonMockedModules/outerPreviewMock';
import { OuterPermissionMocks } from '../utils/commonMockedModules/outerPermissionMocks';

@SeEntryModule('OuterMocksModule')
@NgModule({
    imports: [
        OuterAuthorizationMocks,
        OuterWhoAmIMocks,
        i18nMocks,
        OuterLanguagesMocks,
        OuterPreviewMocks,
        OuterPermissionMocks
    ],

    providers: [
        moduleUtils.provideValues({
            SMARTEDIT_ROOT: 'web/webroot',
            SMARTEDIT_RESOURCE_URI_REGEXP: /^(.*)\/test\/e2e/
        }),
        moduleUtils.bootstrap(
            (httpBackendService: HttpBackendService, languageService: LanguageService) => {
                httpBackendService.matchLatestDefinitionEnabled(true);

                httpBackendService.whenGET(/test\/e2e/).passThrough();
                httpBackendService.whenGET(/static-resources/).passThrough();

                httpBackendService.whenPOST(/thepreviewTicketURI/).respond({
                    ticketId: 'dasdfasdfasdfa',
                    resourcePath: document.location.origin + '/test/utils/storefront.html'
                });

                httpBackendService.whenGET(/fragments/).passThrough();

                httpBackendService.whenGET(/cmswebservices\/v1\/languages/).respond({
                    languages: [
                        {
                            language: 'en',
                            required: true
                        }
                    ]
                });

                httpBackendService
                    .whenGET(
                        '/cmswebservices/v1/i18n/languages/' + languageService.getBrowserLocale()
                    )
                    .respond({});

                httpBackendService.whenGET(/cmswebservices\/v1\/sites\/toys\/languages/).respond({
                    languages: [
                        {
                            nativeName: 'English',
                            isocode: 'en',
                            required: true
                        },
                        {
                            nativeName: 'Polish',
                            isocode: 'pl',
                            required: true
                        },
                        {
                            nativeName: 'Italian',
                            isocode: 'it'
                        }
                    ]
                });

                httpBackendService.whenGET(/cmswebservices\/v1\/sites\/action\/languages/).respond({
                    languages: [
                        {
                            nativeName: 'English',
                            isocode: 'en',
                            required: true
                        },
                        {
                            nativeName: 'Polish',
                            isocode: 'pl',
                            required: true
                        },
                        {
                            nativeName: 'Italian',
                            isocode: 'it'
                        }
                    ]
                });

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/sites\/electronics\/languages/)
                    .respond({
                        languages: [
                            {
                                nativeName: 'English',
                                isocode: 'en',
                                required: true
                            },
                            {
                                nativeName: 'Polish',
                                isocode: 'pl',
                                required: true
                            },
                            {
                                nativeName: 'Italian',
                                isocode: 'it'
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
                            },
                            {
                                nativeName: 'French',
                                isocode: 'fr'
                            }
                        ]
                    });

                httpBackendService
                    .whenGET(
                        /cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/synchronization\/versions\/Staged\/Online/
                    )
                    .respond({
                        date: '2016-01-29T16:25:28+0000',
                        status: 'RUNNING'
                    });

                httpBackendService
                    .whenGET(
                        /cmswebservices\/v1\/catalogs\/apparel-deContentCatalog\/synchronization\/versions\/Staged\/Online/
                    )
                    .respond({
                        date: '2015-01-29T16:25:44+0000',
                        status: 'ABORTED'
                    });

                httpBackendService
                    .whenGET(
                        /cmswebservices\/v1\/catalogs\/electronicsContentCatalog\/synchronization\/versions\/Staged\/Online/
                    )
                    .respond({
                        date: '2014-01-28T17:05:29+0000',
                        status: 'FINISHED'
                    });

                httpBackendService
                    .whenGET(
                        /cmswebservices\/v1\/catalogs\/actionFiguresContentCatalog\/synchronization\/versions\/Staged\/Online/
                    )
                    .respond({
                        date: '2013-01-28T17:05:29+0000',
                        status: 'FINISHED'
                    });

                httpBackendService
                    .whenPUT(
                        /cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/synchronization\/versions\/Staged\/Online/
                    )
                    .respond({
                        date: '2016-01-29T16:25:28+0000',
                        status: 'RUNNING'
                    });

                httpBackendService
                    .whenPUT(
                        /cmswebservices\/v1\/catalogs\/apparel-deContentCatalog\/synchronization\/versions\/Staged\/Online/
                    )
                    .respond({
                        date: '2016-01-29T16:25:44+0000',
                        status: 'ABORTED'
                    });

                httpBackendService
                    .whenPUT(
                        /cmswebservices\/v1\/catalogs\/electronicsContentCatalog\/synchronization\/versions\/Staged\/Online/
                    )
                    .respond({
                        date: '2014-01-28T17:05:29+0000',
                        status: 'FINISHED'
                    });

                httpBackendService
                    .whenPUT(
                        /cmswebservices\/v1\/catalogs\/actionFiguresContentCatalog\/synchronization\/versions\/Staged\/Online/
                    )
                    .respond({
                        date: '2013-01-28T17:05:29+0000',
                        status: 'FINISHED'
                    });

                const allSites = [
                    {
                        previewUrl: '/test/utils/storefront.html',
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
                    },
                    {
                        previewUrl: '/test/utils/storefront.html',
                        name: {
                            en: 'Apparels'
                        },
                        redirectUrl: 'redirecturlApparels',
                        uid: 'apparel-de',
                        contentCatalogs: ['apparel-deContentCatalog']
                    },
                    {
                        previewUrl: '/test/utils/storefront.html',
                        name: {
                            en: 'Toys'
                        },
                        redirectUrl: 'redirectSomeOtherSite',
                        uid: 'toys',
                        contentCatalogs: ['toysContentCatalog']
                    },
                    {
                        previewUrl: '/test/utils/storefront.html',
                        name: {
                            en: 'Action Figures'
                        },
                        redirectUrl: 'redirectSomeOtherSite',
                        uid: 'action',
                        contentCatalogs: ['toysContentCatalog', 'actionFiguresContentCatalog']
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
                    .whenGET(/cmssmarteditwebservices\/v1\/sites\/apparel-de\/contentcatalogs/)
                    .respond({
                        catalogs: [
                            {
                                catalogId: 'apparel-deContentCatalog',
                                name: {
                                    en: 'Apparel DE Content Catalog'
                                },
                                versions: [
                                    {
                                        version: 'Online',
                                        active: true,
                                        uuid: 'apparel-deContentCatalog/Online'
                                    },
                                    {
                                        version: 'Staged',
                                        active: false,
                                        uuid: 'apparel-deContentCatalog/Online'
                                    }
                                ]
                            }
                        ]
                    });

                httpBackendService
                    .whenGET(/cmssmarteditwebservices\/v1\/sites\/toys\/contentcatalogs/)
                    .respond({
                        catalogs: [
                            {
                                catalogId: 'toysContentCatalog',
                                name: {
                                    en: 'Toys Content Catalog'
                                },
                                versions: [
                                    {
                                        version: 'Online',
                                        active: true
                                    },
                                    {
                                        version: 'Staged',
                                        active: false
                                    }
                                ]
                            }
                        ]
                    });

                httpBackendService
                    .whenGET(/cmssmarteditwebservices\/v1\/sites\/action\/contentcatalogs/)
                    .respond({
                        catalogs: [
                            {
                                catalogId: 'toysContentCatalog',
                                name: {
                                    en: 'Toys Content Catalog'
                                },
                                versions: [
                                    {
                                        version: 'Online',
                                        active: true
                                    },
                                    {
                                        version: 'Staged',
                                        active: false
                                    }
                                ]
                            },
                            {
                                catalogId: 'actionFiguresContentCatalog',
                                name: {
                                    en: 'Action Figures Content Catalog'
                                },
                                versions: [
                                    {
                                        version: 'Online',
                                        active: true
                                    },
                                    {
                                        version: 'Staged',
                                        active: false
                                    }
                                ]
                            }
                        ]
                    });

                httpBackendService
                    .whenGET(/cmssmarteditwebservices\/v1\/sites\/toys\/productcatalogs/)
                    .respond({
                        catalogs: [
                            {
                                catalogId: 'toysProductCatalog',
                                name: {
                                    en: 'Toys Product Catalog'
                                },
                                versions: [
                                    {
                                        active: true,
                                        uuid: 'toysProductCatalog/Online',
                                        version: 'Online'
                                    },
                                    {
                                        active: false,
                                        uuid: 'toysProductCatalog/Staged',
                                        version: 'Staged'
                                    }
                                ]
                            }
                        ]
                    });

                httpBackendService
                    .whenGET(/cmssmarteditwebservices\/v1\/sites\/action\/productcatalogs/)
                    .respond({
                        catalogs: [
                            {
                                catalogId: 'actionProductCatalog',
                                name: {
                                    en: 'Action Product Catalog'
                                },
                                versions: [
                                    {
                                        active: true,
                                        uuid: 'actionProductCatalog/Online',
                                        version: 'Online'
                                    },
                                    {
                                        active: false,
                                        uuid: 'actionProductCatalog/Staged',
                                        version: 'Staged'
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

                const map = [
                    {
                        value: '"thepreviewTicketURI"',
                        key: 'previewTicketURI'
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
            [HttpBackendService, LanguageService]
        )
    ]
})
export class OuterMocksModule {}

window.pushModules(OuterMocksModule);
