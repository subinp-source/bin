/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.interf.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationProductUtilImplTest
{
	private static final String PRODUCT_CODE = "product code";
	private List<CatalogVersionModel> currentCatalogVersions;
	@Mock
	private ProductModel productModel;
	@Mock
	private CatalogModel productCatalog;
	@Mock
	private ContentCatalogModel contentCatalog;
	@Mock
	private ClassificationSystemModel classificationCatalog;
	@Mock
	private CatalogVersionModel currentCatalogVersion;
	@Mock
	private CatalogVersionModel currentParentCatalogVersion;
	@Mock
	private CatalogVersionModel currentContentCatalogVersion;
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private ProductDao productDao;
	@InjectMocks
	private ConfigurationProductUtilImpl classUnderTest;


	@Before
	public void setup()
	{
		currentCatalogVersions = new ArrayList<>();
		currentCatalogVersions.add(currentCatalogVersion);
		currentCatalogVersions.add(currentContentCatalogVersion);
		currentCatalogVersions.add(currentParentCatalogVersion);
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(currentCatalogVersions);
		when(productDao.findProductsByCode(currentCatalogVersion, PRODUCT_CODE)).thenReturn(Arrays.asList(productModel));
		when(productDao.findProductsByCode(currentParentCatalogVersion, PRODUCT_CODE)).thenReturn(Collections.emptyList());
		when(currentCatalogVersion.getActive()).thenReturn(Boolean.TRUE);
		when(currentCatalogVersion.getCatalog()).thenReturn(productCatalog);
		when(currentParentCatalogVersion.getActive()).thenReturn(Boolean.TRUE);
		when(currentParentCatalogVersion.getCatalog()).thenReturn(productCatalog);
		when(currentContentCatalogVersion.getCatalog()).thenReturn(contentCatalog);
		when(currentContentCatalogVersion.getActive()).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testGetProductForCurrentCatalog()
	{
		final ProductModel result = classUnderTest.getProductForCurrentCatalog(PRODUCT_CODE);
		assertNotNull(result);
		assertEquals(productModel, result);
		verify(productDao).findProductsByCode(currentCatalogVersion, PRODUCT_CODE);
	}

	@Test
	public void testGetCurrentCatalogVersion()
	{
		final List<CatalogVersionModel> currentCatalogVersions = classUnderTest.getCurrentCatalogVersions();
		assertNotNull(currentCatalogVersions);
		assertEquals(2, currentCatalogVersions.size());
		final CatalogVersionModel result = currentCatalogVersions.get(0);
		assertNotNull(result);
		assertEquals(currentCatalogVersion, result);
		verify(catalogVersionService).getSessionCatalogVersions();
	}


	@Test(expected = IllegalStateException.class)
	public void testGetCurrentCatalogVersionNoneFound()
	{
		currentCatalogVersions.clear();
		classUnderTest.getCurrentCatalogVersions();
	}

	@Test
	public void testIsProductCatalogProduct()
	{
		assertTrue(classUnderTest.isProductCatalogVersionActive(currentCatalogVersion));
	}

	@Test
	public void testIsProductCatalogContent()
	{
		when(currentCatalogVersion.getCatalog()).thenReturn(contentCatalog);
		assertFalse(classUnderTest.isProductCatalogVersionActive(currentCatalogVersion));
	}

	@Test
	public void testIsProductCatalogClassification()
	{
		when(currentCatalogVersion.getCatalog()).thenReturn(classificationCatalog);
		assertFalse(classUnderTest.isProductCatalogVersionActive(currentCatalogVersion));
	}

	@Test
	public void testIsProductCatalogInactive()
	{
		when(currentCatalogVersion.getActive()).thenReturn(Boolean.FALSE);
		assertFalse(classUnderTest.isProductCatalogVersionActive(currentCatalogVersion));
	}

	@Test
	public void testGetProductForActiveVersions()
	{
		final ProductModel product = classUnderTest.getProductForActiveVersions(currentCatalogVersions, PRODUCT_CODE);
		assertEquals(productModel, product);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetProductForActiveVersionsNotFoundAtAll()
	{
		when(productDao.findProductsByCode(currentCatalogVersion, PRODUCT_CODE)).thenReturn(Collections.emptyList());
		classUnderTest.getProductForActiveVersions(currentCatalogVersions, PRODUCT_CODE);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetProductForActiveVersionsTooManyMatches()
	{
		when(productDao.findProductsByCode(currentParentCatalogVersion, PRODUCT_CODE)).thenReturn(Arrays.asList(productModel));
		classUnderTest.getProductForActiveVersions(currentCatalogVersions, PRODUCT_CODE);
	}

	@Test
	public void testGetProductDao()
	{
		assertEquals(productDao, classUnderTest.getProductDao());
	}
}
