/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.monitoring.impl;

import static org.apache.olingo.odata2.api.commons.HttpContentType.APPLICATION_JSON;
import static org.apache.olingo.odata2.api.commons.HttpContentType.APPLICATION_JSON_UTF8;

import de.hybris.platform.integrationservices.util.HttpStatus;
import de.hybris.platform.odata2services.odata.monitoring.IntegrationKeyExtractor;

import org.apache.commons.lang.StringUtils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 * This {@link IntegrationKeyExtractor} extracts the integration key value from a JSON response
 */
public class JsonIntegrationKeyExtractor extends IntegrationKeyExtractorTemplate
{
	private static final String SUCCESS_PATH_EXPRESSION = "$.d.integrationKey";
	private static final String ERROR_PATH_EXPRESSION = "$.error.innererror";

	@Override
	public boolean isApplicable(final String contentType)
	{
		return (APPLICATION_JSON.equalsIgnoreCase(contentType) || APPLICATION_JSON_UTF8.equalsIgnoreCase(contentType));
	}

	@Override
	protected String extractIntegrationKeyFromNonEmptyBody(final String responseBody, final int statusCode)
	{
		try
		{
			final DocumentContext ctx = JsonPath.parse(responseBody);
			return ctx.read(getPathExpression(statusCode), String.class);
		}
		catch(final PathNotFoundException e)
		{
			return StringUtils.EMPTY;
		}
	}

	private static String getPathExpression(final int statusCode)
	{
		return HttpStatus.valueOf(statusCode).isError()
				? ERROR_PATH_EXPRESSION
				: SUCCESS_PATH_EXPRESSION;
	}
}
