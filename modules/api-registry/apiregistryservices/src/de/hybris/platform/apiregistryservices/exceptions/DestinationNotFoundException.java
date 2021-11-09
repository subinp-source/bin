/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.exceptions;

public class DestinationNotFoundException extends ApiRegistrationException
{
	public DestinationNotFoundException(final String message)
	{
		super(message);
	}

	public DestinationNotFoundException(final String message, final Throwable t)
	{
		super(message, t);
	}
}
