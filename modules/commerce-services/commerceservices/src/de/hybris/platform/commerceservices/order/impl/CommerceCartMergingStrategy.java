/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;


import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.core.model.order.CartModel;

import java.util.List;


/**
 * A strategy for merging carts.
 */
public interface CommerceCartMergingStrategy
{
	/**
	 * Merge two carts and add modifications
	 * 
	 * @param fromCart
	 *           - Cart from merging is done
	 * @param toCart
	 *           - Cart to merge to
	 * @param modifications
	 *           - List of modifications
	 * @throws CommerceCartMergingException
	 */
	void mergeCarts(CartModel fromCart, CartModel toCart, List<CommerceCartModification> modifications)
			throws CommerceCartMergingException;
}
