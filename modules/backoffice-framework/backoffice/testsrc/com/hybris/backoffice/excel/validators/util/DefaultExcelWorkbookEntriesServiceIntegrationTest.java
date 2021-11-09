/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.validators.util;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.hybris.backoffice.excel.data.ExcelColumn;
import com.hybris.backoffice.excel.data.ExcelWorksheet;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.importing.parser.ImportParameterParser;


@IntegrationTest
public class DefaultExcelWorkbookEntriesServiceIntegrationTest extends ServicelayerTest
{

	@Resource
	private TypeService typeService;

	@Resource(name = "defaultImportParameterParser")
	private ImportParameterParser importParameterParser;

	@Resource
	private ExcelWorkbookEntriesService excelWorkbookEntriesService;

	@Test
	public void shouldGenerateEntryKeysForCatalog()
	{
		// given
		final ExcelWorksheet worksheet = new ExcelWorksheet(CatalogModel._TYPECODE);
		worksheet.add(0, prepareExcelColumn(CatalogModel._TYPECODE, CatalogModel.ID, 0),
				prepareImportParameters(StringUtils.EMPTY, "Clothing"));
		worksheet.add(0, prepareExcelColumn(CatalogModel._TYPECODE, CatalogModel.NAME, 1),
				prepareImportParameters(StringUtils.EMPTY, "ClothingName"));
		worksheet.add(1, prepareExcelColumn(CatalogModel._TYPECODE, CatalogModel.ID, 0),
				prepareImportParameters(StringUtils.EMPTY, "Default"));
		worksheet.add(1, prepareExcelColumn(CatalogModel._TYPECODE, CatalogModel.NAME, 1),
				prepareImportParameters(StringUtils.EMPTY, "DefaultName"));

		// when
		final List<WorksheetEntryKey> entriesKeys = (List<WorksheetEntryKey>) excelWorkbookEntriesService.generateEntryKeys(worksheet);

		// then
		assertThat(entriesKeys).hasSize(2);
		assertThat(entriesKeys.get(0).getUniqueAttributesValues()).containsKey("Catalog.id").containsValue("Clothing");
		assertThat(entriesKeys.get(1).getUniqueAttributesValues()).containsKey("Catalog.id").containsValue("Default");
	}

	@Test
	public void shouldGenerateEntryKeysForCatalogVersion()
	{
		// given
		final ExcelWorksheet worksheet = new ExcelWorksheet(CatalogVersionModel._TYPECODE);
		worksheet.add(0, prepareExcelColumn(CatalogVersionModel._TYPECODE, CatalogVersionModel.VERSION, 0),
				prepareImportParameters(StringUtils.EMPTY, "Staged"));
		worksheet.add(0, prepareExcelColumn(CatalogVersionModel._TYPECODE, CatalogVersionModel.CATALOG, 1),
				prepareImportParameters("Catalog.id", "Clothing"));
		worksheet.add(1, prepareExcelColumn(CatalogVersionModel._TYPECODE, CatalogVersionModel.VERSION, 0),
				prepareImportParameters(StringUtils.EMPTY, "Online"));
		worksheet.add(1, prepareExcelColumn(CatalogVersionModel._TYPECODE, CatalogVersionModel.CATALOG, 1),
				prepareImportParameters("Catalog.id", "Default"));


		// when
		final List<WorksheetEntryKey> entriesKeys = (List<WorksheetEntryKey>) excelWorkbookEntriesService.generateEntryKeys(worksheet);

		// then
		assertThat(entriesKeys).hasSize(2);
		assertThat(entriesKeys.get(0).getUniqueAttributesValues()).containsKeys("Catalog.id", "CatalogVersion.version")
				.containsValues("Clothing", "Staged");
		assertThat(entriesKeys.get(1).getUniqueAttributesValues()).containsKeys("Catalog.id", "CatalogVersion.version")
				.containsValues("Default", "Online");
	}

	@Test
	public void shouldGenerateEntryKeysForCategory()
	{
		// given
		final ExcelWorksheet worksheet = new ExcelWorksheet(CatalogVersionModel._TYPECODE);
		worksheet.add(0, prepareExcelColumn(CategoryModel._TYPECODE, Category.CODE, 0),
				prepareImportParameters(StringUtils.EMPTY, "Cat1"));
		worksheet.add(0, prepareExcelColumn(CategoryModel._TYPECODE, Category.CATALOGVERSION, 1),
				prepareImportParameters("CatalogVersion.version:Catalog.id", "Staged:Default"));
		worksheet.add(0, prepareExcelColumn(CategoryModel._TYPECODE, Category.NAME, 2),
				prepareImportParameters(StringUtils.EMPTY, "Category1Name"));
		worksheet.add(1, prepareExcelColumn(CategoryModel._TYPECODE, Category.CODE, 0),
				prepareImportParameters(StringUtils.EMPTY, "Cat2"));
		worksheet.add(1, prepareExcelColumn(CategoryModel._TYPECODE, Category.CATALOGVERSION, 1),
				prepareImportParameters("CatalogVersion.version:Catalog.id", "Online:Default"));
		worksheet.add(1, prepareExcelColumn(CategoryModel._TYPECODE, Category.NAME, 2),
				prepareImportParameters(StringUtils.EMPTY, "Category2Name"));

		// when
		final List<WorksheetEntryKey> entriesKeys = (List<WorksheetEntryKey>) excelWorkbookEntriesService.generateEntryKeys(worksheet);

		// then
		assertThat(entriesKeys).hasSize(2);
		assertThat(entriesKeys.get(0).getUniqueAttributesValues())
				.containsKeys("Catalog.id", "CatalogVersion.version", "Category.code").doesNotContainKeys("Category.name")
				.containsValues("Default", "Staged", "Cat1").doesNotContainValue("Category1Name");
		assertThat(entriesKeys.get(1).getUniqueAttributesValues())
				.containsKeys("Catalog.id", "CatalogVersion.version", "Category.code").doesNotContainKeys("Category.name")
				.containsValues("Default", "Online", "Cat2").doesNotContainValue("Category2Name");
	}

	private ExcelColumn prepareExcelColumn(final String type, final String attribute, final int columnIndex)
	{
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		selectedAttribute.setAttributeDescriptor(typeService.getAttributeDescriptor(type, attribute));
		return new ExcelColumn(selectedAttribute, columnIndex);
	}

	private ImportParameters prepareImportParameters(final String referenceFormat, final String cellValue)
	{
		final List<Map<String, String>> parameters = importParameterParser.parseValue(referenceFormat, StringUtils.EMPTY, cellValue)
				.getParameters();
		return new ImportParameters(StringUtils.EMPTY, StringUtils.EMPTY, cellValue, StringUtils.EMPTY, parameters);
	}
}
