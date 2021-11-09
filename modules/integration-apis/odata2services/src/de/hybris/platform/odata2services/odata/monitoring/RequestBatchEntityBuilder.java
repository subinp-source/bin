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

package de.hybris.platform.odata2services.odata.monitoring;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.INTEGRATION_MESSAGE_ID;
import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.SERVICE;

import de.hybris.platform.integrationservices.util.Log;

import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.PathSegment;
import org.slf4j.Logger;

/**
 * A builder for {@link RequestBatchEntity}.
 */
public class RequestBatchEntityBuilder
{
	private static final Logger LOG = Log.getLogger(RequestBatchEntityBuilder.class);

	// capture the integration key between the parentheses, e.g Products(%27Stage%7Default%productCode%27)
	private static final Pattern INTEGRATION_KEY_PATTERN = Pattern.compile("\\w+\\((.+)\\)");

	private InputStream batchContent;
	private String messageId;
	private String integrationObjectType;
	private String integrationKey;
	private int numberOfChangeSets = 1;

	public static RequestBatchEntityBuilder requestBatchEntity()
	{
		return new RequestBatchEntityBuilder();
	}

	public RequestBatchEntityBuilder withContext(final ODataContext context)
	{
		return withMessageId(context.getRequestHeader(INTEGRATION_MESSAGE_ID))
				.withIntegrationObjectType(deriveIntegrationObjectType(context))
				.withIntegrationKey(deriveIntegrationKey(context));
	}

	public RequestBatchEntityBuilder withBatchContent(final InputStream body)
	{
		batchContent = body;
		return this;
	}

	public RequestBatchEntityBuilder withMessageId(final String id)
	{
		messageId = id;
		return this;
	}

	public RequestBatchEntityBuilder withIntegrationObjectType(final String type)
	{
		integrationObjectType = type;
		return this;
	}

	public RequestBatchEntityBuilder withNumberOfChangeSets(final int cnt)
	{
		numberOfChangeSets = cnt;
		return this;
	}

	public RequestBatchEntityBuilder withIntegrationKey(final String key)
	{
		integrationKey = key;
		return this;
	}

	public RequestBatchEntity build()
	{
		return new RequestBatchEntity(messageId, batchContent, integrationObjectType, numberOfChangeSets, integrationKey);
	}

	protected String deriveIntegrationObjectType(final ODataContext context)
	{
		final Object value = context.getParameter(SERVICE);
		return value instanceof String ? (String) value : null;
	}

	protected String deriveIntegrationKey(final ODataContext context)
	{
		try
		{
			if (context.getPathInfo() != null && context.getPathInfo().getODataSegments() != null)
			{
				final PathSegment pathSegment = !context.getPathInfo().getODataSegments().isEmpty() ?
						context.getPathInfo().getODataSegments().get(0) : null;
				return extractIntegrationKey(pathSegment);
			}
		}
		catch (final ODataException | RuntimeException e)
		{
			LOG.error("Failed to extract integration key from request path: ", e);
		}
		return "";
	}

	protected String extractIntegrationKey(final PathSegment pathSegment)
	{
		if (pathSegment != null)
		{
			final Matcher matcher = INTEGRATION_KEY_PATTERN.matcher(pathSegment.getPath());
			if (matcher.matches() && matcher.groupCount() == 1)
			{
				final String key = URLDecoder.decode(matcher.group(1), StandardCharsets.UTF_8);
				return key.replaceAll("['\"]", "");
			}
		}
		return "";
	}
}
