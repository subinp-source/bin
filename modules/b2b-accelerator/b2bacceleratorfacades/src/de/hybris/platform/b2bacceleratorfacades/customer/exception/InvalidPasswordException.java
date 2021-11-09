/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.customer.exception;

import de.hybris.platform.servicelayer.exceptions.SystemException;


/**
 * Exception is thrown when there is attempt to change customer password but it does not match the validation regex.
 */
public class InvalidPasswordException extends SystemException
{

	public InvalidPasswordException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public InvalidPasswordException(final String message)
	{
		super(message);
	}

	public InvalidPasswordException(final Throwable cause)
	{
		super(cause);
	}

}
