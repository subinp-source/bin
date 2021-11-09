/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import {
    CONTEXT_CATALOG,
    CONTEXT_CATALOG_VERSION,
    CONTEXT_SITE_ID,
    GatewayProxied,
    ICatalogService,
    ICatalogVersionPermissionService,
    SeDowngradeService
} from 'smarteditcommons';
import {
    CatalogVersionPermission,
    CatalogVersionPermissionRestService
} from './CatalogVersionPermissionRestService';

enum PERMISSION_TYPES {
    READ = 'read',
    WRITE = 'write'
}

@SeDowngradeService(ICatalogVersionPermissionService)
@GatewayProxied(
    'hasWritePermission',
    'hasReadPermission',
    'hasWritePermissionOnCurrent',
    'hasReadPermissionOnCurrent'
)
export class CatalogVersionPermissionService extends ICatalogVersionPermissionService {
    constructor(
        private catalogVersionPermissionRestService: CatalogVersionPermissionRestService,
        private catalogService: ICatalogService
    ) {
        super();
    }

    public async hasPermission(
        accessType: PERMISSION_TYPES,
        catalogId: string,
        catalogVersion: string,
        siteId?: string
    ): Promise<boolean> {
        const [shouldOverride, response] = await Promise.all([
            this.shouldIgnoreCatalogPermissions(accessType, catalogId, catalogVersion, siteId),
            this.catalogVersionPermissionRestService.getCatalogVersionPermissions(
                catalogId,
                catalogVersion
            )
        ]);
        if (this.isCatalogVersionPermissionResponse(response)) {
            const targetPermission = response.permissions.find(
                (permission) => permission.key === accessType
            );
            const value = targetPermission ? targetPermission.value : 'false';
            return value === 'true' || shouldOverride;
        }
        return false;
    }

    public async hasSyncPermissionFromCurrentToActiveCatalogVersion(): Promise<boolean> {
        const data = await this.catalogService.retrieveUriContext();

        return await this.hasSyncPermissionToActiveCatalogVersion(
            data[CONTEXT_CATALOG],
            data[CONTEXT_CATALOG_VERSION]
        );
    }

    public async hasSyncPermissionToActiveCatalogVersion(
        catalogId: string,
        catalogVersion: string
    ): Promise<boolean> {
        const targetCatalogVersion = await this.catalogService.getActiveContentCatalogVersionByCatalogId(
            catalogId
        );
        return await this.hasSyncPermission(catalogId, catalogVersion, targetCatalogVersion);
    }

    public async hasSyncPermission(
        catalogId: string,
        sourceCatalogVersion: string,
        targetCatalogVersion: string
    ): Promise<boolean> {
        const response = await this.catalogVersionPermissionRestService.getCatalogVersionPermissions(
            catalogId,
            sourceCatalogVersion
        );
        if (
            this.isCatalogVersionPermissionResponse(response) &&
            response.syncPermissions &&
            response.syncPermissions.length > 0
        ) {
            const permission = response.syncPermissions.some((syncPermission) => {
                return syncPermission
                    ? syncPermission.canSynchronize === true &&
                          syncPermission.targetCatalogVersion === targetCatalogVersion
                    : false;
            });
            return permission;
        }
        return false;
    }

    public hasWritePermissionOnCurrent(): Promise<boolean> {
        return this.hasCurrentCatalogPermission(PERMISSION_TYPES.WRITE);
    }

    public hasReadPermissionOnCurrent(): Promise<boolean> {
        return this.hasCurrentCatalogPermission(PERMISSION_TYPES.READ);
    }

    public hasWritePermission(catalogId: string, catalogVersion: string): Promise<boolean> {
        return this.hasPermission(PERMISSION_TYPES.WRITE, catalogId, catalogVersion);
    }

    public hasReadPermission(catalogId: string, catalogVersion: string): Promise<boolean> {
        return this.hasPermission(PERMISSION_TYPES.READ, catalogId, catalogVersion);
    }

    /**
     * if in the context of an experience AND the catalogVersion is the active one, then permissions should be ignored in read mode
     */
    private async shouldIgnoreCatalogPermissions(
        accessType: PERMISSION_TYPES,
        catalogId: string,
        catalogVersion: string,
        siteId: string
    ): Promise<boolean> {
        const promise =
            siteId && accessType === PERMISSION_TYPES.READ
                ? this.catalogService.getActiveContentCatalogVersionByCatalogId(catalogId)
                : Promise.resolve();
        const versionCheckedAgainst = await promise;
        return versionCheckedAgainst === catalogVersion;
    }

    /**
     * Verifies whether current user has write or read permission for current catalog version.
     * @param {String} accessType
     */
    private async hasCurrentCatalogPermission(accessType: PERMISSION_TYPES): Promise<boolean> {
        const data = await this.catalogService.retrieveUriContext();
        return await this.hasPermission(
            accessType,
            data[CONTEXT_CATALOG],
            data[CONTEXT_CATALOG_VERSION],
            data[CONTEXT_SITE_ID]
        );
    }

    private isCatalogVersionPermissionResponse(
        response: unknown
    ): response is CatalogVersionPermission {
        return !lodash.isEmpty(response);
    }
}
