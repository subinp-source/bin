/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.converter.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.searchservices.admin.data.SnCurrency;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnLanguage;
import de.hybris.platform.searchservices.model.AbstractSnSearchProviderConfigurationModel;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderFactory;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderMapping;
import de.hybris.platform.searchservices.util.ConverterUtils;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates {@link SnIndexConfiguration} from {@link SnIndexConfigurationModel}.
 */
public class SnIndexConfigurationPopulator implements Populator<SnIndexConfigurationModel, SnIndexConfiguration>
{
	private I18NService i18NService;
	private Converter<LanguageModel, SnLanguage> snLanguageConverter;
	private Converter<CurrencyModel, SnCurrency> snCurrencyConverter;
	private SnSearchProviderFactory snSearchProviderFactory;

	@Override
	public void populate(final SnIndexConfigurationModel source, final SnIndexConfiguration target)
	{
		target.setId(source.getId());
		target.setName(buildLocalizedName(source));

		final UserModel user = source.getUser();
		if (user != null)
		{
			target.setUser(user.getUid());
		}

		target.setLanguages(ConverterUtils.convertAll(source.getLanguages(), snLanguageConverter::convert));
		target.setCurrencies(ConverterUtils.convertAll(source.getCurrencies(), snCurrencyConverter::convert));
		target.setListeners(ConverterUtils.convertAll(source.getListeners(), listener -> listener));
		target.setSynonymDictionaryIds(
				ConverterUtils.convertAll(source.getSynonymDictionaries(), dictionary -> dictionary.getId()));

		final AbstractSnSearchProviderConfigurationModel searchProviderConfiguration = source.getSearchProviderConfiguration();
		if (searchProviderConfiguration != null)
		{
			final SnSearchProviderMapping searchProviderMapping = snSearchProviderFactory
					.getSearchProviderMappingForConfigurationModel(searchProviderConfiguration);
			target.setSearchProviderConfiguration(searchProviderMapping.getLoadStrategy().load(searchProviderConfiguration));
		}
	}

	protected Map<Locale, String> buildLocalizedName(final SnIndexConfigurationModel source)
	{
		final Set<Locale> supportedLocales = i18NService.getSupportedLocales();
		final Map<Locale, String> target = new LinkedHashMap<>();

		for (final Locale locale : supportedLocales)
		{
			target.put(locale, source.getName(locale));
		}

		return target;
	}

	public I18NService getI18NService()
	{
		return i18NService;
	}

	@Required
	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
	}

	public Converter<LanguageModel, SnLanguage> getSnLanguageConverter()
	{
		return snLanguageConverter;
	}

	@Required
	public void setSnLanguageConverter(final Converter<LanguageModel, SnLanguage> snLanguageConverter)
	{
		this.snLanguageConverter = snLanguageConverter;
	}

	public Converter<CurrencyModel, SnCurrency> getSnCurrencyConverter()
	{
		return snCurrencyConverter;
	}

	@Required
	public void setSnCurrencyConverter(final Converter<CurrencyModel, SnCurrency> snCurrencyConverter)
	{
		this.snCurrencyConverter = snCurrencyConverter;
	}

	public SnSearchProviderFactory getSnSearchProviderFactory()
	{
		return snSearchProviderFactory;
	}

	@Required
	public void setSnSearchProviderFactory(final SnSearchProviderFactory snSearchProviderFactory)
	{
		this.snSearchProviderFactory = snSearchProviderFactory;
	}
}
