/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.exceptions;

/**
 * Thrown when there is a problem in creating the ssl context
 */
public class SSLContextFactoryCreationException extends Exception
{
	public SSLContextFactoryCreationException(final String message)
	{
		super(message);
	}

	public SSLContextFactoryCreationException(final String message, final Throwable t)
	{
		super(message, t);
	}
}
