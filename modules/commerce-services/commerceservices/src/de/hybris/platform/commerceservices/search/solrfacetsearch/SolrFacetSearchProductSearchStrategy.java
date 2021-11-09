/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.ProductSearchStrategy;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;


/**
 * SolrFacetSearchProductSearchStrategy interface.
 * <p>
 * Abstraction for solrfacetsearch product search implementations.
 *
 * @param <ITEM> The type of items returned as part of the search results. For example
 *               {@link de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData}
 */
public interface SolrFacetSearchProductSearchStrategy<ITEM> extends
		ProductSearchStrategy<SolrSearchQueryData, ITEM, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>, AutocompleteSuggestion>
{
	//solrfacetsearch product search strategy marker interface
}
