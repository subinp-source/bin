/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.export;

import com.hybris.backoffice.excel.export.wizard.ExcelExportWizardForm;
import com.hybris.backoffice.excel.export.wizard.provider.DefaultExcelFileNameProvider;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.time.TimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;


@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelFileNameProviderTest
{

	@Mock
	TimeService timeService;
	@InjectMocks
	@Spy
	DefaultExcelFileNameProvider provider;

	@Test
	public void shouldGenerateTemplateNameFromTypeCode()
	{
		// given
		final String typeCode = ProductModel._TYPECODE;

		final LocalDateTime localDateTime = LocalDateTime.of(2020, 2, 24, 11, 35);
		final Instant instant = localDateTime.atZone(ZoneId.of("UTC")).toInstant();
		given(timeService.getCurrentTime()).willReturn(Date.from(instant));

		// when
		final String fileName = provider.provide(typeCode);

		// then
		assertThat(fileName).isEqualTo("Product_2020-02-24_1135.xlsx");
	}


	@Test
	public void shouldGenerateTemplateNameFromForm()
	{
		// given
		final ExcelExportWizardForm form = new ExcelExportWizardForm();
		final String typeCode = ProductModel._TYPECODE;

		form.setTypeCode(typeCode);
		doReturn("Product_2020-03-24_1140.xlsx").when(provider).provide(typeCode);

		// when
		final String fileName = provider.provide(form);

		// then
		assertThat(fileName).isEqualTo("Product_2020-03-24_1140.xlsx");
	}

	@Test
	public void shouldWeekYearNotBeUsedInDateFormat()
	{
		// given
		final String typeCode = ProductModel._TYPECODE;

		final LocalDateTime localDateTime = LocalDateTime.of(2020, 1, 1, 8, 25);
		final Instant instant = localDateTime.atZone(ZoneId.of("UTC")).toInstant();
		given(timeService.getCurrentTime()).willReturn(Date.from(instant));

		// when
		final String filename = provider.provide(typeCode);

		// then
		assertThat(filename).isEqualTo("Product_2020-01-01_0825.xlsx");
	}

}
