/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;

import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.core.model.product.ProductModel;


public interface ProductReferenceTargetStrategy
{
	/**
	 * Gets the target product for the given product reference.
	 * 
	 * Should return either the a proposed target product or null if this strategy cannot offer a better target than the
	 * default.
	 * 
	 * @param sourceProduct
	 *           the source product
	 * @param reference
	 *           the product reference
	 * @return the target product
	 */
	ProductModel getTarget(ProductModel sourceProduct, ProductReferenceModel reference);
}
