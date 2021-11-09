/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.product.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.product.CommerceProductService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.StockService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link CommerceProductService}
 */
public class DefaultCommerceProductService implements CommerceProductService
{
	private StockService stockService;
	private WarehouseService warehouseService;

	protected StockService getStockService()
	{
		return stockService;
	}

	@Required
	public void setStockService(final StockService stockService)
	{
		this.stockService = stockService;
	}

	protected WarehouseService getWarehouseService()
	{
		return warehouseService;
	}

	@Required
	public void setWarehouseService(final WarehouseService warehouseService)
	{
		this.warehouseService = warehouseService;
	}


	@Override
	public Collection<CategoryModel> getSuperCategoriesExceptClassificationClassesForProduct(final ProductModel productModel)
			throws IllegalArgumentException
	{
		validateParameterNotNull(productModel, "Product model cannot be null");

		final String catalogId = productModel.getCatalogVersion().getCatalog().getId();
		final Collection<CategoryModel> resultList = new ArrayList<CategoryModel>();

		for (final CategoryModel categoryModel : productModel.getSupercategories())
		{
			if (toBeConverted(categoryModel, catalogId))
			{
				resultList.add(categoryModel);
			}
		}
		return resultList;
	}


	/**
	 * @deprecated Since 5.0.
	 */
	@Override
	@Deprecated(since = "5.0", forRemoval = true)
	public Integer getStockLevelForProduct(final ProductModel productModel)
	{
		final List<WarehouseModel> defaultWarehouses = getWarehouseService().getDefWarehouse();
		if (CollectionUtils.isEmpty(defaultWarehouses))
		{
			return null;
		}

		final Collection<StockLevelModel> stockLevels = getStockService().getStockLevels(productModel, defaultWarehouses);

		int stockLevel = 0;
		for (final StockLevelModel stockLevelModel : stockLevels)
		{
			stockLevel += stockLevelModel.getAvailable();
		}

		return Integer.valueOf(stockLevel);
	}

	protected boolean toBeConverted(final CategoryModel categoryModel, final String catalogId)
	{
		return categoryModel.getCatalogVersion().getCatalog().getId().equals(catalogId)
				&& !(categoryModel instanceof ClassificationClassModel);
	}

}
