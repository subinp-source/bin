/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.exception;

/**
 * Exception used if the item in workflow can not be edited by current user.
 */
public class NonEditableItemInWorkflowException extends RuntimeException
{
	public NonEditableItemInWorkflowException(final String message)
	{
		super(message);
	}

	public NonEditableItemInWorkflowException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public NonEditableItemInWorkflowException(final Throwable cause)
	{
		super(cause);
	}
}
