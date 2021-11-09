/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.exception;

/**
 * Exception thrown when there is any problem during the Localization process using Localization
 */
public class LocalizationConsumerException extends RuntimeException
{
	private static final long serialVersionUID = 5274407655926846269L;

	public LocalizationConsumerException()
	{
		super();
	}

	public LocalizationConsumerException(final String message)
	{
		super(message);
	}

	public LocalizationConsumerException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public LocalizationConsumerException(final Throwable cause)
	{
		super(cause);
	}
}
