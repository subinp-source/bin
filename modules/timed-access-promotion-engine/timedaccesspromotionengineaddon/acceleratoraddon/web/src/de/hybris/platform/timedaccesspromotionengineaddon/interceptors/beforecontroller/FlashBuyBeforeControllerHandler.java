/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionengineaddon.interceptors.beforecontroller;

import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeControllerHandler;
import de.hybris.platform.timedaccesspromotionenginefacades.FlashBuyFacade;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;


/**
 * Sets the cart flash buy status
 */
public class FlashBuyBeforeControllerHandler implements BeforeControllerHandler
{
	@Resource(name = "flashBuyFacade")
	private FlashBuyFacade flashBuyFacade;

	/**
	 * Updates the flash buy status for cart when placing order
	 *
	 * @param request
	 *           the http request
	 * @param response
	 *           the http response
	 * @param handler
	 *           Handler method
	 * @return true if the execution chain should proceed with the next interceptor or the handler itself, else
	 *         DispatcherServlet assumes that this interceptor has already dealt with the response itself
	 * @throws Exception
	 *            throw Exception when updating flash buy status failed
	 * 
	 */
	@Override
	public boolean beforeController(final HttpServletRequest request, final HttpServletResponse response,
			final HandlerMethod handler)
	{
		final String url = request.getRequestURI();
		if (url.contains("/checkout/multi/summary/placeOrder"))
		{
			flashBuyFacade.updateFlashBuyStatusForCart();
		}
		return true;
	}

}
