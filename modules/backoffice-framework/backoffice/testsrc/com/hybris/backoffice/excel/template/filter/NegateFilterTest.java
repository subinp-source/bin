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
public class NegateFilterTest
{

	private NegateFilter negateFilter = new NegateFilter();
	private UniqueCheckingFilter uniqueCheckingFilter = new UniqueCheckingFilter();

	@Test
	public void shouldNegateResult()
	{
		// given
		final AttributeDescriptorModel model = mock(AttributeDescriptorModel.class);
		given(model.getUnique()).willReturn(true);
		negateFilter.setExcelFilter(uniqueCheckingFilter);

		// when
		final boolean uniqueResult = uniqueCheckingFilter.test(model);
		final boolean negateResult = negateFilter.test(model);

		// then
		assertFalse(negateResult);
		assertTrue(uniqueResult);
	}
}
