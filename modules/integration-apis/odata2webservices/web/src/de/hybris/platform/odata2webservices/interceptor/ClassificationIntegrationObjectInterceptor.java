/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.interceptor;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * An interceptor that catches POST and PATCH requests. It verifies the Integration Object
 * does not contain classification attributes before letting the request through.
 * This interceptor will be removed once POST and PATCH for classification attributes are implemented.
 */
public final class ClassificationIntegrationObjectInterceptor implements HandlerInterceptor
{
	private static final Logger LOG = Log.getLogger(ClassificationIntegrationObjectInterceptor.class);
	private static final int MIN_PATH_INFO_SEGMENTS = 2;

	private IntegrationObjectService integrationObjectService;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
	{
		if (isPatch(request) && integrationObjectContainsClassificationAttributes(request))
		{
			throw new UpsertIntegrationObjectWithClassificationAttributeException(request.getMethod());
		}
		return true;
	}

	private boolean isPatch(final HttpServletRequest request)
	{
		return "patch".equalsIgnoreCase(request.getMethod());
	}

	private boolean integrationObjectContainsClassificationAttributes(final HttpServletRequest request)
	{
		final String ioCode = extractCode(request.getPathInfo());
		try
		{
			final IntegrationObjectModel ioModel = integrationObjectService.findIntegrationObject(ioCode);
			return ioModel.getClassificationAttributesPresent();
		}
		catch (final ModelNotFoundException | IllegalArgumentException e)
		{
			LOG.trace("IntegrationObject not found for code: '{}'", ioCode, e);
			return false;
		}
	}

	private String extractCode(final String pathInfo)
	{
		if (pathInfo == null)
		{
			return StringUtils.EMPTY;
		}
		final String[] elements = pathInfo.split("/", 3);
		return elements.length >= MIN_PATH_INFO_SEGMENTS ? elements[1] : StringUtils.EMPTY;
	}

	@Required
	public void setIntegrationObjectService(final IntegrationObjectService integrationObjectService)
	{
		this.integrationObjectService = integrationObjectService;
	}
}
