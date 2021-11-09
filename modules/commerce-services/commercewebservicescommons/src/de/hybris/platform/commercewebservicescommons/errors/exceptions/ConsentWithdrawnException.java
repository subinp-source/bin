/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.errors.exceptions;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;


/**
 * Exception for handling error if we withdraw a consent that was already withdrawn.
 */
public class ConsentWithdrawnException extends WebserviceException
{

	public static final String CONSENT_WITHDRAWN = "consentWithdrawn";

	private static final String TYPE = "ConsentWithdrawnError";
	private static final String SUBJECT_TYPE = "consent";

	/**
	 * Message and reason for the exception
	 *
	 * @param message
	 * @param reason
	 */
	public ConsentWithdrawnException(final String message, final String reason)
	{
		super(message, reason);
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
