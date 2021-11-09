/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;


import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

/**
 * Overrides {@code DefaultCommerceCartStrategy} to make bundle price rules working.
 */
public class BundleCommerceAddToCartStrategy extends DefaultCommerceAddToCartStrategy
{
	@Override
	public CommerceCartModification addToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		final CommerceCartModification modification = doAddToCart(parameter);
		afterAddToCart(parameter, modification);
		// It is required for price calculation of bundle entries.
		mergeEntry(modification, parameter);
		getCommerceCartCalculationStrategy().calculateCart(parameter);
		return modification;
	}
}