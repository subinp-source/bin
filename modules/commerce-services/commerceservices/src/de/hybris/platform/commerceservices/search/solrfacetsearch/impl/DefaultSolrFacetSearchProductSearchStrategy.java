/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.SolrFacetSearchProductSearchStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.SolrIndexedTypeCodeResolver;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.suggester.SolrAutoSuggestService;
import de.hybris.platform.solrfacetsearch.suggester.SolrSuggestion;
import de.hybris.platform.solrfacetsearch.suggester.exceptions.SolrAutoSuggestException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.convert.converter.Converter;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of the {@link SolrFacetSearchProductSearchStrategy}
 *
 * @param <ITEM> the type of items returned as part of the search results
 */
public class DefaultSolrFacetSearchProductSearchStrategy<ITEM> implements SolrFacetSearchProductSearchStrategy<ITEM>
{
	private static final Logger LOG = Logger.getLogger(DefaultSolrFacetSearchProductSearchStrategy.class);

	private FacetSearchConfigService facetSearchConfigService;
	private CommonI18NService commonI18NService;
	private SolrAutoSuggestService solrAutoSuggestService;
	private SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver;
	private SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private SessionService sessionService;

	private Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> searchQueryPageableConverter;
	private Converter<SolrSearchRequest, SolrSearchResponse> searchRequestConverter;
	private Converter<SolrSearchResponse, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>> searchResponseConverter;

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> textSearch(final String text,
			final PageableData pageableData)
	{
		final SolrSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setFreeTextSearch(text);
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData>emptyList());

