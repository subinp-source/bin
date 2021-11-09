/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.exceptions;


/**
 * Thrown when business event parameter is missing for business process actions.
 */
public class BusinessEventParameterMissingException extends Exception
{
	public BusinessEventParameterMissingException(final String message)
	{
		super(message);
	}

	public BusinessEventParameterMissingException(final String message, final Throwable t)
	{
		super(message, t);
	}
}
