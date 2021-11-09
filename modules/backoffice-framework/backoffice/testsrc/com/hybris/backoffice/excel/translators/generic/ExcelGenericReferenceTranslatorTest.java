/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.translators.generic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.Ordered;


@RunWith(MockitoJUnitRunner.class)
public class ExcelGenericReferenceTranslatorTest
{

	@InjectMocks
	private ExcelGenericReferenceTranslator excelGenericReferenceTranslator;

	@Test
	public void shouldUseDefaultOrderWhenAnotherOrderIsNotSet()
	{
		// given

		// when
		final int order = excelGenericReferenceTranslator.getOrder();

		// then
		assertThat(order).isEqualTo(Ordered.LOWEST_PRECEDENCE - 100);
	}

	@Test
	public void shouldOverrideDefaultOrder()
	{
		// given
		excelGenericReferenceTranslator.setOrder(100);

		// when
		final int order = excelGenericReferenceTranslator.getOrder();

		// then
		assertThat(order).isEqualTo(100);
	}
}
