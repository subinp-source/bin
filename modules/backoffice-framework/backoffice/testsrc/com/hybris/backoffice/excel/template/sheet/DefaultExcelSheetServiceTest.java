/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.sheet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.Locale;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;


@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelSheetServiceTest
{

	@Mock
	ExcelCellService excelCellService;

	@Mock
	ExcelWorkbookService excelWorkbookService;

	@InjectMocks
	DefaultExcelSheetService excelSheetService;

	@Test
	public void shouldFindDisplayNameInTypeSystemSheet()
	{
		// given
		final Sheet typeSystemSheet = mock(Sheet.class);
		final ExcelAttributeDescriptorAttribute attribute = mockAttribute(typeSystemSheet, "code", "Article number",
				"Article number*^", "", Locale.ENGLISH);

		// when
		final String result = excelSheetService.findAttributeDisplayNameInTypeSystemSheet(typeSystemSheet, attribute);

		// then
		assertThat(result).isEqualTo("Article number*^");
	}

	@Test
	public void shouldFindDisplayNameInTypeSystemSheetForAttributeWithEmptyName()
	{
		// given
		final Sheet typeSystemSheet = mock(Sheet.class);
		final ExcelAttributeDescriptorAttribute attributeWithEmptyName = mockAttribute(typeSystemSheet, "code", "", "code*^", "",
				Locale.ENGLISH);

		// when
		final String result = excelSheetService.findAttributeDisplayNameInTypeSystemSheet(typeSystemSheet, attributeWithEmptyName);

		// then
		assertThat(result).isEqualTo("code*^");
	}

	private ExcelAttributeDescriptorAttribute mockAttribute(final Sheet typeSystemSheet, final String qualifier, final String name,
			final String displayName, final String lang, final Locale workbookLocale)
	{
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		given(attributeDescriptor.getQualifier()).willReturn(qualifier);
		given(attributeDescriptor.getName(workbookLocale)).willReturn(name);

		final ExcelAttributeDescriptorAttribute attribute = mock(ExcelAttributeDescriptorAttribute.class);
		given(attribute.getAttributeDescriptorModel()).willReturn(attributeDescriptor);

		final Row articleNumberRow = mockRow(typeSystemSheet, qualifier, name, displayName, lang);

		final Workbook workbook = mock(Workbook.class);
		given(excelWorkbookService.getProperty(workbook, DefaultExcelSheetService.ISO_CODE_KEY))
				.willReturn(Optional.of(workbookLocale.toLanguageTag()));

		given(typeSystemSheet.getLastRowNum()).willReturn(0);
		given(typeSystemSheet.getRow(0)).willReturn(articleNumberRow);
		given(typeSystemSheet.getWorkbook()).willReturn(workbook);

		return attribute;
	}

	private Row mockRow(final Sheet typeSystemSheet, final String qualifier, final String name, final String displayName,
			final String lang)
	{
		final Row row = mock(Row.class);
		final Cell qualifierCell = mockCell(qualifier);
		final Cell displayNameCell = mockCell(displayName);
		final Cell nameCell = mockCell(name);
		final Cell langCell = mockCell(lang);
		given(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_QUALIFIER.getIndex())).willReturn(qualifierCell);
		given(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_NAME.getIndex())).willReturn(nameCell);
		given(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_LOC_LANG.getIndex())).willReturn(langCell);
		given(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_DISPLAYED_NAME.getIndex())).willReturn(displayNameCell);
		given(row.getSheet()).willReturn(typeSystemSheet);
		return row;
	}

	private Cell mockCell(final String cellValue)
	{
		final Cell cell = mock(Cell.class);
		given(excelCellService.getCellValue(cell)).willReturn(cellValue);
		return cell;
	}
}
