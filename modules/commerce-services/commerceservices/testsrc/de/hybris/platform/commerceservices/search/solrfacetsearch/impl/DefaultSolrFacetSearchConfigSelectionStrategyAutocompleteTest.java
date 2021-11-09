/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.SolrIndexedTypeCodeResolver;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.suggester.SolrAutoSuggestService;
import de.hybris.platform.solrfacetsearch.suggester.SolrSuggestion;
import de.hybris.platform.solrfacetsearch.suggester.exceptions.SolrAutoSuggestException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@UnitTest
public class DefaultSolrFacetSearchConfigSelectionStrategyAutocompleteTest
{
	private static final String FACET_SEARCH_CONFIG_NAME = "electronics";
	private static final String INDEXED_TYPE_NAME = "product";

	private static final String QUERY = "blue cam";
	private static final String SUGGESTION1 = "blue camera";
	private static final String SUGGESTION2 = "blue cameras";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private FacetSearchConfigService facetSearchConfigService;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private SolrAutoSuggestService solrAutoSuggestService;

	@Mock
	private SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver;

	@Mock
	private SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy;

	@Mock
	private SolrFacetSearchConfigModel facetSearchConfigModel;

	@Mock
	private SolrIndexedTypeModel indexedTypeModel;

	@Mock
	private LanguageModel language;

	private DefaultSolrFacetSearchProductSearchStrategy<SearchResultValueData> solrFacetSearchProductSearchStrategy;

	@Before
	public void setUp() throws FacetConfigServiceException, SolrAutoSuggestException, NoValidSolrConfigException
	{
		MockitoAnnotations.initMocks(this);

		final IndexedType indexedType = new IndexedType();
		indexedType.setUniqueIndexedTypeCode(INDEXED_TYPE_NAME);

		final IndexConfig indexConfig = new IndexConfig();
		indexConfig.setIndexedTypes(Collections.singletonMap(INDEXED_TYPE_NAME, indexedType));

		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
		facetSearchConfig.setIndexConfig(indexConfig);

		when(facetSearchConfigModel.getName()).thenReturn(FACET_SEARCH_CONFIG_NAME);
		when(facetSearchConfigModel.getSolrIndexedTypes()).thenReturn(Collections.singletonList(indexedTypeModel));
		when(solrIndexedTypeCodeResolver.resolveIndexedTypeCode(indexedTypeModel)).thenReturn(INDEXED_TYPE_NAME);
		when(solrFacetSearchConfigSelectionStrategy.getCurrentSolrFacetSearchConfig()).thenReturn(facetSearchConfigModel);
		when(facetSearchConfigService.getConfiguration(FACET_SEARCH_CONFIG_NAME)).thenReturn(facetSearchConfig);
		when(commonI18NService.getCurrentLanguage()).thenReturn(language);

		solrFacetSearchProductSearchStrategy = new DefaultSolrFacetSearchProductSearchStrategy();
		solrFacetSearchProductSearchStrategy.setFacetSearchConfigService(facetSearchConfigService);
		solrFacetSearchProductSearchStrategy.setSolrAutoSuggestService(solrAutoSuggestService);
		solrFacetSearchProductSearchStrategy.setCommonI18NService(commonI18NService);
		solrFacetSearchProductSearchStrategy.setSolrIndexedTypeCodeResolver(solrIndexedTypeCodeResolver);
		solrFacetSearchProductSearchStrategy.setSolrFacetSearchConfigSelectionStrategy(solrFacetSearchConfigSelectionStrategy);
	}

	@Test
	public void suggestWithEmptyResult() throws SolrAutoSuggestException
	{
		final Map<String, Collection<String>> suggestions = new HashMap<>();
		final Collection<String> collations = new ArrayList<>();

		final SolrSuggestion solrSuggestion = new SolrSuggestion(suggestions, collations);

		when(solrAutoSuggestService.getAutoSuggestionsForQuery(language, indexedTypeModel, QUERY)).thenReturn(solrSuggestion);

		// when
		final List<AutocompleteSuggestion> result = solrFacetSearchProductSearchStrategy.getAutocompleteSuggestions(QUERY);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	public void suggestUsingLegacySuggester() throws SolrAutoSuggestException
	{
		final Map<String, Collection<String>> suggestions = new HashMap<>();
		suggestions.put("cam", Arrays.asList("camera", "cameras"));
		final Collection<String> collations = new ArrayList<>();

		final SolrSuggestion solrSuggestion = new SolrSuggestion(suggestions, collations);

		when(solrAutoSuggestService.getAutoSuggestionsForQuery(language, indexedTypeModel, QUERY)).thenReturn(solrSuggestion);

		// when
		final List<AutocompleteSuggestion> result = solrFacetSearchProductSearchStrategy.getAutocompleteSuggestions(QUERY);

		// then
		assertThat(result).extracting(AutocompleteSuggestion::getTerm).containsExactly(SUGGESTION1, SUGGESTION2);
	}

	@Test
	public void suggestUsingNewSuggester() throws SolrAutoSuggestException
	{
		final Map<String, Collection<String>> suggestions = new HashMap<>();
		final Collection<String> collations = Arrays.asList(SUGGESTION1, SUGGESTION2);

		final SolrSuggestion solrSuggestion = new SolrSuggestion(suggestions, collations);

		when(solrAutoSuggestService.getAutoSuggestionsForQuery(language, indexedTypeModel, QUERY)).thenReturn(solrSuggestion);

		// when
		final List<AutocompleteSuggestion> result = solrFacetSearchProductSearchStrategy.getAutocompleteSuggestions(QUERY);

		// then
		assertThat(result).extracting(AutocompleteSuggestion::getTerm).containsExactly(SUGGESTION1, SUGGESTION2);
	}
}
