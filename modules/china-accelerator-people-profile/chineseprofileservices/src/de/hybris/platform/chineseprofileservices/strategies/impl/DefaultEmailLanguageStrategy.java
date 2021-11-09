/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.chineseprofileservices.strategies.impl;

import de.hybris.platform.chineseprofileservices.strategies.EmailLanguageStrategy;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class DefaultEmailLanguageStrategy implements EmailLanguageStrategy
{
	private final ConfigurationService configService;
	private final CommerceCommonI18NService commerceCommonI18NService;
	private final CommonI18NService commonI18NService;
	private static final String DEFAULT_EMAIL_LANGUAGE = "email.language.default";

	private static final Logger LOG = Logger.getLogger(DefaultEmailLanguageStrategy.class.getName());

	public DefaultEmailLanguageStrategy(final ConfigurationService configService,
			final CommerceCommonI18NService commerceCommonI18NService, final CommonI18NService commonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
		this.commonI18NService = commonI18NService;
		this.configService = configService;

	}

	@Override
	public String getDefaultEmailLanguage()
	{
		final String configuredEmailLanguage = configService.getConfiguration().getString(DEFAULT_EMAIL_LANGUAGE);
		if (LOG.isDebugEnabled())
		{
			LOG.info("The configured default email language is:" + configuredEmailLanguage);
		}
		if (!isValidEmailLanguage(configuredEmailLanguage))
		{
			final String currentLanguage = getCommonI18NService().getCurrentLanguage().getIsocode();

			LOG.warn(String.format(
					"The configured default email language '%s' is not supported. The current browsing language '%s' is used.",
					configuredEmailLanguage, currentLanguage));
			return currentLanguage;

		}
		return configuredEmailLanguage;
	}

	protected boolean isValidEmailLanguage(final String language)
	{

		return !(StringUtils.isBlank(language) || getAllLanguages().stream().noneMatch(lang -> language.equals(lang.getIsocode())));
	}

	protected Collection<LanguageModel> getAllLanguages()
	{
		Collection<LanguageModel> languages = getCommerceCommonI18NService().getAllLanguages();
		if (languages.isEmpty())
		{
			languages = getCommonI18NService().getAllLanguages();
		}
		return languages;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}


	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configService;
	}

}
