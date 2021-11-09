/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pages;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.data.CMSPageOperationsData;
import de.hybris.platform.cmsfacades.data.PageTypeData;
import de.hybris.platform.cmsfacades.enums.CMSPageOperation;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.List;


/**
 * Component facade interface which deals with methods related to page operations.
 */
public interface PageFacade
{

	/**
	 * Find all pages.
	 *
	 * @return list of {@link AbstractPageData} ordered by title ascending; never <tt>null</tt>
	 *
	 * @deprecated since 6.6. Please use
	 *             {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade#findCMSItems(de.hybris.platform.cmsfacades.data.CMSItemSearchData, PageableData)}
	 *             instead.
	 */
	@Deprecated(since = "6.6", forRemoval = true)
	List<AbstractPageData> findAllPages();

	/**
	 * Find all page types.
	 *
	 * @return list of all {@link PageTypeData}; never <code>null</code>
	 */
	List<PageTypeData> findAllPageTypes();

	/**
	 * Find all default or variant pages for a given page type.
	 *
	 * @param typeCode
	 *           - the page typecode
	 * @param isDefaultPage
	 *           - set to true to find all default pages; set to false to find all variant pages
	 * @return list of default or variant {@link AbstractPageData} ordered by name ascending; never <tt>null</tt>
	 *
	 * @deprecated since 6.6. Please use
	 *             {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade#findCMSItems(de.hybris.platform.cmsfacades.data.CMSItemSearchData, PageableData)}
	 *             instead.
	 */
	@Deprecated(since = "6.6", forRemoval = true)
	List<AbstractPageData> findPagesByType(String typeCode, Boolean isDefaultPage);

	/**
	 * Find all variant pages for a given page.
	 *
	 * @param pageId
	 *           - the page identifier
	 * @return list of variation page uids; empty if the given page is already a variation page; never <tt>null</tt>
	 * @throws CMSItemNotFoundException
	 *            when the page could not be found
	 */
	List<String> findVariationPages(String pageId) throws CMSItemNotFoundException;

	/**
	 * Find all default pages for a given page.
	 *
	 * @param pageId
	 *           - the page identifier
	 * @return list of default page uids; empty if the given page is already a default page; never <tt>null</tt>
	 * @throws CMSItemNotFoundException
	 *            when the page could not be found
	 */
	List<String> findFallbackPages(String pageId) throws CMSItemNotFoundException;

	/**
	 * Find a single page by its uid. This should be used for management purposes.
	 *
	 * @param uid
	 *           - the uid of the page to retrieve.
	 * @return the page matching the given uid
	 * @throws CMSItemNotFoundException
	 *            when the page could not be found
	 *
	 * @deprecated since 6.6. Please use
	 *             {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade#getCMSItemByUuid(String)} instead.
	 */
	@Deprecated(since = "6.6", forRemoval = true)
	AbstractPageData getPageByUid(String uid) throws CMSItemNotFoundException;

	/**
	 * Returns {@link AbstractPageData} object based on pageLabelOrId or code. This should be used for rendering
	 * purposes.
	 *
	 * @param pageType
	 *           the page type
	 * @param pageLabelOrId
	 *           the page label or id. This field is used only when the page type is ContentPage.
	 * @param code
	 *           the code depends on the page type. If the page type is ProductPage then the code should be a product
	 *           code. If the page type is CategoryPage then the code should be a category code. If the page type is
	 *           CatalogPage then the code should be a catalog page.
	 * @return the {@link AbstractPageData} object
	 * @throws CMSItemNotFoundException
	 *            when the page does not exists
	 */
	AbstractPageData getPageData(String pageType, String pageLabelOrId, String code) throws CMSItemNotFoundException;

	/**
	 * Returns {@link AbstractPageData} object based on the pageId. This should be used for rendering purposes.
	 *
	 * @param pageId
	 *           the page id
	 * @return the {@link AbstractPageData} object
	 * @throws CMSItemNotFoundException
	 *            when the page does not exists
	 */
	AbstractPageData getPageData(String pageId) throws CMSItemNotFoundException;

	/**
	 * Find all pages. The result is paginated. This should be used for rendering purposes.
	 *
	 * @param pageType
	 *           the page type code
	 * @param searchPageData
	 *           the pagination and sorting information
	 * @return a search result containing a list of {@link AbstractPageData}; can be empty, never {@code NULL}
	 */
	SearchPageData<AbstractPageData> findAllPageDataForType(String pageType, SearchPageData searchPageData);

	/**
	 * Performs different operations defined by {@link CMSPageOperation} on the page such as trash page.
	 *
	 * @param pageId
	 *           The uid of the page.
	 * @param cmsWorkflowOperationData
	 *           The {@link CMSPageOperationsData} containing the data to perform the operation.
	 * @return The {@link CMSPageOperationsData} if the operation is successful.
	 * @throws CMSItemNotFoundException
	 *            when the page does not exists
	 */
	CMSPageOperationsData performOperation(final String pageId, CMSPageOperationsData cmsPageOperationData)
			throws CMSItemNotFoundException;

}
