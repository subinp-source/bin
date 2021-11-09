/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.searchservices.data.SnSearchResultConverterData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.SnNamedSort;
import de.hybris.platform.searchservices.search.data.SnSearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

/**
 * Populates search result sorts
 */
public class SearchResultSortsPopulator implements
		Populator<SnSearchResultConverterData<SnSearchResult>, ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel>>
{
	@Override
	public void populate(final SnSearchResultConverterData<SnSearchResult> source,
			final ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel> target)
	{
		final SnSearchResult searchResult = source.getSnSearchResult();

		final String sortId = searchResult.getSort() != null ? searchResult.getSort().getId() : null;
		final List<SortData> sorts = new ArrayList<>();

		if (CollectionUtils.isEmpty(searchResult.getAvailableSorts()))
		{
			sorts.add(createDefaultSort());
		}
		else
		{
			for (final SnNamedSort sort : searchResult.getAvailableSorts())
			{
				sorts.add(convertSort(sort, sortId));
			}
		}

		target.setSorts(sorts);
	}

	protected SortData createDefaultSort()
	{
		final SortData defaultSort = new SortData();
		defaultSort.setCode("relevance");
		defaultSort.setName("Relevance");
		defaultSort.setSelected(true);

		return defaultSort;
	}

	protected SortData convertSort(final SnNamedSort source, final String currentSortId)
	{
		final SortData target = new SortData();
		target.setCode(source.getId());
		target.setName(source.getName());
		target.setSelected(Objects.equals(currentSortId, source.getId()));

		return target;
	}
}
