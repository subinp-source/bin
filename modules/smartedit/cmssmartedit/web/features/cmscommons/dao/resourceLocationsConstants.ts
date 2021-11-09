/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:CONTEXT_CATALOG
 *
 * @description
 * Constant containing the name of the catalog uid placeholder in URLs
 */

export const CONTEXT_CATALOG = 'CURRENT_CONTEXT_CATALOG';

/**
 * @ngdoc object
 * @name resourceLocationsModule.object:CONTEXT_CATALOG_VERSION
 *
 * @description
 * Constant containing the name of the catalog version placeholder in URLs
 */

export const CONTEXT_CATALOG_VERSION = 'CURRENT_CONTEXT_CATALOG_VERSION';

/**
 * @ngdoc object
 * @name resourceLocationsModule.object:CONTEXT_SITE_ID
 *
 * @description
 * Constant containing the name of the site uid placeholder in URLs
 */

export const CONTEXT_SITE_ID = 'CURRENT_CONTEXT_SITE_ID';
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:PAGE_CONTEXT_CATALOG
 *
 * @description
 * Constant containing the name of the current page catalog uid placeholder in URLs
 */
export const PAGE_CONTEXT_CATALOG = 'CURRENT_PAGE_CONTEXT_CATALOG';
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:PAGE_CONTEXT_CATALOG_VERSION
 *
 * @description
 * Constant containing the name of the current page catalog version placeholder in URLs
 */
export const PAGE_CONTEXT_CATALOG_VERSION = 'CURRENT_PAGE_CONTEXT_CATALOG_VERSION';

/**
 * @ngdoc object
 * @name resourceLocationsModule.object:PAGE_CONTEXT_SITE_ID
 *
 * @description
 * Constant containing the name of the current page site uid placeholder in URLs
 */

export const PAGE_CONTEXT_SITE_ID = 'CURRENT_PAGE_CONTEXT_SITE_ID';
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:TYPES_RESOURCE_URI
 *
 * @description
 * Resource URI of the component types REST service.
 */
export const TYPES_RESOURCE_URI = '/cmswebservices/v1/types';
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:ITEMS_RESOURCE_URI
 *
 * @description
 * Resource URI of the custom components REST service.
 */
export const ITEMS_RESOURCE_URI = `/cmswebservices/v1/sites/${CONTEXT_SITE_ID}/catalogs/${CONTEXT_CATALOG}/versions/${CONTEXT_CATALOG_VERSION}/items`;
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:ITEMS_RESOURCE_URI
 *
 * @description
 * Resource URI of the custom components REST service.
 */

export const PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI = `/cmswebservices/v1/sites/${PAGE_CONTEXT_SITE_ID}/catalogs/${PAGE_CONTEXT_CATALOG}/versions/${PAGE_CONTEXT_CATALOG_VERSION}/pagescontentslotscomponents`;
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI
 *
 * @description
 * Resource URI of the content slot type restrictions REST service.
 */
export const CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI = `/cmswebservices/v1/catalogs/${PAGE_CONTEXT_CATALOG}/versions/${PAGE_CONTEXT_CATALOG_VERSION}/pages/:pageUid/contentslots/:slotUid/typerestrictions`;
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI
 *
 * @description
 * Resource URI of the content slot type restrictions REST service given the page uid.
 */
export const CONTENT_SLOTS_TYPE_RESTRICTION_RESOURCE_URI = `/cmswebservices/v1/catalogs/${PAGE_CONTEXT_CATALOG}/versions/${PAGE_CONTEXT_CATALOG_VERSION}/pages/:pageUid/typerestrictions`;
/**
 * @ngdoc object
 * @name resourceLocationsMod`ule.object:PAGES_LIST_RESOURCE_URI
 *
 * @description
 * Resource URI of the pages REST service.
 */
export const PAGES_LIST_RESOURCE_URI = `/cmswebservices/v1/sites/${CONTEXT_SITE_ID}/catalogs/${CONTEXT_CATALOG}/versions/${CONTEXT_CATALOG_VERSION}/pages`;
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:PAGE_LIST_PATH
 *
 * @description
 * Path of the page list
 */
export const PAGE_LIST_PATH = '/pages/:siteId/:catalogId/:catalogVersion';
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:TRASHED_PAGE_LIST_PATH
 *
 * @description
 * Path of the page list
 */
export const TRASHED_PAGE_LIST_PATH = '/trashedpages/:siteId/:catalogId/:catalogVersion';
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:PAGES_CONTENT_SLOT_RESOURCE_URI
 *
 * @description
 * Resource URI of the page content slots REST service
 */
export const PAGES_CONTENT_SLOT_RESOURCE_URI = `/cmswebservices/v1/sites/${PAGE_CONTEXT_SITE_ID}/catalogs/${PAGE_CONTEXT_CATALOG}/versions/${PAGE_CONTEXT_CATALOG_VERSION}/pagescontentslots`;
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:PAGE_TEMPLATES_URI
 *
 * @description
 * Resource URI of the page templates REST service
 */
