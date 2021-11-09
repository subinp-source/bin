/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webhookservices.service.impl;

/**
 * Webhook retryable runtime exception to trigger the retry mechanism in {@link WebhookImmediateRetryOutboundServiceFacade}.
 */
public class WebhookRetryableException extends RuntimeException
{

	/**
	 * Constructs a new WebhookRetryableException exception with a specific message.
	 *
	 * @param message the exception message
	 */
	public WebhookRetryableException(final String message)
	{
		super(message);
	}

	/**
	 * Constructs a new WebhookRetryableException exception with a specific message and cause.
	 *
	 * @param message the exception message
	 * @param cause   the cause of the exception
	 */
	public WebhookRetryableException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
