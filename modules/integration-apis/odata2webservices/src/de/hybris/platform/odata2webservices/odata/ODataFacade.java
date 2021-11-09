/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata;

import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataResponse;

/**
 * Receives an ODataContext and delegates its handling. Returns an ODataResponse.
 */
public interface ODataFacade
{
	/**
	 * Obtains ODataResponse with a stream that contains odata EDMX schema specified by the {@code oDataContext}.
	 *
	 * @param oDataContext contains information about what schema should be retrieved.
	 * @return requested ODataResponse with a stream that contains EDMX schema
	 */
	ODataResponse handleGetSchema(ODataContext oDataContext);

	/**
	 * Obtains ODataResponse with a stream that contains entity data model.
	 *
	 * @param oDataContext contains information about what entity should be retrieved.
	 * @return requested ODataResponse with a stream that contains entity data.
	 * @deprecated User {@link #handleRequest(ODataContext)} instead
	 */
	@Deprecated(since = "1905", forRemoval = true)
	ODataResponse handleGetEntity(ODataContext oDataContext);

	/**
	 * Creates an integration object item based on request.
	 *
	 * @param oDataContext contains information about what item should be created
	 * @return response with information about the newly created item
	 * @deprecated Use {@link #handleRequest(ODataContext)} instead
	 */
	@Deprecated(since = "1905", forRemoval = true)
	ODataResponse handlePost(ODataContext oDataContext);

	/**
	 * Handles create, read, update or delete requests on an integration object item.
	 *
	 * @param oDataContext contains the information about the item
	 * @return response with information about the item
	 */
	ODataResponse handleRequest(ODataContext oDataContext);
}
