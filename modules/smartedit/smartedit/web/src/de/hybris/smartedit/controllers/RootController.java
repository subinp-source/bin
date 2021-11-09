/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Controller
@Api(tags = "root")
public class RootController
{
	protected static final String SMART_EDIT_ROOT_PAGE = "index";

	@GetMapping(value = "/")
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Get the SmartEdit index page", notes = "Endpoint to retrieve the index page of SmartEdit",
					nickname = "getSmartEditPage")
	public String getSmartEditPage()
	{
		return SMART_EDIT_ROOT_PAGE;
	}
}
