/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commerceservices.util.ConverterFactory;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;



@UnitTest
public class ProductReferencePopulatorTest
{
	private static final String DESCRIPTION = "descText";
	private static final Integer QUANTITY = Integer.valueOf(10);

	private final AbstractPopulatingConverter<ProductReferenceModel, ProductReferenceData> productReferenceConverter = new ConverterFactory<ProductReferenceModel, ProductReferenceData, ProductReferencePopulator>()
			.create(ProductReferenceData.class, new ProductReferencePopulator());

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testConvert()
	{
		final ProductReferenceModel source = mock(ProductReferenceModel.class);

		given(source.getQuantity()).willReturn(QUANTITY);
		given(source.getDescription()).willReturn(DESCRIPTION);
		given(source.getReferenceType()).willReturn(ProductReferenceTypeEnum.ACCESSORIES);

		final ProductReferenceData result = productReferenceConverter.convert(source);

		Assert.assertEquals(QUANTITY, result.getQuantity());
		Assert.assertEquals(DESCRIPTION, result.getDescription());
		Assert.assertEquals(ProductReferenceTypeEnum.ACCESSORIES, result.getReferenceType());
	}
}
