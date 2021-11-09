/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.service.strategies;

import de.hybris.platform.core.model.user.CustomerModel;


/**
 * Strategy used to get mobile phone number in notification preference
 * 
 * @deprecated since 6.7. Use {@link NotificationChannelStrategy}
 */
@Deprecated(since = "6.7", forRemoval= true )
public interface SmsNotificationChannelStrategy
{
	/**
	 * Get the mobile phone number from project.properties configuration file
	 *
	 * @param customer
	 *           get phone number from this customer
	 *
	 * @return the mobile number will be returned
	 */
	String getMobilePhoneNumber(CustomerModel customer);
}
