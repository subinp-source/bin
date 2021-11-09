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
public class DoubleAttributeToDataAttributeContentConverterTest
{
	
	private DefaultAttributeToDataContentConverter converter = new DefaultAttributeToDataContentConverter();
	
	@Test
	public void whenConvertNullValueReturnsNull()
	{
		assertThat(converter.convert(null), nullValue());
	}


	@Test
	public void whenConvertNoDecimalValueReturnsAsIs()
	{
		assertThat(converter.convert(12d), is(12d));
	}

	@Test
	public void whenConvertDecimalValueReturnsAsIs()
	{
		assertThat(converter.convert(123.123d), is(123.123d));
	}

	@Test
	public void whenConvertMaxValueReturnsAsIs()
	{
		assertThat(converter.convert(Double.MAX_VALUE), is(Double.MAX_VALUE));
	}

	@Test
	public void whenConvertMinValueReturnsAsIs()
	{
		assertThat(converter.convert(Double.MIN_VALUE), is(Double.MIN_VALUE));
	}

}
