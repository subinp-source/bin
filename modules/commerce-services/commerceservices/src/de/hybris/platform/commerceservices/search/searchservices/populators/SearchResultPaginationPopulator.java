/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.searchservices.data.SnSearchResultConverterData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.SnSearchResult;

/**
 * Populates search result pagination settings
 */
public class SearchResultPaginationPopulator implements
		Populator<SnSearchResultConverterData<SnSearchResult>, ProductCategorySearchPageData<SolrSearchQueryData, SnSearchResult, CategoryModel>>
{
	@Override
	public void populate(final SnSearchResultConverterData<SnSearchResult> source,
			final ProductCategorySearchPageData<SolrSearchQueryData, SnSearchResult, CategoryModel> target)
	{
		final SnSearchResult searchResult = source.getSnSearchResult();

		if (searchResult != null)
		{
			final PaginationData pagination = new PaginationData();
			final Integer limit = searchResult.getLimit() != null ? searchResult.getLimit() : 0;
			final Integer offset = searchResult.getOffset() != null ? searchResult.getOffset() : 0;
			final Integer totalSize = searchResult.getTotalSize() != null ? searchResult.getTotalSize() : 0;

			pagination.setCurrentPage(limit <= 0 ? 0 : (offset / limit));
			pagination.setNumberOfPages(limit <= 0 ?
					0 :
					((totalSize + limit - 1) / limit));
			pagination.setPageSize(limit);
			pagination.setTotalNumberOfResults(totalSize);

			if (searchResult.getSort() != null)
			{
				pagination.setSort(searchResult.getSort().getId());
			}

			target.setPagination(pagination);
		}
	}
}
