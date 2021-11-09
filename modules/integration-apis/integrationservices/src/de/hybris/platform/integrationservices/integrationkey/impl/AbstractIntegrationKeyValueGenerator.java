/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.integrationservices.integrationkey.impl;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyValueGenerator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractIntegrationKeyValueGenerator<T, E> implements IntegrationKeyValueGenerator<T, E>
{
	private static final Logger LOG = Log.getLogger(AbstractIntegrationKeyValueGenerator.class);

	private String encoding;

	protected String encodeValue(final String value)
	{
		try
		{
			return URLEncoder.encode(value, getEncoding());
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.warn("Value [{}] was not able to be encoded.", value, e);
			return value;
		}
	}

	/**
	 * Implementations should transform a value into string representation. For instance Date.
	 *
	 * @param attributeValue the value to be converted.
	 * @return The string representation
	 */
	protected abstract String transformValueToString(final Object attributeValue);

	protected String getEncoding()
	{
		return encoding;
	}

	@Required
	public void setEncoding(final String encoding)
	{
		this.encoding = encoding;
	}
}
