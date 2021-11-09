/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.notificationservices.service.strategies.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.service.strategies.NotificationChannelStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.lang3.StringUtils;


/**
 * Sms notification channel strategy to get phone number
 * 
 * @deprecated since 19.05 .
 */
@Deprecated(since = "1905", forRemoval= true )
public class DefaultSmsChannelStrategy implements NotificationChannelStrategy
{	
	private ConfigurationService configurationService;

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@Override
	public String getChannelValue(final CustomerModel customer)
	{

		return StringUtils.EMPTY;
	}

}
