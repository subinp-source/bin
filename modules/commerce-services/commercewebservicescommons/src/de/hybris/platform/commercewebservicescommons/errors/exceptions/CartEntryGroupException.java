/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.errors.exceptions;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;


public class CartEntryGroupException extends WebserviceException
{
	public static final String NOT_FOUND = "notFound";
	private static final String TYPE = "CartEntryGroupError";
	private static final String SUBJECT_TYPE = "entryGroup";

	public CartEntryGroupException(final String message)
	{
		super(message);
	}

	public CartEntryGroupException(final String message, final String reason)
	{
		super(message, reason);
	}

	public CartEntryGroupException(final String message, final String reason, final Throwable cause)
	{
		super(message, reason, cause);
	}

	public CartEntryGroupException(final String message, final String reason, final String subject)
	{
		super(message, reason, subject);
	}

	public CartEntryGroupException(final String message, final String reason, final String subject, final Throwable cause)
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
