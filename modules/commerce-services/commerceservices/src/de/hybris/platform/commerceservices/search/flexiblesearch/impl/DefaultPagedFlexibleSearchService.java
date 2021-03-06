/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.flexiblesearch.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Default implementation of {@link PagedFlexibleSearchService}
 *
 * @deprecated Since 6.6. Use
 *             {@link de.hybris.platform.servicelayer.search.paginated.impl.DefaultPaginatedFlexibleSearchService}
 *             instead
 */
@Deprecated(since = "6.6", forRemoval = true)
public class DefaultPagedFlexibleSearchService implements PagedFlexibleSearchService
{
	private FlexibleSearchService flexibleSearchService;

	@Override
	public <T> SearchPageData<T> search(final FlexibleSearchQuery searchQuery, final PageableData pageableData)
	{
		validateParameterNotNullStandardMessage("searchQuery", searchQuery);
		validatePagableParameters(pageableData);
		populatePagableProperties(pageableData, searchQuery);

		final SearchResult<T> searchResult = getFlexibleSearchService().search(searchQuery);
		final SearchPageData<T> result = createPagedSearchResult(searchResult, pageableData);
		return result;
	}

	@Override
	public <T> SearchPageData<T> search(final String query, final Map<String, ?> queryParams, final PageableData pageableData)
	{
		validateParameterNotNullStandardMessage("query", query);
		validatePagableParameters(pageableData);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		if (MapUtils.isNotEmpty(queryParams))
		{
			searchQuery.addQueryParameters(queryParams);
		}
		return search(searchQuery, pageableData);
	}

	@Override
	public <T> SearchPageData<T> search(final List<SortQueryData> sortQueries, final String defaultSortCode,
			final Map<String, ?> queryParams, final PageableData pageableData)
	{
		validateParameterNotNullStandardMessage("sortQueries", sortQueries);
		validateParameterNotNullStandardMessage("defaultSortCode", defaultSortCode);
		Assert.isTrue(!sortQueries.isEmpty(), "sortQueries must not be empty");
		validatePagableParameters(pageableData);

		// Work out which sort and query to use
		final SortQueryData selectedSortQuery = findSortQueryData(sortQueries, pageableData.getSort(), defaultSortCode);

		// Execute the query
		final SearchPageData<T> searchPageData = search(selectedSortQuery.getQuery(), queryParams, pageableData);

		// Specify which sort was used
		searchPageData.getPagination().setSort(selectedSortQuery.getSortCode());
		searchPageData.setSorts(createSorts(sortQueries, selectedSortQuery.getSortCode()));
		return searchPageData;
	}

	protected void validatePagableParameters(final PageableData pageableData)
	{
		validateParameterNotNullStandardMessage("pageableData", pageableData);
		Assert.isTrue(pageableData.getCurrentPage() >= 0, "pageableData current page must be zero or greater");
	}

	protected void populatePagableProperties(final PageableData pageableData, final FlexibleSearchQuery searchQuery)
	{
		searchQuery.setNeedTotal(true);
		searchQuery.setStart(pageableData.getCurrentPage() * pageableData.getPageSize());
		searchQuery.setCount(pageableData.getPageSize());
	}

	protected <T> SearchPageData<T> createPagedSearchResult(final SearchResult<T> searchResult, final PageableData pageableData)
	{
		final SearchPageData<T> result = createSearchPageData();
		result.setResults(searchResult.getResult());
		result.setPagination(createPagination(pageableData, searchResult));
		// Note: does not set sorts
		return result;
	}

	protected <T> PaginationData createPagination(final PageableData pageableData, final SearchResult<T> searchResult)
	{
		final PaginationData paginationData = createPaginationData();
		paginationData.setPageSize(pageableData.getPageSize());
		paginationData.setSort(pageableData.getSort());
		paginationData.setTotalNumberOfResults(searchResult.getTotalCount());

		// Calculate the number of pages
		paginationData.setNumberOfPages(
				(int) Math.ceil(((double) paginationData.getTotalNumberOfResults()) / paginationData.getPageSize()));

		// Work out the current page, fixing any invalid page values
		paginationData.setCurrentPage(Math.max(0, Math.min(paginationData.getNumberOfPages(), pageableData.getCurrentPage())));

		return paginationData;
	}

	protected SortQueryData findSortQueryData(final List<SortQueryData> sortQueries, final String requestedSortCode,
			final String defaultSortCode)
	{
		validateParameterNotNullStandardMessage("sortQueries", sortQueries);
		validateParameterNotNullStandardMessage("defaultSortCode", defaultSortCode);

		SortQueryData defaultQuery = null;
		SortQueryData requestedQuery = null;

		for (final SortQueryData sortQueryData : sortQueries)
		{
			if (defaultSortCode.equals(sortQueryData.getSortCode()))
			{
				defaultQuery = sortQueryData;
			}

			if (requestedSortCode != null && requestedSortCode.equals(sortQueryData.getSortCode()))
			{
				requestedQuery = sortQueryData;
			}
		}

		return requestedQuery != null ? requestedQuery : defaultQuery;
	}

	protected List<SortData> createSorts(final List<SortQueryData> sortQueries, final String selectedSortCode)
	{
		final List<SortData> result = new ArrayList<SortData>(sortQueries.size());

		for (final SortQueryData sortQuery : sortQueries)
		{
			result.add(createSort(sortQuery, selectedSortCode));
		}
		return result;
	}

	protected SortData createSort(final SortQueryData sortQuery, final String selectedSortCode)
	{
		final SortData sortData = createSortData();
		sortData.setCode(sortQuery.getSortCode());
		sortData.setName(sortQuery.getSortName());
		sortData.setSelected(selectedSortCode != null && selectedSortCode.equals(sortQuery.getSortCode()));
		return sortData;
	}

	protected PaginationData createPaginationData()
	{
		return new PaginationData();
	}

	protected SortData createSortData()
	{
		return new SortData();
	}

	protected <T> SearchPageData<T> createSearchPageData()
	{
		return new SearchPageData<T>();
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
