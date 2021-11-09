/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.exceptions;

import de.hybris.platform.commerceservices.enums.QuoteAction;
import de.hybris.platform.core.enums.QuoteState;


/**
 * Exception thrown when an action cannot be performed for a quote. Encapsulates basic quote information.
 */
public class IllegalQuoteStateException extends RuntimeException
{
	private static final String EXCEPTION_MSG_FORMAT = "Action [%s] is not allowed for quote code [%s] in quote state [%s] having version [%s].";

	private final QuoteAction quoteAction;
	private final String quoteCode;
	private final QuoteState quoteState;
	private final Integer quoteVersion;
	private final boolean hasLocalizedMessage;



	public IllegalQuoteStateException(final QuoteAction quoteAction, final String quoteCode, final QuoteState quoteState,
			final Integer quoteVersion)
	{
		super(String.format(EXCEPTION_MSG_FORMAT, quoteAction, quoteCode, quoteState, quoteVersion));
		this.quoteAction = quoteAction;
		this.quoteCode = quoteCode;
		this.quoteState = quoteState;
		this.quoteVersion = quoteVersion;
		this.hasLocalizedMessage = false;
	}

	public IllegalQuoteStateException(final QuoteAction quoteAction, final String quoteCode, final QuoteState quoteState,
			final Integer quoteVersion, final Throwable cause)
	{
		super(String.format(EXCEPTION_MSG_FORMAT, quoteAction, quoteCode, quoteState, quoteVersion), cause);
		this.quoteAction = quoteAction;
		this.quoteCode = quoteCode;
		this.quoteState = quoteState;
		this.quoteVersion = quoteVersion;
		this.hasLocalizedMessage = false;
	}

	public IllegalQuoteStateException(final QuoteAction quoteAction, final String quoteCode, final QuoteState quoteState,
			final Integer quoteVersion, final String message)
	{
		super(message);
		this.quoteAction = quoteAction;
		this.quoteCode = quoteCode;
		this.quoteState = quoteState;
		this.quoteVersion = quoteVersion;
		this.hasLocalizedMessage = false;
	}

	public IllegalQuoteStateException(final QuoteAction quoteAction, final String quoteCode, final QuoteState quoteState,
			final Integer quoteVersion, final String message, final boolean hasLocalizedMessage)
	{
		super(message);
		this.quoteAction = quoteAction;
		this.quoteCode = quoteCode;
		this.quoteState = quoteState;
		this.quoteVersion = quoteVersion;
		this.hasLocalizedMessage = hasLocalizedMessage;
	}

	public IllegalQuoteStateException(final QuoteAction quoteAction, final String quoteCode, final QuoteState quoteState,
			final Integer quoteVersion, final String message, final Throwable cause)
	{
		super(message, cause);
		this.quoteAction = quoteAction;
		this.quoteCode = quoteCode;
		this.quoteState = quoteState;
		this.quoteVersion = quoteVersion;
		this.hasLocalizedMessage = false;
	}

	public QuoteAction getQuoteAction()
	{
		return quoteAction;
	}

	public String getQuoteCode()
	{
		return quoteCode;
	}

	public QuoteState getQuoteState()
	{
		return quoteState;
	}

	public Integer getQuoteVersion()
	{
		return quoteVersion;
	}

	public boolean hasLocalizedMessage()
	{
		return hasLocalizedMessage;
	}
}
