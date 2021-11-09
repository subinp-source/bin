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
package de.hybris.platform.odata2services.odata.processor;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.slf4j.Logger;

/**
 * Default implementation of the {@link ServiceNameExtractor}
 */
public class DefaultServiceNameExtractor implements ServiceNameExtractor
{
	private static final Logger LOG = Log.getLogger(DefaultServiceNameExtractor.class);

	private static final int MIN_PATH_INFO_SEGMENTS = 2;
	public static final String ELEMENT_DIVIDER = "/";
	public static final int MAX_ELEMENTS = 3;

	@Override
	public String extract(final ODataContext context)
	{
		try
		{
			final URI serviceRoot = context.getPathInfo().getServiceRoot();
			if (serviceRoot == null || serviceRoot.getPath() == null)
			{
				throw new InternalProcessingException("Service Name was not found.");
			}
			String path = serviceRoot.getPath();
			if (path.endsWith("/"))
			{
				path = path.substring(0, path.length() - 1);
			}
			return path.substring(path.lastIndexOf('/') + 1);
		}
		catch (final ODataException e)
		{
			LOG.warn("Failed to extract the service name from the context", e);
			throw new InternalProcessingException(e);
		}
	}

	@Override
	public String extract(final HttpServletRequest httpServletRequest)
	{
		final String pathInfo = httpServletRequest.getPathInfo();

		if (pathInfo == null)
		{
			return StringUtils.EMPTY;
		}
		final String[] elements = pathInfo.split(ELEMENT_DIVIDER, MAX_ELEMENTS);
		return elements.length >= MIN_PATH_INFO_SEGMENTS ? elements[1] : StringUtils.EMPTY;
	}

	@Override
	public String extract(final ODataContext context, final String integrationKey)
	{
		return extract(context);
	}
}
