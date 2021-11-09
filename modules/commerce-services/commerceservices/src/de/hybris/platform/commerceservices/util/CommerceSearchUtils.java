/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.util;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.Collections;


/**
 * Some helper methods for the Search.
 */
public final class CommerceSearchUtils
{
	private CommerceSearchUtils()
	{
		// private constructor to avoid instantiation
	}

	/**
	 * @return {@link PageableData} with pageSize set to -1 which will display all pages
	 */
	public static PageableData getAllOnOnePagePageableData()
	{
		final PageableData pageable = new PageableData();
		pageable.setCurrentPage(0);
		pageable.setPageSize(-1);
		pageable.setSort("asc");
		return pageable;
	}

	public static <T> SearchPageData<T> createEmptySearchPageData()
	{
		final SearchPageData<T> searchPageData = new SearchPageData<>();
		final PaginationData pagination = new PaginationData();
		pagination.setTotalNumberOfResults(0);
		searchPageData.setPagination(pagination);
		searchPageData.setResults(Collections.emptyList());
		searchPageData.setSorts(Collections.emptyList());
		return searchPageData;
	}
}
