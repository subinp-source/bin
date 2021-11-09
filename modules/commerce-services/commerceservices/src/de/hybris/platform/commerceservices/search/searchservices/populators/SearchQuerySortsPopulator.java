/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.populators;

import de.hybris.platform.commerceservices.search.searchservices.data.SnSearchQueryConverterData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.data.SnSort;

import org.apache.commons.lang.StringUtils;

import static de.hybris.platform.searchservices.util.ConverterUtils.convert;

/**
 * Populates sorts
 */
public class SearchQuerySortsPopulator implements Populator<SnSearchQueryConverterData, SnSearchQuery>
{
	@Override
	public void populate(final SnSearchQueryConverterData source, final SnSearchQuery target)
	{
		if (source.getPageable() != null && StringUtils.isNotBlank(source.getPageable().getSort()))
		{
			// use the sort from the pageable
			target.setSort(convert(source.getPageable().getSort(), this::convertSort));
		}
		else if (source.getSearchQuery() != null && StringUtils.isNotBlank(source.getSearchQuery().getSort()))
		{
			// fallback to the last sort used in the searchQuery
			target.setSort(convert(source.getSearchQuery().getSort(), this::convertSort));
		}
	}

	protected SnSort convertSort(final String source)
	{
		final SnSort target = new SnSort();
		target.setId(source);

		return target;
	}
}
