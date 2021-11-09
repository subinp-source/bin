/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.populators;

import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;

/**
 */
public class SearchResponseCategoryCodePopulator<STATE, ITEM, CATEGORY> implements Populator<SolrSearchResponse, ProductCategorySearchPageData<STATE, ITEM, CATEGORY>>
{
	@Override
	public void populate(final SolrSearchResponse source, final ProductCategorySearchPageData<STATE, ITEM, CATEGORY> target)
	{
		target.setCategoryCode(source.getRequest().getSearchQueryData().getCategoryCode());
	}
}
