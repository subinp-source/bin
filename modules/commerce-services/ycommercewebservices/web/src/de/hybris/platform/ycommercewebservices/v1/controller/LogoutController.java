/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ycommercewebservices.v1.controller;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.ycommercewebservices.auth.data.LogoutResponse;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller("logoutControllerV1")
@RequestMapping(value = "/customers")
public class LogoutController
{
	/**
	 * Spring security logout successful redirect.
	 */
	@RequestMapping(value = "/current/logout", method = RequestMethod.POST)
	@ResponseBody
	public LogoutResponse logout(final HttpServletRequest request)
	{
		JaloSession.getCurrentSession().close();
		request.getSession().invalidate();
		final LogoutResponse logoutResponse = new LogoutResponse();
		logoutResponse.setSuccess(true);
		return logoutResponse;
	}
}
