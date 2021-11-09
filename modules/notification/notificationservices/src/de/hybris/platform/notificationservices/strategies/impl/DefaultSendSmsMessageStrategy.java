/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.strategies.impl;

import de.hybris.platform.notificationservices.strategies.SendSmsMessageStrategy;

import org.apache.log4j.Logger;


/**
 * Default implementation of {@link SendSmsMessageStrategy}
 */
public class DefaultSendSmsMessageStrategy implements SendSmsMessageStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultSendSmsMessageStrategy.class);

	@Override
	public void sendMessage(final String phoneNumber, final String message)
	{
		LOG.info(String.format("send message '%s' to %s via SMS", message, phoneNumber));
	}
}
