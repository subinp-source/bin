/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.populators;


import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;

/**
 */
public class SearchResponseFreeTextSearchPopulator<STATE, ITEM> implements Populator<SolrSearchResponse, ProductSearchPageData<STATE, ITEM>>
{
	@Override
	public void populate(final SolrSearchResponse source, final ProductSearchPageData<STATE, ITEM> target)
	{
		target.setFreeTextSearch(source.getRequest().getSearchText());
	}
}
