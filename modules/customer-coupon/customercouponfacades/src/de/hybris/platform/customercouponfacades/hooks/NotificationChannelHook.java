/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponfacades.hooks;

import de.hybris.platform.core.model.user.CustomerModel;


/**
 * Checks customer notification preference
 * 
 * @deprecated since 6.7.
 */
@Deprecated(since = "6.7", forRemoval= true )
public interface NotificationChannelHook
{
	/**
	 * Checks the channel status for the customer
	 * 
	 * @param customer
	 *           the customer model
	 * @return true if the specific channel is on and false otherwise
	 */
	public Boolean isChannelOn(final CustomerModel customer);
}
