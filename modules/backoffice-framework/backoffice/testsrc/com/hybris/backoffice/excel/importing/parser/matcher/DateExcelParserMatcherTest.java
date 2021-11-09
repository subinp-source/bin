/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.importing.parser.matcher;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.util.ExcelDateUtils;


@RunWith(MockitoJUnitRunner.class)
public class DateExcelParserMatcherTest
{

	@Mock
	private ExcelDateUtils excelDateUtils;
	@InjectMocks
	private DateExcelParserMatcher matcher = new DateExcelParserMatcher();

	@Test
	public void shouldInputMatchThePattern()
	{
		// given
		final String pattern = "dd.MM.yyyy HH:mm:ss";
		given(excelDateUtils.getDateTimeFormat()).willReturn(pattern);

		// expect
		assertTrue(matcher.test(pattern));
	}
}
