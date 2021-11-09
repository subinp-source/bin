/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer;

import de.hybris.platform.core.model.user.CustomerModel;

/**
 * Service interface used to lookup the contact email address for a customer
 */
public interface CustomerEmailResolutionService
{
	/**
	 * Retrieves email address of a given customer
	 *
	 * @param customerModel the customer
	 * @return the customer's email address
	 */
	String getEmailForCustomer(CustomerModel customerModel);
}
