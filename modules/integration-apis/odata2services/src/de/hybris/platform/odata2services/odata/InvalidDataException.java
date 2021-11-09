/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.odata2services.odata;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

/**
 * Exception to throw for error scenarios produced by invalid Data.
 * Will result in HttpStatus 400
 */
public class InvalidDataException extends OData2ServicesException
{
	private static final HttpStatusCodes STATUS_CODE = HttpStatusCodes.BAD_REQUEST;
	private final String entityType;

	/**
	 * Constructor to create InvalidDataException
	 *
	 * @param message error message
	 * @param errorCode error code
	 */
	public InvalidDataException(final String message, final String errorCode, final String entityType)
	{
		super(message, STATUS_CODE, errorCode);
		this.entityType = entityType;
	}

	/**
	 * Constructor to create InvalidDataException
	 *
	 * @param message error message
	 * @param errorCode error code
	 * @param e exception to get Message from
	 * @param entityType entityTypeName for the current entity that is under concern
	 */
	public InvalidDataException(final String message, final String errorCode, final Throwable e, final String entityType)
	{
		super(message, STATUS_CODE, errorCode, e);
		this.entityType = entityType;
	}

	public String getEntityType()
	{
		return entityType;
	}
}