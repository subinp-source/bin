/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.exception;

import de.hybris.platform.odata2services.odata.OData2ServicesException;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

/**
 * Exception to throw when a value cannot be assigned to the property that is under concern.
 */
public class InvalidPropertyValueException extends OData2ServicesException
{
	private static final HttpStatusCodes STATUS_CODE = HttpStatusCodes.BAD_REQUEST;

	/**
	 * Constructor to create InvalidPropertyValueException.
	 * 
	 * @param message error message
	 */
	public InvalidPropertyValueException(final String message)
	{
		super(message, STATUS_CODE, "invalid_parameter_value");
	}
}
