/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization;

/**
 * Exception used when the synchronization is already in progress
 */
public class SynchronizationInProgressException extends RuntimeException
{
	private static final long serialVersionUID = -8051464932011941508L;

	public SynchronizationInProgressException()
	{
	}

	public SynchronizationInProgressException(final String message)
	{
		super(message);
	}

	public SynchronizationInProgressException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
