/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.converters.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hybris.merchandising.metric.rollup.strategies.MerchandisingMetricRollupStrategy;
import com.hybris.merchandising.metric.rollup.strategies.impl.NoRollupMerchandisingMetricRollupStrategy;
import com.hybris.merchandising.model.IndexedPropertyInfo;
import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.model.Product;
import com.hybris.merchandising.model.ProductIndexContainer;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

@UnitTest
public class ProductDetailsPopulatorTest
{
	@Mock
	private ProductIndexContainer source;
	@Mock
	private LanguageModel language;
	@Mock
	private InputDocument inputDocument;
	@Mock
	private SearchQuery searchQuery;
	@Mock
	private FacetSearchConfig facetSearchConfig;
	@Mock
	private IndexConfig indexConfig;
	@Mock
	private MerchProductDirectoryConfigModel model;

	private static final String KEY = "TEST";
	private static final String ISOCODE = "NAME";
	private static final String TEST_VALUE = "TEST_VALUE";
	private static final String TEST_URL_VALUE = "TestURL";
	private static final String TEST_CODE = "TestCode";
	private static final String CATEGORIES_KEY = "allCategories";
	private static final String STRATEGY = "ROLL_UP";
	private static final String URL = "url";
	private static final String CODE = "code";

	private ProductDetailsPopulator productDetailsPopulator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		productDetailsPopulator = new ProductDetailsPopulator();
		final MerchandisingMetricRollupStrategy rollupStrategy = new NoRollupMerchandisingMetricRollupStrategy();

		final List<MerchandisingMetricRollupStrategy> rollupStrategies = Arrays.asList(rollupStrategy);
		productDetailsPopulator.setRollupStrategies(rollupStrategies);

		final IndexedPropertyInfo testIndexPropertyInfo = Mockito.mock(IndexedPropertyInfo.class);
		when(testIndexPropertyInfo.getTranslatedFieldName()).thenReturn(KEY);

		final IndexedPropertyInfo categoryIndexPropertyInfo = Mockito.mock(IndexedPropertyInfo.class);
		when(categoryIndexPropertyInfo.getTranslatedFieldName()).thenReturn(CATEGORIES_KEY);

		final IndexedPropertyInfo urlIndexPropertyInfo = Mockito.mock(IndexedPropertyInfo.class);
		when(urlIndexPropertyInfo.getTranslatedFieldName()).thenReturn(URL);

		final HashMap<String, IndexedPropertyInfo> map = new HashMap<>();
		map.put(KEY, testIndexPropertyInfo);
		map.put(CATEGORIES_KEY, categoryIndexPropertyInfo);
		map.put(URL, urlIndexPropertyInfo);

		final HashMap<String, String> mappings = new HashMap<>();
		mappings.put(KEY, KEY);
		mappings.put(CATEGORIES_KEY, CATEGORIES_KEY);
		mappings.put(URL, URL);

		when(source.getIndexedPropertiesMapping()).thenReturn(map);
		when(language.getIsocode()).thenReturn(ISOCODE);
		when(source.getInputDocument()).thenReturn(inputDocument);
		when(source.getSearchQuery()).thenReturn(searchQuery);
		when(searchQuery.getFacetSearchConfig()).thenReturn(facetSearchConfig);
		when(facetSearchConfig.getIndexConfig()).thenReturn(indexConfig);
		when(indexConfig.getLanguages()).thenReturn(Arrays.asList(language));
		when(inputDocument.getFieldValue(KEY)).thenReturn(TEST_VALUE);
		when(inputDocument.getFieldValue(URL)).thenReturn(TEST_URL_VALUE);
		when(inputDocument.getFieldValue(CODE)).thenReturn(TEST_CODE);
		when(inputDocument.getFieldValue(CATEGORIES_KEY)).thenReturn(Arrays.asList(TEST_VALUE));
		when(source.getMerchPropertiesMapping()).thenReturn(mappings);
		when(source.getMerchProductDirectoryConfigModel()).thenReturn(model);
		when(model.getRollUpStrategy()).thenReturn(STRATEGY);
		when(model.getBaseImageUrl()).thenReturn("TEST.URL");
	}

	@Test
	public void testPopulate()
	{
		final Product target = new Product();

		productDetailsPopulator.populate(source, target);
		assertEquals("Expected reporting group code to be set correctly", TEST_CODE, target.getReportingGroup());
	}

	@Test
	public void testPopulateMerchProperties()
	{
		final Map<String, Object> properties = productDetailsPopulator.populateMerchProperties(source);
		assertNotNull("Expected properties to not be null", properties);
		assertEquals("Expected url to be populated", TEST_URL_VALUE, properties.get(URL));
		assertEquals("Expected test field to be popoulated", TEST_VALUE, properties.get(KEY));
		assertEquals("Expected categories field to be populated", Arrays.asList(TEST_VALUE), properties.get(CATEGORIES_KEY));
	}

	@Test
	public void testPopulateCategory()
	{
		//Test a product with > 1 category
		assertEquals("Category value did not match expected value", TEST_VALUE, productDetailsPopulator.populateCategory(source).get(0));
		//Test a product with 1 category
		when(inputDocument.getFieldValue(CATEGORIES_KEY)).thenReturn(TEST_VALUE);
		assertEquals("Category value did not match expected value", TEST_VALUE, productDetailsPopulator.populateCategory(source).get(0));
	}
}
