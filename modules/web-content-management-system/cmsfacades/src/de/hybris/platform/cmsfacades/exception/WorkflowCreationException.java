/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.exception;

/**
 * Exception thrown when a workflow cannot be created successfully.
 */
public class WorkflowCreationException extends RuntimeException
{
	private static final long serialVersionUID = -6067882917389051080L;

	public WorkflowCreationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public WorkflowCreationException(final String message)
	{
		super(message);
	}

	public WorkflowCreationException(final Throwable cause)
	{
		super(cause);
	}
}
