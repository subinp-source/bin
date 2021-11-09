/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.exceptions;

import de.hybris.platform.servicelayer.exceptions.SystemException;


/**
 * Thrown when path traversal is somehow violated.
 */
public class PathTraversalException extends SystemException

{
	/**
	 * Constructs the exception with given message.
	 *
	 * @param message
	 *           a message
	 */
	public PathTraversalException(final String message)
	{
		super(message);
	}

	/**
	 * Constructs the exception with given message and a cause.
	 *
	 * @param message
	 *           the entity message
	 * @param cause
	 *           the cause
	 */
	public PathTraversalException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}