/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import de.hybris.platform.integrationservices.item.LocalizedValue;
import de.hybris.platform.integrationservices.service.IntegrationLocalizationService;

import java.util.Locale;

import javax.validation.constraints.NotNull;

/**
 * Default implementation of the {@link LocalizedValueProvider}
 */
public class DefaultLocalizedValueProvider implements LocalizedValueProvider
{
	private final IntegrationLocalizationService localizationService;

	public DefaultLocalizedValueProvider(@NotNull final IntegrationLocalizationService service)
	{
		localizationService = service;
	}

	@Override
	public final LocalizedValue toLocalizedValue(final ConversionParameters parameters)
	{
		return asLocalizedValue(parameters.getContentLocale(), parameters.getAttributeValue());
	}

	@Override
	public final LocalizedValue getNullLocalizedValueForAllLanguages()
	{
		return localizationService.getAllSupportedLocales().stream()
		                          .map(lang -> asLocalizedValue(lang, null))
		                          .reduce(LocalizedValue.EMPTY, LocalizedValue::combine);
	}

	@Override
	public final LocalizedValue toNullLocalizedValue(final ConversionParameters parameters)
	{
		return parameters.isContentLanguagePresent()
				? asLocalizedValue(parameters.getContentLocale(), null)
				: getNullLocalizedValueForAllLanguages();
	}

	private LocalizedValue asLocalizedValue(final Locale contentLocale, final Object attributeValue)
	{
		return LocalizedValue.of(contentLocale, attributeValue);
	}
}
