/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.util;

import de.hybris.platform.integrationservices.util.JsonObject;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;

/**
 * A helper class for simpler evaluation of error reponses in the tests.
 */
public class JsonErrorResponse
{
	private static final String ERROR_CODE_PATH = "error.code";
	private static final String ERROR_MSG_PATH = "error.message.value";

	private final HttpStatusCodes httpStatusCode;
	private final JsonObject responseBody;

	public JsonErrorResponse(final HttpStatusCodes status, final JsonObject json)
	{
		httpStatusCode = status;
		responseBody = json;
	}

	/**
	 * Creates this error response
	 *
	 * @param response OData2 response received in the test
	 * @throws ODataException if response body cannot be read
	 */
	public static JsonErrorResponse createFrom(final ODataResponse response) throws ODataException
	{
		final var status = response.getStatus();
		final var json = JsonObject.createFrom(response.getEntityAsStream());
		return new JsonErrorResponse(status, json);
	}

	/**
	 * Retrieves HTTP status code of the response.
	 *
	 * @return HTTP status code even, if response is not error but is successful
	 */
	public int getStatusCode()
	{
		return httpStatusCode.getStatusCode();
	}

	/**
	 * Retrieves error code reported in the response body.
	 *
	 * @return value of the {@code "error.code"} path in the response body or {@code null}, if error code is not present in the
	 * response body.
	 */
	public String getErrorCode()
	{
		return responseBody.getString(ERROR_CODE_PATH);
	}

	/**
	 * Retrieves error message reported in the response body.
	 *
	 * @return value of the {@code "error.message.value"} path in the response body or {@code null}, if error message is not
	 * present in the body.
	 */
	public String getMessage()
	{
		return responseBody.getString(ERROR_MSG_PATH);
	}
}
