/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Body, Controller, Get, Headers, NotFoundException, Post, Query } from '@nestjs/common';
import { adminData, adminToken, CMSManagerData } from 'fixtures/constants/authorization';

@Controller()
export class MiscellaneousController {
    @Get('*authorizationserver/oauth/whoami')
    getAuthorization(@Headers('authorization') token: string) {
        return token === 'bearer ' + adminToken.access_token ? adminData : CMSManagerData;
    }

    @Get('cmswebservices/v1/enums')
    getComponentType(@Query('enumClass') enumClass: string) {
        if (enumClass === 'de.mypackage.Orientation') {
            return {
                enums: [
                    {
                        code: 'vertical',
                        label: 'Vertical'
                    },
                    {
                        code: 'horizontal',
                        label: 'Horizontal'
                    }
                ]
            };
        } else {
            throw new NotFoundException();
        }
    }

    @Post('*thepreviewTicketURI')
    getPreviewTickerURI(@Body() payload: any) {
        const STOREFRONT_URI = 'http://127.0.0.1:9000/jsTests/e2e/storefront.html';
        const resourcePath = payload.siteId === 'apparel' ? payload.resourcePath : STOREFRONT_URI;
        return {
            ticketId: 'dasdfasdfasdfa',
            resourcePath,
            versionId: payload.versionId
        };
    }
}
