/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.configuration;


/**
 * Thrown when a configuration was not found
 */
public class SmarteditConfigurationNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 6032378484891557906L;

	public SmarteditConfigurationNotFoundException(final String message)
	{
		super(message);
	}

	public SmarteditConfigurationNotFoundException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public SmarteditConfigurationNotFoundException(final Throwable cause)
	{
		super(cause);
	}
}
