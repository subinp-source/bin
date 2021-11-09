/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer;

import de.hybris.platform.searchservices.core.SnRuntimeException;


/**
 * Exception thrown when a suitable indexer item source mapping could not be found.
 */
public class SnIndexerItemSourceMappingNotFoundException extends SnRuntimeException
{
	/**
	 * Constructs a new exception with null as its detail message.
	 *
	 * @see Exception#Exception()
	 */
	public SnIndexerItemSourceMappingNotFoundException()
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
	public SnIndexerItemSourceMappingNotFoundException(final String message)
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
	public SnIndexerItemSourceMappingNotFoundException(final String message, final Throwable cause)
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
	public SnIndexerItemSourceMappingNotFoundException(final Throwable cause)
	{
		super(cause);
	}
}
