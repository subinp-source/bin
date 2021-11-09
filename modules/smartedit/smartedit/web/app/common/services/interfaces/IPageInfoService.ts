/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:IPageInfoService
 *
 * @description
 * The IPageInfoService provides information about the storefront page currently loaded in the iFrame.
 */

export abstract class IPageInfoService {
    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPageInfoService#getPageUID
     * @methodOf smarteditServicesModule.interface:IPageInfoService
     *
     * @description
     * This extracts the pageUID of the storefront page loaded in the smartedit iframe.
     *
     * @return {Promise<string>} A promise resolving to a string matching the page's ID
     */
    getPageUID(): Promise<string> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPageInfoService#getPageUUID
     * @methodOf smarteditServicesModule.interface:IPageInfoService
     *
     * @description
     * This extracts the pageUUID of the storefront page loaded in the smartedit iframe.
     * The UUID is different from the UID in that it is an encoding of uid and catalog version combined
     *
     * @return {Promise<string>} A promise resolving to the page's UUID
     */
    getPageUUID(): Promise<string> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.interface:IPageInfoService#getCatalogVersionUUIDFromPage
     * @methodOf smarteditServicesModule.interface:IPageInfoService
     *
     * @description
     * This extracts the catalogVersionUUID of the storefront page loaded in the smartedit iframe.
     * The UUID is different from the UID in that it is an encoding of uid and catalog version combined
     *
     * @return {Promise<string>} A promise resolving to the page's UUID
     */
    getCatalogVersionUUIDFromPage(): Promise<string> {
        'proxyFunction';
        return null;
    }
}
