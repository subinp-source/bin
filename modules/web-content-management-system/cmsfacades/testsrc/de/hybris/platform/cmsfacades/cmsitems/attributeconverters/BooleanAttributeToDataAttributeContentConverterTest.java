/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.attributeconverters;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BooleanAttributeToDataAttributeContentConverterTest
{
	private DefaultAttributeToDataContentConverter converter = new DefaultAttributeToDataContentConverter();

	@Test
	public void whenConvertNullValueReturnsNull()
	{
		assertThat(converter.convert(null), nullValue());
	}

	@Test
	public void whenConvertBooleanTrueValueReturnsValidTrue()
	{
		assertThat(converter.convert(Boolean.TRUE), is(true));
	}

	@Test
	public void whenConvertBooleanFalseValueReturnsValidFalse()
	{
		assertThat(converter.convert(Boolean.FALSE), is(false));
	}
}
