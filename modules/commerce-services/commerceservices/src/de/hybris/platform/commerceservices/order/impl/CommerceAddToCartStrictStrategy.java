/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * A strict strategy to add an entry to the cart. It throws an exception when there is not enough products in stock
 * 
 */
public class CommerceAddToCartStrictStrategy extends DefaultCommerceAddToCartStrategy
{
	@Override
	protected void validateAddToCart(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{
		super.validateAddToCart(parameters);

		if (!isStockLevelSufficient(parameters.getCart(), parameters.getProduct(), parameters.getPointOfService(),
				parameters.getQuantity()))
		{
			throw new CommerceCartModificationException("Insufficient stock level");
		}
	}
}
