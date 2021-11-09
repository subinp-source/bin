/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.frontend.interceptor;

import de.hybris.platform.addonsupport.interceptors.BeforeControllerHandlerAdaptee;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.sap.productconfig.facades.ConfigurationExpertModeFacade;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

public class ProductConfigurationBeforeControllerHandler implements BeforeControllerHandlerAdaptee
{

	private ConfigurationExpertModeFacade configurationExpertModeFacade;

	@Override
	public boolean beforeController(final HttpServletRequest request, final HttpServletResponse response, final HandlerMethod handler) throws Exception
	{
		if (isGetMethod(request) && isProductConfigCall(request))
		{
			handleExpertMode(request.getQueryString());
		}

		return true;
	}

	protected boolean isProductConfigCall(final HttpServletRequest request)
	{
		return request.getRequestURI().contains(ConfiguratorType.CPQCONFIGURATOR.toString());
	}

	protected boolean isGetMethod(final HttpServletRequest request)
	{
		return RequestMethod.GET.name().equalsIgnoreCase(request.getMethod());
	}

	protected void handleExpertMode(final String queryString)
	{
		if (StringUtils.isNotEmpty(queryString))
		{
			if (isExpMode(queryString, "true"))
			{
				getConfigurationExpertModeFacade().enableExpertMode();
			}
			else if (isExpMode(queryString, "false"))
			{
				getConfigurationExpertModeFacade().disableExpertMode();
			}
		}
	}

	protected boolean isExpMode(final String queryString, final String status)
	{
		return queryString.toLowerCase(Locale.ENGLISH).contains("expmode=" + status);
	}


	protected ConfigurationExpertModeFacade getConfigurationExpertModeFacade()
	{
		return configurationExpertModeFacade;
	}

	/**
	 * Facade to support expert mode handling.
	 *
	 * @param configurationExpertModeFacade The expert mode facade
	 */
	@Required
	public void setConfigurationExpertModeFacade(final ConfigurationExpertModeFacade configurationExpertModeFacade)
	{
		this.configurationExpertModeFacade = configurationExpertModeFacade;
	}

}
