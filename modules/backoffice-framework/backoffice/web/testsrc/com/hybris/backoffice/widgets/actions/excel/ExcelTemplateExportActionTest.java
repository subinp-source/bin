/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.actions.excel;

import com.hybris.backoffice.excel.export.wizard.provider.ExcelFileNameProvider;
import com.hybris.backoffice.excel.exporting.ExcelExportService;
import com.hybris.backoffice.excel.exporting.ExcelExportWorkbookPostProcessor;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.testing.AbstractActionUnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


public class ExcelTemplateExportActionTest extends AbstractActionUnitTest<ExcelTemplateExportAction>
{
	@Mock
	private ExcelExportService excelExportService;
	@Mock
	private TypeService typeService;
	@Mock
	private PermissionFacade permissionFacade;
	@Mock
	private ExcelExportWorkbookPostProcessor excelExportWorkbookPostProcessor;
	@Mock
	private ExcelFileNameProvider excelFileNameProvider;

	@InjectMocks
	@Spy
	private ExcelTemplateExportAction action;

	@Override
	public ExcelTemplateExportAction getActionInstance()
	{
		return action;
	}

	@Before
	public void setUp()
	{
		doNothing().when(action).saveFile(any(), any());
	}

	@Test
	public void shouldNotOpenWizardWhenTypeCodeIsNotAssignedFromProduct()
	{
		// given
		final String typeCode = CatalogVersionModel._TYPECODE;
		final ActionContext<String> ctx = new ActionContext<>(typeCode, null, null, null);
		given(typeService.isAssignableFrom(ProductModel._TYPECODE, typeCode)).willReturn(false);
		given(excelExportService.exportTemplate(typeCode)).willReturn(mock(Workbook.class));

		// when
		action.perform(ctx);

		// then
		verify(excelExportService).exportTemplate(typeCode);
		verify(action).saveFile(any(), any());
		verify(action, never()).sendOutput(any(), any());
	}

	@Test
	public void shouldOpenWizardwhenTypeCodeIsAssignedFromProduct()
	{
		// given
		final String typeCode = ProductModel._TYPECODE;
		final ActionContext<String> ctx = new ActionContext<>(typeCode, null, null, null);
		given(typeService.isAssignableFrom(ProductModel._TYPECODE, typeCode)).willReturn(true);

		// when
		action.perform(ctx);

		// then
		verify(action).sendOutput(any(), any());
		verify(excelExportService, never()).exportTemplate(typeCode);
		verify(action, never()).saveFile(any(), any());
	}

	@Test
	public void shouldProvideTemplateFileName()
	{
		//given
		final String typeCode = ProductModel._TYPECODE;
		given(excelFileNameProvider.provide(typeCode)).willReturn("Product_2020-02-24_1135.xlsx");

		//when
		final String fileName = action.getFilename(typeCode);

		//then
		assertThat(fileName).isEqualTo("Product_2020-02-24_1135.xlsx");
	}


}
