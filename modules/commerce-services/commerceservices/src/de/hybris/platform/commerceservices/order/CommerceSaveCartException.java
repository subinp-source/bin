/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.servicelayer.exceptions.BusinessException;


/**
 * Exception thrown if the cart cannot be saved
 */
public class CommerceSaveCartException extends BusinessException
{
	public CommerceSaveCartException(final String message)
	{
		super(message);
	}

	public CommerceSaveCartException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
