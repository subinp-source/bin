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
public class DefaultAttributeToDataAttributeContentConverterTest
{
	private final DefaultAttributeToDataContentConverter converter = new DefaultAttributeToDataContentConverter();

	@Test
	public void shouldThrowsInvalidArgumentException()
	{
		final Object value = converter.convert(null);
		assertThat(value, nullValue());
	}

	@Test
	public void shouldInvokeToString()
	{
		final String object  = "VALUE";
		final Object value = converter.convert(object);
		assertThat(value, is(object));
	}
}
