/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.service.strategies.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.service.strategies.SmsNotificationChannelStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;


/**
 * Default Strategy used to get the mobile phone number from project.properties configuration file
 * 
 * @deprecated since 6.7 . use {@link DefaultSmsChannelStrategy}
 */
@Deprecated(since = "6.7", forRemoval= true )
public class DefaultSmsNotificationChannelStrategy implements SmsNotificationChannelStrategy
{
	private static final String SMS_CONFIGURED_MOBIL_NUMBER = "sms.channel.mobilePhone";
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
	public String getMobilePhoneNumber(final CustomerModel customer)
	{
		return getConfigurationService().getConfiguration().getString(SMS_CONFIGURED_MOBIL_NUMBER);
	}
}
