/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.impl;

import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.commerceservices.strategies.ProductReferenceTargetStrategy;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class ChainProductReferenceTargetStrategy implements ProductReferenceTargetStrategy
{
	private List<ProductReferenceTargetStrategy> productReferenceTargetStrategies;

	protected List<ProductReferenceTargetStrategy> getProductReferenceTargetStrategies()
	{
		return productReferenceTargetStrategies;
	}

	@Required
	public void setProductReferenceTargetStrategies(final List<ProductReferenceTargetStrategy> productReferenceTargetStrategies)
	{
		this.productReferenceTargetStrategies = productReferenceTargetStrategies;
	}

	@Override
	public ProductModel getTarget(final ProductModel sourceProduct, final ProductReferenceModel reference)
	{
		final List<ProductReferenceTargetStrategy> strategies = getProductReferenceTargetStrategies();
		if (strategies != null && !strategies.isEmpty())
		{
			for (final ProductReferenceTargetStrategy strategy : strategies)
			{
				final ProductModel target = strategy.getTarget(sourceProduct, reference);
				if (target != null && !target.equals(reference.getTarget()))
				{
					return target;
				}
			}
		}
		return null;
	}
}
