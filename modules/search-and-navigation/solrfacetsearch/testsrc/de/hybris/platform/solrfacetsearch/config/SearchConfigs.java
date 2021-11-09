/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.config;

import java.util.List;


public final class SearchConfigs
{
	// Suppresses default constructor, ensuring non-instantiability.
	private SearchConfigs()
	{
	}

	public static SearchConfig createSearchConfig(final List emptyList, final int i)
	{
		final SearchConfig config = new SearchConfig();
		config.setDefaultSortOrder(emptyList);
		config.setPageSize(i);
		return config;
	}

}
