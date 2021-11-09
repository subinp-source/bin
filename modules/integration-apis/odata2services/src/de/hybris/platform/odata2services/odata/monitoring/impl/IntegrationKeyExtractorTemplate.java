/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.monitoring.impl;

import de.hybris.platform.odata2services.odata.monitoring.IntegrationKeyExtractor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.StringUtils;

/**
 * Base flow for extracting integration key values
 */
public abstract class IntegrationKeyExtractorTemplate  implements IntegrationKeyExtractor
{

	/**
	 * Extracts integration key from the provided response body. If response body is not empty or {@code null}, then
	 * it's passed to {@link #extractIntegrationKeyFromNonEmptyBody(String, int)}, which extracts integration key
	 * from the specific response structure. Then the extracted value is decoded by {@link #decode(String)} method.
	 * @param responseBody response body to extract the integrationKey value from
	 * @param statusCode status code of the response, which may be important when response body has different
	 *                   structure for different response statuses.
	 * @return extracted value
	 */
	@Override
	public String extractIntegrationKey(final String responseBody, final int statusCode)
	{
		final String key = StringUtils.isNotEmpty(responseBody)
				? extractIntegrationKeyFromNonEmptyBody(responseBody, statusCode)
				: "";
		return decode(key);
	}

	/**
	 * Extracts integration key from the response body in a way specific to the implemenation.
	 * @param responseBody response body to be parsed.
	 * @param statusCode status code of the response.
	 * @return integration key value.
	 */
	protected abstract String extractIntegrationKeyFromNonEmptyBody(String responseBody, int statusCode);

	/**
	 * Decodes value. This method uses {@link URLDecoder} for decoding.
	 * @param value an integration key value to be decoded.
	 * @return decoded value.
	 */
	protected String decode(final String value)
	{
		return URLDecoder.decode(value, StandardCharsets.UTF_8);
	}
}
