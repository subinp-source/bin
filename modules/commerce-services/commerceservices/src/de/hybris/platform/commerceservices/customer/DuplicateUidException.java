/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer;

/**
 * Exception is thrown when an attempt to store an UID that is already assigned
 */
public class DuplicateUidException extends Exception
{

	/**
	 * Default constructor
	 */
	public DuplicateUidException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DuplicateUidException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public DuplicateUidException(final String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public DuplicateUidException(final Throwable cause)
	{
		super(cause);
	}

}
