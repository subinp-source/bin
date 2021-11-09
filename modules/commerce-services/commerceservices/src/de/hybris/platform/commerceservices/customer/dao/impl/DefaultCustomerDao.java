/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer.dao.impl;

import de.hybris.platform.commerceservices.customer.dao.CustomerDao;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.Collections;
import java.util.List;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Dao for retrieving the customer
 */
public class DefaultCustomerDao extends DefaultGenericDao<CustomerModel> implements CustomerDao
{

	public DefaultCustomerDao()
	{
		super(CustomerModel._TYPECODE);
	}

	@Override
	public CustomerModel findCustomerByCustomerId(final String customerId)
	{
		validateParameterNotNullStandardMessage("customerId", customerId);
		final List<CustomerModel> customerList = this.find(Collections.singletonMap(CustomerModel.CUSTOMERID, customerId));
		if (customerList.size() > 1)
		{
			throw new AmbiguousIdentifierException(
					String.format("Found %d customers with the customerId value: '%s', which should be unique", customerList.size(),
							customerId));
		}
		else
		{
			return customerList.isEmpty() ? null : customerList.get(0);
		}
	}
}
