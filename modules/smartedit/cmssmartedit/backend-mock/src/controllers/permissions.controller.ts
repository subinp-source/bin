/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Controller, Get, Query } from '@nestjs/common';
import {
    globalPermissions,
    onlineCatalogPermissions,
    stagedCatalogPermissions
} from 'fixtures/constants/permissions';
import { ICatalogPermission } from 'fixtures/entities/permissions';

@Controller()
export class PermissionsController {
    @Get('permissionswebservices/v1/permissions/principals/:principalUid/global*')
    getGlobalPermissions() {
        return globalPermissions;
    }

    @Get('permissionswebservices/v1/permissions/principals/:principalUid/catalogs*')
    getCatalogPermissions(@Query('catalogVersion') catalogVersion: string) {
        const permissionsList: ICatalogPermission[] =
            catalogVersion === 'Online' ? onlineCatalogPermissions : stagedCatalogPermissions;
        return {
            permissionsList
        };
    }

    @Get('permissionswebservices/v1/permissions/principals/:principalUid/attributes*')
    getAttributesPermissions() {
        return {
            permissionsList: []
        };
    }
}
