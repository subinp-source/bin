/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;


/**
 * Interface responsible for retrieving page for rendering purposes.
 */
public interface PageRenderingService
{

	/**
	 * Returns {@link AbstractPageData} object based on pageLabelOrId or code.
	 *
	 * @param pageTypeCode
	 *           the page type
	 * @param pageLabelOrId
	 *           the page label or id. This field is used only when the page type is ContentPage.
	 * @param code
	 *           the code depends on the page type. If the page type is ProductPage then the code should be a product
	 *           code. If the page type is CategoryPage then the code should be a category code. If the page type is
	 *           CatalogPage then the code should be a catalog page.
	 * @return the {@link AbstractPageData} object.
	 * @throws CMSItemNotFoundException
	 *            if the page does not exists.
	 */
	AbstractPageData getPageRenderingData(final String pageTypeCode, final String pageLabelOrId, final String code)
			throws CMSItemNotFoundException;

	/**
	 * Returns {@link AbstractPageData} object based on pageId.
	 *
	 * @param pageId
	 *           the page id
	 * @return the {@link AbstractPageData} object.
	 * @throws CMSItemNotFoundException
	 *            if the page does not exists.
	 */
	AbstractPageData getPageRenderingData(String pageId) throws CMSItemNotFoundException;

	/**
	 * Find all pages. The result is paginated. This should be used for rendering purposes.
	 *
	 * @param typeCode
	 *           the page type code
	 * @param paginationData
	 *           the pagination and sorting information
	 * @return a search result containing a list of {@link AbstractPageData}; can be empty, never {@code NULL}
	 */
	SearchPageData<AbstractPageData> findAllRenderingPageData(String typeCode, SearchPageData searchPageData);
}
