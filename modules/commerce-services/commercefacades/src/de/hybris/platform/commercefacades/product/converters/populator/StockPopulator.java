/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.converters.populator;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populate the product data with stock information
 */
public class StockPopulator<SOURCE extends ProductModel, TARGET extends StockData> implements Populator<SOURCE, TARGET>
{
	private CommerceStockService commerceStockService;
	private BaseStoreService baseStoreService;
	private BaseSiteService baseSiteService;
	private CategoryService categoryService;

	@Override
	public void populate(final SOURCE productModel, final TARGET stockData) throws ConversionException
	{
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		if (!isStockSystemEnabled(baseStore))
		{
			stockData.setStockLevelStatus(StockLevelStatus.INSTOCK);
			stockData.setStockLevel(Long.valueOf(0));
		}
		else
		{
			stockData.setStockLevel(getCommerceStockService().getStockLevelForProductAndBaseStore(productModel, baseStore));
			stockData
					.setStockLevelStatus(getCommerceStockService().getStockLevelStatusForProductAndBaseStore(productModel, baseStore));
			stockData.setStockThreshold(getThresholdValue(productModel));
		}
	}

	protected boolean isStockSystemEnabled()
	{
		return getCommerceStockService().isStockSystemEnabled(getBaseStoreService().getCurrentBaseStore());
	}

	protected boolean isStockSystemEnabled(final BaseStoreModel baseStore)
	{
		return getCommerceStockService().isStockSystemEnabled(baseStore);
	}

	/**
	 * Loop in all the super categories of a product belongs to, if it is a variant product, get all super categories
	 * from its base product also, then get the lowest level value from each of the categories tree, then the minimum
	 * values from all the categories trees is the threshold will be applied on a product. If no threshold is set on any
	 * categories, then apply the default threshold value set at site level.
	 *
	 * @param productModel
	 * @return the threshold value to apply on the given product
	 *
	 */
	protected Integer getThresholdValue(final ProductModel productModel)
	{
		final Map<CategoryModel, Integer> thresholdOnCategory = new HashMap<>();
		final Set<CategoryModel> processedCategories = new HashSet<>();

		final Collection<CategoryModel> productSupercategories = new HashSet<>();
		productSupercategories.addAll(productModel.getSupercategories());

		if (productModel instanceof VariantProductModel)
		{
			final ProductModel baseProduct = ((VariantProductModel) productModel).getBaseProduct();
			final Collection<CategoryModel> baseProductSupercategories = baseProduct.getSupercategories();
			if (!CollectionUtils.isEmpty(baseProductSupercategories))
			{
				productSupercategories.addAll(baseProductSupercategories);
			}
		}

		for (final CategoryModel category : productSupercategories)
		{
			getThresholdfromOneTree(category, processedCategories, thresholdOnCategory);
		}

		if (MapUtils.isEmpty(thresholdOnCategory))
		{
			return getBaseSiteService().getCurrentBaseSite().getDefaultStockLevelThreshold();
		}
		else
		{
			for (final CategoryModel processedCategory : processedCategories)
			{
				if (thresholdOnCategory.containsKey(processedCategory))
				{
					thresholdOnCategory.remove(processedCategory);
				}
			}

			final Collection<Integer> thresholdToCompare = thresholdOnCategory.values();
			return Collections.min(thresholdToCompare);
		}
	}

	/**
	 * The recursive method get the threshold value from a category tree one level up each time, it will exit the
	 * recursion when get an value from one level of category and flag all its super categories as processed
	 *
	 * @param currentCategory
	 * @param processedCategories
	 * @param thresholdOnCategory
	 *           a map contains the threshold value and the category where it set from
	 *
	 */
	protected void getThresholdfromOneTree(final CategoryModel currentCategory, final Set<CategoryModel> processedCategories,
			final Map<CategoryModel, Integer> thresholdOnCategory)
	{

		if (currentCategory.getStockLevelThreshold() != null)
		{
			final Collection<CategoryModel> allSupercategories = getCategoryService()
					.getAllSupercategoriesForCategory(currentCategory);
			if (!CollectionUtils.isEmpty(allSupercategories))
			{
				processedCategories.addAll(allSupercategories);
			}
			thresholdOnCategory.put(currentCategory, currentCategory.getStockLevelThreshold());
			return;
		}

		final Collection<CategoryModel> supercategories = currentCategory.getSupercategories();

		if (!CollectionUtils.isEmpty(supercategories))
		{
			for (final CategoryModel category : supercategories)
			{
				getThresholdfromOneTree(category, processedCategories, thresholdOnCategory);
			}
		}
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	protected CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}


	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}
}
