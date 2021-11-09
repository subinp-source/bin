/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    Body,
    Controller,
    Delete,
    Get,
    HttpCode,
    HttpStatus,
    NotFoundException,
    Param,
    Post,
    Put,
    Query,
    Res
} from '@nestjs/common';
import { IVersion } from '../../fixtures/entities/versions';

import { VersionsService } from '../services';

@Controller()
export class VersionsController {
    constructor(private readonly versionsService: VersionsService) {}

    @Post('cmswebservices/v1/sites/:siteId/cmsitems/:itemUUID/versions/:versionId/rollbacks')
    @HttpCode(204)
    setCMSVersionOnItem(@Param('versionId') versionId: string) {
        if (versionId === 'homepage_version4') {
            throw new NotFoundException();
        }
    }

    @Delete('cmswebservices/v1/sites/:siteId/cmsitems/:itemUUID/versions/:versionId')
    @HttpCode(204)
    deleteVersion(@Param('versionId') versionId: string) {
        if (versionId === 'homepage_version4') {
            throw new NotFoundException();
        }
        this.versionsService.deletePageVersionByID(versionId);
    }

    @Put('cmswebservices/v1/sites/:siteId/cmsitems/:itemUUID/versions/:versionId')
    replacePageVersion(
        @Res() res: any,
        @Param('siteId') siteId: string,
        @Param('itemUUID') itemUUID: string,
        @Param('versionId') versionId: string,
        @Body() versionPayload: IVersion
    ) {
        if (versionId === 'homepage_version4') {
            return res
                .status(HttpStatus.BAD_REQUEST)
                .json({
                    errors: [
                        {
                            message: 'The value provided is already in use.',
                            reason: 'invalid',
                            subject: 'label',
                            subjectType: 'parameter',
                            type: 'ValidationError'
                        }
                    ]
                })
                .send();
        }
        this.versionsService.updatePageMockVersion(siteId, itemUUID, versionId, versionPayload);
        return res.json(versionPayload).send();
    }

    @Post('cmswebservices/v1/sites/:siteId/cmsitems/:itemUUID/versions')
    createCMSVersion(
        @Res() res: any,
        @Body() versionData: IVersion,
        @Param('itemUUID') itemUUID: string
    ) {
        if (versionData.label === 'New Test Version') {
            return res
                .json({
                    uid: 'homepage_version_new',
                    itemUUID,
                    label: versionData.label,
                    description: versionData.description,
                    creationtime: '2018-01-01T21:59:59+0000'
                })
                .send();
        }
        return res
            .status(HttpStatus.BAD_REQUEST)
            .json({
                errors: [
                    {
                        message: 'The value provided is already in use.',
                        reason: 'invalid',
                        subject: 'label',
                        subjectType: 'parameter',
                        type: 'ValidationError'
                    }
                ]
            })
            .send();
    }

    @Get('cmswebservices/v1/sites/:siteId/cmsitems/:itemUUID/versions*')
    getCMSVersions(
        @Param('siteId') siteId: string,
        @Param('itemUUID') itemUUID: string,
        @Query('mask') mask: string,
        @Query('currentPage') currentPage: string,
        @Query('pageSize') pageSize: string
    ) {
        const pSize: number = pageSize && /^\d+$/.test(pageSize) ? +pageSize : 1;
        const currPage: number = currentPage && /^\d+$/.test(currentPage) ? +currentPage : 0;
        const filteredVersions: IVersion[] = this.versionsService.filterVersionsByMask(
            siteId,
            itemUUID,
            mask
        );
        const slicedVersions: IVersion[] = this.versionsService.sliceVersions(
            currPage,
            pSize,
            filteredVersions
        );
        const pageResult = {
            pagination: {
                count: pSize,
                hasNest: true,
                hasPrevious: true,
                page: currPage,
                totalCount: filteredVersions.length,
                totalPages: Math.floor(filteredVersions.length / pSize)
            },
            results: slicedVersions
        };
        return pageResult;
    }

    @Post('cmswebservices/refresh/versions')
    refreshFixture() {
        this.versionsService.refreshPageMockVersions();
    }
}
