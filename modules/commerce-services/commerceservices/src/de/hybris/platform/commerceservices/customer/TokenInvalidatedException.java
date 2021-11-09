/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer;

/**
 * Exception is thrown if the user tries to use the same token multiple times.
 */
public class TokenInvalidatedException extends Exception
{

	/**
	 * Default Constructor
	 */
	public TokenInvalidatedException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TokenInvalidatedException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public TokenInvalidatedException(final String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public TokenInvalidatedException(final Throwable cause)
	{
		super(cause);
	}

}
