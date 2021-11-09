/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.url.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.helper.ProductAndCategoryHelper;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.site.BaseSiteService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * URL resolver for ProductModel instances. The pattern could be of the form:
 * /{category-path}/{product-name}/p/{product-code}
 */
public class DefaultProductModelUrlResolver extends AbstractUrlResolver<ProductModel>
{
	private final String CACHE_KEY = DefaultProductModelUrlResolver.class.getName();

	private CommerceCategoryService commerceCategoryService;
	private BaseSiteService baseSiteService;
	private String defaultPattern;
	private ProductAndCategoryHelper productAndCategoryHelper;

	protected String getDefaultPattern()
	{
		return defaultPattern;
	}

	@Required
	public void setDefaultPattern(final String defaultPattern)
	{
		this.defaultPattern = defaultPattern;
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

	protected CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}

	@Required
	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}

	protected ProductAndCategoryHelper getProductAndCategoryHelper()
	{
		return productAndCategoryHelper;
	}

	@Required
	public void setProductAndCategoryHelper(final ProductAndCategoryHelper productAndCategoryHelper)
	{
		this.productAndCategoryHelper = productAndCategoryHelper;
	}

	protected String getPattern()
	{
		return getDefaultPattern();
	}


	@Override
	protected String getKey(final ProductModel source)
	{
		return CACHE_KEY + "." + source.getPk().toString();
	}

	@Override
	protected String resolveInternal(final ProductModel source)
	{
		final ProductModel baseProduct = getProductAndCategoryHelper().getBaseProduct(source);

		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();

		String url = getPattern();

		if (currentBaseSite != null && url.contains("{baseSite-uid}"))
		{
			url = url.replace("{baseSite-uid}", urlEncode(currentBaseSite.getUid()));
		}
		if (url.contains("{category-path}"))
		{
			url = url.replace("{category-path}", buildPathString(getCategoryPath(baseProduct)));
		}

		if (url.contains("{product-name}"))
		{
			url = url.replace("{product-name}", urlSafe(baseProduct.getName()));
		}
		if (url.contains("{product-code}"))
		{
			url = url.replace("{product-code}", urlEncode(source.getCode()));
		}

		return url;
	}

	protected String buildPathString(final List<CategoryModel> path)
	{
		if (path == null || path.isEmpty())
		{
			return "c"; // Default category part of path when missing category
		}

		final StringBuilder result = new StringBuilder();

		for (int i = 0; i < path.size(); i++)
		{
			if (i != 0)
			{
				result.append('/');
			}
			result.append(urlSafe(path.get(i).getName()));
		}

		return result.toString();
	}

	protected List<CategoryModel> getCategoryPath(final ProductModel product)
	{
		final CategoryModel category = getPrimaryCategoryForProduct(product);
		if (category != null)
		{
			return getCategoryPath(category);
		}
		return Collections.emptyList();
	}

	protected CategoryModel getPrimaryCategoryForProduct(final ProductModel product)
	{
		// Get the first super-category from the product that isn't a classification category

		for (final CategoryModel category : product.getSupercategories())
		{
			if (getProductAndCategoryHelper().isValidProductCategory(category))
			{
				return category;
			}
		}
		return null;
	}


	protected List<CategoryModel> getCategoryPath(final CategoryModel category)
	{
		final Collection<List<CategoryModel>> paths = getCommerceCategoryService().getPathsForCategory(category);
		// Return first - there will always be at least 1
		return paths.iterator().next();
	}

}
