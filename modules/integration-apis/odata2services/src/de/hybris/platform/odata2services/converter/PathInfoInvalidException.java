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

import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.uri.PathInfo;

/**
 * An exception that occurs when the incoming HttpServletRequest method is not supported by the ODataRequest.
 *
 * @see org.apache.olingo.odata2.api.commons.ODataHttpMethod
 */
public class PathInfoInvalidException extends RuntimeException
{
	private final String requestUri;

	/**
	 * Constructor to create PathInfoInvalidException
	 *
	 * @param requestUri {@link HttpServletRequest} requestURI
	 * @param cause {@link ODataException} that occurred while
	 * attempting to build the {@link PathInfo} from the requestUri
	 */
	public PathInfoInvalidException(final String requestUri, final ODataException cause)
	{
		super(cause);
		this.requestUri = requestUri;
	}

	/**
	 * Retrieves the request URI from the HttpServletRequest that failed to parse
	 *
	 * @return String with the URI
	 */
	public String getRequestUri()
	{
		return requestUri;
	}
}
