/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util.timeout;

/**
 * This exception is thrown when the {@link TimeoutService} encounters an exception
 * other than the {@link java.util.concurrent.TimeoutException}. It is a wrapper
 * around the actual cause. Use {@link Throwable#getCause()} to get the actual cause
 * of this exception.
 */
public class IntegrationExecutionException extends RuntimeException
{
	/**
	 * Instantiates an IntegrationExecutionException
	 *
	 * @param cause The cause of the execution exception
	 */
	public IntegrationExecutionException(final Throwable cause)
	{
		this("The execution encountered an exception.", cause);
	}

	/**
	 * Instantiates an IntegrationExecutionException with a message
	 *
	 * @param msg   The error message
	 * @param cause The cause of the execution exception
	 */
	public IntegrationExecutionException(final String msg, final Throwable cause)
	{
		super(msg, actualCause(cause));
	}

	private static Throwable actualCause(final Throwable cause)
	{
		return cause instanceof IntegrationExecutionException ? cause.getCause() : cause;
	}
}
