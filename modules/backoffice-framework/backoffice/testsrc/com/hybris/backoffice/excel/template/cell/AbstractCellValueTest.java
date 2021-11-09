/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.cell;

public abstract class AbstractCellValueTest
{
	public abstract void shouldGivenTypeCellBeHandled();

	public abstract void shouldGivenTypeCellNotBeHandled();

	public abstract void shouldGivenValueBeHandledCorrectly();
}
