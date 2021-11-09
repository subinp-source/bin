/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.util;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;


public class B2BCommerceTestUtils
{
	private static final int PAGE_SIZE = 5;

	public static PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);
		pageableData.setPageSize(pageSize);
		return pageableData;
	}

	public static PageableData createPageableData()
	{
		return createPageableData(0, PAGE_SIZE, null);
	}
}
