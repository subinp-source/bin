/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.controllers;

import static de.hybris.platform.smarteditwebservices.constants.SmarteditwebservicesConstants.API_VERSION;

import de.hybris.platform.smarteditwebservices.data.SmarteditLanguageListData;
import de.hybris.platform.smarteditwebservices.data.TranslationMapData;
import de.hybris.platform.smarteditwebservices.i18n.facade.SmarteditI18nFacade;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Controller to retrieve cms management internationalization data
 */
@Controller
@RequestMapping(API_VERSION + "/i18n")
@PreAuthorize("permitAll")
@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
@Api(tags = "languages")
public class I18nController
{
	@Resource
	private SmarteditI18nFacade smarteEditI18nFacade;

	@GetMapping(value = "/translations/{locale}")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Get a Translation Map", notes = "Endpoint to retrieve translated data using the specified locale value")
	public TranslationMapData getTranslationMap( //
			@ApiParam(value = "Locale identifier consisting of a language and region", required = true)
			@PathVariable("locale")
			final Locale locale)
	{
		final TranslationMapData translationMapData = new TranslationMapData();
		translationMapData.setValue(getSmartEditI18nFacade().getTranslationMap(locale));
		return translationMapData;
	}

	@GetMapping(value = "/languages")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Get Languages", notes = "Endpoint to retrieve list of supported languages")
	public SmarteditLanguageListData getToolingLanguages()
	{
		final SmarteditLanguageListData result = new SmarteditLanguageListData();
		result.setLanguages(getSmartEditI18nFacade().getSupportedLanguages());
		return result;
	}

	protected SmarteditI18nFacade getSmartEditI18nFacade()
	{
		return smarteEditI18nFacade;
	}

	public void setSmartEditI18nFacade(final SmarteditI18nFacade cmsI18nFacade)
	{
		this.smarteEditI18nFacade = cmsI18nFacade;
	}
}
