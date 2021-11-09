/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.actions.excel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.type.TypeService;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.testing.AbstractActionUnitTest;


public class ExcelImportActionTest extends AbstractActionUnitTest<ExcelImportAction>
{
	@InjectMocks
	private ExcelImportAction excelExportAction;

	@Mock
	private TypeService typeService;

	@Override
	public ExcelImportAction getActionInstance()
	{
		return excelExportAction;
	}

	@Test
	public void canImportProduct()
	{
		final ActionContext<String> actionContext = new ActionContext<>(ProductModel._TYPECODE, null, null, null);

		when(typeService.isAssignableFrom(ItemModel._TYPECODE, ProductModel._TYPECODE)).thenReturn(true);

		assertThat(getActionInstance().canPerform(actionContext)).isTrue();

	}

	@Test
	public void cannotImportPojo()
	{
		final ActionContext<String> actionContext = new ActionContext<>("SomePojo", null, null, null);

		assertThat(getActionInstance().canPerform(actionContext)).isFalse();
	}
}
