/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { moduleUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';
import { OuterAuthorizationMocks } from '../utils/commonMockedModules/outerAuthorizationMock';
import { OuterWhoAmIMocks } from '../utils/commonMockedModules/outerWhoAmIMocks';
import { OuterConfigurationMocks } from '../utils/commonMockedModules/outerConfigurationMock';
import { i18nMocks } from '../utils/commonMockedModules/outerI18nMock';
import { OuterLanguagesMocks } from '../utils/commonMockedModules/outerLanguagesMock';
import { OuterOtherMocks } from '../utils/commonMockedModules/outerOtherMock';
import { OuterPreviewMocks } from '../utils/commonMockedModules/outerPreviewMock';
import { OuterSitesMocks } from '../utils/commonMockedModules/outerSitesMock';
import { OuterPermissionMocks } from '../utils/commonMockedModules/outerPermissionMocks';

@SeEntryModule('OuterMocksModule')
@NgModule({
    imports: [
        OuterAuthorizationMocks,
        OuterWhoAmIMocks,
        OuterConfigurationMocks,
        i18nMocks,
        OuterLanguagesMocks,
        OuterOtherMocks,
        OuterPreviewMocks,
        OuterSitesMocks,
        OuterPreviewMocks,
        OuterPermissionMocks
    ],
    providers: [
        moduleUtils.provideValues({
            SMARTEDIT_ROOT: 'web/webroot',
            SMARTEDIT_RESOURCE_URI_REGEXP: /^(.*)\/test\/e2e/
        }),
        moduleUtils.bootstrap(
            (httpBackendService: HttpBackendService) => {
                httpBackendService.matchLatestDefinitionEnabled(true);

                httpBackendService.whenGET(/test\/e2e/).passThrough();
                httpBackendService.whenGET(/static-resources/).passThrough();

                const map = [
                    {
                        value: '"thepreviewTicketURI"',
                        key: 'previewTicketURI'
                    },
                    {
                        value:
                            '{"smartEditContainerLocation":"/test/e2e/select/generated_outerapp.js"}',
                        key: 'applications.OuterappModule'
                    },
                    {
                        value: '"/cmswebservices/v1/i18n/languages"',
                        key: 'i18nAPIRoot'
                    }
                ];

                httpBackendService.whenGET(/smartedit\/configuration/).respond(() => {
                    return [200, map];
                });

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

                httpBackendService.whenGET(/i18n/).passThrough();
                httpBackendService.whenGET(/web\/common\/services\/select\/.*\.html/).passThrough();
            },
            [HttpBackendService]
        )
    ]
})
export class OuterMocksModule {}

window.pushModules(OuterMocksModule);

export const mockLanguagesV1 = [
    {
        id: 'en',
        label: 'English'
    },
    {
        id: 'de',
        label: 'German'
    },
    {
        id: 'ru',
        label: 'Russian'
    },
    {
        id: 'pl',
        label: 'Polish'
    }
];

export const mockLanguagesV2 = [
    {
        id: 'fr',
        label: 'French'
    },
    {
        id: 'en',
        label: 'English'
    },
    {
        id: 'es',
        label: 'Spanish'
    },
    {
        id: 'it',
        label: 'Italian'
    }
];

export const mockSites = [
    {
        id: 'site1',
        label: 'Site1'
    },
    {
        id: 'site2',
        label: 'Site2'
    },
    {
        id: 'site3',
        label: 'Site3'
    },
    {
        id: 'site4',
        label: 'Site4'
    },
    {
        id: 'site5',
        label: 'Site5'
    },
    {
        id: 'site6',
        label: 'Site6'
    },
    {
        id: 'site7',
        label: 'Site7'
    },
    {
        id: 'site8',
        label: 'Site8'
    },
    {
        id: 'site9',
        label: 'Site9'
    },
    {
        id: 'site10',
        label: 'Site10'
    },
    {
        id: 'site11',
        label: 'Site11'
    },
    {
        id: 'site12',
        label: 'Site12'
    },
    {
        id: 'site13',
        label: 'Site13'
    },
    {
        id: 'site14',
        label: 'Site14'
    },
    {
        id: 'site15',
        label: 'Site15'
    },
    {
        id: 'site16',
        label: 'Site16'
    },
    {
        id: 'site17',
        label: 'Site17'
    },
    {
        id: 'site18',
        label: 'Site18'
    },
    {
        id: 'site19',
        label: 'Site19'
    },
    {
        id: 'site20',
        label: 'Site20'
    }
];

export const mockProductsV1 = [
    {
        id: 'product1',
        label: 'Test Product 1#',
        image: '',
        price: 123
    },
    {
        id: 'product2',
        label: 'Test Product 2#',
        image: '',
        price: 234
    },
    {
        id: 'product3',
        label: 'Test Product 3#',
        image: '',
        price: 567
    },
    {
        id: 'product4',
        label: 'Test Product 4#',
        image: '',
        price: 554
    },
    {
        id: 'product5',
        label: 'Test Product 5#',
        image: '',
        price: 557
    },
    {
        id: 'product6',
        label: 'Test Product 6#',
        image: '',
        price: 557
    },
    {
        id: 'product7',
        label: 'Test Product 7#',
        image: '',
        price: 557
    },
    {
        id: 'product8',
        label: 'Test Product 8#',
        image: '',
        price: 557
    },
    {
        id: 'product9',
        label: 'Test Product 9#',
        image: '',
        price: 557
    },
    {
        id: 'product10',
        label: 'Test Product 10#',
        image: '',
        price: 557
    },
    {
        id: 'product11',
        label: 'Test Product 11#',
        image: '',
        price: 557
    },
    {
        id: 'product12',
        label: 'Test Product 12#',
        image: '',
        price: 557
    },
    {
        id: 'product13',
        label: 'Test Product 13#',
        image: '',
        price: 557
    },
    {
        id: 'product14',
        label: 'Test Product 14#',
        image: '',
        price: 557
    },
    {
        id: 'product15',
        label: 'Test Product 15#',
        image: '',
        price: 557
    },
    {
        id: 'product16',
        label: 'Test Product 16#',
        image: '',
        price: 557
    },
    {
        id: 'product17',
        label: 'Test Product 17#',
        image: '',
        price: 557
    },
    {
        id: 'product18',
        label: 'Test Product 18#',
        image: '',
        price: 557
    },
    {
        id: 'product19',
        label: 'Test Product 19#',
        image: '',
        price: 557
    },
    {
        id: 'product20',
        label: 'Test Product 20#',
        image: '',
        price: 557
    }
];

export const mockProductsV2 = [
    {
        id: 'product1',
        label: 'Test Product 1#',
        image: '',
        price: 123
    },
    {
        id: 'product3',
        label: 'Test Product 3#',
        image: '',
        price: 789
    },
    {
        id: 'product4',
        label: 'Test Product 4#',
        image: '',
        price: 234
    },
    {
        id: 'product5',
        label: 'Test Product 5#',
        image: '',
        price: 234
    }
];
