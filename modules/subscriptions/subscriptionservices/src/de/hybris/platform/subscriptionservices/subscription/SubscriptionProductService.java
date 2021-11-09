/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.subscription;

import de.hybris.platform.core.model.product.ProductModel;

/**
 * Service interface holding subscription-related operations.
 */
public interface SubscriptionProductService
{
	/**
	 * Determines whether given product is subscription-capable.
	 *
	 * @param product product to check
	 * @return true if it is a subscription product
	 */
	boolean isSubscription(ProductModel product);
}
