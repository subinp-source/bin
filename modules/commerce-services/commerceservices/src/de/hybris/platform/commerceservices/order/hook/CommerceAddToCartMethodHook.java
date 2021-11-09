/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.hook;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

/**
 * A hook strategy to run custom code before adding product to cart
 */
public interface CommerceAddToCartMethodHook
{

	/**
	 * Execute custom logic before adding product to cart
	 *
	 * @param parameters A parameter object
	 * @throws CommerceCartModificationException
	 *
	 */
	void beforeAddToCart(CommerceCartParameter parameters) throws CommerceCartModificationException;

	/**
	 * Execute custom logic after adding product to cart
	 *
	 * @param parameters A parameter object
	 * @param result     A return value of addToCart method
	 * @throws CommerceCartModificationException
	 *
	 */
	void afterAddToCart(CommerceCartParameter parameters, CommerceCartModification result) throws CommerceCartModificationException;
}
