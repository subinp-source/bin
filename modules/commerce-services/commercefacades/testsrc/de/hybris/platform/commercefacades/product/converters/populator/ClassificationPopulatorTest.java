/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commerceservices.util.ConverterFactory;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;



@UnitTest
public class ClassificationPopulatorTest
{
	private static final String CLASS_CODE = "clsCode";
	private static final String CLASS_NAME = "clsName";

	private final AbstractPopulatingConverter<ClassificationClassModel, ClassificationData> classificationConverter = new ConverterFactory<ClassificationClassModel, ClassificationData, ClassificationPopulator>()
			.create(ClassificationData.class, new ClassificationPopulator());

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testConvert()
	{
		final ClassificationClassModel source = mock(ClassificationClassModel.class);

		given(source.getCode()).willReturn(CLASS_CODE);
		given(source.getName()).willReturn(CLASS_NAME);

		final ClassificationData result = classificationConverter.convert(source);

		Assert.assertEquals(CLASS_CODE, result.getCode());
		Assert.assertEquals(CLASS_NAME, result.getName());
	}
}
