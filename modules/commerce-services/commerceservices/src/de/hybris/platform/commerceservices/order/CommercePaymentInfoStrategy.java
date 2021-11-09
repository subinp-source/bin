/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;

/**
 * Payment info strategy
 */
public interface CommercePaymentInfoStrategy
{
	/**
	 * Stores payment info for the passed in cart.
	 *
	 * @param parameter A parameter object for cart and payment info
	 * @return true on success.
	 */
	boolean storePaymentInfoForCart(CommerceCheckoutParameter parameter);
}
