/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.model;

import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import org.springframework.beans.factory.annotation.Required;

/**
 * Dynamic attribute handler for the Customer.contactEmail attribute.
 * The Customer.contactEmail attribute is determined by {@link CustomerEmailResolutionService}.
 */
public class ContactEmailAttribute extends AbstractDynamicAttributeHandler<String, CustomerModel>
{
	private CustomerEmailResolutionService customerEmailResolutionService;

	protected CustomerEmailResolutionService getCustomerEmailResolutionService()
	{
		return customerEmailResolutionService;
	}

	@Required
	public void setCustomerEmailResolutionService(final CustomerEmailResolutionService customerEmailResolutionService)
	{
		this.customerEmailResolutionService = customerEmailResolutionService;
	}

	@Override
	public String get(final CustomerModel customerModel)
	{
		if (customerModel == null)
		{
			throw new IllegalArgumentException("customer model is required");
		}

		return getCustomerEmailResolutionService().getEmailForCustomer(customerModel);
	}
}
