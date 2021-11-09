/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.util;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;


/**
 * Utility class for helper methods related to search
 */
public final class SearchUtils
{

	private SearchUtils()
	{
		throw new IllegalAccessError("Utility class may not be instantiated");
	}

	/**
	 * Creates a pageable data based on the given parameters
	 *
	 * @param currentPage is the current page which is returned
	 * @param pageSize    is the page size
	 * @param sort        is the sorting method
	 * @return pageable data
	 */
	public static PageableData createPageableData(final int currentPage, final int pageSize, final String sort)
	{
		final PageableData pageable = new PageableData();
		pageable.setCurrentPage(currentPage);
		pageable.setPageSize(pageSize);
		pageable.setSort(sort);
		return pageable;
	}
}
