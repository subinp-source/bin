/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.ConfigurationInfoData;
import de.hybris.platform.commercefacades.product.converters.populator.ProductClassificationPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.facades.constants.SapproductconfigfacadesConstants;
import de.hybris.platform.sap.productconfig.facades.populator.FeatureProvider;
import de.hybris.platform.sap.productconfig.facades.populator.VariantOverviewPopulatorTest;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;


@SuppressWarnings("javadoc")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class VariantConfigurationInfoProviderImplTest
{
	@InjectMocks
	private VariantConfigurationInfoProviderImpl classUnderTest;

	@Mock
	private Populator<ProductModel, ProductData> classificationPopulator;
	private final FeatureProvider featureProvider = new FeatureProvider();
	@Mock
	private ProductClassificationPopulator<ProductModel, ProductData> productPopulatorMock;

	private ProductModel productModelKmat;
	private VariantProductModel productModelVariant;
	private ProductModel productModel;
	private ProductModel baseProductModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		final VariantTypeModel variantType = new VariantTypeModel();
		variantType.setCode(VariantProductModel._TYPECODE);
		baseProductModel = new ProductModel();
		baseProductModel.setVariantType(variantType);

		productModelVariant = new VariantProductModel();
		productModelVariant.setCode("Product Variant");
		productModelVariant.setBaseProduct(baseProductModel);

		classUnderTest.setFeatureProvider(featureProvider);
		mockClassificationPopulator();
	}

	protected void mockClassificationPopulator()
	{
		final Answer answer = new Answer<Object>()
		{
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
				final ProductData data = (ProductData) args[1];
				data.setClassifications(VariantOverviewPopulatorTest.mockClassifications());
				return null;
			}
		};
		Mockito.doAnswer(answer).when(productPopulatorMock).populate(Mockito.any(ProductModel.class),
				Mockito.any(ProductData.class));
		classUnderTest.setClassificationPopulator(productPopulatorMock);
	}

	@Test
	public void testRetrieveConfigurationInfosTwoItems()
	{
		final int numberOfMaxCstics = 2;
		classUnderTest.setMaxNumberOfDisplayedCsticsInCart(numberOfMaxCstics);
		final List<ConfigurationInfoData> configInfoDataList = classUnderTest.retrieveVariantConfigurationInfo(productModelVariant);
		assertEquals("2 ConfigurationInfoData entries should be returned", 2, configInfoDataList.size());
		assertEquals("Second entry's cstic: ", VariantOverviewPopulatorTest.CSTIC_ENGINE,
				configInfoDataList.get(1).getConfigurationLabel());
		assertEquals("Second entry's cstic's value: ", VariantOverviewPopulatorTest.VALUE_HYBRID,
				configInfoDataList.get(1).getConfigurationValue());
	}

	@Test
	public void testRetrieveConfigurationInfosFourItems()
	{
		final int numberOfMaxCstics = SapproductconfigfacadesConstants.DEFAULT_MAX_NUMBER_OF_DISPLAYED_CSTICS_IN_CART;
		classUnderTest.setMaxNumberOfDisplayedCsticsInCart(numberOfMaxCstics);
		final List<ConfigurationInfoData> configInfoDataList = classUnderTest.retrieveVariantConfigurationInfo(productModelVariant);

		assertEquals("3 ConfigurationInfoData entries should be returned", 3, configInfoDataList.size());
		assertEquals("Second entry's cstic: ", VariantOverviewPopulatorTest.CSTIC_ENGINE,
				configInfoDataList.get(1).getConfigurationLabel());
		assertEquals("Second entry's cstic's value: ", VariantOverviewPopulatorTest.VALUE_HYBRID,
				configInfoDataList.get(1).getConfigurationValue());
		assertEquals("Third entry's cstic: ", VariantOverviewPopulatorTest.CSTIC_ACC,
				configInfoDataList.get(2).getConfigurationLabel());
		assertEquals("Third entry's cstic's values: ", "Advanced Radio 3000; Cup Holder; Navigation System",
				configInfoDataList.get(2).getConfigurationValue());
	}
}
