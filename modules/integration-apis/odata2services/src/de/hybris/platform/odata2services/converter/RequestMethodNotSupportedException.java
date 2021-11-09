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
package de.hybris.platform.odata2services.converter;

import javax.servlet.http.HttpServletRequest;

/**
 * An exception that occurs when the incoming HttpServletRequest method is not supported by the ODataRequest.
 *
 * @see org.apache.olingo.odata2.api.commons.ODataHttpMethod
 */
public class RequestMethodNotSupportedException extends RuntimeException
{
	private final String method;

	/**
	 * Constructor to create RequestMethodNotSupportedException
	 *
	 * @param method {@link HttpServletRequest} method
	 * @param cause internal exception that was thrown
	 */
	public RequestMethodNotSupportedException(final String method, final Throwable cause)
	{
		super(cause);
		this.method = method;
	}

	/**
	 * Retrieves the Http method that came in the HttpServletRequest that's not supported by an ODataRequest
	 *
	 * @return Method name that's not supported by ODataRequest
	 */
	public String getMethod()
	{
		return method;
	}
}
