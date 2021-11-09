/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.populator;

import static org.fest.assertions.Assertions.assertThat;

import java.util.function.Function;

import org.junit.Test;


public class ExcelClassificationCellPopulatorTest
{

	@Test
	public void shouldBeAFunctionalInterface()
	{
		assertThat((ExcelClassificationCellPopulator) context -> null).isInstanceOf(Function.class);
	}
}
