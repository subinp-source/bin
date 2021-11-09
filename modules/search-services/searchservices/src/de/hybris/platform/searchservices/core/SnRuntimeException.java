/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core;

/**
 * Represents a search related runtime exception.
 */
public class SnRuntimeException extends RuntimeException
{
	/**
	 * Constructs a new exception with null as its detail message.
	 *
	 * @see RuntimeException#RuntimeException()
	 */
	public SnRuntimeException()
	{
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message
	 *           - the message
	 *
	 * @see RuntimeException#RuntimeException(String)
	 */
	public SnRuntimeException(final String message)
	{
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 *
	 * @param message
	 *           - the message
	 * @param cause
	 *           - the cause
	 *
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public SnRuntimeException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified cause.
	 *
	 * @param cause
	 *           - the cause
	 *
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public SnRuntimeException(final Throwable cause)
	{
		super(cause);
	}
}
