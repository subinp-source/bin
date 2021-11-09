/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import {
    rarelyChangingContent,
    userEvictionTag,
    Cached,
    CATALOG_VERSION_PERMISSIONS_RESOURCE_URI_CONSTANT,
    ISessionService,
    RestServiceFactory,
    SeDowngradeService,
    URIBuilder
} from 'smarteditcommons';

export interface CatalogVersionSyncPermission {
    canSynchronize: boolean;
    targetCatalogVersion: string;
}

export interface CatalogVersionPermissionMap {
    key: string;
    value: string;
}

export interface CatalogVersionPermission {
    catalogId: string;
    catalogVersion: string;
    permissions: CatalogVersionPermissionMap[];
    syncPermissions: CatalogVersionSyncPermission[];
}

/**
 * @ngdoc service
 * @name smarteditServicesModule.service:catalogVersionPermissionRestService
 *
 * @description
 * The catalog version permission service is used to check if the current user has been granted certain permissions
 * on a given catalog ID and catalog Version.
 */
@SeDowngradeService()
export class CatalogVersionPermissionRestService {
    constructor(
        private restServiceFactory: RestServiceFactory,
        private sessionService: ISessionService
    ) {}

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogVersionPermissionRestService#getCatalogVersionPermissions
     * @methodOf smarteditServicesModule.service:catalogVersionPermissionRestService
     *
     * @description
     * This method returns permissions from the Catalog Version Permissions Service API.
     *
     * Sample Request:
     * GET /permissionswebservices/v1/permissions/principals/{principal}/catalogs?catalogId=apparel-deContentCatalog&catalogVersion=Online
     *
     * Sample Response from API:
     * {
     * "permissionsList": [
     *     {
     *       "catalogId": "apparel-deContentCatalog",
     *       "catalogVersion": "Online",
     *       "permissions": [
     *         {
     *           "key": "read",
     *           "value": "true"
     *         },
     *         {
     *           "key": "write",
     *           "value": "false"
     *         }
     *       ],
     *      "syncPermissions": [
     *        {
     *          "canSynchronize": "true",
     *          "targetCatalogVersion": "Online"
     *        }
     *     }
     *    ]
     * }
     *
     * Sample Response returned by the service:
     * {
     *   "catalogId": "apparel-deContentCatalog",
     *   "catalogVersion": "Online",
     *   "permissions": [
     *      {
     *        "key": "read",
     *        "value": "true"
     *      },
     *      {
     *        "key": "write",
     *        "value": "false"
     *      }
     *     ],
     *    "syncPermissions": [
     *      {
     *        "canSynchronize": "true",
     *        "targetCatalogVersion": "Online"
     *      }
     *    ]
     *  }
     *
     * @param {String} catalogId The Catalog ID
     * @param {String} catalogVersion The Catalog Version name
     *
     * @returns {Promise} A Promise which returns an object exposing a permissions array containing the catalog version permissions
     */
    @Cached({ actions: [rarelyChangingContent], tags: [userEvictionTag] })
    getCatalogVersionPermissions(
        catalogId: string,
        catalogVersion: string
    ): Promise<CatalogVersionPermission | {}> {
        this.validateParams(catalogId, catalogVersion);

        return this.sessionService.getCurrentUsername().then((principal) => {
            const postURI = new URIBuilder(CATALOG_VERSION_PERMISSIONS_RESOURCE_URI_CONSTANT)
                .replaceParams({ principal })
                .build();

            const restService = this.restServiceFactory.get<{
                permissionsList: CatalogVersionPermission[];
            }>(postURI);

            return restService
                .get({
                    catalogId,
                    catalogVersion
                })
                .then(({ permissionsList }) => {
                    return permissionsList[0] || {};
                });
        });
    }

    // TODO: When everything has been migrated to typescript it is sufficient enough to remove this validation.
    private validateParams(catalogId: string, catalogVersion: string): void {
        if (!catalogId) {
            throw new Error('catalog.version.permission.service.catalogid.is.required');
        }

        if (!catalogVersion) {
            throw new Error('catalog.version.permission.service.catalogversion.is.required');
        }
    }
}
