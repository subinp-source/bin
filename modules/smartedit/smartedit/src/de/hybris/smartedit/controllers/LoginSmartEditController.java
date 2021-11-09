/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.controllers;

import static de.hybris.smartedit.controllers.Page.LOGIN_PAGE;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @deprecated since 2005, no longer needed.
 */
@Deprecated(since = "2005", forRemoval = true)
@Controller
public class LoginSmartEditController
{

	/**
	 * @deprecated since 2005, no longer needed.
	 */
	@GetMapping(value = "/login")
	@Deprecated(since = "2005", forRemoval = true)
	public String getLoginPage()
	{
		return LOGIN_PAGE.getViewName();
	}

}