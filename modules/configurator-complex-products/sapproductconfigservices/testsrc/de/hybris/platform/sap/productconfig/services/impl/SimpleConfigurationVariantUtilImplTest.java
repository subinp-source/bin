/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.services.model.MockVariantProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@SuppressWarnings("javadoc")
@UnitTest
public class SimpleConfigurationVariantUtilImplTest
{
	private static final String BASE_PRODUCT_CODE = "baseProductCode";
	private final SimpleConfigurationVariantUtilImpl classUnderTest = new SimpleConfigurationVariantUtilImpl();
	@Mock
	private ProductModel baseProductModel;

	@Mock
	private VariantProductModel variantProductModel;

	@Mock
	private MockVariantProductModel mockVariantProductModel;


	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		given(variantProductModel.getBaseProduct()).willReturn(baseProductModel);

		given(baseProductModel.getCode()).willReturn(BASE_PRODUCT_CODE);
	}

	@Test
	public void testIsCPQBaseProduct()
	{
		assertFalse(classUnderTest.isCPQBaseProduct(baseProductModel));
	}

	@Test
	public void isCPQVariantProduct()
	{
		assertFalse(classUnderTest.isCPQVariantProduct(variantProductModel));
	}

	@Test
	public void getBaseProductCode()
	{
		assertEquals(BASE_PRODUCT_CODE, classUnderTest.getBaseProductCode(variantProductModel));
	}

	@Test
	public void testIsCPQChangeableVariantProductFalseBaseProduct()
	{
		assertFalse(classUnderTest.isCPQChangeableVariantProduct(baseProductModel));
	}

	@Test
	public void testIsCPQNotChangeableVariantProductFalseBaseProduct()
	{
		assertFalse(classUnderTest.isCPQNotChangeableVariantProduct(baseProductModel));
	}

	@Test
	public void testIsCPQNotChangeableVariantProductTrueMockVariantProduct()
	{
		assertTrue(classUnderTest.isCPQNotChangeableVariantProduct(mockVariantProductModel));
	}
}
