/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.exception;

/**
 * Exception thrown when attempting to add or move a component to a slot that doesn't support it.
 */
public class ComponentNotFoundInSlotException extends RuntimeException
{

	private static final long serialVersionUID = 7427385832156186056L;

	public ComponentNotFoundInSlotException(final String message)
    {
        super(message);
    }

    public ComponentNotFoundInSlotException(final Throwable cause)
    {
        super(cause);
    }

    public ComponentNotFoundInSlotException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}