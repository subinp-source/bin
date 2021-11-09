/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.exceptions;

/**
 * Represents a product search strategy related runtime exception.
 */
public class ProductSearchStrategyRuntimeException extends RuntimeException
{
	/**
	 * Constructs a new exception with null as its detail message.
	 *
	 * @see RuntimeException#RuntimeException()
	 */
	public ProductSearchStrategyRuntimeException()
	{
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message - the message
	 * @see RuntimeException#RuntimeException(String)
	 */
	public ProductSearchStrategyRuntimeException(final String message)
	{
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 *
	 * @param message - the message
	 * @param cause   - the cause
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public ProductSearchStrategyRuntimeException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified cause.
	 *
	 * @param cause - the cause
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public ProductSearchStrategyRuntimeException(final Throwable cause)
	{
		super(cause);
	}
}
