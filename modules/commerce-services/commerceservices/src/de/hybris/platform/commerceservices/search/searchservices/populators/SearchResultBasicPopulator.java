/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.searchservices.data.SnSearchResultConverterData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.SnSearchResult;

/**
 * Populates common search result properties
 */
public class SearchResultBasicPopulator implements
		Populator<SnSearchResultConverterData<SnSearchResult>, ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel>>
{
	@Override
	public void populate(final SnSearchResultConverterData<SnSearchResult> source,
			final ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel> target)
	{
		final SnSearchResult searchResult = source.getSnSearchResult();
		final String sortId = searchResult.getSort() != null ? searchResult.getSort().getId() : null;

		final SolrSearchQueryData currentQuery = cloneSearchQuery(source.getSearchQuery());
		currentQuery.setSort(sortId);

		target.setFreeTextSearch(source.getSearchQuery().getFreeTextSearch());
		target.setCurrentQuery(currentQuery);
		target.setCategoryCode(source.getSearchQuery().getCategoryCode());
	}

	protected SolrSearchQueryData cloneSearchQuery(final SolrSearchQueryData source)
	{
		final SolrSearchQueryData target = new SolrSearchQueryData();
		target.setFreeTextSearch(source.getFreeTextSearch());
		target.setCategoryCode(source.getCategoryCode());
		target.setSort(source.getSort());
		target.setFilterTerms(source.getFilterTerms());
		target.setFilterQueries(source.getFilterQueries());
		target.setSearchQueryContext(source.getSearchQueryContext());

		return target;
	}
}
