/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer.impl;

import de.hybris.platform.commerceservices.customer.CustomerService;
import de.hybris.platform.commerceservices.customer.dao.CustomerDao;
import de.hybris.platform.core.model.user.CustomerModel;


/**
 * Provides methods for retrieving the customer and verify customer id
 */
public class DefaultCustomerService implements CustomerService
{

	private final CustomerDao customerDao;

	private final String regexp;

	/**
	 * Service constructor
	 *
	 * @param customerDao
	 * 		DAO for retrieving customer
	 * @param regexp
	 * 		regular expression for UUID validation
	 */
	public DefaultCustomerService(final CustomerDao customerDao, final String regexp)
	{
		this.customerDao = customerDao;
		this.regexp = regexp;
	}

	@Override
	public boolean isUUID(final String id)
	{
		return id != null && id.matches(this.regexp);
	}

	@Override
	public CustomerModel getCustomerByCustomerId(final String customerId)
	{
		return getCustomerDao().findCustomerByCustomerId(customerId);
	}

	protected CustomerDao getCustomerDao()
	{
		return customerDao;
	}

}
