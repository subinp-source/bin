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
public class LongAttributeToDataAttributeContentConverterTest
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
		assertThat(converter.convert(12l), is(12l));
	}

	@Test
	public void whenConvertMaxValueReturnsAsIs()
	{
		assertThat(converter.convert(Long.MAX_VALUE), is(Long.MAX_VALUE));
	}

	@Test
	public void whenConvertMinValueReturnsAsIs()
	{
		assertThat(converter.convert(Long.MIN_VALUE), is(Long.MIN_VALUE));
	}

}
