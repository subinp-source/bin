/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.sync.selective;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;


public class LanguageHelper
{
	private CommonI18NService commonI18NService;
	private ModelService modelService;
	private List<Locale> suportetLocales;

	public LanguageHelper(final CommonI18NService commonI18NService, final ModelService modelService)
	{
		this.commonI18NService = commonI18NService;
		this.modelService = modelService;

		suportetLocales = new ArrayList<>(3);
		suportetLocales.add(Locale.ENGLISH);
		suportetLocales.add(Locale.FRENCH);
		suportetLocales.add(Locale.forLanguageTag("pl"));
	}

	public List<LanguageModel> getLanguages()
	{
		return suportetLocales.stream().map(this::getOrCreateLanguageModel).collect(Collectors.toList());
	}

	public Set<Language> getJaloLanguages()
	{
		return getLanguages().stream().map(modelService::getSource).map(Language.class::cast).collect(Collectors.toSet());
	}

	protected LanguageModel getOrCreateLanguageModel(final Locale isoCode)
	{
		try
		{
			return commonI18NService.getLanguage(isoCode.getLanguage());
		}
		catch (final Exception e)
		{

			final LanguageModel langModel = modelService.create(LanguageModel.class);
			langModel.setIsocode(isoCode.getLanguage());
			modelService.save(langModel);
			return langModel;
		}
	}
}

