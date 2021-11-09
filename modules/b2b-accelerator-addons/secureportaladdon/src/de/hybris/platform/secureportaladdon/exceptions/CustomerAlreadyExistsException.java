/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon.exceptions;

import de.hybris.platform.servicelayer.exceptions.BusinessException;


/**
 * Thrown when a customer already exists
 */
public class CustomerAlreadyExistsException extends BusinessException
{

	public CustomerAlreadyExistsException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public CustomerAlreadyExistsException(final String message)
	{
		super(message);
	}

	public CustomerAlreadyExistsException(final Throwable cause)
	{
		super(cause);

	}

}
