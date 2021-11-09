/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    IBaseCatalog,
    IBaseCatalogVersion,
    ICatalog,
    ICatalogVersion
} from 'smarteditcommons/dtos';
import { IUriContext } from './IUriContext';
import { ISite } from './ISite';

/**
 * @ngdoc service
 * @name smarteditServicesModule.service:catalogService
 *
 * @description
 * The Catalog Service fetches catalogs for a specified site or for all sites registered on the hybris platform using
 * REST calls to the cmswebservices Catalog Version Details API.
 */
export abstract class ICatalogService {
    // ------------------------------------------------------------------------------------------------------------------------
    //  Deprecated
    // ------------------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------------------------
    //  Active
    // ------------------------------------------------------------------------------------------------------------------------
    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#retrieveUriContext
     * @methodOf smarteditServicesModule.service:catalogService
     *
     * @description
     * Convenience method to return a full {@link resourceLocationsModule.object:UriContext uriContext} to the invoker through a promise.
     * <br/>if uriContext is provided, it will be returned as such.
     * <br/>if uriContext is not provided, A uriContext will be built from the experience present in {@link  smarteditServicesModule.sharedDataService sharedDataService}.
     * if we fail to find a uriContext in sharedDataService, an exception will be thrown.
     * @param {=Object=} uriContext An optional uriContext that, if provided, is simply returned wrapped in a promise
     *
     * @returns {Object} a {@link resourceLocationsModule.object:UriContext uriContext}
     */
    retrieveUriContext(_uriContext?: IUriContext): Promise<IUriContext> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#getContentCatalogsForSite
     * @methodOf smarteditServicesModule.service:catalogService
     *
     * @description
     * Fetches a list of content catalogs for the site that corresponds to the specified site UID.
     *
     * @param {String} siteUID The UID of the site that the catalog versions are to be fetched.
     *
     * @returns {Array} An array of catalog descriptors. Each descriptor provides the following catalog properties:
     * catalog (name), catalogId, and catalog version descriptors.
     */
    getContentCatalogsForSite(siteUID: string): Promise<IBaseCatalog[]> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#getAllContentCatalogsGroupedById
     * @methodOf smarteditServicesModule.service:catalogService
     *
     * @description
     * Fetches a list of content catalog groupings for all sites.
     *
     * @returns {Array} An array of catalog groupings sorted by catalog ID, each of which has a name, a catalog ID, and a list of
     * catalog version descriptors.
     */
    getAllContentCatalogsGroupedById(): Promise<ICatalog[][]> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#getCatalogByVersion
     * @methodOf smarteditServicesModule.service:catalogService
     *
     * @description
     * Fetches a list of catalogs for the given site UID and a given catalog version.
     *
     * @param {String} siteUID The UID of the site that the catalog versions are to be fetched.
     * @param {String} catalogVersion The version of the catalog that is to be fetched.
     *
     * @returns {Array} An array containing the catalog descriptor (if any). Each descriptor provides the following catalog properties:
     * catalog (name), catalogId, and catalogVersion.
     */
    // FIXME : this method does not seem to be safe for same catalogversion version name across multiple catalogs
    getCatalogByVersion(siteUID: string, catalogVersionName: string): Promise<IBaseCatalog[]> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#isContentCatalogVersionNonActive
     * @methodOf smarteditServicesModule.service:catalogService
     *
     * @description
     * Determines whether the catalog version identified by the given uriContext is a non active one
     * if no uriContext is provided, an attempt will be made to retrieve an experience from {@link smarteditServicesModule.sharedDataService sharedDataService}
     *
     * @param {Object} uriContext the {@link resourceLocationsModule.object:UriContext UriContext}. Optional
     * @returns {Boolean} true if the given catalog version is non active
     */
    isContentCatalogVersionNonActive(_uriContext?: IUriContext): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#getContentCatalogActiveVersion
     * @methodOf smarteditServicesModule.service:catalogService
     *
     * @description
     * find the version that is flagged as active for the given uriContext
     * if no uriContext is provided, an attempt will be made to retrieve an experience from {@link smarteditServicesModule.sharedDataService sharedDataService}
     *
     * @param {Object} uriContext the {@link resourceLocationsModule.object:UriContext UriContext}. Optional
     * @returns {String} the version name
     */
    getContentCatalogActiveVersion(_uriContext?: IUriContext): Promise<string> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#getActiveContentCatalogVersionByCatalogId
     * @methodOf smarteditServicesModule.service:catalogService
     *
     * @description
     * Finds the version name that is flagged as active for the given content catalog.
     *
     * @param {String} contentCatalogId The UID of content catalog for which to retrieve its active catalog version name.
     * @returns {String} the version name
     */
    getActiveContentCatalogVersionByCatalogId(contentCatalogId: string): Promise<string> {
        'proxyFunction';
        return null;
    }

