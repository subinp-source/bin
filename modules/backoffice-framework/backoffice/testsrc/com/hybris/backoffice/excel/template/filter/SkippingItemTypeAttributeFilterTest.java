/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SkippingItemTypeAttributeFilterTest
{

	private SkippingItemTypeAttributeFilter filter = new SkippingItemTypeAttributeFilter();

	@Test
	public void shouldReturnFalseWhenAttributeDescriptorIsItemType()
	{
		// given
		final AttributeDescriptorModel model = mock(AttributeDescriptorModel.class);
		given(model.getQualifier()).willReturn(ItemModel.ITEMTYPE);

		// when
		final boolean result = filter.test(model);

		// then
		assertFalse(result);
	}

	@Test
	public void shouldReturnTrueWhenAttributeDescriptorIsNotItemType()
	{
		// given
		final AttributeDescriptorModel model = mock(AttributeDescriptorModel.class);
		given(model.getQualifier()).willReturn(ItemModel.COMMENTS);

		// when
		final boolean result = filter.test(model);

		// then
		assertTrue(result);
	}

}
