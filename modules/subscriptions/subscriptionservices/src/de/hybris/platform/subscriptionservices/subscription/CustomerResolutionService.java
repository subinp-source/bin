/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.subscription;

import de.hybris.platform.core.model.user.CustomerModel;

import javax.annotation.Nullable;


/**
 * Service interface to resolve information about the current customer in different contexts (e.g. accelerator
 * storefronts, CS cockpit).
 */
public interface CustomerResolutionService
{
	/**
	 * Returns the current customer.
	 * 
	 * @return {@link CustomerModel} the current customer
	 */
	@Nullable
	CustomerModel getCurrentCustomer();

	/**
	 * Returns the ISO currency code.
	 * 
	 * @return {@link String} the ISO currency code
	 */
	@Nullable
	String getCurrencyIso();
}
