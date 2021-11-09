/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefaultValueCheckingFilterTest
{

	private final DefaultValueCheckingFilter filter = new DefaultValueCheckingFilter();

	@Test
	public void shouldReturnFalseWhenAttributeHasNoDefaultValue()
	{
		// given
		final AttributeDescriptorModel attributeDescriptorModel = mock(AttributeDescriptorModel.class);
		given(attributeDescriptorModel.getDefaultValue()).willReturn(null);

		// when
		final boolean result = filter.test(attributeDescriptorModel);

		// then
		assertFalse(result);
	}

	@Test
	public void shouldReturnTrueWhenAttributeHasDefaultValue()
	{
		// given
		final AttributeDescriptorModel attributeDescriptorModel = mock(AttributeDescriptorModel.class);
		given(attributeDescriptorModel.getDefaultValue()).willReturn(new Object());

		// when
		final boolean result = filter.test(attributeDescriptorModel);

		// then
		assertTrue(result);
	}

}
