/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { NgModule } from '@angular/core';

import {
    moduleUtils,
    urlUtils,
    HttpBackendService,
    I18N_LANGUAGES_RESOURCE_URI,
    SeEntryModule
} from 'smarteditcommons';

import { OuterAuthorizationMocks } from '../utils/commonMockedModules/outerAuthorizationMock';
import { OuterWhoAmIMocks } from '../utils/commonMockedModules/outerWhoAmIMocks';
import { i18nMocks } from '../utils/commonMockedModules/outerI18nMock';
import { OuterLanguagesMocks } from '../utils/commonMockedModules/outerLanguagesMock';
import { OuterSitesMocks } from '../utils/commonMockedModules/outerSitesMock';
import { OuterPermissionMocks } from '../utils/commonMockedModules/outerPermissionMocks';
import { E2eOnLoadingSetupModule } from '../outerE2eOnLoadingSetup';

@SeEntryModule('OuterMocksModule')
@NgModule({
    imports: [
        OuterAuthorizationMocks,
        OuterWhoAmIMocks,
        i18nMocks,
        OuterLanguagesMocks,
        OuterSitesMocks,
        OuterPermissionMocks,
        E2eOnLoadingSetupModule
    ],

    providers: [
        moduleUtils.provideValues({
            SMARTEDIT_ROOT: 'web/webroot',
            SMARTEDIT_RESOURCE_URI_REGEXP: /^(.*)\/test\/e2e/
        }),
        moduleUtils.bootstrap(
            (httpBackendService: HttpBackendService) => {
                httpBackendService.matchLatestDefinitionEnabled(true);

                httpBackendService.whenGET(/smartedit\/settings/).respond({
                    'smartedit.sso.enabled': 'false'
                });

                httpBackendService.whenGET(I18N_LANGUAGES_RESOURCE_URI).respond({
                    languages: [
                        {
                            isoCode: 'en',
                            name: 'English'
                        },
                        {
                            isoCode: 'fr',
                            name: 'French'
                        }
                    ]
                });

                httpBackendService.whenGET(/cmswebservices\/sites\/.*\/languages/).respond({
                    languages: [
                        {
                            nativeName: 'English',
                            isocode: 'en',
                            name: 'English',
                            required: true
                        }
                    ]
                });

                const oauthToken0 = {
                    access_token: 'access-token0',
                    token_type: 'bearer'
                };

                httpBackendService
                    .whenPOST(/authorizationserver\/oauth\/token/)
                    .respond(function(method, url, data) {
                        const query = urlUtils.parseQuery(data) as any;
                        if (
                            query.client_id === 'smartedit' &&
                            query.client_secret === undefined &&
                            query.grant_type === 'password' &&
                            query.username === 'customermanager' &&
                            query.password === '123'
                        ) {
                            return [200, oauthToken0];
                        } else {
                            return [401, null];
                        }
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
                    }
                ];

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/sites$/)
                    .respond(function(method, url, data, headers) {
                        if (headers.Authorization === 'bearer ' + oauthToken0.access_token) {
                            return [
                                200,
                                {
                                    sites: allSites
                                }
                            ];
                        } else {
                            return [401, null];
                        }
                    });

                httpBackendService
                    .whenPOST(/cmswebservices\/v1\/sites\/catalogs/)
                    .respond(function(method, url, data, headers) {
                        const params = JSON.parse(data);
                        const catalogIds = params.catalogIds;

                        if (
                            headers.Authorization === 'bearer ' + oauthToken0.access_token &&
                            catalogIds
                        ) {
                            const filteredItems = allSites.filter(function(site) {
                                return (
                                    catalogIds.indexOf(
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
                        } else {
                            return [401, null];
                        }
                    });

                const map = [
                    {
                        value: '"thepreviewTicketURI"',
                        key: 'previewTicketURI'
                    },
                    {
                        value: '"somepath"',
                        key: 'i18nAPIRoot'
                    },
                    {
                        value:
                            '{"smartEditLocation":"/test/e2e/languageSelector/generated_innerDummyFakeModuleDecorators.js"}',
                        key: 'applications.FakeModule'
                    }
                ];

                httpBackendService
                    .whenGET(/smartedit\/configuration/)
                    .respond(function(method, url, data, headers) {
                        if (headers.Authorization === 'bearer ' + oauthToken0.access_token) {
                            return [200, map];
                        } else {
                            return [401, null];
                        }
                    });
                httpBackendService.whenGET(/fragments/).passThrough();
            },
            [HttpBackendService]
        )
    ]
})
export class OuterMocksModule {}

window.pushModules(OuterMocksModule);
