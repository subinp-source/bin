/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.service.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.integrationservices.exception.LocaleNotSupportedException;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.service.IntegrationLocalizationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * An implementation of the {@link IntegrationLocalizationService}, which is built on top of {@link de.hybris.platform.servicelayer.i18n.CommonI18NService}.
 */
public class DefaultIntegrationLocalizationService implements IntegrationLocalizationService
{
	private static final Logger LOG = Log.getLogger(DefaultIntegrationLocalizationService.class);
	private CommonI18NService internationalizationService;

	@Override
	public Optional<Locale> getSupportedLocale(final String isoCode)
	{
		final Locale locale = getLocaleForLanguageTag(isoCode);
		return supportedLocale(locale);
	}

	@Override
	public final Locale getSupportedLocaleForLanguageTag(final String isoCode)
	{
		final Locale locale = getLocaleForLanguageTag(isoCode);
		validateLocale(locale);
		return locale;
	}

	private Locale getLocaleForLanguageTag(final String languageTag)
	{
		return getInternationalizationService().getLocaleForIsoCode(languageTag);
	}

	@Override
	public void validateLocale(final Locale locale)
	{
		if (locale == null || supportedLocale(locale).isEmpty())
		{
			throw new LocaleNotSupportedException(locale);
		}
	}

	private Optional<Locale> supportedLocale(final Locale locale)
	{
		try
		{
			getInternationalizationService().getLanguage(locale.getLanguage());
			return Optional.of(locale);
		}
		catch (final UnknownIdentifierException | IllegalArgumentException e)
		{
			LOG.debug("{} is not supported by the platform", locale.getLanguage(), e);
			return Optional.empty();
		}
	}

	@Override
	public Collection<Locale> getAllSupportedLocales()
	{
		return internationalizationService.getAllLanguages().stream()
		                                  .map(internationalizationService::getLocaleForLanguage)
		                                  .collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the current language locale in the commerce suite, i.e. {@link CommonI18NService#getCurrentLanguage()} or
	 * {@link Locale#ENGLISH}, if current language in the system is {@code null}.
	 */
	@Override
	public Locale getDefaultLocale()
	{
		final LanguageModel language = internationalizationService.getCurrentLanguage();
		return language != null
				? internationalizationService.getLocaleForLanguage(language)
				: Locale.ENGLISH;
	}


	protected CommonI18NService getInternationalizationService()
	{
		return internationalizationService;
	}

	@Required
	public void setInternationalizationService(final CommonI18NService service)
	{
		internationalizationService = service;
	}
}
