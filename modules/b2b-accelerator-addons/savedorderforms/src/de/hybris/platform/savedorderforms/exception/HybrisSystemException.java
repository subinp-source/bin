/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.savedorderforms.exception;

/**
 * The generic exception class for the com.hybris package.
 */
public class HybrisSystemException extends RuntimeException
{
    private static final long serialVersionUID = -7403307082023533142L;
    /**
     * Creates a new instance with the given message.
     *
     * @param message the reason for this HybrisSystemException
     */
    public HybrisSystemException(final String message)
    {
        super(message);
    }
    /**
     * Creates a new instance using the given message and
     * cause exception.
     *
     * @param message The reason for this HybrisSystemException.
     * @param cause the Throwable that caused this HybrisSystemException.
     */
    public HybrisSystemException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
