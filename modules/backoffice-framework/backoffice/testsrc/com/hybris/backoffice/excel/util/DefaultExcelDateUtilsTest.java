/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.platform.servicelayer.i18n.I18NService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelDateUtilsTest
{
	@Mock
	private I18NService i18NService;

	@InjectMocks
	private DefaultExcelDateUtils extractDateUtils;

	@Test
	public void testTestExtractRange()
	{
		setTimeZone("UTC");
		final Pair<String, String> range = extractDateUtils.extractDateRange("123 to 200");
		assertThat(range.getLeft()).isEqualTo("123");
		assertThat(range.getRight()).isEqualTo("200");
	}

	@Test
	public void testExtractRangeWhitespaces()
	{
		setTimeZone("UTC");
		final Pair<String, String> range = extractDateUtils.extractDateRange("123    to	 200");
		assertThat(range.getLeft()).isEqualTo("123");
		assertThat(range.getRight()).isEqualTo("200");
	}

	@Test
	public void testExportDateWhenServerIsInUTC()
	{
		setTimeZone("UTC");
		final ZonedDateTime zonedDateTime = ZonedDateTime.of(2017, 10, 23, 10, 46, 0, 0, ZoneId.of("UTC"));
		final Date dateFrom = Date.from(zonedDateTime.toInstant());
		assertThat(extractDateUtils.exportDate(dateFrom)).isEqualTo("23.10.2017 10:46:00");
	}

	@Test
	public void testExportDateWhenServerInCET()
	{
		setTimeZone("CET");
		final ZonedDateTime zonedDateTime = ZonedDateTime.of(2017, 10, 23, 10, 46, 0, 0, ZoneId.of("CET"));
		final Date dateFrom = Date.from(zonedDateTime.toInstant());
		assertThat(extractDateUtils.exportDate(dateFrom)).isEqualTo("23.10.2017 08:46:00");
	}

	@Test
	public void testImportDateWhenServerIsInUTC()
	{
		setTimeZone("UTC");
		assertThat(extractDateUtils.importDate("23.10.2017 10:46:00")).isEqualTo("23.10.2017 10:46:00");
	}

	@Test
	public void testImportDateWhenServerInCET()
	{
		setTimeZone("CET");
		assertThat(extractDateUtils.importDate("23.10.2017 10:46:00")).isEqualTo("23.10.2017 12:46:00");
	}

	private void setTimeZone(final String timeZone)
	{
		given(i18NService.getCurrentTimeZone()).willReturn(TimeZone.getTimeZone(timeZone));
	}
}
