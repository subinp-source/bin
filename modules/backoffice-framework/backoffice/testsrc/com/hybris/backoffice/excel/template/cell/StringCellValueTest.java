/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.cell;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class StringCellValueTest extends AbstractCellValueTest
{
	private StringCellValue stringCellValue = new StringCellValue();

	@Test
	@Override
	public void shouldGivenTypeCellBeHandled()
	{
		assertTrue(stringCellValue.canHandle(CellType.STRING));
	}

	@Test
	@Override
	public void shouldGivenTypeCellNotBeHandled()
	{
		assertFalse(stringCellValue.canHandle(CellType.BOOLEAN));
	}

	@Test
	@Override
	public void shouldGivenValueBeHandledCorrectly()
	{
		// given
		final String givenValue = "val";
		final Cell cell = mock(Cell.class);
		given(cell.getStringCellValue()).willReturn(givenValue);

		// when
		final Optional<String> receivedValue = stringCellValue.getValue(cell);

		// then
		assertTrue(receivedValue.isPresent());
		assertThat(receivedValue.get()).isEqualTo(givenValue);
	}
}
