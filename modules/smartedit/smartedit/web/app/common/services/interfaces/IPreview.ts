/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Payload } from '@smart/utils';

/**
 * @name smarteditServicesModule.interface:ICatalogVersionData
 *
 * @description
 * Interface for data representing a catalog version
 */
export interface IPreviewCatalogVersionData extends Payload {
    /**
     * @name catalog
     * @propertyOf smarteditServicesModule.interface:ICatalogVersionData
     * @description
     * the catalog id
     */
    catalog: string;

    /**
     * @name catalogVersion
     * @propertyOf smarteditServicesModule.interface:ICatalogVersionData
     * @description
     * the catalog version
     */
    catalogVersion: string;
}
/**
 * @name smarteditServicesModule.interface:IPreviewData
 *
 * @description
 * Interface for data sent/received to/from the preview API.
 *
 * Since the preview api is extensible, you can send more fields by adding a new interface that extends this one.
 * All additional members of the Object passed to the preview API will be included in the request.
 */
export interface IPreviewData extends Payload {
    /**
     * @name catalogVersions
     * @propertyOf smarteditServicesModule.interface:IPreviewData
     * @description
     * an array of {@link smarteditServicesModule.interface:IPreviewCatalogVersionData} to preview
     */
    catalogVersions: IPreviewCatalogVersionData[];

    /**
     * @name language
     * @propertyOf smarteditServicesModule.interface:IPreviewData
     * @description
     * the isocode of the language to preview
     */
    language: string;

    /**
     * @name resourcePath
     * @propertyOf smarteditServicesModule.interface:IPreviewData
     * @description
     * the resource path to preview
     */
    resourcePath: string;

    /**
     * @name pageId
     * @propertyOf smarteditServicesModule.interface:IPreviewData
     * @description
     * the uid of the page to preview
     */
    pageId?: string;

    /**
     * @name siteId
     * @propertyOf smarteditServicesModule.interface:IPreviewData
     * @description
     * the uid of the site corresponding to the page to preview
     */
    siteId?: string;

    /**
     * @name time
     * @propertyOf smarteditServicesModule.interface:IPreviewData
     * @description
     * the time in utc format
     */
    time?: string;

    /**
     * @name ticketId
     * @propertyOf smarteditServicesModule.interface:IPreviewData
     * @description
     * Identifier for the preview
     */
    ticketId?: string;
}
