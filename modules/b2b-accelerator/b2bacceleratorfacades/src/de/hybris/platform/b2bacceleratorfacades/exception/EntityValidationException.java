/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.exception;
/**
 * The Class de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException.
 */
public class EntityValidationException extends DomainException
{
    private static final long serialVersionUID = 1L;
    /**
     * Creates a new instance with the given message.
     *
     * @param message the reason for this de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException
     */
    public EntityValidationException(final String message)
    {
        super(message);
    }
    /**
     * Creates a new instance using the given message and cause
     * exception.
     *
     * @param message The reason for this de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException.
     * @param cause the Throwable that caused this de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException.
     */
    public EntityValidationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
