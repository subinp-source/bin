/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:ICatalogVersionPermissionService
 * @description Provides a logic that allows to verify read and write permissions for a particular catalog version.
 */
export abstract class ICatalogVersionPermissionService {
    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:ICatalogVersionPermissionService#hasWritePermission
     * @methodOf smarteditServicesModule.interface:ICatalogVersionPermissionService
     *
     * @description
     * Verifies whether current user has write permission for provided catalogId and catalogVersion.
     *
     * @param {String} catalogId catalog id
     * @param {String} catalogVersion catalog version
     * @returns {Promise<Boolean>} A promise resolving to a boolean `true` if current user has write permission, else `false`
     */
    hasWritePermission(catalogId: string, catalogVersion: string): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:ICatalogVersionPermissionService#hasReadPermission
     * @methodOf smarteditServicesModule.interface:ICatalogVersionPermissionService
     *
     * @description
     * Verifies whether current user has read permission for provided catalogId and catalogVersion.
     *
     * @param {String} catalogId catalog id
     * @param {String} catalogVersion catalog version
     * @returns {Promise<Boolean>} A promise resolving to a boolean `true` if current user has read permission, else `false`
     */
    hasReadPermission(catalogId: string, catalogVersion: string): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:ICatalogVersionPermissionService#hasWritePermissionOnCurrent
     * @methodOf smarteditServicesModule.interface:ICatalogVersionPermissionService
     *
     * @description
     * Verifies whether current user has write permission for current catalog version.
     *
     * @returns {Promise<Boolean>} A promise resolving to a boolean `true` if current user has write permission, else `false`
     */
    hasWritePermissionOnCurrent(): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:ICatalogVersionPermissionService#hasReadPermissionOnCurrent
     * @methodOf smarteditServicesModule.interface:ICatalogVersionPermissionService
     *
     * @description
     * Verifies whether current user has read permission for current catalog version.
     *
     * @returns {Promise<Boolean>} A promise resolving to a boolean `true` if current user has read permission, else `false`
     */
    hasReadPermissionOnCurrent(): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:ICatalogVersionPermissionService#hasSyncPermission
     * @methodOf smarteditServicesModule.interface:ICatalogVersionPermissionService
     *
     * @description
     * Verifies whether current user has sync permission for provided catalogId, source and target catalog versions.
     *
     * @param {String} catalogId catalog id
     * @param {String} sourceCatalogVersion source catalog version
     * @param {String} targetCatalogVersion target catalog version
     * @returns {Promise<Boolean>} A promise resolving to a boolean `true` if current user has sync permission, else `false`
     */
    hasSyncPermission(
        catalogId: string,
        sourceCatalogVersion: string,
        targetCatalogVersion: string
    ): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:ICatalogVersionPermissionService#hasSyncPermissionFromCurrentToActiveCatalogVersion
     * @methodOf smarteditServicesModule.interface:ICatalogVersionPermissionService
     *
     * @description
     * Verifies whether current user has sync permission for current catalog version.
     *
     * @returns {Promise<Boolean>} A promise resolving to a boolean `true` if current user has sync permission, else `false`
     */
    hasSyncPermissionFromCurrentToActiveCatalogVersion(): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:ICatalogVersionPermissionService#hasSyncPermissionToActiveCatalogVersion
     * @methodOf smarteditServicesModule.interface:ICatalogVersionPermissionService
     *
     * @description
     * Verifies whether current user has sync permission for provided catalogId and catalog version.
     *
     * @param {String} catalogId catalog id
     * @param {String} catalogVersion catalog version
     * @returns {Promise<Boolean>} A promise resolving to a boolean `true` if current user has sync permission, else `false`
     */
    hasSyncPermissionToActiveCatalogVersion(
        catalogId: string,
        catalogVersion: string
    ): Promise<boolean> {
        'proxyFunction';
        return null;
    }
}
