/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer.dao;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;


/**
 * Data Access Object for looking up the customer.
 */
public interface CustomerDao extends Dao
{
	/**
	 * Finds the customer by the unique customerId
	 *
	 * @param customerId
	 * 		the unique customerId in UUID format, that represents the customer
	 * @return The customer found
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 * 		If more than one customer was found
	 */
	CustomerModel findCustomerByCustomerId(String customerId);
}
