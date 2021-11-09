/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.persistence;

import de.hybris.platform.odata2services.odata.OData2ServicesException;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

/**
 * Exception to throw for Internal processing Exceptions.
 * Will result in HttpStatus 500
 */
public class InternalProcessingException extends OData2ServicesException
{
	private static final HttpStatusCodes STATUS_CODE = HttpStatusCodes.INTERNAL_SERVER_ERROR;
	private static final String DEFAULT_ERROR_CODE = "internal_error";
	private static final String BASE_MESSAGE = "There was an error encountered during the processing of the " +
			"integration object.";

	/**
	 * Constructor to create InternalProcessingException
	 *
	 * @param message error message
	 */
	public InternalProcessingException(final String message)
	{
		super(message, STATUS_CODE, DEFAULT_ERROR_CODE);
	}

	/**
	 * Constructor to create InternalProcessingException
	 *
	 * @param e exception that was thrown
	 */
	public InternalProcessingException(final Throwable e)
	{
		super(BASE_MESSAGE, STATUS_CODE, DEFAULT_ERROR_CODE, e);
	}
}
