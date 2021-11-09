/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Body, Controller, Get, Param, Post, Query } from '@nestjs/common';

import {
    bottomHeaderSlot,
    footerSlot,
    otherSlot,
    pageContentSlotComponentList,
    pageContentSlotList,
    topHeaderSlot
} from '../../fixtures/constants/pageContents';

import { pages } from '../../fixtures/constants/pages';
import { IPage } from '../../fixtures/entities/pages';

@Controller()
export class PagesController {
    @Get(
        'cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:versionId/pages/:pageId/fallbacks'
    )
    getFallbackPages() {
        return {
            uids: ['secondpage']
        };
    }

    @Get(
        'cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:versionId/pagescontentslotscomponents*'
    )
    getPageContentSlotComponents() {
        return { pageContentSlotComponentList };
    }

    @Get(
        'cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:versionId/pagescontentslotscontainers*'
    )
    getPageContentSlotContainers() {
        return { pageContentSlotContainerList: [] };
    }

    @Get(
        'cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:versionId/pagescontentslots*'
    )
    getPageContentSlots(@Param('versionId') versionId: string, @Query('slotId') slotId: string) {
        switch (slotId) {
            case 'topHeaderSlot':
                return { pageContentSlotList: [topHeaderSlot] };
            case 'bottomHeaderSlot':
                return { pageContentSlotList: [bottomHeaderSlot] };
            case 'otherSlot':
                return { pageContentSlotList: [otherSlot] };
            case 'footerSlot':
                return { pageContentSlotList: [footerSlot] };
        }
        if (versionId === 'Online') {
            return { pageContentSlotList: [] };
        }
        return { pageContentSlotList };
    }

    @Get('cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:versionId/pagetemplates*')
    getPageTemplateData() {
        return {
            templates: [
                {
                    frontEndName: 'pageTemplate1',
                    name: 'Page Template 1',
                    previewIcon: 'previewIcon 1',
                    uid: 'pageTemplate1',
                    uuid: 'pageTemplate1'
                },
                {
                    frontEndName: 'pageTemplate2',
                    name: 'Page Template 2',
                    previewIcon: 'previewIcon 2',
                    uid: 'pageTemplate2',
                    uuid: 'pageTemplate2'
                }
            ]
        };
    }

    @Get(
        'cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:versionId/pages/:pageId/variations'
    )
    getVariationPages(@Query('pageId') pageId: string) {
        if (pageId && pageId === 'homepage') {
            return {
                uids: ['MOCKED_VARIATION_PAGE_ID']
            };
        } else {
            return {
                uids: []
            };
        }
    }

    @Get('cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:versionId/pages/:pageId')
    getPageByID(@Param('pageId') pageId: string) {
        if (pageId === 'thirdpage') {
            return {
                uids: []
            };
        }
        const resultPage: IPage | undefined = pages.find((page: IPage) => page.uid === pageId);
        return resultPage ? resultPage : pages[0];
    }

    @Post('cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:versionId/pages')
    createPage(@Body() payload: any) {
        if (payload.uid && payload.uid === 'bla') {
            return [
                400,
                {
                    errors: [
                        {
                            message: 'Some error msg.',
                            reason: 'invalid',
                            subject: 'uid',
                            subjectType: 'parameter',
                            type: 'ValidationError'
                        }
                    ]
                }
            ];
        }
        return {
            uid: 'valid'
        };
    }

    @Get('cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:versionId/pages*')
    getDefaultPages(@Query('typeCode') typeCode: string) {
        if (typeCode) {
            switch (typeCode) {
                case 'ContentPage':
                    return {
                        pages: [
                            {
                                creationtime: '2016-04-08T21:16:41+0000',
                                modifiedtime: '2016-04-08T21:16:41+0000',
                                pk: '8796387968048',
                                masterTemplate: 'PageTemplate',
                                name: 'page1TitleSuffix',
                                label: 'page1TitleSuffix',
                                typeCode: 'ContentPage',
                                uid: 'auid1'
                            }
                        ]
                    };
                case 'CategoryPage':
                    return {
                        pages: [
                            {
                                creationtime: '2016-04-08T21:16:41+0000',
                                modifiedtime: '2016-04-08T21:16:41+0000',
                                pk: '8796387968048',
                                masterTemplate: 'PageTemplate',
                                name: 'page1TitleSuffix',
                                label: 'page1TitleSuffix',
                                typeCode: 'ContentPage',
                                uid: 'auid1'
                            }
                        ]
                    };
                case 'ProductPage':
                    return {
                        pages: [
                            {
                                creationtime: '2016-04-08T21:16:41+0000',
                                modifiedtime: '2016-04-08T21:16:41+0000',
                                pk: '8796387968058',
                                masterTemplate: 'PageTemplate',
                                name: 'productPage1',
                                label: 'productPage1',
                                typeCode: 'CategoryPage',
                                uid: 'auid2'
                            }
                        ]
                    };
                default:
                    return {
                        pagination: {
                            count: 10,
                            page: 1,
                            totalCount: pages.length,
                            totalPages: 3
                        },
                        pages
                    };
            }
        } else {
            return { pages };
        }
    }
}
