/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.populators;

import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.search.SearchResult;


/**
 */
public class SearchResponsePaginationPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, ITEM>
		implements
		Populator<SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, IndexedTypeSort, SearchResult>, SearchPageData<ITEM>>
{
	@Override
	public void populate(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, IndexedTypeSort, SearchResult> source,
			final SearchPageData<ITEM> target)
	{
		target.setPagination(buildPaginationData(source, source.getSearchResult()));
	}

	protected PaginationData buildPaginationData(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, IndexedTypeSort, SearchResult> source,
			final SearchResult solrSearchResult)
	{
		final PaginationData paginationData = createPaginationData();

		if (solrSearchResult != null)
		{
			paginationData.setTotalNumberOfResults(solrSearchResult.getNumberOfResults());
			paginationData.setPageSize(solrSearchResult.getPageSize());
			paginationData.setCurrentPage(solrSearchResult.getOffset());
			paginationData.setNumberOfPages((int) solrSearchResult.getNumberOfPages());

			// Set the current sort if there is one
			if(solrSearchResult.getCurrentNamedSort() != null)
			{
				paginationData.setSort(solrSearchResult.getCurrentNamedSort().getCode());
			}
		}



		return paginationData;
	}

	protected PaginationData createPaginationData()
	{
		return new PaginationData();
	}
}
