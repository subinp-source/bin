/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;


import de.hybris.platform.servicelayer.exceptions.SystemException;


/**
 * Exception thrown if Quote has Expired
 */
public class CommerceQuoteExpirationTimeException extends SystemException
{
	public CommerceQuoteExpirationTimeException(final String message)
	{
		super(message);
	}

	public CommerceQuoteExpirationTimeException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
