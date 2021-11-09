/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.filter;

import de.hybris.platform.odata2services.odata.OData2ServicesException;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.EdmException;

/**
 * Exception to throw for exceptions that occur during the processing of a $filter query parameter
 * Will result in HttpStatus 500
 */
public class FilterProcessingException extends OData2ServicesException
{
	private static final String DEFAULT_ERROR_CODE = "invalid_filter";
	private static final String DEFAULT_MESSAGE = "An error occurred while parsing the filter expression. Please make sure the filter is valid.";
	private static final HttpStatusCodes STATUS_CODE = HttpStatusCodes.INTERNAL_SERVER_ERROR;

	/**
	 * Constructor to create FilterProcessingException
	 *
	 * @param e {@link EdmException} that was thrown
	 */
	public FilterProcessingException(final EdmException e)
	{
		super(DEFAULT_MESSAGE, STATUS_CODE, DEFAULT_ERROR_CODE, e);
	}

	/**
	 * Constructor to create a FilterProcessingException
	 */
	public FilterProcessingException()
	{
		super(DEFAULT_MESSAGE, STATUS_CODE, DEFAULT_ERROR_CODE);
	}
}