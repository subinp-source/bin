/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.populator.typesheet;

import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TYPE_SYSTEM;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TypeSystem.ATTR_DISPLAYED_NAME;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TypeSystem.ATTR_LOCALIZED;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TypeSystem.ATTR_LOC_LANG;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TypeSystem.ATTR_NAME;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TypeSystem.ATTR_OPTIONAL;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TypeSystem.ATTR_QUALIFIER;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TypeSystem.ATTR_TYPE_CODE;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TypeSystem.ATTR_TYPE_ITEMTYPE;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TypeSystem.ATTR_UNIQUE;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TypeSystem.REFERENCE_FORMAT;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TypeSystem.TYPE_CODE;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.TypeSystem.TYPE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.cell.DefaultExcelCellService;
import com.hybris.backoffice.excel.template.mapper.ExcelMapper;


@RunWith(MockitoJUnitRunner.class)
public class TypeSystemSheetPopulatorTest
{
	@Spy
	DefaultExcelCellService excelCellService;
	@Mock
	ExcelMapper<ExcelExportResult, AttributeDescriptorModel> mapper;
	@Mock
	TypeSystemRowFactory mockedTypeSystemRowFactory;
	@InjectMocks
	TypeSystemSheetPopulator populator;

	@Test
	public void shouldPopulateRowsWithAttributeDescriptorsData()
	{
		// given
		final Sheet typeSystemSheet = mock(Sheet.class);
		final Map<Integer, Cell> row = createRowMock(typeSystemSheet, 1);
		final Workbook workbook = mock(Workbook.class);
		given(workbook.getSheet(TYPE_SYSTEM)).willReturn(typeSystemSheet);

		final AttributeDescriptorModel attributeDescriptorModel = mock(AttributeDescriptorModel.class);
		given(attributeDescriptorModel.getQualifier()).willReturn("product");

		final TypeSystemRow typeSystemRow = createTypeSystemRow();
		given(mockedTypeSystemRowFactory.create(attributeDescriptorModel)).willReturn(typeSystemRow);


		// when
		populator.populate(workbook.getSheet(TYPE_SYSTEM), Collections.singletonList(attributeDescriptorModel));

		// then
		verify(excelCellService).insertAttributeValue(row.get(TYPE_CODE.getIndex()), "typeCode");
		verify(excelCellService).insertAttributeValue(row.get(TYPE_NAME.getIndex()), "typeName");
		verify(excelCellService).insertAttributeValue(row.get(ATTR_QUALIFIER.getIndex()), "attrQualifier");
		verify(excelCellService).insertAttributeValue(row.get(ATTR_NAME.getIndex()), "attrName");
		verify(excelCellService).insertAttributeValue(row.get(ATTR_TYPE_CODE.getIndex()), "attrTypeCode");
		verify(excelCellService).insertAttributeValue(row.get(ATTR_TYPE_ITEMTYPE.getIndex()), "attrTypeItemType");
		verify(excelCellService).insertAttributeValue(row.get(ATTR_LOC_LANG.getIndex()), "attrLocLang");
		verify(excelCellService).insertAttributeValue(row.get(ATTR_DISPLAYED_NAME.getIndex()), "attrDisplayName");
		verify(excelCellService).insertAttributeValue(row.get(REFERENCE_FORMAT.getIndex()), "attrReferenceFormat");
		verify(excelCellService).insertAttributeValue(row.get(ATTR_OPTIONAL.getIndex()), true);
		verify(excelCellService).insertAttributeValue(row.get(ATTR_LOCALIZED.getIndex()), true);
		verify(excelCellService).insertAttributeValue(row.get(ATTR_UNIQUE.getIndex()), true);
	}

