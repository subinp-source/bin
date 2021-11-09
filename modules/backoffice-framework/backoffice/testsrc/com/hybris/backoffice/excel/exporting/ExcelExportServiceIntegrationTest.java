/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.exporting;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.io.IOException;
import java.util.Collections;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;

import com.hybris.backoffice.excel.ExcelIntegrationTest;
import com.hybris.backoffice.excel.data.SelectedAttribute;


public class ExcelExportServiceIntegrationTest extends ExcelIntegrationTest
{

	@Rule
	public JUnitSoftAssertions soft = new JUnitSoftAssertions();

	@Resource
	DefaultExcelExportService excelExportService;

	@Test
	public void shouldExportSelectedItems() throws IOException
	{
		// given
		final CatalogVersionModel catalogVersion = createCatalogVersionModel("catalog", "1.0");
		final ProductModel product = prepareProduct("product", catalogVersion);
		product.setEan("123");
		saveItem(product);
		final SelectedAttribute selectedAttribute = new SelectedAttribute();
		selectedAttribute.setAttributeDescriptor(getAttributeDescriptorOf(product, "ean"));

		// when
		try (final Workbook workbook = excelExportService.exportData(Collections.singletonList(product),
				Collections.singletonList(selectedAttribute)))
		{
			// then
			assertThat(workbook.getSheet(PRODUCT_SHEET_NAME)).isNotNull();
			soft.assertThat(getCellAt(workbook, PRODUCT_SHEET_NAME, 0, 3)).isEqualTo("1.0:catalog");
			soft.assertThat(getCellAt(workbook, PRODUCT_SHEET_NAME, 1, 3)).isEqualTo("product");
			soft.assertThat(getCellAt(workbook, PRODUCT_SHEET_NAME, 2, 3)).isEqualTo("123");
			assertThat(workbook.getSheet(TYPE_SYSTEM_SHEET_NAME)).isNotNull();
		}
	}

	@Test
	public void shouldExportTemplate() throws IOException
	{
		// given
		final VariantTypeModel variantTypeModel = saveItem(prepareVariant());
		final ProductModel product = saveItem(
				prepareProductWithVariant(prepareProduct("product", createCatalogVersionModel("catalog", "1.0")), variantTypeModel));
		saveItem(prepareVariantProductModel(product, variantTypeModel));

		// when
		try (final Workbook workbook = excelExportService.exportTemplate(ProductModel._TYPECODE))
		{
			// then
			assertThat(workbook.getSheet(PRODUCT_SHEET_NAME)).isNotNull();
			soft.assertThat(getCellAt(workbook, PRODUCT_SHEET_NAME, 0, 0)).isEqualTo("catalogVersion*^");
			soft.assertThat(getCellAt(workbook, PRODUCT_SHEET_NAME, 1, 0)).isEqualTo("code*^");
		}
	}

	protected String getCellAt(final Workbook workbook, final String sheetName, final int column, final int row)
	{
		return workbook.getSheet(sheetName).getRow(row).getCell(column).getStringCellValue();
	}

}
