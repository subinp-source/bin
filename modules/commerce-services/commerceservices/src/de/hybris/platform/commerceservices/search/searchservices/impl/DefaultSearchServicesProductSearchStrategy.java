/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.ProductSearchStrategy;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.searchservices.data.SnSearchQueryConverterData;
import de.hybris.platform.commerceservices.search.searchservices.data.SnSearchResultConverterData;
import de.hybris.platform.commerceservices.search.searchservices.strategies.SnProductIndexTypeSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import de.hybris.platform.searchservices.search.service.SnSearchRequest;
import de.hybris.platform.searchservices.search.service.SnSearchResponse;
import de.hybris.platform.searchservices.search.service.SnSearchService;
import de.hybris.platform.searchservices.suggest.data.SnSuggestHit;
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery;
import de.hybris.platform.searchservices.suggest.service.SnSuggestRequest;
import de.hybris.platform.searchservices.suggest.service.SnSuggestResponse;
import de.hybris.platform.searchservices.suggest.service.SnSuggestService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.convert.converter.Converter;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of the {@link ProductSearchStrategy} for search services
 *
 * @param <ITEM> the type of items returned as part of the search results
 */
public class DefaultSearchServicesProductSearchStrategy<ITEM> implements
		ProductSearchStrategy<SolrSearchQueryData, ITEM, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>, AutocompleteSuggestion>
{
	private static final Logger LOG = Logger.getLogger(DefaultSearchServicesProductSearchStrategy.class);

	private static final int DEFAULT_LIMIT = 25;

	private SnSearchService snSearchService;
	private SnSuggestService snSuggestService;
	private SnProductIndexTypeSelectionStrategy snProductIndexTypeSelectionStrategy;
	private SessionService sessionService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;

	private Converter<SnSearchQueryConverterData, SnSearchQuery> searchQueryConverter;
	private Converter<SnSearchResultConverterData<SnSearchResult>, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>> searchResultConverter;

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> textSearch(final String text,
			final PageableData pageable)
	{
		final SolrSearchQueryData searchQuery = createSearchQuery();
		searchQuery.setFreeTextSearch(text);

		return doSearch(searchQuery, pageable);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> textSearch(final String text,
			final SearchQueryContext searchQueryContext, final PageableData pageable)
	{
		final SolrSearchQueryData searchQuery = createSearchQuery();
		searchQuery.setFreeTextSearch(text);

		return doSearch(searchQuery, pageable);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> categorySearch(final String categoryCode,
			final PageableData pageable)
	{
		final SolrSearchQueryData searchQuery = createSearchQuery();
		searchQuery.setCategoryCode(categoryCode);

		return doSearch(searchQuery, pageable);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> categorySearch(final String categoryCode,
			final SearchQueryContext searchQueryContext, final PageableData pageable)
	{
		final SolrSearchQueryData searchQuery = createSearchQuery();
		searchQuery.setCategoryCode(categoryCode);

		return doSearch(searchQuery, pageable);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> searchAgain(
			final SolrSearchQueryData searchQuery, final PageableData pageable)
	{
		return doSearch(searchQuery, pageable);
	}

	@Override
	public List<AutocompleteSuggestion> getAutocompleteSuggestions(final String input)
	{
		try
		{
			final String indexTypeId = findIndexTypeId();
			final SnSuggestQuery suggestQuery = createSuggestQuery(input);

			final SnSuggestRequest suggestRequest = getSnSuggestService().createSuggestRequest(indexTypeId, suggestQuery);
			final SnSuggestResponse suggestResponse = getSnSuggestService().suggest(suggestRequest);

			return convertSuggestResponse(suggestResponse);
		}
		catch (final SnException e)
		{
			LOG.error("Exception while executing search services suggest", e);
			return Collections.emptyList();
		}
	}


	@Override
	public Set<String> getIndexTypes(final String baseSiteId, final String catalogId, final String catalogVersion)
	{
		final BaseSiteModel baseSiteForUID = baseSiteService.getBaseSiteForUID(baseSiteId);
		if (baseSiteForUID != null && baseSiteForUID.getProductIndexType() != null)
		{
			return Collections.singleton(baseSiteForUID.getProductIndexType().getId());
		}

		final BaseStoreModel baseStore = getBaseStore(baseSiteId);
		if (baseStore != null && baseStore.getProductIndexType() != null)
		{
			return Collections.singleton(baseStore.getProductIndexType().getId());
		}

		return Collections.emptySet();
	}

	protected BaseStoreModel getBaseStore(final String baseSiteId)
	{
		return sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public BaseStoreModel execute()
			{
				baseSiteService.setCurrentBaseSite(baseSiteId, true);

				return baseStoreService.getCurrentBaseStore();
			}
		});
	}

	protected SnSuggestQuery createSuggestQuery(final String input)
	{
		final SnSuggestQuery suggestQuery = new SnSuggestQuery();
		suggestQuery.setQuery(input);
		suggestQuery.setLimit(DEFAULT_LIMIT);

		return suggestQuery;
	}

	protected List<AutocompleteSuggestion> convertSuggestResponse(final SnSuggestResponse suggestResponse)
	{
		return suggestResponse.getSuggestResult().getSuggestHits().stream().filter(Objects::nonNull).map(SnSuggestHit::getQuery)
				.map(this::createAutocompleteSuggestion).collect(Collectors.toList());
	}

	protected AutocompleteSuggestion createAutocompleteSuggestion(final String term)
	{
		final AutocompleteSuggestion autocompleteSuggestion = new AutocompleteSuggestion();
		autocompleteSuggestion.setTerm(term);

		return autocompleteSuggestion;
	}

	protected ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> doSearch(
			final SolrSearchQueryData searchQuery, final PageableData pageable)
	{
		validateParameterNotNull(searchQuery, "SolrSearchQueryData cannot be null");

		try
		{
			final String indexTypeId = findIndexTypeId();
			final SnSearchQuery snSearchQuery = convertSearchQuery(indexTypeId, searchQuery, pageable);

			final SnSearchRequest snSearchRequest = getSnSearchService().createSearchRequest(indexTypeId, snSearchQuery);
			final SnSearchResponse snSearchResponse = getSnSearchService().search(snSearchRequest);

			return convertSearchResult(snSearchResponse, searchQuery);
		}
		catch (final SnException e)
		{
			LOG.error("Exception while executing search services search", e);
			return createEmptyProductCategorySearchPageData(searchQuery);
		}
	}

	protected ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> createEmptyProductCategorySearchPageData(
			final SolrSearchQueryData searchQuery)
	{
		final ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> searchPageData = new ProductCategorySearchPageData<>();
		final SolrSearchQueryData clonedSearchQuery = cloneSearchQuery(searchQuery);
		searchPageData.setCurrentQuery(clonedSearchQuery);
		searchPageData.setCategoryCode(clonedSearchQuery.getCategoryCode());
		searchPageData.setPagination(new PaginationData());

		return searchPageData;
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

	protected String findIndexTypeId() throws SnException
	{
		return getProductTypeSelectionStrategy().getProductIndexTypeId()
				.orElseThrow(() -> new SnException("Product index type has not been found!"));
	}

	protected SnSearchQuery convertSearchQuery(final String indexTypeId, final SolrSearchQueryData searchQuery,
			final PageableData pageable)
	{
		final SnSearchQueryConverterData source = new SnSearchQueryConverterData();
		source.setIndexTypeId(indexTypeId);
		source.setSearchQuery(searchQuery);
		source.setPageable(pageable);

		return getSearchQueryConverter().convert(source);
	}

	protected ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> convertSearchResult(
			final SnSearchResponse snSearchResponse, final SolrSearchQueryData searchQuery)
	{
		final SnSearchResultConverterData<SnSearchResult> source = new SnSearchResultConverterData<>();
		source.setIndexConfiguration(snSearchResponse.getIndexConfiguration());
		source.setIndexType(snSearchResponse.getIndexType());
		source.setSnSearchResult(snSearchResponse.getSearchResult());
		source.setSearchQuery(searchQuery);

		return getSearchResultConverter().convert(source);
	}

	protected SolrSearchQueryData createSearchQuery()
	{
		return new SolrSearchQueryData();
	}

	public SnSearchService getSnSearchService()
	{
		return snSearchService;
	}

	@Required
	public void setSnSearchService(final SnSearchService snSearchService)
	{
		this.snSearchService = snSearchService;
	}

	public SnProductIndexTypeSelectionStrategy getProductTypeSelectionStrategy()
	{
		return snProductIndexTypeSelectionStrategy;
	}

	@Required
	public void setProductTypeSelectionStrategy(final SnProductIndexTypeSelectionStrategy snProductIndexTypeSelectionStrategy)
	{
		this.snProductIndexTypeSelectionStrategy = snProductIndexTypeSelectionStrategy;
	}

	public SnSuggestService getSnSuggestService()
	{
		return snSuggestService;
	}

	@Required
	public void setSnSuggestService(final SnSuggestService snSuggestService)
	{
		this.snSuggestService = snSuggestService;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	public Converter<SnSearchQueryConverterData, SnSearchQuery> getSearchQueryConverter()
	{
		return searchQueryConverter;
	}

	@Required
	public void setSearchQueryConverter(final Converter<SnSearchQueryConverterData, SnSearchQuery> searchQueryConverter)
	{
		this.searchQueryConverter = searchQueryConverter;
	}

	public Converter<SnSearchResultConverterData<SnSearchResult>, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>> getSearchResultConverter()
	{
		return searchResultConverter;
	}

	@Required
	public void setSearchResultConverter(
			final Converter<SnSearchResultConverterData<SnSearchResult>, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>> searchResultConverter)
	{
		this.searchResultConverter = searchResultConverter;
	}
}
