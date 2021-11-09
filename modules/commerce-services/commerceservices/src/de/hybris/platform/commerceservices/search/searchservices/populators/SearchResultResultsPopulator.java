/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.searchservices.data.SnSearchResultConverterData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.SnSearchHit;
import de.hybris.platform.searchservices.search.data.SnSearchResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Populates search results
 */
public class SearchResultResultsPopulator implements
		Populator<SnSearchResultConverterData<SnSearchResult>, ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel>>
{
	@Override
	public void populate(final SnSearchResultConverterData<SnSearchResult> source,
			final ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel> target)
	{
		final SnSearchResult searchResult = source.getSnSearchResult();

		if (searchResult != null)
		{
			final List<SearchResultValueData> results = searchResult.getSearchHits().stream().map(this::buildSearchResultValueData)
					.collect(Collectors.toList());

			target.setResults(results);
		}
	}

	protected SearchResultValueData buildSearchResultValueData(final SnSearchHit searchHit)
	{
		final SearchResultValueData searchResultValueData = new SearchResultValueData();
		searchResultValueData.setValues(searchHit.getFields());
		searchResultValueData.setTags(searchHit.getTags());

		return searchResultValueData;
	}
}
