/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;

import de.hybris.platform.core.model.user.CustomerModel;

public interface CheckoutCustomerStrategy
{
	/**
	 * Checks if the checkout is a anonymous Checkout
	 *
	 * @return  boolean
	 */
	boolean isAnonymousCheckout();

	/**
	 * Returns {@link CustomerModel} for the current checkout.
	 *
	 * @return
	 */
	CustomerModel getCurrentUserForCheckout();
}
