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

import org.apache.olingo.odata2.api.exception.ODataUnsupportedMediaTypeException;
import org.apache.olingo.odata2.core.commons.ContentType;

/**
 * An exception that occurs when the incoming HttpServletRequest content type is not supported by the ODataRequest.
 */
public class ContentTypeNotSupportedException extends RuntimeException
{
	private final String contentType;

	/**
	 * Constructor to create ContentTypeNotSupportedException
	 *
	 * @param contentType requested contentType
	 * @param cause {@link ODataUnsupportedMediaTypeException} thrown when searching for a {@link ContentType}
	 */
	public ContentTypeNotSupportedException(final String contentType, final ODataUnsupportedMediaTypeException cause)
	{
		super(cause);
		this.contentType = contentType;
	}

	/**
	 * Retrieves the content type that failed to parse.
	 *
	 * @return String contentType that came in the HttpServletRequest.
	 */
	public String getContentType()
	{
		return contentType;
	}
}
