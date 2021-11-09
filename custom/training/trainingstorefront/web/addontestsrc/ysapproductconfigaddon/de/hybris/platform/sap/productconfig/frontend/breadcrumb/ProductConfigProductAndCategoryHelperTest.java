/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.frontend.breadcrumb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.services.impl.CPQConfigurableChecker;
import de.hybris.platform.variants.model.VariantProductModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ProductConfigProductAndCategoryHelperTest
{

	private final ProductConfigProductAndCategoryHelper classUnderTest = new ProductConfigProductAndCategoryHelper();

	@Mock
	private CPQConfigurableChecker cpqConfigurableChecker;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest.setCpqConfigurableChecker(cpqConfigurableChecker);
	}

	@Test
	public void testGetBaseProductForVariant()
	{
		final VariantProductModel mockVariant = mock(VariantProductModel.class);
		final ProductModel mockBaseProduct = mock(ProductModel.class);
		when(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(mockVariant)).thenReturn(false);
		when(mockVariant.getBaseProduct()).thenReturn(mockBaseProduct);
		final ProductModel result = classUnderTest.getBaseProduct(mockVariant);
		assertNotEquals(mockVariant, result);
	}

	@Test
	public void testGetBaseProductForConfigurableProduct()
	{
		final ProductModel mockProduct = mock(ProductModel.class);
		when(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(mockProduct)).thenReturn(true);
		final ProductModel result = classUnderTest.getBaseProduct(mockProduct);
		assertEquals(mockProduct, result);
	}

	@Test
	public void testGetBaseProductForPartiallyConfiguredVariant()
	{
		final ProductModel mockPartiallyConfiguredVariant = mock(VariantProductModel.class);
		when(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(mockPartiallyConfiguredVariant)).thenReturn(true);
		final ProductModel result = classUnderTest.getBaseProduct(mockPartiallyConfiguredVariant);
		assertEquals(mockPartiallyConfiguredVariant, result);
	}
}
