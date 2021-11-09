/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.helper;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class ProductAndCategoryHelper
{
	private List<Class<? extends CategoryModel>> productCategoryBlacklist;

	public boolean isValidProductCategory(final CategoryModel category)
	{
		if (category == null)
		{
			return false;
		}
		for (final Class filteredCategory : productCategoryBlacklist)
		{
			if (filteredCategory.isInstance(category))
			{
				return false;
			}
		}
		return true;
	}

	public ProductModel getBaseProduct(final ProductModel product)
	{
		validateParameterNotNull(product, "Product can not be null.");
		ProductModel current = product;
		while (current instanceof VariantProductModel)
		{
			final ProductModel baseProduct = ((VariantProductModel) current).getBaseProduct();
			if (baseProduct == null)
			{
				break;
			}
			else
			{
				current = baseProduct;
			}
		}
		return current;
	}

	protected List<Class<? extends CategoryModel>> getProductCategoryBlacklist()
	{
		return productCategoryBlacklist;
	}

	@Required
	public void setProductCategoryBlacklist(final List<Class<? extends CategoryModel>> productCategoryBlacklist)
	{
		this.productCategoryBlacklist = productCategoryBlacklist;
	}

}
