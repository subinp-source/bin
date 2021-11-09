/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.servicelayer.exceptions.BusinessException;


/**
 * Exception thrown if the cart cannot be merged
 */
public class CommerceCartMergingException extends BusinessException
{
	public CommerceCartMergingException(final String message)
	{
		super(message);
	}

	public CommerceCartMergingException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
