/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.strategies;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.model.SiteMessageModel;

import java.util.List;


/**
 * Strategy to send site message
 */
public interface SendSiteMessageStrategy
{

	/**
	 * Send message for specific customer
	 *
	 * @param customer
	 *           the recevier
	 * @param message
	 *           the message to be sent
	 */
	default void sendMessage(final CustomerModel customer, final SiteMessageModel message)
	{
		// Default empty implementation.
	}

	/**
	 * Send messages for specific customer
	 *
	 * @param customer
	 *           the receiver
	 * @param messages
	 *           the messages to be sent
	 */
	default void sendMessage(final CustomerModel customer, final List<SiteMessageModel> messages)
	{
		// Default empty implementation.
	}

	/**
	 * Send message for given customers
	 *
	 * @param customers
	 *           the receivers
	 * @param message
	 *           the message to be sent
	 */
	default void sendMessage(final List<CustomerModel> customers, final SiteMessageModel message)
	{
		// Default empty implementation.
	}
}
