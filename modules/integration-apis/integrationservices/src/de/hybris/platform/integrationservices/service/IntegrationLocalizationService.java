/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.service;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/**
 * A service for determining locale specific information for the integration purposes.
 */
public interface IntegrationLocalizationService
{
	/**
	 * Retrieves a locale supported by the platform, which corresponds to the specified ISO code.
	 * @param isoCode an <a href=https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes">ISO code</a>, for which a {@code Locale}
	 * should be retrieved.
	 * @return an {@code Optional} containing the {@code Locale}, if the ISO code is valid and is supported by the platform; otherwise,
	 * {@code Optional.empty()} is returned.
	 */
	Optional<Locale> getSupportedLocale(final String isoCode);

	/**
	 * Retrieves all locales supported by the platform
	 * @return a list of all locales, which could be used with integration objects data.
	 */
	Collection<Locale> getAllSupportedLocales();

	/**
	 * Retrieves a locale to be used when the locale is not explicitly specified for the incoming/outgoing integration data.
	 * @return a default locale to use when no explicit locale specified.
	 */
	Locale getDefaultLocale();

	/**
	 * Validates the locale language matches corresponds to an existing LanguageModel.
	 *
	 * @param locale - the locale that is being validated
	 */
	void validateLocale(Locale locale);

	/**
	 * Creates a Locale for the provided language tag, and verifies that the locale is supported.
	 * Implementations are expected to throw an exception if the langaugeTag is not supported.
	 * @param languageTag - language tag that holds information used to construct a Locale
	 * @return - a supported Locale for the languageTag
	 */
	Locale getSupportedLocaleForLanguageTag(String languageTag);
}
