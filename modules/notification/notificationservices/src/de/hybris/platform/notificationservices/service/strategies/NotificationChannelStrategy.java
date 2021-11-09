/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.notificationservices.service.strategies;

import de.hybris.platform.core.model.user.CustomerModel;

public interface NotificationChannelStrategy
{
	/**
	 * get the channel value for the customer
	 *
	 * @param customer
	 *           the customer
	 * @return return the notification channel value (for example, return email address for EMAIl channel)
	 */
	String getChannelValue(CustomerModel customer);

}
