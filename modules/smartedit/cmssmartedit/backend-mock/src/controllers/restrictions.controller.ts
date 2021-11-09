/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Body, Controller, Get, HttpCode, Param, Post } from '@nestjs/common';
import {
    pageRestrictionList,
    pageTypeRestrictionTypeList,
    restrictionTypes
} from 'fixtures/constants/restrictions';
import {
    bottomHeaderSlot,
    emptyDummySlot,
    footerSlot,
    otherSlot,
    searchBoxSlot,
    staticDummySlot,
    topHeaderSlot
} from 'fixtures/constants/contentSlots';

@Controller()
export class RestrictionsController {
    @Get(
        'cmswebservices/v1/catalogs/:catalogId/versions/:versionId/pages/:pageId/contentslots/:slotId/typerestrictions'
    )
    getTypeRestrictions(@Param('pageId') pageId: string, @Param('slotId') slotId: string) {
        if (pageId === 'secondpage') {
            return {
                contentSlotName: '',
                validComponentTypes: []
            };
        } else if (pageId === 'homepage') {
            switch (slotId) {
                case 'topHeaderSlot':
                    return topHeaderSlot;
                case 'bottomHeaderSlot':
                    return bottomHeaderSlot;
                case 'footerSlot':
                    return footerSlot;
                case 'otherSlot':
                    return otherSlot;
                case 'staticDummySlot':
                    return staticDummySlot;
                case 'emptyDummySlot':
                    return emptyDummySlot;
                case 'searchBoxSlot':
                    return searchBoxSlot;
            }
        }
        return {};
    }

    @Post(
        'cmswebservices/v1/catalogs/:catalogId/versions/:versionId/pages/:pageId/typerestrictions'
    )
    @HttpCode(200)
    getTypeRestrictionsBySlotIds(@Param('pageId') pageId: string, @Body() payload: any) {
        if (pageId === 'homepage' && payload.pageUid === 'homepage') {
            return [
                topHeaderSlot,
                bottomHeaderSlot,
                footerSlot,
                otherSlot,
                staticDummySlot,
                emptyDummySlot,
                searchBoxSlot
            ];
        }
        return [];
    }

    @Get('cmswebservices/v1/pagetypesrestrictiontypes')
    getPageTypeRestrictionTypes() {
        return { pageTypeRestrictionTypeList };
    }

    @Get('cmswebservices/v1/restrictiontypes')
    getRestrictionTypes() {
        return { restrictionTypes };
    }

    @Get('cmswebservices/v1/pagesrestrictions')
    getPageRestrictions() {
        return { pageRestrictionList };
    }
}
