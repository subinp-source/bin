/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.impl.user;

import de.hybris.platform.commerceservices.customer.CustomerService;
import de.hybris.platform.commerceservices.strategies.UserPropertyMatchingStrategy;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Optional;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Matches the customer by the customerId
 */
public class CustomerIdMatchingStrategy implements UserPropertyMatchingStrategy
{

	private final CustomerService customerService;

	public CustomerIdMatchingStrategy(final CustomerService customerService)
	{
		this.customerService = customerService;
	}

	@Override
	public <T extends UserModel> Optional<T> getUserByProperty(final String propertyValue, final Class<T> clazz)
	{
		validateParameterNotNull(propertyValue, "The property value used to identify a customer must not be null");
		validateParameterNotNull(clazz, "The class of returned user model must not be null");
		return isSupported(propertyValue, clazz) ?
				Optional.ofNullable((T) getCustomerService().getCustomerByCustomerId(propertyValue)) :
				Optional.empty();
	}

	protected boolean isSupported(final String propertyValue, final Class<?> clazz)
	{
		return getCustomerService().isUUID(propertyValue) && clazz.isAssignableFrom(CustomerModel.class);
	}

	protected CustomerService getCustomerService()
	{
		return customerService;
	}
}