	/**
	 * Finds the current site ID
	 * @returns The ID of the current site.
	 */
	getCurrentSiteID(): Promise<string> {
		'proxyFunction';
		return null;
	}
    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#getDefaultSiteForContentCatalog
     * @methodOf smarteditServicesModule.service:catalogService
     * @description
     * Finds the ID of the default site configured for the provided content catalog.
     *
     * @param {String} contentCatalogId The UID of content catalog for which to retrieve its default site ID.
     * @returns {String} the ID of the default site found.
     */
    getDefaultSiteForContentCatalog(contentCatalogId: string): Promise<ISite> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#getContentCatalogVersion
     * @methodOf smarteditServicesModule.service:catalogService
     *
     * @description
     * Finds the catalog version given an uriContext object.
     *
     * @param {Object} uriContext  An object that represents the current context, containing information about the site.
     * @returns {Promise<IBaseCatalogVersion>} A promise that resolves to the catalog version descriptor found.
     */
    getContentCatalogVersion(uriContext: IUriContext): Promise<IBaseCatalogVersion> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#getCatalogVersionByUuid
     * @methodOf smarteditServicesModule.service:catalogService
     *
     * @description
     * Finds the catalog version descriptor identified by the provided UUID. An exception is thrown if no
     * match is found.
     *
     * @param {String} catalogVersionUuid The UID of the catalog version descriptor to find.
     * @param {String=} siteId the ID of the site where to perform the search. If no ID is provided, the search will
     * be performed on all permitted sites.
     * @returns {Promise} A promise that resolves to the catalog version descriptor found.
     *
     */
    getCatalogVersionByUuid(catalogVersionUuid: string, siteId?: string): Promise<ICatalogVersion> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#getCatalogVersionUUid
     * @methodOf smarteditServicesModule.service:catalogService
     *
     * @description
     * Finds the catalog version UUID given an optional urlContext object. The current catalog version UUID from the active experience selector is returned, if the URL is not present in the call.
     *
     * @param {Object} urlContext An object that represents the current context, containing information about the site.
     * @returns {Promise<String>} A promise that resolves to the catalog version uuid.
     *
     */
    getCatalogVersionUUid(_uriContext?: IUriContext): Promise<string> {
        'proxyFunction';
        return null;
    }

    /**
     * Fetches a list of product catalogs for the site that corresponds to the specified site UID key.
     *
     * @param siteUIDKey The UID of the site that the catalog versions are to be fetched.
     *
     * @returns An array of catalog descriptors. Each descriptor provides the following catalog properties:
     * catalog (name), catalogId, and catalog version descriptors.
     */
    getProductCatalogsBySiteKey(siteUIDKey: string): Promise<IBaseCatalog[]> {
        'proxyFunction';
        return null;
    }

    /**
     * Fetches a list of product catalogs for the site that corresponds to the specified site UID value.
     *
     * @param siteUIDValue The UID value of the site that the catalog versions are to be fetched.
     *
     * @returns An array of catalog descriptors. Each descriptor provides the following catalog properties:
     * catalog (name), catalogId, and catalog version descriptors.
     */
    getProductCatalogsForSite(siteUIDValue: string): Promise<IBaseCatalog[]> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#getActiveProductCatalogVersionByCatalogId
     * @methodOf smarteditServicesModule.service:catalogService
     *
     * @description
     * Finds the version name that is flagged as active for the given product catalog.
     *
     * @param {String} productCatalogId The UID of product catalog for which to retrieve its active catalog version name.
     * @returns {String} the version name
     */
    getActiveProductCatalogVersionByCatalogId(productCatalogId: string): Promise<string> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:catalogService#returnActiveCatalogVersionUIDs
     * @methodOf smarteditServicesModule.service:catalogService
     *
     * @description
     * Fetches all the active catalog version uuid's for a provided array of catalogs.
     *
     * @param {Array} An array of catalogs objects. Each catalog object must have a versions array.
     * @returns {Array} An array of catalog version uuid's
     */
    returnActiveCatalogVersionUIDs(catalogs: ICatalog[]): string[] {
        'proxyFunction';
        return null;
    }
}
