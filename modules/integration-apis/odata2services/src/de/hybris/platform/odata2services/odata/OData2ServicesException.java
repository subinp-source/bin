/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.odata2services.odata;


import java.util.Locale;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.exception.ODataRuntimeApplicationException;

/**
 * This exception is intended to be extended in order to set a custom error message
 * that can be displayed to the user. We should never call any of these constructors
 * without providing a custom exception message that prevents secure data from being
 * exposed in the response.
 *
 */
public abstract class OData2ServicesException extends ODataRuntimeApplicationException
{
	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	/**
	 * Constructor to create OData2ServicesException
	 *
	 * @param message error message for the response
	 * @param status response status errorCode
	 * @param errorCode error code
	 */
	public OData2ServicesException(final String message, final HttpStatusCodes status, final String errorCode)
	{
		super(message, DEFAULT_LOCALE, status, errorCode);
	}

	/**
	 * Constructor to create OData2ServicesException
	 *
	 * @param message error message for the response
	 * @param status response status code
	 * @param errorCode error code
	 * @param e exception that was thrown
	 */
	public OData2ServicesException(final String message, final HttpStatusCodes status, final String errorCode, final Throwable e)
	{
		super(message, DEFAULT_LOCALE, status, errorCode, e);
	}
}
