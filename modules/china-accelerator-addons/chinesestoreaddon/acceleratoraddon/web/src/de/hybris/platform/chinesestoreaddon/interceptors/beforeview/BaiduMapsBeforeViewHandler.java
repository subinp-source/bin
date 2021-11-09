/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesestoreaddon.interceptors.beforeview;

import de.hybris.platform.acceleratorservices.config.HostConfigService;
import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;


/**
 * Handler to load Baidu Maps API Keys into the model
 */
public class BaiduMapsBeforeViewHandler implements BeforeViewHandler
{
	@Resource(name = "hostConfigService")
	private HostConfigService hostConfigService;

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
	{
		final String baiduApiKey = getHostConfigService().getProperty("baiduApiKey", request.getServerName());
		if (StringUtils.isNotEmpty(baiduApiKey))
		{
			// Over-write the Google Maps API key in order to reuse its implementation
			modelAndView.addObject("googleApiKey", baiduApiKey);
		}
	}

	protected HostConfigService getHostConfigService()
	{
		return hostConfigService;
	}
}
