/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.notificationservices.service.strategies.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.service.strategies.NotificationChannelStrategy;


/**
 * Email notification channel strategy to get email address
 */
public class DefaultEmailChannelStrategy implements NotificationChannelStrategy
{

	@Override
	public String getChannelValue(final CustomerModel customer)
	{

		return customer.getContactEmail();
	}

}
