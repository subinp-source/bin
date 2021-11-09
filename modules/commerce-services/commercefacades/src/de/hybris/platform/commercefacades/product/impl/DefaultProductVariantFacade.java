/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.impl;

import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Iterables;


/**
 * Extension of {@link DefaultProductFacade} that works with a product's variants.
 */
public class DefaultProductVariantFacade extends DefaultProductFacade<ProductModel>
{
	/**
	 * @deprecated Since 6.2.
	 */
	@Deprecated(since = "6.2", forRemoval = true)
	@Override
	public ProductData getProductForOptions(final ProductModel productModel, final Collection<ProductOption> options)
	{
		if (CollectionUtils.isNotEmpty(options) && options.contains(ProductOption.VARIANT_FIRST_VARIANT)
				&& CollectionUtils.isNotEmpty(productModel.getVariants()))
		{
			final ProductModel firstVariant = Iterables.get(productModel.getVariants(), 0);
			return super.getProductForOptions(firstVariant, options);
		}
		else
		{
			return super.getProductForOptions(productModel, options);
		}
	}

}
