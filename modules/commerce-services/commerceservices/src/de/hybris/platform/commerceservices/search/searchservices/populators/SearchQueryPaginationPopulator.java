/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.populators;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.searchservices.data.SnSearchQueryConverterData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;

/**
 * Populates pagination settings
 */
public class SearchQueryPaginationPopulator implements Populator<SnSearchQueryConverterData, SnSearchQuery>
{
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final int MAX_PAGE_SIZE = 100;

	@Override
	public void populate(final SnSearchQueryConverterData source, final SnSearchQuery target)
	{
		final PageableData pageable = source.getPageable();
		if (pageable != null)
		{
			int pageSize = Math.min(MAX_PAGE_SIZE, pageable.getPageSize());
			if (pageSize <= 0)
			{
				pageSize = DEFAULT_PAGE_SIZE;
			}

			int currentPage = pageable.getCurrentPage();
			if (currentPage <= 0)
			{
				currentPage = 0;
			}

			target.setOffset(pageSize * currentPage);
			target.setLimit(pageSize);
		}
		else
		{
			target.setOffset(0);
			target.setLimit(DEFAULT_PAGE_SIZE);
		}
	}
}
