/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.errors.exceptions;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;


/**
 * Exception thrown if stock system is not enabled
 */
public class StockSystemException extends WebserviceException
{
	public static final String NOT_ENABLED = "notEnabled";
	private static final String TYPE = "StockSystemError";
	private static final String SUBJECT_TYPE = "site";

	public StockSystemException(final String message)
	{
		super(message);
	}

	public StockSystemException(final String message, final String reason)
	{
		super(message, reason);
	}

	public StockSystemException(final String message, final String reason, final Throwable cause)
	{
		super(message, reason, cause);
	}

	public StockSystemException(final String message, final String reason, final String subject)
	{
		super(message, reason, subject);
	}

	public StockSystemException(final String message, final String reason, final String subject, final Throwable cause)
	{
		super(message, reason, subject, cause);
	}

	@Override
	public String getType()
	{
		return TYPE;
	}

	@Override
	public String getSubjectType()
	{
		return SUBJECT_TYPE;
	}
}
