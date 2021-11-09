/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.converters.populators;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hybris.merchandising.model.IndexedPropertyInfo;
import com.hybris.merchandising.model.Product;
import com.hybris.merchandising.model.ProductIndexContainer;
import com.hybris.merchandising.model.ProductMetadata;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

@UnitTest
public class ProductMetadataPopulatorTest
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

	private static final String NAME_KEY = "name";
	private static final String DESCRIPTION_KEY = "description";
	private static final String SUMMARY_KEY = "summary";
	private static final String VALUE = "name_text_en";
	private static final String FIELD_NAME = "FIELD_NAME";
	private static final String VALUE_NO_ENCODING = "VALUE<>";
	private static final String VALUE_ENCODING = "VALUE%3C%3E";

	private static final String ISOCODE = "NAME";

	private ProductMetadataPopulator productMetadataPopulator;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		productMetadataPopulator = new ProductMetadataPopulator();

		final HashMap<String, IndexedPropertyInfo> map = new HashMap<>();
		map.put(FIELD_NAME, indexedPropertyInfo);
		map.put(NAME_KEY, indexedPropertyInfo);
		map.put(SUMMARY_KEY, indexedPropertyInfo);
		map.put(DESCRIPTION_KEY, indexedPropertyInfo);

		when(source.getIndexedPropertiesMapping()).thenReturn(map);
		when(indexedPropertyInfo.getTranslatedFieldNames()).thenReturn(Arrays.asList(FIELD_NAME));
		when(language.getIsocode()).thenReturn(ISOCODE);
		when(source.getInputDocument()).thenReturn(inputDocument);
		when(source.getSearchQuery()).thenReturn(searchQuery);
		when(searchQuery.getFacetSearchConfig()).thenReturn(facetSearchConfig);
		when(facetSearchConfig.getIndexConfig()).thenReturn(indexConfig);
		when(indexConfig.getLanguages()).thenReturn(Arrays.asList(language));
		when(inputDocument.getFieldValue(Mockito.any())).thenReturn(VALUE);
	}

	@Test
	public void testPopulate()
	{
		final Product target = new Product();
		productMetadataPopulator.populate(source, target);
		assertTrue("product ", target.getMetadata().containsKey(language.getIsocode()));
	}

	@Test
	public void testExtractLanguage()
	{
		assertEquals("SubstringAfterLast expecting last substring after '_'", "en",
				productMetadataPopulator.extractLanguage(VALUE));
	}

	@Test
	public void testExtractFieldValue()
	{
		assertEquals(String.format("Expected value is %s", VALUE), VALUE,
				productMetadataPopulator.extractFieldValue(source, language, FIELD_NAME));
		verify(source, times(1)).getIndexedPropertiesMapping();

		when(language.getIsocode()).thenReturn("NONE");
		assertEquals("Expecting value to be empty string", StringUtils.EMPTY,
				productMetadataPopulator.extractFieldValue(source, language, FIELD_NAME));
	}

	@Test
	public void testBuildMetaData()
	{
		final ProductMetadata productMetadata = productMetadataPopulator.buildMetaData(source, language);
		assertEquals(String.format("Expected value is %s", VALUE), VALUE, productMetadata.getName());
		assertEquals(String.format("Expected value is %s", VALUE), VALUE, productMetadata.getDescription());
		assertEquals(String.format("Expected value is %s", VALUE), VALUE, productMetadata.getSummary());
	}

	@Test
	public void testSanitisation()
	{
		when(inputDocument.getFieldValue(Mockito.any())).thenReturn(VALUE_NO_ENCODING);
		final ProductMetadata productMetadata = productMetadataPopulator.buildMetaData(source, language);
		assertEquals(VALUE_ENCODING, productMetadata.getName());
	}
}
