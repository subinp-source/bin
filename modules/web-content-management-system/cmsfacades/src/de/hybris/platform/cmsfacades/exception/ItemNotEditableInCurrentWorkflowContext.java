/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.exception;

/**
 * Exception used if the item can not be edited in current workflow context.
 */
public class ItemNotEditableInCurrentWorkflowContext extends RuntimeException
{
	public ItemNotEditableInCurrentWorkflowContext(final String message)
	{
		super(message);
	}

	public ItemNotEditableInCurrentWorkflowContext(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public ItemNotEditableInCurrentWorkflowContext(final Throwable cause)
	{
		super(cause);
	}
}
