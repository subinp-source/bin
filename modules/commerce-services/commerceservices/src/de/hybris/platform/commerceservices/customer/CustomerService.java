/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer;

import de.hybris.platform.core.model.user.CustomerModel;


/**
 * Provides methods for retrieving customers
 */
public interface CustomerService
{
	/**
	 * Verifies if given id is in UUID format
	 *
	 * @param id
	 * 		the id
	 * @return <code>true<code/> if UUID format
	 */
	boolean isUUID(final String id);

	/**
	 * Gets the customer by customerId
	 *
	 * @param customerId
	 * 		the customerID in UUID format of the customer
	 * @return the found customer
	 */
	CustomerModel getCustomerByCustomerId(final String customerId);
}
