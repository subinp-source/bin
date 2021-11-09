/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.populator;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;


public class ClassificationAttributeLocLangPopulatorTest
{
	ClassificationAttributeLocLangPopulator populator = new ClassificationAttributeLocLangPopulator();

	@Test
	public void shouldGetEnglishLocLang()
	{
		// given
		final ExcelClassificationAttribute attribute = mock(ExcelClassificationAttribute.class);
		given(attribute.isLocalized()).willReturn(true);
		given(attribute.getIsoCode()).willReturn("en");

		// when
		final String result = populator.apply(DefaultExcelAttributeContext.ofExcelAttribute(attribute));

		// then
		assertThat(result).isEqualTo("en");
	}

	@Test
	public void shouldGetFrenchLocLang()
	{
		// given
		final ExcelClassificationAttribute attribute = mock(ExcelClassificationAttribute.class);
		given(attribute.isLocalized()).willReturn(true);
		given(attribute.getIsoCode()).willReturn("fr");

		// when
		final String result = populator.apply(DefaultExcelAttributeContext.ofExcelAttribute(attribute));

		// then
		assertThat(result).isEqualTo("fr");
	}
}
