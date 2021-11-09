/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.converters.populators;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hybris.merchandising.model.ProductFacet;
import com.hybris.merchandising.model.FacetValue;
import com.hybris.merchandising.model.IndexedPropertyInfo;
import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.model.Product;
import com.hybris.merchandising.model.ProductIndexContainer;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.search.FacetField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

@UnitTest
public class FacetPopulatorTest
{
	@Mock
	private ProductIndexContainer source;
	@Mock
	private LanguageModel language;
	@Mock
	private IndexedPropertyInfo indexedPropertyInfo;
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
	@Mock
	private IndexedProperty indexedProperty;
	@Mock
	private FacetField facetField;

	private static final String NAME_KEY = "name";
	private static final String VALUE = "TEST";
	private static final String ISOCODE = "NAME";
	private static final String KEY = "TEST_VALUE";
	private static final String CATEGORIES_KEY = "allCategories";
	private static final String STRATEGY = "ROLL_UP";

	private FacetPopulator facetPopulator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		facetPopulator = new FacetPopulator();

		final HashMap<String, IndexedPropertyInfo> map = new HashMap<>();
		map.put(KEY, indexedPropertyInfo);
		map.put(CATEGORIES_KEY, indexedPropertyInfo);

		final HashMap<String, String> mappings = new HashMap<>();
		mappings.put(KEY, KEY);
		mappings.put(CATEGORIES_KEY, CATEGORIES_KEY);

		final HashMap<String, FacetField> facetMappings = new HashMap<>();
		facetMappings.put(KEY, facetField);

		when(source.getIndexedPropertiesMapping()).thenReturn(map);
		when(source.getMerchFacetPropertiesMapping()).thenReturn(facetMappings);
		when(facetField.getField()).thenReturn(KEY);
		when(language.getIsocode()).thenReturn(ISOCODE);
		when(source.getInputDocument()).thenReturn(inputDocument);
		when(source.getSearchQuery()).thenReturn(searchQuery);
		when(searchQuery.getFacetSearchConfig()).thenReturn(facetSearchConfig);
		when(facetSearchConfig.getIndexConfig()).thenReturn(indexConfig);
		when(indexConfig.getLanguages()).thenReturn(Arrays.asList(language));
		when(inputDocument.getFieldValue(Mockito.any())).thenReturn(Arrays.asList(VALUE));
		when(source.getMerchPropertiesMapping()).thenReturn(mappings);
		when(source.getMerchProductDirectoryConfigModel()).thenReturn(model);
		when(model.getRollUpStrategy()).thenReturn(STRATEGY);
		when(model.getBaseImageUrl()).thenReturn("TEST.URL");
		when(indexedPropertyInfo.getTranslatedFieldName()).thenReturn(NAME_KEY);
		when(indexedPropertyInfo.isLocalised()).thenReturn(Boolean.FALSE);
		when(indexedPropertyInfo.getIndexedProperty()).thenReturn(indexedProperty);
		when(indexedProperty.getDisplayName()).thenReturn(NAME_KEY);
	}

	@Test
	public void testPopulate()
	{
		final Product target = new Product();

		facetPopulator.populate(source, target);
		final List<ProductFacet> facetFields = target.getFacets();
		assertEquals("Expected size should be one", 1, facetFields.size());
		assertEquals(String.format("Expected value is %s", NAME_KEY), NAME_KEY, facetFields.get(0).getName());
	}

	@Test
	public void testCreateFacetValueMapping()
	{
		final Object displayNameProvider = new Object();
		final FacetValue facetValueString = facetPopulator.createFacetValueMapping(VALUE, searchQuery, indexedProperty,
				displayNameProvider);
		assertEquals("Expected value is:", VALUE, facetValueString.getId());

		final Double expectedFieldValue = Double.valueOf(2.0);
		final FacetValue facetValueDouble = facetPopulator.createFacetValueMapping(expectedFieldValue, searchQuery, indexedProperty, displayNameProvider);
		assertEquals("Expected value is:", expectedFieldValue, facetValueDouble.getId());
	}

	@Test
	public void testCreateMerchFacetValues()
	{
		List<FacetValue> facetValues = facetPopulator.createMerchFacetValues(facetField, searchQuery, indexedProperty,
				inputDocument, indexedPropertyInfo);
		assertEquals("Expected size should be one", 1, facetValues.size());
		assertEquals("Expected value is:", VALUE, facetValues.get(0).getId());

		when(inputDocument.getFieldValue(Mockito.anyString())).thenReturn(null);
		facetValues = facetPopulator.createMerchFacetValues(facetField, searchQuery, indexedProperty, inputDocument,
				indexedPropertyInfo);
		assertEquals("Expected size should be zero", 0, facetValues.size());

		final Double expectedFieldValue = Double.valueOf(2.0);
		when(inputDocument.getFieldValue(Mockito.anyString())).thenReturn(expectedFieldValue);
		facetValues = facetPopulator.createMerchFacetValues(facetField, searchQuery, indexedProperty, inputDocument,
				indexedPropertyInfo);
		assertEquals("Expected value should be set", expectedFieldValue, facetValues.get(0).getId());
	}
}
