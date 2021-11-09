/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.user.exceptions;

import de.hybris.platform.servicelayer.exceptions.SystemException;


/**
 * Exception is thrown when there is attempt to change customer password but given old password does not match the one
 * stored in the system.
 * 
 */
public class PasswordMismatchException extends SystemException
{
	public PasswordMismatchException(final String message)
	{
		super(message);
	}

	public PasswordMismatchException(final Throwable cause)
	{
		super(cause);
	}

	public PasswordMismatchException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