	@Test
	public void shouldMergeAttributesByQualifierAndName()
	{
		// given
		final String code = "code";
		final String articleNumber = "Article Number";
		final String shoeNumber = "Shoe Number";
		final String catalogVersionQualifier = "catalogVersion";
		final String catalogVersionName = "Catalog Version";

		final TypeSystemRow productCodeTypeSystemRow = mock(TypeSystemRow.class);
		final TypeSystemRow shoeCodeTypeSystemRow = mock(TypeSystemRow.class);
		final TypeSystemRow catalogVersionTypeSystemRow = mock(TypeSystemRow.class);

		final AttributeDescriptorModel productCodeAttr = mockAttributeDescriptor(code, articleNumber, productCodeTypeSystemRow);
		final AttributeDescriptorModel shoeCodeAttr = mockAttributeDescriptor(code, shoeNumber, shoeCodeTypeSystemRow);
		final AttributeDescriptorModel productCatalogVersionAttr = mockAttributeDescriptor(catalogVersionQualifier,
				catalogVersionName, catalogVersionTypeSystemRow);
		final AttributeDescriptorModel shoeCatalogVersionAttr = mockAttributeDescriptor(catalogVersionQualifier, catalogVersionName,
				catalogVersionTypeSystemRow);

		final List<AttributeDescriptorModel> attributes = List.of(productCodeAttr, shoeCodeAttr, productCatalogVersionAttr,
				shoeCatalogVersionAttr);

		given(mockedTypeSystemRowFactory.merge(any(), any()))
				.willAnswer(invocationOnMock -> invocationOnMock.getArgumentAt(0, TypeSystemRow.class));

		// when
		final Map<String, TypeSystemRow> result = populator.mergeAttributesByQualifier(attributes);

		// then
		assertThat(result).hasSize(3);
		assertThat(result.get(code + ":" + articleNumber)).isEqualTo(productCodeTypeSystemRow);
		assertThat(result.get(code + ":" + shoeNumber)).isEqualTo(shoeCodeTypeSystemRow);
		assertThat(result.get(catalogVersionQualifier + ":" + catalogVersionName)).isEqualTo(catalogVersionTypeSystemRow);

	}

	private AttributeDescriptorModel mockAttributeDescriptor(final String qualifier, final String name,
			final TypeSystemRow typeSystemRow)
	{
		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		given(attributeDescriptor.getQualifier()).willReturn(qualifier);
		given(attributeDescriptor.getName()).willReturn(name);
		given(mockedTypeSystemRowFactory.create(attributeDescriptor)).willReturn(typeSystemRow);
		return attributeDescriptor;
	}

	private Map<Integer, Cell> createRowMock(final Sheet sheet, final int rowIndex)
	{
		final Row row = mock(Row.class);
		final Function<Integer, Cell> cellProducer = index -> {
			final Cell cell = mock(Cell.class);
			given(row.createCell(index)).willReturn(cell);
			return cell;
		};
		given(sheet.createRow(rowIndex)).willReturn(row);
		return IntStream.rangeClosed(TYPE_CODE.getIndex(), REFERENCE_FORMAT.getIndex()) //
				.boxed() //
				.collect(Collectors.toMap(Function.identity(), cellProducer));
	}

	private static TypeSystemRow createTypeSystemRow()
	{
		final TypeSystemRow typeSystemRow = new TypeSystemRow();
		typeSystemRow.setTypeCode("typeCode");
		typeSystemRow.setTypeName("typeName");
		typeSystemRow.setAttrQualifier("attrQualifier");
		typeSystemRow.setAttrName("attrName");
		typeSystemRow.setAttrOptional(true);
		typeSystemRow.setAttrTypeCode("attrTypeCode");
		typeSystemRow.setAttrTypeItemType("attrTypeItemType");
		typeSystemRow.setAttrLocalized(true);
		typeSystemRow.setAttrLocLang("attrLocLang");
		typeSystemRow.setAttrDisplayName("attrDisplayName");
		typeSystemRow.setAttrUnique(true);
		typeSystemRow.setAttrReferenceFormat("attrReferenceFormat");
		return typeSystemRow;
	}
}
