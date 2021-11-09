/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.exceptions;

/**
 * Endpoint Registration Exception
 */
public class ApiRegistrationException extends Exception
{
	public ApiRegistrationException(final String message)
	{
		super(message);
	}

	public ApiRegistrationException(final String message, final Throwable t)
	{
		super(message, t);
	}
}
