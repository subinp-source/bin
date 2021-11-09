/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.solrfacetsearch.indexer.SolrIndexedTypeCodeResolver;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultSolrIndexedTypeCodeResolverTest
{
	private SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver;
	@Mock
	private ComposedTypeModel mockType;

	@Before
	public void setUp()
	{
		solrIndexedTypeCodeResolver = new DefaultSolrIndexedTypeCodeResolver();
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullSolrIndexedType()
	{
		solrIndexedTypeCodeResolver.resolveIndexedTypeCode(null);
	}

	@Test
	public void testSolrIndexedTypeWithoutIndexedName()
	{
		Mockito.when(mockType.getCode()).thenReturn("Product");
		final SolrIndexedTypeModel solrIndexedTypeModel = new SolrIndexedTypeModel();
		solrIndexedTypeModel.setType(mockType);
		Assert.assertEquals("Product", solrIndexedTypeCodeResolver.resolveIndexedTypeCode(solrIndexedTypeModel));
	}

	@Test
	public void testSolrIndexedTypeWithIndexedName()
	{
		Mockito.when(mockType.getCode()).thenReturn("Product");
		final SolrIndexedTypeModel solrIndexedTypeModel = new SolrIndexedTypeModel();
		solrIndexedTypeModel.setType(mockType);
		solrIndexedTypeModel.setIndexName("myName");
		Assert.assertEquals("Product_myName", solrIndexedTypeCodeResolver.resolveIndexedTypeCode(solrIndexedTypeModel));
	}
}
