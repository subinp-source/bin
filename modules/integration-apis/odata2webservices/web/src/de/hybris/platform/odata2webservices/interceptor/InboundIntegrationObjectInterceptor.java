/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.interceptor;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.odata2webservices.enums.IntegrationType;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * Intercepts requests into the application and verifies whether the URL contains correct reference to an existing integration object.
 * If the integration object specified in the URL does not exist, 404 Not Found is returned.
 */
public class InboundIntegrationObjectInterceptor implements HandlerInterceptor
{
	private static final int MIN_PATH_INFO_SEGMENTS = 2;
	private static final Logger LOG = Log.getLogger(InboundIntegrationObjectInterceptor.class);
	private IntegrationObjectService integrationObjectService;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object o) throws IOException
	{
		if (isForExistingIntegrationObject(request))
		{
			return true;
		}
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return false;
	}

	private boolean isForExistingIntegrationObject(final HttpServletRequest request)
	{
		final String ioCode = extractCode(request.getPathInfo());
		try
		{
			final IntegrationObjectModel ioModel = getIntegrationObjectService().findIntegrationObject(ioCode);
			return IntegrationType.INBOUND.equals(ioModel.getIntegrationType());
		}
		catch (final ModelNotFoundException | IllegalArgumentException e)
		{
			LOG.trace("IntegrationObject not found for code: '{}'", ioCode, e);
			return false;
		}
	}

	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object o, final ModelAndView modelAndView)
	{
		// not implemented
	}

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object o, final Exception e)
	{
		// not implemented
	}

	protected String extractCode(final String pathInfo)
	{
		if(pathInfo == null)
		{
			return StringUtils.EMPTY;
		}
		final String[] elements = pathInfo.split("/", 3);
		return elements.length >= MIN_PATH_INFO_SEGMENTS ? elements[1] : StringUtils.EMPTY;
	}

	protected IntegrationObjectService getIntegrationObjectService()
	{
		return integrationObjectService;
	}

	@Required
	public void setIntegrationObjectService(final IntegrationObjectService flexibleSearch)
	{
		this.integrationObjectService = flexibleSearch;
	}
}
