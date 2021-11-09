/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.configuration;


/**
 * Thrown when there is a problem while calling any of the SmarteditConfigurationFacade methods
 */
public class SmarteditConfigurationException extends RuntimeException
{
	private static final long serialVersionUID = -3178966706411273787L;

	public SmarteditConfigurationException(final String message)
	{
		super(message);
	}

	public SmarteditConfigurationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public SmarteditConfigurationException(final Throwable cause)
	{
		super(cause);
	}
}
