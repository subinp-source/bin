/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.product.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.StockService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JUnit test suite for {@link DefaultCommerceProductServiceTest}
 */
@UnitTest
public class DefaultCommerceProductServiceTest
{
	private static final String CATALOG_ID = "testCatalogId";

	private DefaultCommerceProductService defaultCommerceProductService;

	@Mock
	private StockService stockService;

	@Mock
	private WarehouseService warehouseService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		defaultCommerceProductService = new DefaultCommerceProductService();
		defaultCommerceProductService.setStockService(stockService);
		defaultCommerceProductService.setWarehouseService(warehouseService);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldGetExceptionOnGettingSuperCategoriesWhenProductIsNull()
	{
		defaultCommerceProductService.getSuperCategoriesExceptClassificationClassesForProduct(null);
	}

	@Test
	public void shouldGetExpectedSuperCategories()
	{
		final ProductModel productModel = mock(ProductModel.class);
		final CatalogVersionModel catalogVersionModel = mock(CatalogVersionModel.class);
		final CatalogModel catalogModel = mock(CatalogModel.class);
		final CatalogVersionModel catalogVersionModelForCat = mock(CatalogVersionModel.class);
		final CatalogModel catalogModelForCat = mock(CatalogModel.class);
		final CatalogVersionModel classificationSystemVersionModel = mock(ClassificationSystemVersionModel.class);
		final CatalogModel classificationSystemModel = mock(ClassificationSystemModel.class);
		final CategoryModel includedCat = mock(CategoryModel.class);
		final CategoryModel excludedCat = mock(CategoryModel.class);
		final CategoryModel classificationClass = mock(ClassificationClassModel.class);
		final List<CategoryModel> supercategories = new ArrayList<CategoryModel>();
		supercategories.add(includedCat);
		supercategories.add(excludedCat);
		supercategories.add(classificationClass);


		given(productModel.getCatalogVersion()).willReturn(catalogVersionModel);
		given(catalogVersionModel.getCatalog()).willReturn(catalogModel);
		given(classificationSystemModel.getId()).willReturn(CATALOG_ID);

		given(classificationSystemVersionModel.getCatalog()).willReturn(classificationSystemModel);
		given(catalogModel.getId()).willReturn(CATALOG_ID);

		given(excludedCat.getCatalogVersion()).willReturn(catalogVersionModelForCat);

		given(classificationClass.getCatalogVersion()).willReturn(classificationSystemVersionModel);
		given(includedCat.getCatalogVersion()).willReturn(catalogVersionModel);

		given(productModel.getCatalogVersion()).willReturn(catalogVersionModel);
		given(catalogVersionModelForCat.getCatalog()).willReturn(catalogModelForCat);
		given(catalogModelForCat.getId()).willReturn("dummyId");

		given(productModel.getSupercategories()).willReturn(supercategories);

		final Collection<CategoryModel> result = defaultCommerceProductService
				.getSuperCategoriesExceptClassificationClassesForProduct(productModel);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(includedCat, result.iterator().next());
	}

	@Test
	public void shouldGetExpectedStockLevelForProduct()
	{
		final ProductModel productModel = mock(ProductModel.class);
		final List<WarehouseModel> defaultWarehouses = mock(ArrayList.class);
		final List<StockLevelModel> stockLevels = new ArrayList<StockLevelModel>();
		final StockLevelModel stockLevelModel_1 = mock(StockLevelModel.class);
		final StockLevelModel stockLevelModel_2 = mock(StockLevelModel.class);
		final StockLevelModel stockLevelModel_3 = mock(StockLevelModel.class);

		stockLevels.add(stockLevelModel_1);
		stockLevels.add(stockLevelModel_2);
		stockLevels.add(stockLevelModel_3);

		given(warehouseService.getDefWarehouse()).willReturn(defaultWarehouses);
		given(defaultWarehouses.isEmpty()).willReturn(false);

		given(stockService.getStockLevels(productModel, defaultWarehouses)).willReturn(stockLevels);

		given(stockLevelModel_1.getAvailable()).willReturn(1);
		given(stockLevelModel_2.getAvailable()).willReturn(2);
		given(stockLevelModel_3.getAvailable()).willReturn(3);

		final int StockLevelProductResult = defaultCommerceProductService.getStockLevelForProduct(productModel);

		Assert.assertEquals(6, StockLevelProductResult);
	}

	@Test
	public void shouldGetZeroStockLevelWhenProductIsNull()
	{
		final ProductModel productModel = null;
		final List<WarehouseModel> defaultWarehouses = mock(ArrayList.class);
		final List<StockLevelModel> stockLevels = new ArrayList<StockLevelModel>();
		given(warehouseService.getDefWarehouse()).willReturn(defaultWarehouses);
		given(defaultWarehouses.isEmpty()).willReturn(false);

		given(stockService.getStockLevels(productModel, defaultWarehouses)).willReturn(stockLevels);

		final int StockLevelProductResult = defaultCommerceProductService.getStockLevelForProduct(productModel);

		Assert.assertEquals(0, StockLevelProductResult);
	}

	@Test
	public void shouldGetNullStockLevelWhenDefaultWarehouseIsNull()
	{
		final ProductModel productModel = mock(ProductModel.class);
		final List<WarehouseModel> defaultWarehouses = null;
		given(warehouseService.getDefWarehouse()).willReturn(defaultWarehouses);

		final Integer StockLevelProductResult = defaultCommerceProductService.getStockLevelForProduct(productModel);
		Assert.assertEquals(null, StockLevelProductResult);
	}
}
