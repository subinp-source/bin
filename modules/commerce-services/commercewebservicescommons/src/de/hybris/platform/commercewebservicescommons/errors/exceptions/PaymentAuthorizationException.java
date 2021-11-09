/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.errors.exceptions;

/**
 * Specific exception that is thrown when the payment authorization was not accepted
 */
public class PaymentAuthorizationException extends Exception
{
	public PaymentAuthorizationException()
	{
		super("Payment authorization was not successful");
	}

	public PaymentAuthorizationException(final String message)
	{
		super(message);
	}
}
