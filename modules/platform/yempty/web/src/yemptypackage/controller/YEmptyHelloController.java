/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package yemptypackage.controller;

import static yemptypackage.constants.YEmptyConstants.PLATFORM_LOGO_CODE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import yemptypackage.service.YEmptyService;


@Controller
public class YEmptyHelloController
{
	@Autowired
	private YEmptyService yemptyService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome(final ModelMap model)
	{
		model.addAttribute("logoUrl", yemptyService.getHybrisLogoUrl(PLATFORM_LOGO_CODE));
		return "welcome";
	}
}
