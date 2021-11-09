/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.exceptions;

/**
 * Exception for quote that does not meet threshold.
 *
 */
public class QuoteUnderThresholdException extends RuntimeException
{
	private static final String EXCEPTION_MSG_FORMAT = "Quote with code [%s] and version [%s] does not meet the threshold.";

	public QuoteUnderThresholdException(final String quoteCode, final Integer quoteVersion, final Throwable cause)
	{
		super(String.format(EXCEPTION_MSG_FORMAT, quoteCode, quoteVersion), cause);
	}

	public QuoteUnderThresholdException(final String quoteCode, final Integer quoteVersion)
	{
		super(String.format(EXCEPTION_MSG_FORMAT, quoteCode, quoteVersion));
	}

	public QuoteUnderThresholdException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public QuoteUnderThresholdException(final String message)
	{
		super(message);
	}

}
