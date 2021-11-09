/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.cell;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.Test;


public class DataCellValueTest extends AbstractCellValueTest
{

	private DataCellValue dataCellValue = new DataCellValue();

	@Test
	@Override
	public void shouldGivenTypeCellBeHandled()
	{
		// any
		assertTrue(dataCellValue.canHandle(CellType.FORMULA));
	}

	@Test
	@Override
	public void shouldGivenTypeCellNotBeHandled()
	{
		assertThat(true).as("DataCellValue is always handled").isTrue();
	}

	@Test
	@Override
	public void shouldGivenValueBeHandledCorrectly()
	{
		// given
		final Cell cell = mock(Cell.class);
		given(cell.getCellType()).willReturn(CellType.BOOLEAN);
		given(cell.getBooleanCellValue()).willReturn(true);

		// when
		final Optional<String> returnedValue = dataCellValue.getValue(cell);

		// then
		assertTrue(returnedValue.isPresent());
		assertThat(returnedValue.get()).isEqualToIgnoringCase(String.valueOf(true));
	}
}
