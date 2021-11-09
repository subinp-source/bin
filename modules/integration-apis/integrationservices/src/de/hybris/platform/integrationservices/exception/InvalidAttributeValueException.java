/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.exception;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

/**
 * An exception indicating that a value is invalid for the given attribute
 */
public class InvalidAttributeValueException extends IntegrationAttributeProcessingException
{
	private static final String MSG = "Invalid value '%s' for %s attribute %s";

	/**
	 * Instantiates the exception
	 * @param value The attribute value that is invalid
	 * @param attr The {@link TypeAttributeDescriptor} that formulates the exception
	 */
	public InvalidAttributeValueException(final Object value, final TypeAttributeDescriptor attr)
	{
		super(toMessage(value, attr), attr);
	}

	private static String toMessage(final Object value, final TypeAttributeDescriptor attr)
	{
		return String.format(MSG, value, attr.getAttributeType().getTypeCode(), attr.getQualifier());
	}
}
