/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.configuration;


/**
 * Thrown when a key is already present in the data store while creating a new configuration
 */
public class SmarteditConfigurationDuplicateKeyException extends RuntimeException
{
	private static final long serialVersionUID = -4980981544384899570L;

	public SmarteditConfigurationDuplicateKeyException(final String message)
	{
		super(message);
	}

	public SmarteditConfigurationDuplicateKeyException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public SmarteditConfigurationDuplicateKeyException(final Throwable cause)
	{
		super(cause);
	}
}
