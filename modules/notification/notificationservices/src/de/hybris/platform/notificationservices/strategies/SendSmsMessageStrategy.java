/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.strategies;

/**
 * Strategy to send SMS message
 */
public interface SendSmsMessageStrategy
{

	/**
	 * Send message for specific phone number
	 *
	 * @param phoneNumber
	 *           the receiver phone number
	 * @param message
	 *           the message to be sent
	 */
	void sendMessage(String phoneNumber, String message);
}
