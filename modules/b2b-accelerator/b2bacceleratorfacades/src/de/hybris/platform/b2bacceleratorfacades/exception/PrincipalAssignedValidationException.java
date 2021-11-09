/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.exception;

/**
 * The exception class a validation error when checking a workflow action's assigned principal.
 */
public class PrincipalAssignedValidationException extends HybrisSystemException
{

    /**
     * Creates a new PrincipalAssignedValidationException object with the given message.
     *
     * @param message the reason for this PrincipalAssignedValidationException
     */
    public PrincipalAssignedValidationException(final String message)
    {
        super(message);
    }
    /**
     * Creates a new PrincipalAssignedValidationException object using the given message and cause
     * exception.
     *
     * @param message The reason for this PrincipalAssignedValidationException.
     * @param cause the Throwable that caused this PrincipalAssignedValidationException.
     */
    public PrincipalAssignedValidationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
