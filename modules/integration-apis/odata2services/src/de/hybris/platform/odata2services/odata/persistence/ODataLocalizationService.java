/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.persistence;

import de.hybris.platform.integrationservices.service.IntegrationLocalizationService;

import java.util.Locale;

/**
 * Defines functionality to handle localization
 *
 * @deprecated use {@link IntegrationLocalizationService}
 */
@Deprecated(since = "1905.11-CEP", forRemoval = true)
public interface ODataLocalizationService
{
	/**
	 * Looks up a locale supported by the Commerce Suite by the standard ISO code, i.e "en"
	 *
	 * @param isoCode The isoCode used to search for an existing LanguageModel
	 * @return The locale
	 * @deprecated Use {@link IntegrationLocalizationService#getSupportedLocaleForLanguageTag(String)} and
	 * {@link IntegrationLocalizationService#validateLocale(Locale)} instead
	 */
	Locale getLocaleForLanguage(final String isoCode);

	/**
	 * Looks up a all locales supported by this Commerce Suite installation.
	 *
	 * @return an array of locales. May return empty if no locales are supported. Will never return null.
	 * @deprecated Use {@link IntegrationLocalizationService#getAllSupportedLocales()} instead
	 */
	Locale[] getSupportedLocales();

	/**
	 * Gets the locale of the Commerce Suite
	 *
	 * @return The locale
	 * @deprecated Use {@link IntegrationLocalizationService#getDefaultLocale()} instead
	 */
	Locale getCommerceSuiteLocale();
}
