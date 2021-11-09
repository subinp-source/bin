/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.monitoring.impl;

import static org.apache.olingo.odata2.api.commons.HttpContentType.APPLICATION_ATOM_XML;
import static org.apache.olingo.odata2.api.commons.HttpContentType.APPLICATION_ATOM_XML_UTF8;
import static org.apache.olingo.odata2.api.commons.HttpContentType.APPLICATION_XML_UTF8;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import de.hybris.platform.integrationservices.util.HttpStatus;
import de.hybris.platform.integrationservices.util.XmlObject;
import de.hybris.platform.odata2services.odata.monitoring.IntegrationKeyExtractionException;
import de.hybris.platform.odata2services.odata.monitoring.IntegrationKeyExtractor;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * This {@link IntegrationKeyExtractor} extracts the integration key value from a XML response
 */
public class XmlIntegrationKeyExtractor extends IntegrationKeyExtractorTemplate
{
	private static final String SUCCESS_PATH_EXPRESSION = "//entry//content//properties//integrationKey";
	private static final String ERROR_PATH_EXPRESSION = "//error//innererror";

	private static final Set<String> XML_MEDIA_TYPES = Sets.newHashSet(APPLICATION_XML_VALUE, APPLICATION_XML_UTF8, APPLICATION_ATOM_XML, APPLICATION_ATOM_XML_UTF8);

	@Override
	public boolean isApplicable(final String contentType)
	{
		return (XML_MEDIA_TYPES.contains(contentType));
	}

	@Override
	protected String extractIntegrationKeyFromNonEmptyBody(final String responseBody, final int statusCode)
	{
		try
		{
			final XmlObject xml = XmlObject.createFrom(responseBody);
			return xml.get(getPathExpression(statusCode));
		}
		catch (final IllegalArgumentException e)
		{
			throw new IntegrationKeyExtractionException(e);
		}
	}

	private static String getPathExpression(final int responseStatusCode)
	{
		return HttpStatus.valueOf(responseStatusCode).isError()
				? ERROR_PATH_EXPRESSION
				: SUCCESS_PATH_EXPRESSION;
	}
}
