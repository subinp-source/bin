/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IUriContext } from 'smarteditcommons';
import { CmsApprovalStatus, CMSItem, ICMSPage } from '../../dtos';
import { Page } from '../../dao';

/**
 * @ngdoc service
 * @name cmsSmarteditServicesModule.service:PageService
 *
 * @description
 * The pageServiceModule provides services for dealing with CMS page objects
 *
 */
export abstract class IPageService {
    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#getPageById
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * Retrieves the page corresponding to the given page UID in the current contextual
     * site + catalog + catalog version.
     *
     * @param {String} pageUid The identifier of the page
     *
     * @returns {Promise<ICMSPage>} A promise that resolves to the page corresponding to this uid.
     */
    public getPageById(pageUid: string): Promise<ICMSPage> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#getCurrentPageInfo
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * Retrieves the page information of the page identified by the given uuid.
     *
     * @param {String} uuid The unique identifier of the page
     *
     * @returns {Promise<ICMSPage>} A promise that resolves to the page corresponding to the given uuid.
     *
     */
    public getPageByUuid(uuid: string): Promise<ICMSPage> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#getCurrentPageInfo
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * Retrieves the page information of the page that is currently loaded.
     *
     * @returns {Promise<ICMSPage>} A promise that resolves to a CMS Item object containing
     * information related to the current page
     */
    public getCurrentPageInfo(): Promise<ICMSPage> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#getCurrentPageInfoByVersion
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * Retrieves a version, as identified by the provided version id, of the page information that is currently loaded.
     *
     * @param {String} versionId The ID of the page version to load.
     *
     * @returns {Promise<ICMSPage>} A promise that resolves to a CMS Item object containing
     * information related to the version selected of the current page
     */
    public getCurrentPageInfoByVersion(versionId: string): Promise<ICMSPage> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#isPagePrimary
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * Determines if a page belonging to the current contextual site+catalog+catalogversion is primary.
     *
     * @param {String} pageUid The UID of the page.
     *
     * @returns {Promise<boolean>} A promise that resolves to true if the page is primary, false otherwise.
     */
    public isPagePrimary(pageUid: string): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#isPagePrimaryWithContext
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * Determines if a page belonging to the provided contextual site+catalog+catalogversion is primary.
     *
     * @param {String} pageUid The UID of the page.
     * @param {IUriContext} uriContext The uriContext for the pageId
     *
     * @returns {Promise<boolean>} A promise that resolves to true if the page is primary, false otherwise.
     */
    public isPagePrimaryWithContext(pageUid: string, uriContext: IUriContext): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#getPrimaryPage
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * Retrieves the primary page of the given variation page in the current site+catalog+catalogversion.
     *
     * @param {String} variationPageId The UID of the variation page for which to find its primary page.
     *
     * @returns {Promise<ICMSPage>} A promise that resolves to the page object or undefined if no primary page was found.
     */
    public getPrimaryPage(variationPageUid: string): Promise<ICMSPage> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#primaryPageForPageTypeExists
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * Returns true if primary page exists for a given page type
     *
     * @param {String} pageTypeCode A page type code to filter pages by
     * @param {Object} uriParams An optional {@link resourceLocationsModule.object:UriContext UriContext}
     *
     * @returns {Promise<boolean>} A promise that resolves to true if the page exists, false otherwise.
     */

    public primaryPageForPageTypeExists(
        pageTypeCode: string,
        uriParams?: IUriContext
    ): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#getPaginatedPrimaryPagesForPageType
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * Fetches a pagination page for list of pages for a given site+catalog+catalogversion and page
     *
     * @param {String} pageTypeCode A page type code to filter pages by
     * @param {Object} [uriParams] An optional {@link resourceLocationsModule.object:UriContext UriContext}
     * @param {Object} [fetchPageParams] An optional Fetch Page strategy parameters. If not provided it will return results for the first page only.
     * @param {Object} [fetchPageParams.currentPage=0]
     * @param {Object} [fetchPageParams.pageSize=10]
     *
     * @returns {Promise<Page<ICMSPage>>} A promise that resolves to pagination with array of pages
     */
    public getPaginatedPrimaryPagesForPageType(
        pageTypeCode: string,
        uriParams?: IUriContext,
        fetchPageParams?: {
            search: string;
            pageSize: number;
            currentPage: number;
        }
    ): Promise<Page<CMSItem>> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#getVariationPages
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * Retrieves the variation pages of the given primary page in the current site+catalog+catalogversion.
     *
     * @param {String} primaryPageId The UID of the primary page.
     *
     * @returns {Promise<ICMSPage[]>} A promise that resolves an array of variation pages or an empty list if none are found.
     */
    public getVariationPages(primaryPageUid: string): Promise<ICMSPage[]> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#updatePageById
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * Updates the page corresponding to the given page UID with the payload provided for the current site+catalog+catalogversion.
     *
     * @param {String} pageUid The UID of the page
     * @param {ICMSPage} payload The information to update the page with.
     *
     * @returns {Promise<ICMSPage>} A promise that resolves to the JSON page object as it now exists in the backend
     */
    public updatePageById(pageUid: string, payload: ICMSPage): Promise<ICMSPage> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#forcePageApprovalStatus
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * This method will forcefully update the page approval status (as long as the current user has the right permissions) of the page loaded
     * in the current context to the given status.
     *
     * @returns {Promise<ICMSPage>} If request is successful, it returns a promise that resolves with the updated CMS Item object. If the
     * request fails, it resolves with errors from the backend.
     */
    public forcePageApprovalStatus(newPageStatus: CmsApprovalStatus): Promise<ICMSPage> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#isPageApproved
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * This method is used to determine whether the given page is approved (and can be synched).
     *
     * @param {string|ICMSPage} pageParam The page to check.
     *
     * @returns {Promise<boolean>} A promise that resolves to a boolean. If the page is already approved it will return true.
     * False, otherwise.
     */
    public isPageApproved(pageParam: string | ICMSPage): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:PageService#buildUriContextForCurrentPage
     * @methodOf cmsSmarteditServicesModule.service:PageService
     *
     * @description
     * Returns the uriContext populated with the siteId, catalogId and catalogVersion taken from $routeParams and fallback to the currentExperience
     * Note: From the page list, $routeParams are defined. From the storefront, $routeParams are undefined.
     *
     * @return {Promise<IUriContext>} promise resolve to the uri context
     */
    public buildUriContextForCurrentPage(): Promise<IUriContext> {
        'proxyFunction';
        return null;
    }
}
