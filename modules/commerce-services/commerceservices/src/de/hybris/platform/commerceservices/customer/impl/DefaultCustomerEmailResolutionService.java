/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.mail.MailUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link CustomerEmailResolutionService}.
 */
public class DefaultCustomerEmailResolutionService implements CustomerEmailResolutionService
{
	private static final Logger LOG = Logger.getLogger(DefaultCustomerEmailResolutionService.class);

	public static final String DEFAULT_CUSTOMER_KEY = "customer.email.default";
	public static final String DEFAULT_CUSTOMER_EMAIL = "demo@example.com";

	private ConfigurationService configurationService;

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@Override
	public String getEmailForCustomer(final CustomerModel customerModel)
	{
		final String emailAfterProcessing = validateAndProcessEmailForCustomer(customerModel);
		if (StringUtils.isNotEmpty(emailAfterProcessing))
		{
			return emailAfterProcessing;
		}

		return getConfigurationService().getConfiguration().getString(DEFAULT_CUSTOMER_KEY, DEFAULT_CUSTOMER_EMAIL);
	}

	protected String validateAndProcessEmailForCustomer(final CustomerModel customerModel)
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);

		final String email = CustomerType.GUEST.equals(customerModel.getType()) ? StringUtils.substringAfter(
				customerModel.getUid(), "|") : customerModel.getUid();
		try
		{
			MailUtils.validateEmailAddress(email, "customer email");
			return email;
		}
		catch (final EmailException e) //NOSONAR
		{
			LOG.info("Given uid is not appropriate email. Customer PK: " + String.valueOf(customerModel.getPk()) + " Exception: "
					+ e.getClass().getName());
		}
		return null;
	}

}
