/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* tslint:disable:max-classes-per-file */

import { NgModule } from '@angular/core';

import { moduleUtils, HttpBackendService, Payload, SeEntryModule } from 'smarteditcommons';
import { STOREFRONT_URI } from '../utils/outerConstants';

class PreviewTicketDataService {
    private currentPreviewTicket: string = 'defaultTicket';

    setCurrentPreviewTicket(previewTicket: string): void {
        this.currentPreviewTicket = previewTicket;
    }
    getCurrentPreviewTicket(): string {
        return this.currentPreviewTicket;
    }
}

@SeEntryModule('OuterBackendMocks')
@NgModule({
    providers: [
        PreviewTicketDataService,

        moduleUtils.bootstrap(
            (
                httpBackendService: HttpBackendService,
                previewTicketDataService: PreviewTicketDataService
            ) => {
                httpBackendService.matchLatestDefinitionEnabled(true);
                httpBackendService.whenGET(/fragments/).passThrough();

                httpBackendService.whenPUT(/smartedit\/configuration/).respond(() => [404, null]);

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
                    .whenPOST(/previewwebservices\/v1\/preview/)
                    .respond((method, url, data) => {
                        const postedData = JSON.parse(data);

                        const contentCatalogObject = postedData.catalogVersions.find(
                            (catalogVersion: any) => {
                                return catalogVersion.catalog.indexOf('ContentCatalog') > -1;
                            }
                        );

                        if (
                            contentCatalogObject.catalog === 'electronicsContentCatalog' &&
                            contentCatalogObject.catalogVersion === 'Online' &&
                            postedData.language === 'it'
                        ) {
                            return [
                                400,
                                {
                                    errors: [
                                        {
                                            message:
                                                "CatalogVersion with catalogId 'electronicsContentCatalog' and version 'Online' not found!",
                                            type: 'UnknownIdentifierError'
                                        }
                                    ]
                                }
                            ];
                        }

                        if (
                            contentCatalogObject.catalog === 'electronicsContentCatalog' &&
                            contentCatalogObject.catalogVersion === 'Online' &&
                            // postedData.resourcePath === STOREFRONT_URI &&
                            postedData.language === 'pl'
                        ) {
                            return [
                                200,
                                {
                                    catalog: 'electronicsContentCatalog',
                                    catalogVersion: 'Online',
                                    resourcePath: STOREFRONT_URI,
                                    language: 'pl',
                                    ticketId: 'validTicketId1'
                                }
                            ];
                        }

                        if (
                            contentCatalogObject.catalog === 'apparel-ukContentCatalog' &&
                            contentCatalogObject.catalogVersion === 'Staged' &&
                            // postedData.resourcePath === STOREFRONT_URI &&
                            postedData.language === 'en'
                        ) {
                            return [
                                200,
                                {
                                    catalog: 'apparel-ukContentCatalog',
                                    catalogVersion: 'Staged',
                                    resourcePath: STOREFRONT_URI,
                                    language: 'en',
                                    ticketId: 'apparel-ukContentCatalogStagedValidTicket'
                                }
                            ];
                        }

                        // We can not check hours and minutes  here because of the difference between developer's time zone
                        // and the timezone of the pipeline.
                        if (
                            contentCatalogObject.catalog === 'apparel-ukContentCatalog' &&
                            contentCatalogObject.catalogVersion === 'Online' &&
                            // postedData.resourcePath === STOREFRONT_URI &&
                            postedData.time &&
                            postedData.time.indexOf('2016-01-01T') >= 0 &&
                            postedData.language === 'fr'
                        ) {
                            return [
                                200,
                                {
                                    catalog: 'apparel-ukContentCatalog',
                                    catalogVersion: 'Online',
                                    resourcePath: STOREFRONT_URI,
                                    language: 'fr',
                                    time: '1/1/16 1:00 PM',
                                    ticketId: 'apparel-ukContentCatalogOnlineValidTicket'
                                }
                            ];
                        }

                        if (
                            contentCatalogObject.catalog === 'apparel-ukContentCatalog' &&
                            contentCatalogObject.catalogVersion === 'Online' &&
                            // postedData.resourcePath === STOREFRONT_URI &&
                            postedData.language === 'fr'
                        ) {
                            return [
                                200,
                                {
                                    catalog: 'apparel-ukContentCatalog',
                                    catalogVersion: 'Online',
                                    resourcePath: STOREFRONT_URI,
                                    language: 'fr',
                                    ticketId: 'apparel-ukContentCatalogOnlineValidTicket'
                                }
                            ];
                        }

                        if (
                            contentCatalogObject.catalog === 'electronicsContentCatalog' &&
                            contentCatalogObject.catalogVersion === 'Online' &&
                            // postedData.resourcePath === STOREFRONT_URI &&
                            postedData.time &&
                            postedData.time.indexOf('2016-01-01T13:00') >= 0 &&
                            postedData.language === 'pl'
                        ) {
                            return [
                                200,
                                {
                                    catalog: 'electronicsContentCatalog',
                                    catalogVersion: 'Online',
                                    resourcePath: STOREFRONT_URI,
                                    language: 'pl',
                                    time: '1/1/16 1:00 PM',
                                    ticketId: 'validTicketId2'
                                }
                            ];
                        }

                        if (
                            contentCatalogObject.catalog === 'electronicsContentCatalog' &&
                            contentCatalogObject.catalogVersion === 'Staged' &&
                            postedData.newField === 'New Data For Preview' &&
                            // postedData.resourcePath === STOREFRONT_URI &&
                            postedData.time &&
                            postedData.time.indexOf('2016-01-01T00:00:00') >= 0 &&
                            postedData.language === 'it'
                        ) {
                            return [
                                200,
                                {
                                    catalog: 'electronicsContentCatalog',
                                    catalogVersion: 'Staged',
                                    resourcePath: STOREFRONT_URI,
                                    language: 'it',
                                    newField: 'New Data For Preview',
                                    time: '1/1/16 12:00 AM',
                                    ticketId: 'validTicketId2'
                                }
                            ];
                        }

                        if (
                            contentCatalogObject.catalog === 'electronicsContentCatalog' &&
                            contentCatalogObject.catalogVersion === 'Staged' &&
                            // postedData.resourcePath === STOREFRONT_URI &&
                            postedData.time &&
                            postedData.time.indexOf('2016-01-01T00:00:00') >= 0 &&
                            postedData.language === 'it'
                        ) {
                            return [
                                200,
                                {
                                    catalog: 'electronicsContentCatalog',
                                    catalogVersion: 'Staged',
                                    resourcePath: STOREFRONT_URI,
                                    language: 'it',
                                    time: '1/1/16 12:00 AM',
                                    ticketId: 'validTicketId2'
                                }
                            ];
                        }

                        if (previewTicketDataService.getCurrentPreviewTicket() !== '') {
                            previewTicketDataService.setCurrentPreviewTicket('validTicketId');
                        }

                        return [
                            200,
                            {
                                ...postedData,
                                resourcePath: STOREFRONT_URI,
                                ticketId: 'validTicketId'
                            }
                        ];
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
                    .whenGET(/storefront\.html/)
                    .respond(() => [200, ('<somehtml/>' as any) as Payload]);

                httpBackendService.whenGET(/\/dummystorefrontOtherPage.html/).respond(() => {
                    // Test if we already loaded the homepage of the initial experience with a valid ticket
                    if (previewTicketDataService.getCurrentPreviewTicket() === 'validTicketId') {
                        previewTicketDataService.setCurrentPreviewTicket('');
                        return [404, null];
                    } else {
                        return [200, {}];
                    }
                });
            },

            [HttpBackendService, PreviewTicketDataService]
        )
    ]
})
export class OuterBackendMocks {}
