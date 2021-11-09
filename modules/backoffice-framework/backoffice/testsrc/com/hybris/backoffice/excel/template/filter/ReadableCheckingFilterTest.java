/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.filter;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import org.junit.Test;


public class ReadableCheckingFilterTest
{

	private final ReadableCheckingFilter filter = new ReadableCheckingFilter();

	@Test
	public void shouldFilterOutNotReadableAttributes()
	{
		// given
		final AttributeDescriptorModel attributeDescriptorModel = mock(AttributeDescriptorModel.class);
		given(attributeDescriptorModel.getReadable()).willReturn(false);

		// when
		final boolean result = filter.test(attributeDescriptorModel);

		// then
		assertThat(result).isFalse();
	}

}
