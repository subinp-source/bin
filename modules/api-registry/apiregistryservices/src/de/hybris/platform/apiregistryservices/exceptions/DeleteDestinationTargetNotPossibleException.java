/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.exceptions;

/**
 * Thrown when the destination target deletion is not possible.
 */
public class DeleteDestinationTargetNotPossibleException extends Exception
{
	public DeleteDestinationTargetNotPossibleException(final String message)
	{
		super(message);
	}

	public DeleteDestinationTargetNotPossibleException(final String message, final Throwable t)
	{
		super(message, t);
	}
}
