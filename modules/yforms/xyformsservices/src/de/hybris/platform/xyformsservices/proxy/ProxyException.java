/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.proxy;

/**
 * Exception thrown when dealing with Content Proxy
 */
public class ProxyException extends Exception
{
	/**
	 * Message based exception.
	 *
	 * @param message
	 */
	public ProxyException(final String message)
	{
		super(message);
	}

	/**
	 * Message and throwable based Exception
	 *
	 * @param message
	 * @param t
	 */
	public ProxyException(final String message, final Throwable t)
	{
		super(message, t);
	}

	/**
	 * Throwable based Exception
	 *
	 * @param t
	 */
	public ProxyException(final Throwable t)
	{
		super(t);
	}
}
