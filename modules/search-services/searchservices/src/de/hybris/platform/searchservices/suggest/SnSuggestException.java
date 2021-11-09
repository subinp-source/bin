/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest;

import de.hybris.platform.searchservices.core.SnException;


/**
 * Represents a suggest related exception.
 */
public class SnSuggestException extends SnException
{
	/**
	 * Constructs a new exception with null as its detail message.
	 *
	 * @see Exception#Exception()
	 */
	public SnSuggestException()
	{
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message
	 *           - the message
	 *
	 * @see Exception#Exception(String)
	 */
	public SnSuggestException(final String message)
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
	 * @see Exception#Exception(String, Throwable)
	 */
	public SnSuggestException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified cause.
	 *
	 * @param cause
	 *           - the cause
	 *
	 * @see Exception#Exception(Throwable)
	 */
	public SnSuggestException(final Throwable cause)
	{
		super(cause);
	}
}
