/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.ProductSearchService;
import de.hybris.platform.commerceservices.search.ProductSearchStrategyFactory;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.convert.converter.Converter;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of the {@link ProductSearchService}
 *
 * @param <ITEM> the type of items returned as part of the search results
 */
public class DefaultSolrProductSearchService<ITEM> implements
		ProductSearchService<SolrSearchQueryData, ITEM, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>>
{

	private Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> searchQueryPageableConverter;
	private Converter<SolrSearchRequest, SolrSearchResponse> searchRequestConverter;
	private Converter<SolrSearchResponse, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>> searchResponseConverter;
	private ProductSearchStrategyFactory<ITEM> productSearchStrategyFactory;

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#getSearchQueryPageableConverter}
	 */
	@Deprecated(since = "2011")
	protected Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> getSearchQueryPageableConverter()
	{
		return searchQueryPageableConverter;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#setSearchQueryPageableConverter}
	 */
	@Deprecated(since = "2011")
	@Required
	public void setSearchQueryPageableConverter(
			final Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> searchQueryPageableConverter)
	{
		this.searchQueryPageableConverter = searchQueryPageableConverter;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#getSearchRequestConverter}
	 */
	@Deprecated(since = "2011")
	protected Converter<SolrSearchRequest, SolrSearchResponse> getSearchRequestConverter()
	{
		return searchRequestConverter;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#setSearchRequestConverter}
	 */
	@Deprecated(since = "2011")
	@Required
	public void setSearchRequestConverter(final Converter<SolrSearchRequest, SolrSearchResponse> searchRequestConverter)
	{
		this.searchRequestConverter = searchRequestConverter;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#getSearchResponseConverter}
	 */
	@Deprecated(since = "2011")
	protected Converter<SolrSearchResponse, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>> getSearchResponseConverter()
	{
		return searchResponseConverter;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#setSearchResponseConverter}
	 */
	@Deprecated(since = "2011")
	@Required
	public void setSearchResponseConverter(
			final Converter<SolrSearchResponse, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>> searchResponseConverter)
	{
		this.searchResponseConverter = searchResponseConverter;
	}

	public ProductSearchStrategyFactory<ITEM> getProductSearchStrategyFactory()
	{
		return productSearchStrategyFactory;
	}

	@Required
	public void setProductSearchStrategyFactory(final ProductSearchStrategyFactory<ITEM> productSearchStrategyFactory)
	{
		this.productSearchStrategyFactory = productSearchStrategyFactory;
	}

	// End spring inject methods

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> textSearch(final String text,
			final PageableData pageableData)
	{
		return productSearchStrategyFactory.getSearchStrategy().textSearch(text, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> textSearch(final String text,
			final SearchQueryContext searchQueryContext, final PageableData pageableData)
	{
		return productSearchStrategyFactory.getSearchStrategy().textSearch(text, searchQueryContext, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> categorySearch(final String categoryCode,
			final PageableData pageableData)
	{
		return productSearchStrategyFactory.getSearchStrategy().categorySearch(categoryCode, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> categorySearch(final String categoryCode,
			final SearchQueryContext searchQueryContext, final PageableData pageableData)
	{
		return productSearchStrategyFactory.getSearchStrategy().categorySearch(categoryCode, searchQueryContext, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> searchAgain(
			final SolrSearchQueryData searchQueryData, final PageableData pageableData)
	{
		return productSearchStrategyFactory.getSearchStrategy().searchAgain(searchQueryData, pageableData);
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#doSearch}
	 */
	@Deprecated(since = "2011")
	protected ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> doSearch(
			final SolrSearchQueryData searchQueryData, final PageableData pageableData)
	{
		validateParameterNotNull(searchQueryData, "SearchQueryData cannot be null");

		// Create the SearchQueryPageableData that contains our parameters
		final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = buildSearchQueryPageableData(searchQueryData,
				pageableData);

		// Build up the search request
		final SolrSearchRequest solrSearchRequest = getSearchQueryPageableConverter().convert(searchQueryPageableData);

		// Execute the search
		final SolrSearchResponse solrSearchResponse = getSearchRequestConverter().convert(solrSearchRequest);

		// Convert the response
		return getSearchResponseConverter().convert(solrSearchResponse);
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#buildSearchQueryPageableData}
	 */
	@Deprecated(since = "2011")
	protected SearchQueryPageableData<SolrSearchQueryData> buildSearchQueryPageableData(final SolrSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = createSearchQueryPageableData();
		searchQueryPageableData.setSearchQueryData(searchQueryData);
		searchQueryPageableData.setPageableData(pageableData);
		return searchQueryPageableData;
	}

	// Create methods for data object - can be overridden in spring config

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#createSearchQueryPageableData}
	 */
	@Deprecated(since = "2011")
	protected SearchQueryPageableData<SolrSearchQueryData> createSearchQueryPageableData()
	{
		return new SearchQueryPageableData<>();
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#createSearchQueryData}
	 */
	@Deprecated(since = "2011")
	protected SolrSearchQueryData createSearchQueryData()
	{
		return new SolrSearchQueryData();
	}
}
