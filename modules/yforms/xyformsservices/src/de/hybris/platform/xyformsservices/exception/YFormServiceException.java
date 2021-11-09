/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.exception;

/**
 * Exception thrown while handling yForms
 */
public class YFormServiceException extends Exception
{
	public YFormServiceException(final String message)
	{
		super(message);
	}

	public YFormServiceException(final String message, final Throwable t)
	{
		super(message, t);
	}

	public YFormServiceException(final Throwable t)
	{
		super(t);
	}
}
