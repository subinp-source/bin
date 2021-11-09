/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;


/**
 * ProductSearchStrategyFactory interface. Implementations of this interface are responsible for getting the applicable instance of {@link ProductSearchStrategy}.
 *
 * @param <ITEM> The type of items returned as part of the search results of specific
 *               {@link de.hybris.platform.commerceservices.search.ProductSearchStrategy}. For example
 *               {@link de.hybris.platform.commerceservices.search.solrfacetsearch.impl.DefaultSolrFacetSearchProductSearchStrategy}
 */
public interface ProductSearchStrategyFactory<ITEM>
{

	/**
	 * Returns applicable instance of {@link ProductSearchStrategy}.
	 *
	 * @return product search strategy
	 */
	ProductSearchStrategy<SolrSearchQueryData, ITEM, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>, AutocompleteSuggestion> getSearchStrategy();
}
