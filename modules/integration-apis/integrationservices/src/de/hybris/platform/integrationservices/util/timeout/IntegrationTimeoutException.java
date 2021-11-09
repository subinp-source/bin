/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util.timeout;

/**
 * This exception is thrown when a timeout occurs in the {@link TimeoutService}
 */
public class IntegrationTimeoutException extends RuntimeException
{
	private static final String ERROR_MSG = "The execution timed out after %d ms.";

	private final long timeout;

	/**
	 * Instantiates an IntegrationTimeoutException
	 *
	 * @param timeout The timeout value
	 */
	public IntegrationTimeoutException(final long timeout)
	{
		this(toMessage(timeout), timeout);
	}

	/**
	 * Instantiates an IntegrationTimeoutException with a message
	 *
	 * @param msg     Exception message
	 * @param timeout The timeout value
	 */
	public IntegrationTimeoutException(final String msg, final long timeout)
	{
		super(msg);
		this.timeout = timeout;
	}

	/**
	 * Gets the timeout value
	 *
	 * @return Timeout value
	 */
	public long getTimeout()
	{
		return timeout;
	}

	private static String toMessage(final long timeout)
	{
		return String.format(ERROR_MSG, timeout);
	}
}
