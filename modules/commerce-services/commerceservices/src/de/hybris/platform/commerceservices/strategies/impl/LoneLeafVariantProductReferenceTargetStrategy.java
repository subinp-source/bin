/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.impl;

import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.commerceservices.strategies.ProductReferenceTargetStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;


public class LoneLeafVariantProductReferenceTargetStrategy implements ProductReferenceTargetStrategy
{
	@Override
	public ProductModel getTarget(final ProductModel sourceProduct, final ProductReferenceModel reference)
	{
		if (CollectionUtils.isNotEmpty(reference.getTarget().getVariants()))
		{
			return findSingleLeafVariant(reference.getTarget());
		}
		return null;
	}

	protected ProductModel findSingleLeafVariant(final ProductModel currentProduct)
	{
		final Collection<VariantProductModel> childVariants = currentProduct.getVariants();

		// If the variant has no child variants, then this is the variant we are looking for
		if (childVariants == null || childVariants.isEmpty())
		{
			return currentProduct;
		}

		// If the variant only has a single child then follow that path down
		if (childVariants.size() == 1)
		{
			return findSingleLeafVariant(childVariants.iterator().next());
		}

		// The variant has multiple child variants, so this strategy fails
		return null;
	}

}
