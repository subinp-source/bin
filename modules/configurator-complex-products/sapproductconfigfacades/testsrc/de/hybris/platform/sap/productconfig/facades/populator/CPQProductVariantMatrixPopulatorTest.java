/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.populator;

import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.services.impl.SimpleConfigurationVariantUtilImpl;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@SuppressWarnings("javadoc")
@UnitTest
public class CPQProductVariantMatrixPopulatorTest
{
	CPQProductVariantMatrixPopulator classUnderTest = new CPQProductVariantMatrixPopulator();

	@Mock
	private ProductModel productModel;
	@Mock
	private VariantTypeModel variantTypeModel;
	@Mock
	private SimpleConfigurationVariantUtilImpl configurationVariantUtil;

	final ProductData productData = new ProductData();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(productModel.getApprovalStatus()).thenReturn(ArticleApprovalStatus.APPROVED);
		Mockito.when(productModel.getVariantType()).thenReturn(variantTypeModel);
		Mockito.when(variantTypeModel.getCode()).thenReturn(VariantProductModel._TYPECODE);

		classUnderTest.setConfigurationVariantUtil(configurationVariantUtil);
	}

	@Test
	public void testPopulateVariantAttributes()
	{
		Mockito.when(configurationVariantUtil.isCPQBaseProduct(productModel)).thenReturn(true);

		productData.setMultidimensional(Boolean.TRUE);
		classUnderTest.populate(productModel, productData);
		assertFalse(productData.getMultidimensional().booleanValue());
	}

	@Test
	public void testPopulateAttributes()
	{
		Mockito.when(configurationVariantUtil.isCPQBaseProduct(productModel)).thenReturn(false);

		productData.setMultidimensional(Boolean.TRUE);
		classUnderTest.populate(productModel, productData);
		assertFalse(productData.getMultidimensional().booleanValue());
	}
}
