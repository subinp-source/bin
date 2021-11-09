/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.importing;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;

import com.hybris.backoffice.excel.ExcelIntegrationTest;


public class ExcelImportServiceIntegrationTest extends ExcelIntegrationTest
{
	private static final String QUERY_FOR_IMPORTED_PRODUCTS = "SELECT {product:PK} FROM {Product AS product} WHERE {product:code} like 'product'";

	@Rule
	public JUnitSoftAssertions soft = new JUnitSoftAssertions();

	@Resource
	ExcelImportService excelImportService;

	@Resource(name = "excelImpexConverter")
	ImpexConverter impexConverter;

	@Resource
	ImportService importService;

	@Resource
	FlexibleSearchService flexibleSearchService;

	@Test
	public void shouldImportExcelFile() throws IOException
	{
		// given
		// Excel sheet with the following structure:
		// Catalog version*^	Articale number*^	Article order
		// catalog:1.0			product				123
		saveItem(createCatalogVersionModel("catalog", "1.0"));
		setAttributeDescriptorNamesForProductCodeAndCatalogVersion();
		setAttributeDescriptorNameForOrder();
		try (final Workbook workbook = new XSSFWorkbook(getClass().getResourceAsStream("/test/excel/import.xlsx")))
		{
			// when
			final ImportResult importResult = //
					importService.importData( //
							createImportConfig( //
									impexConverter.convert( //
											excelImportService.convertToImpex(workbook))));

			// then
			assertThat(importResult).isNotNull();
			soft.assertThat(importResult.isFinished()).isTrue();
			soft.assertThat(importResult.isError()).isFalse();
			soft.assertThat(importResult.isSuccessful()).isTrue();
			final Collection<ProductModel> importedProducts = getResult(QUERY_FOR_IMPORTED_PRODUCTS);
			soft.assertThat(importedProducts).extracting("code").containsOnly("product");
			soft.assertThat(importedProducts).extracting("order").containsOnly(123);
		}
	}

	private void setAttributeDescriptorNameForOrder()
	{
		final AttributeDescriptorModel order = getTypeService().getAttributeDescriptor(ProductModel._TYPECODE, ProductModel.ORDER);
		order.setName("Article order");
		getModelService().saveAll(order);
	}

	private <T> Collection<T> getResult(final String query)
	{
		return flexibleSearchService.<T> search(query).getResult();
	}
}
