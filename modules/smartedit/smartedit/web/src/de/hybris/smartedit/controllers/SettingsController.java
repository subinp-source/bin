/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.controllers;

import de.hybris.smartedit.facade.SettingsFacade;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * Unauthenticated controller returning a map of non-protected settings necessary for front-end to self-configure
 * <p>
 */
@RestController("settingsController")
@RequestMapping("/settings")
@Api(tags = "settings")
public class SettingsController
{
	@Resource(name = "smarteditSettingsFacade")
	SettingsFacade settingsFacade;

	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Get a map of application settings", notes = "Endpoint to retrieve a map of non-protected settings necessary for front-end to self-configure",
					nickname = "getSettings")
	public Map<String, Object> getSettings()
	{
		return settingsFacade.getSettings();
	}
}
