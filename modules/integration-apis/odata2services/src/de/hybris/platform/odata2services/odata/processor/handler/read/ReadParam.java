/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.read;

import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.UriInfo;

/**
 * Defines the parameters needed to perform the read operation
 */
public interface ReadParam
{
	/**
	 * Gets the {@link UriInfo}
	 * @return UriInfo
	 */
	UriInfo getUriInfo();

	/**
	 * Gets the response content type
	 * @return Content type
	 */
	String getResponseContentType();

	/**
	 * Gets the context of the request as an {@link ODataContext}
	 * @return context
	 */
	ODataContext getContext();
}