		return doSearch(searchQueryData, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> textSearch(final String text,
			final SearchQueryContext searchQueryContext, final PageableData pageableData)
	{
		final SolrSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setFreeTextSearch(text);
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData>emptyList());
		searchQueryData.setSearchQueryContext(searchQueryContext);

		return doSearch(searchQueryData, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> categorySearch(final String categoryCode,
			final PageableData pageableData)
	{
		final SolrSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setCategoryCode(categoryCode);
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData>emptyList());

		return doSearch(searchQueryData, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> categorySearch(final String categoryCode,
			final SearchQueryContext searchQueryContext, final PageableData pageableData)
	{
		final SolrSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setCategoryCode(categoryCode);
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData>emptyList());
		searchQueryData.setSearchQueryContext(searchQueryContext);

		return doSearch(searchQueryData, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> searchAgain(
			final SolrSearchQueryData searchQueryData, final PageableData pageableData)
	{
		return doSearch(searchQueryData, pageableData);
	}

	@Override
	public List<AutocompleteSuggestion> getAutocompleteSuggestions(final String input)
	{
		List<AutocompleteSuggestion> result = new ArrayList<>();
		try
		{
			final SolrFacetSearchConfigModel solrFacetSearchConfigModel = getSolrFacetSearchConfigSelectionStrategy()
					.getCurrentSolrFacetSearchConfig();

			final FacetSearchConfig facetSearchConfig = getFacetSearchConfigService()
					.getConfiguration(solrFacetSearchConfigModel.getName());
			final IndexedType indexedType = getIndexedType(facetSearchConfig);

			final SolrIndexedTypeModel indexedTypeModel = findIndexedTypeModel(solrFacetSearchConfigModel, indexedType);

			final SolrSuggestion suggestions = getSolrAutoSuggestService()
					.getAutoSuggestionsForQuery(getCommonI18NService().getCurrentLanguage(), indexedTypeModel, input);

			if (isLegacySuggesterSuggestions(suggestions))
			{
				result = findBestSuggestionsForLegacySuggester(suggestions, input);
			}
			else
			{
				result = findBestSuggestionsForNewSuggester(suggestions);
			}
		}
		catch (final SolrAutoSuggestException | FacetConfigServiceException | IndexerException | NoValidSolrConfigException e)
		{
			LOG.warn("Error retrieving autocomplete suggestions", e);
		}

		return result;
	}

	@Override
	public Set<String> getIndexTypes(final String baseSiteId, final String catalogId, final String catalogVersion)
	{
		final SolrFacetSearchConfigModel solrFacetSearchConfiguration = getSolrConfig(baseSiteId);

		if (solrFacetSearchConfiguration != null)
		{
			final boolean configCVsContainsParamCV = solrFacetSearchConfiguration.getCatalogVersions().stream().anyMatch(
					cv -> StringUtils.equals(cv.getCatalog().getId(), catalogId) && StringUtils
							.equals(cv.getVersion(), catalogVersion));

			if (configCVsContainsParamCV)
			{

				return solrFacetSearchConfiguration.getSolrIndexedTypes().stream().map(it -> it.getIdentifier())
						.collect(Collectors.toSet());

			}
		}
		return Collections.emptySet();
	}

	protected SolrFacetSearchConfigModel getSolrConfig(final String baseSiteId)
	{
		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID(baseSiteId);
		if (baseSite != null && baseSite.getSolrFacetSearchConfiguration() != null)
		{
			return baseSite.getSolrFacetSearchConfiguration();
		}

		final BaseStoreModel baseStoreModel = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public BaseStoreModel execute()
			{
				baseSiteService.setCurrentBaseSite(baseSite, true);
				return baseStoreService.getCurrentBaseStore();
			}
		});

		if (baseStoreModel != null)
		{
			return baseStoreModel.getSolrFacetSearchConfiguration();
		}

		return null;
	}

	protected IndexedType getIndexedType(final FacetSearchConfig config)
	{
		final IndexConfig indexConfig = config.getIndexConfig();

		// Strategy for working out which of the available indexed types to use
		final Collection<IndexedType> indexedTypes = indexConfig.getIndexedTypes().values();
		if (indexedTypes != null && !indexedTypes.isEmpty())
		{
			// When there are multiple - select the first
			return indexedTypes.iterator().next();
		}

		// No indexed types
		return null;
	}

	protected SolrIndexedTypeModel findIndexedTypeModel(final SolrFacetSearchConfigModel facetSearchConfigModel,
			final IndexedType indexedType) throws IndexerException
	{
		if (indexedType == null)
		{
			throw new IndexerException("indexedType is NULL!");
		}
		for (final SolrIndexedTypeModel type : facetSearchConfigModel.getSolrIndexedTypes())
		{
			if (solrIndexedTypeCodeResolver.resolveIndexedTypeCode(type).equals(indexedType.getUniqueIndexedTypeCode()))
			{
				return type;
			}
		}
		throw new IndexerException("Could not find matching model for type: " + indexedType.getCode());
	}

	protected boolean isLegacySuggesterSuggestions(final SolrSuggestion solrSuggestion)
	{
		return solrSuggestion != null && MapUtils.isNotEmpty(solrSuggestion.getSuggestions());
	}

	protected List<AutocompleteSuggestion> findBestSuggestionsForLegacySuggester(final SolrSuggestion solrSuggestion,
			final String input)
	{
		final String trimmedInput = input.trim();

		final String lastTerm;
		final String precedingTerms;

		// Only provide suggestions for the last 'word' in the input
		final int indexOfLastSpace = trimmedInput.lastIndexOf(' ');
		if (indexOfLastSpace >= 0)
		{
			lastTerm = trimmedInput.substring(indexOfLastSpace + 1);
			precedingTerms = trimmedInput.substring(0, indexOfLastSpace).trim();
		}
		else
		{
			lastTerm = trimmedInput;
			precedingTerms = null;
		}

		// Get the suggestions for the last term
		final String lowerCaseLastTerm = lastTerm.toLowerCase(Locale.ROOT);
		Collection<String> suggestions = solrSuggestion.getSuggestions().get(lowerCaseLastTerm);
		if (suggestions == null)
		{
			final Collection<String> altSuggestions = new ArrayList<>();

			final Map<String, Collection<String>> values = solrSuggestion.getSuggestions();
			for (final Map.Entry<String, Collection<String>> entry : values.entrySet())
			{
				if ((lowerCaseLastTerm.contains(entry.getKey()) || entry.getKey().contains(lowerCaseLastTerm))
						&& entry.getValue() != null)
				{
					altSuggestions.addAll(entry.getValue());
				}
			}

			suggestions = altSuggestions;
		}

		return buildSuggestions(precedingTerms, suggestions);
	}

	protected List<AutocompleteSuggestion> findBestSuggestionsForNewSuggester(final SolrSuggestion solrSuggestion)
	{
		if (CollectionUtils.isEmpty(solrSuggestion.getCollates()))
		{
			return Collections.emptyList();
		}

		return buildSuggestions(null, solrSuggestion.getCollates());
	}


	protected List<AutocompleteSuggestion> buildSuggestions(final String precedingTerms, final Collection<String> suggestions)
	{
		final List<AutocompleteSuggestion> target = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(suggestions))
		{
			for (final String suggestion : suggestions)
			{
				final AutocompleteSuggestion autocompleteSuggestion = new AutocompleteSuggestion();
				autocompleteSuggestion.setTerm(precedingTerms == null ? suggestion : (precedingTerms + " " + suggestion));

				target.add(autocompleteSuggestion);
			}
		}

		return target;
	}

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

	protected SearchQueryPageableData<SolrSearchQueryData> buildSearchQueryPageableData(final SolrSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = createSearchQueryPageableData();
		searchQueryPageableData.setSearchQueryData(searchQueryData);
		searchQueryPageableData.setPageableData(pageableData);
		return searchQueryPageableData;
	}

	// Create methods for data object - can be overridden in spring config

	protected SearchQueryPageableData<SolrSearchQueryData> createSearchQueryPageableData()
	{
		return new SearchQueryPageableData<>();
	}

	protected SolrSearchQueryData createSearchQueryData()
	{
		return new SolrSearchQueryData();
	}


	protected SolrFacetSearchConfigSelectionStrategy getSolrFacetSearchConfigSelectionStrategy()
	{
		return solrFacetSearchConfigSelectionStrategy;
	}

	@Required
	public void setSolrFacetSearchConfigSelectionStrategy(
			final SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy)
	{
		this.solrFacetSearchConfigSelectionStrategy = solrFacetSearchConfigSelectionStrategy;
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

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


	protected Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> getSearchQueryPageableConverter()
	{
		return searchQueryPageableConverter;
	}

	@Required
	public void setSearchQueryPageableConverter(
			final Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> searchQueryPageableConverter)
	{
		this.searchQueryPageableConverter = searchQueryPageableConverter;
	}

	protected Converter<SolrSearchRequest, SolrSearchResponse> getSearchRequestConverter()
	{
		return searchRequestConverter;
	}

	@Required
	public void setSearchRequestConverter(final Converter<SolrSearchRequest, SolrSearchResponse> searchRequestConverter)
	{
		this.searchRequestConverter = searchRequestConverter;
	}

	protected Converter<SolrSearchResponse, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>> getSearchResponseConverter()
	{
		return searchResponseConverter;
	}

	@Required
	public void setSearchResponseConverter(
			final Converter<SolrSearchResponse, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>> searchResponseConverter)
	{
		this.searchResponseConverter = searchResponseConverter;
	}


	protected FacetSearchConfigService getFacetSearchConfigService()
	{
		return facetSearchConfigService;
	}

	@Required
	public void setFacetSearchConfigService(final FacetSearchConfigService facetSearchConfigService)
	{
		this.facetSearchConfigService = facetSearchConfigService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected SolrAutoSuggestService getSolrAutoSuggestService()
	{
		return solrAutoSuggestService;
	}

	@Required
	public void setSolrAutoSuggestService(final SolrAutoSuggestService solrAutoSuggestService)
	{
		this.solrAutoSuggestService = solrAutoSuggestService;
	}

	protected SolrIndexedTypeCodeResolver getSolrIndexedTypeCodeResolver()
	{
		return solrIndexedTypeCodeResolver;
	}

	@Required
	public void setSolrIndexedTypeCodeResolver(final SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver)
	{
		this.solrIndexedTypeCodeResolver = solrIndexedTypeCodeResolver;
	}
}