export const PAGE_TEMPLATES_URI = `/cmswebservices/v1/sites/:${CONTEXT_SITE_ID}/catalogs/:${CONTEXT_CATALOG}/versions/:${CONTEXT_CATALOG_VERSION}/pagetemplates`;
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:NAVIGATION_MANAGEMENT_PAGE_PATH
 *
 * @description
 * Path to the Navigation Management
 */
export const NAVIGATION_MANAGEMENT_PAGE_PATH = '/navigations/:siteId/:catalogId/:catalogVersion';
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:NAVIGATION_MANAGEMENT_RESOURCE_URI
 *
 * @description
 * Resource URI of the navigations REST service.
 */
export const NAVIGATION_MANAGEMENT_RESOURCE_URI = `/cmswebservices/v1/sites/:${CONTEXT_SITE_ID}/catalogs/:${CONTEXT_CATALOG}/versions/:${CONTEXT_CATALOG_VERSION}/navigations`;
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:NAVIGATION_MANAGEMENT_ENTRIES_RESOURCE_URI
 *
 * @description
 * Resource URI of the navigations REST service.
 */
export const NAVIGATION_MANAGEMENT_ENTRIES_RESOURCE_URI = `/cmswebservices/v1/sites/:${CONTEXT_SITE_ID}/catalogs/:${CONTEXT_CATALOG}/versions/:${CONTEXT_CATALOG_VERSION}/navigations/:navigationUid/entries`;
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:NAVIGATION_MANAGEMENT_ENTRY_TYPES_RESOURCE_URI
 *
 * @description
 * Resource URI of the navigation entry types REST service.
 */
export const NAVIGATION_MANAGEMENT_ENTRY_TYPES_RESOURCE_URI =
    '/cmswebservices/v1/navigationentrytypes';
/**
 * @ngdoc object
 * @name resourceLocationsModule.CONTEXTUAL_PAGES_RESTRICTIONS_RESOURCE_URI
 *
 * @description
 * Resource URI of the pages restrictions REST service, with placeholders to be replaced by the currently selected catalog version.
 */
export const CONTEXTUAL_PAGES_RESTRICTIONS_RESOURCE_URI = `/cmswebservices/v1/sites/${PAGE_CONTEXT_SITE_ID}/catalogs/${PAGE_CONTEXT_CATALOG}/versions/${PAGE_CONTEXT_CATALOG_VERSION}/pagesrestrictions`;
/**
 * @ngdoc object
 * @name resourceLocationsModule.PAGES_RESTRICTIONS_RESOURCE_URI
 *
 * @description
 * Resource URI of the pages restrictions REST service, with placeholders to be replaced by the currently selected catalog version.
 */
export const PAGES_RESTRICTIONS_RESOURCE_URI =
    '/cmswebservices/v1/sites/:siteUID/catalogs/:catalogId/versions/:catalogVersion/pagesrestrictions';
/**
 * @ngdoc object
 * @name resourceLocationsModule.RESTRICTION_TYPES_URI
 *
 * @description
 * Resource URI of the restriction types REST service.
 */
export const RESTRICTION_TYPES_URI = '/cmswebservices/v1/restrictiontypes';
/**
 * @ngdoc object
 * @name resourceLocationsModule.RESTRICTION_TYPES_URI
 *
 * @description
 * Resource URI of the pageTypes-restrictionTypes relationship REST service.
 */
export const PAGE_TYPES_RESTRICTION_TYPES_URI = '/cmswebservices/v1/pagetypesrestrictiontypes';
/**
 * @ngdoc object
 * @name resourceLocationsModule.PAGE_TYPES_URI
 *
 * @description
 * Resource URI of the page types REST service.
 */
export const PAGE_TYPES_URI = '/cmswebservices/v1/pagetypes';

/**
 * @ngdoc object
 * @name resourceLocationsModule.object:GET_PAGE_SYNCHRONIZATION_RESOURCE_URI
 *
 * @description
 * Resource URI to retrieve the full synchronization status of page related items
 */
export const GET_PAGE_SYNCHRONIZATION_RESOURCE_URI = `/cmssmarteditwebservices/v1/sites/${PAGE_CONTEXT_SITE_ID}/catalogs/${PAGE_CONTEXT_CATALOG}/versions/${PAGE_CONTEXT_CATALOG_VERSION}/synchronizations/versions/:target/pages/:pageUid`;
/**
 * @ngdoc object
 * @name resourceLocationsModule.object:POST_PAGE_SYNCHRONIZATION_RESOURCE_URI
 *
 * @description
 * Resource URI to perform synchronization of page related items
 */
export const POST_PAGE_SYNCHRONIZATION_RESOURCE_URI = `/cmssmarteditwebservices/v1/sites/${CONTEXT_SITE_ID}/catalogs/${CONTEXT_CATALOG}/versions/${CONTEXT_CATALOG_VERSION}/synchronizations/versions/:target`;
