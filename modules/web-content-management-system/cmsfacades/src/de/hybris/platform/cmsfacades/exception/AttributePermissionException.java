/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.exception;

/**
 * Exception thrown when attempting to user cannot access an attribute
 * 
 * @deprecated since 1905, please use {@link de.hybris.platform.cms2.exceptions.AttributePermissionException}
 */
@Deprecated(since = "1905", forRemoval = true)
public class AttributePermissionException extends RuntimeException
{
	private static final long serialVersionUID = 8033677933215967847L;

	public AttributePermissionException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public AttributePermissionException(final String message)
	{
		super(message);
	}

	public AttributePermissionException(final Throwable cause)
	{
		super(cause);
	}

}
