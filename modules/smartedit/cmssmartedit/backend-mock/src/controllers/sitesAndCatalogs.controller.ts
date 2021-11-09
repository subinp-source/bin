/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Body, Controller, Get, HttpCode, Param, Post, Query } from '@nestjs/common';

import { ISite } from 'fixtures/entities/sites/site.entity';
import { ICatalogIds } from 'fixtures/entities/catalogIds.entity';

import { allSites } from 'fixtures/constants/allSites.constant';
import { versions } from 'fixtures/constants/targetContentCatalogVersions.constant';
import {
    apparelContentCatalogDE,
    apparelContentCatalogGlobal,
    apparelContentCatalogUK,
    productCatalogGlobal
} from 'fixtures/constants/catalogs';

@Controller()
export class PagesAndNavigationsController {
    @Get('cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:versionId/items*')
    getCatalogItems(
        @Param('siteId') siteId: string,
        @Param('catalogId') catalogId: string,
        @Param('versionId') versionId: string,
        @Query('currentPage') currentPage: string,
        @Query('pageSize') pageSize: string,
        @Query('sort') sort: string
    ) {
        if (
            siteId === 'apparel-uk' &&
            catalogId === 'apparel-ukContentCatalog' &&
            versionId === 'Staged' &&
            currentPage === '0' &&
            pageSize === '10' &&
            sort === 'name'
        ) {
            return {
                componentItems: [
                    {
                        creationtime: '2016-08-17T16:05:47+0000',
                        modifiedtime: '2016-08-17T16:05:47+0000',
                        name: 'Component 1',
                        pk: '1',
                        typeCode: 'CMSParagraphComponent',
                        uid: 'component1',
                        visible: true
                    },
                    {
                        creationtime: '2016-08-17T16:05:47+0000',
                        modifiedtime: '2016-08-17T16:05:47+0000',
                        name: 'Component 2',
                        pk: '2',
                        typeCode: 'componentType2',
                        uid: 'component2',
                        visible: true
                    },
                    {
                        creationtime: '2016-08-17T16:05:47+0000',
                        modifiedtime: '2016-08-17T16:05:47+0000',
                        name: 'Component 3',
                        pk: '3',
                        typeCode: 'componentType3',
                        uid: 'component3',
                        visible: true
                    },
                    {
                        creationtime: '2016-08-17T16:05:47+0000',
                        modifiedtime: '2016-08-17T16:05:47+0000',
                        name: 'Component 4',
                        pk: '4',
                        typeCode: 'componentType4',
                        uid: 'component4',
                        visible: true
                    },
                    {
                        creationtime: '2016-08-17T16:05:47+0000',
                        modifiedtime: '2016-08-17T16:05:47+0000',
                        name: 'Component 5',
                        pk: '5',
                        typeCode: 'componentType5',
                        uid: 'component5',
                        visible: true
                    }
                ]
            };
        }
        return {};
    }

    @Get('cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:versionId/targets*')
    getTargetContentCatalogVersions(@Param() params: any) {
        let res = versions;
        if (params.siteId === 'apparel-uk') {
            res =
                params.versionId === 'Staged'
                    ? versions.filter(
                          (version: any) =>
                              version.uuid === 'apparel-ukContentCatalog/Staged' &&
                              version.version === 'Staged'
                      )
                    : [];
        } else if (params.siteId === 'apparel' && params.versionId === 'Staged') {
            res = versions.filter(
                (version: any) =>
                    version.uuid === 'apparelContentCatalog/Staged' && version.version === 'Staged'
            );
        }
        return { versions: res };
    }

    @Get('cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:versionId')
    getCatalogVersions() {
        return {
            name: {
                en: 'Apparel UK Content Catalog'
            },
            pageDisplayConditions: [
                {
                    options: [
                        {
                            label: 'page.displaycondition.variation',
                            id: 'VARIATION'
                        }
                    ],
                    typecode: 'ProductPage'
                },
                {
                    options: [
                        {
                            label: 'page.displaycondition.variation',
                            id: 'VARIATION'
                        }
                    ],
                    typecode: 'CategoryPage'
                },
                {
                    options: [
                        {
                            label: 'page.displaycondition.primary',
                            id: 'PRIMARY'
                        },
                        {
                            label: 'page.displaycondition.variation',
                            id: 'VARIATION'
                        }
                    ],
                    typecode: 'ContentPage'
                }
            ],
            uid: 'apparel-ukContentCatalog',
            version: 'Online'
        };
    }

    @Get('cmssmarteditwebservices/v1/sites/:baseSiteId/contentcatalogs')
    getContentCatalog(@Param('baseSiteId') baseSiteId: string) {
        if (baseSiteId === 'apparel-de') {
            return {
                catalogs: [apparelContentCatalogGlobal, apparelContentCatalogDE]
            };
        } else if (baseSiteId === 'apparel-uk') {
            return {
                catalogs: [apparelContentCatalogGlobal, apparelContentCatalogUK]
            };
        }
        return {
            catalogs: [apparelContentCatalogGlobal]
        };
    }

    @Get('cmssmarteditwebservices/v1/sites/:baseSiteId/productcatalogs')
    getProductCatalog() {
        return {
            catalogs: [productCatalogGlobal]
        };
    }

    @Post('cmswebservices/v1/sites/catalogs')
    @HttpCode(200)
    searchCatalogs(@Body() catalogIdsDTO: ICatalogIds) {
        const sites: ISite[] = allSites.filter(
            (site: ISite) =>
                site.contentCatalogs.filter(
                    (catalog: string) =>
                        catalogIdsDTO.catalogIds && catalogIdsDTO.catalogIds.indexOf(catalog) !== -1
                ).length > 0
        );
        return { sites };
    }

    @Get('cmswebservices/v1/sites')
    getSites() {
        return { sites: allSites };
    }
}
