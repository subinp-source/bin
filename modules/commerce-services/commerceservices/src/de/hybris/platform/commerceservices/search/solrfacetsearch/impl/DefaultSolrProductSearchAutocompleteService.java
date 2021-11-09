/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.impl;

import de.hybris.platform.commerceservices.search.ProductSearchAutocompleteService;
import de.hybris.platform.commerceservices.search.ProductSearchStrategyFactory;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.SolrIndexedTypeCodeResolver;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.suggester.SolrAutoSuggestService;
import de.hybris.platform.solrfacetsearch.suggester.SolrSuggestion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the AutocompleteService
 */
public class DefaultSolrProductSearchAutocompleteService implements ProductSearchAutocompleteService<AutocompleteSuggestion>
{

	private FacetSearchConfigService facetSearchConfigService;
	private CommonI18NService commonI18NService;
	private SolrAutoSuggestService solrAutoSuggestService;
	private SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver;
	private SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy;
	private ProductSearchStrategyFactory<Object> productSearchStrategyFactory;

	@Override
	public List<AutocompleteSuggestion> getAutocompleteSuggestions(final String input)
	{
		return productSearchStrategyFactory.getSearchStrategy().getAutocompleteSuggestions(input);
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#findIndexedTypeModel}
	 */
	@Deprecated(since = "2011")
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

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#isLegacySuggesterSuggestions}
	 */
	@Deprecated(since = "2011")
	protected boolean isLegacySuggesterSuggestions(final SolrSuggestion solrSuggestion)
	{
		return solrSuggestion != null && MapUtils.isNotEmpty(solrSuggestion.getSuggestions());
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#findBestSuggestionsForLegacySuggester}
	 */
	@Deprecated(since = "2011")
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

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#findBestSuggestionsForNewSuggester}
	 */
	@Deprecated(since = "2011")
	protected List<AutocompleteSuggestion> findBestSuggestionsForNewSuggester(final SolrSuggestion solrSuggestion)
	{
		if (CollectionUtils.isEmpty(solrSuggestion.getCollates()))
		{
			return Collections.emptyList();
		}

		return buildSuggestions(null, solrSuggestion.getCollates());
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#buildSuggestions}
	 */
	@Deprecated(since = "2011")
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

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#getFacetSearchConfigService}
	 */
	@Deprecated(since = "2011")
	protected FacetSearchConfigService getFacetSearchConfigService()
	{
		return facetSearchConfigService;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#setFacetSearchConfigService}
	 */
	@Deprecated(since = "2011")
	@Required
	public void setFacetSearchConfigService(final FacetSearchConfigService facetSearchConfigService)
	{
		this.facetSearchConfigService = facetSearchConfigService;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#getCommonI18NService}
	 */
	@Deprecated(since = "2011")
	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#setCommonI18NService}
	 */
	@Deprecated(since = "2011")
	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#getSolrAutoSuggestService}
	 */
	@Deprecated(since = "2011")
	protected SolrAutoSuggestService getSolrAutoSuggestService()
	{
		return solrAutoSuggestService;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#setSolrAutoSuggestService}
	 */
	@Deprecated(since = "2011")
	@Required
	public void setSolrAutoSuggestService(final SolrAutoSuggestService solrAutoSuggestService)
	{
		this.solrAutoSuggestService = solrAutoSuggestService;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#getSolrIndexedTypeCodeResolver}
	 */
	@Deprecated(since = "2011")
	protected SolrIndexedTypeCodeResolver getSolrIndexedTypeCodeResolver()
	{
		return solrIndexedTypeCodeResolver;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#setSolrIndexedTypeCodeResolver}
	 */
	@Deprecated(since = "2011")
	@Required
	public void setSolrIndexedTypeCodeResolver(final SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver)
	{
		this.solrIndexedTypeCodeResolver = solrIndexedTypeCodeResolver;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#getSolrFacetSearchConfigSelectionStrategy}
	 */
	@Deprecated(since = "2011")
	protected SolrFacetSearchConfigSelectionStrategy getSolrFacetSearchConfigSelectionStrategy()
	{
		return solrFacetSearchConfigSelectionStrategy;
	}

	/**
	 * @deprecated since 2011. Use {@link DefaultSolrFacetSearchProductSearchStrategy#setSolrFacetSearchConfigSelectionStrategy}
	 */
	@Deprecated(since = "2011")
	@Required
	public void setSolrFacetSearchConfigSelectionStrategy(
			final SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy)
	{
		this.solrFacetSearchConfigSelectionStrategy = solrFacetSearchConfigSelectionStrategy;
	}

	public ProductSearchStrategyFactory<Object> getProductSearchStrategyFactory()
	{
		return productSearchStrategyFactory;
	}

	@Required
	public void setProductSearchStrategyFactory(final ProductSearchStrategyFactory<Object> productSearchStrategyFactory)
	{
		this.productSearchStrategyFactory = productSearchStrategyFactory;
	}
}
