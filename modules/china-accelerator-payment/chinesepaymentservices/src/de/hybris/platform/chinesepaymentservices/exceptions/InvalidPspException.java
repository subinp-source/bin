/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.chinesepaymentservices.exceptions;

/**
 * Payment service provider exception
 */
public class InvalidPspException extends RuntimeException
{
	public InvalidPspException(final String paymentProviderCode)
	{
		super("Could not find the implementation of [" + paymentProviderCode + "] for ChinesePaymentService.");
	}
}
