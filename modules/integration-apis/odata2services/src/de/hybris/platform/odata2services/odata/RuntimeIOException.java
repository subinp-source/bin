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
package de.hybris.platform.odata2services.odata;

import java.io.IOException;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

/**
 * This exception wraps a thrown {@link IOException} as a runtime exception
 *
 * Will result in HttpStatus 500
 */
public class RuntimeIOException extends OData2ServicesException
{
	/**
	 * There was a problem reading or closing the stream of the request or response entity.
	 *
	 * @param e the internal exception that was thrown
	 */
	public RuntimeIOException(final IOException e)
	{
		super("An error occurred during the processing of the request. Please see the log for more detail.", HttpStatusCodes.INTERNAL_SERVER_ERROR, "request_error", e);
	}
}
