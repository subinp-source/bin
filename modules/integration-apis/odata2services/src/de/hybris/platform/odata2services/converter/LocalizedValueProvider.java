/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import de.hybris.platform.integrationservices.item.LocalizedValue;

/**
 * A provider of special localized values, which are platform specific and depend on the platform configuration.
 */
public interface LocalizedValueProvider
{
	/**
	 * Converts attribute value in the {@code ConversionParameters} to a localized value based on the Content-Language of the
	 * request.
	 * @param parameters a conversion context containing a single value for a localized attribute.
	 * @return localized value of the context attribute
	 */
	LocalizedValue toLocalizedValue(ConversionParameters parameters);

	/**
	 * Creates a localized value that sets {@code null} for all languages configured in the platform.
	 * @return {@code null} value for all possible locales.
	 */
	LocalizedValue getNullLocalizedValueForAllLanguages();

	/**
	 * Creates {@code null} localized value depending on the conversion context.
	 * @param parameters parameters providing context for the value conversion.
	 * @return a {@code null} value for a specific locale, if Content-Language is specified in the request; otherwise
	 * creates a {@code null} value for all possible locales.
	 */
	LocalizedValue toNullLocalizedValue(ConversionParameters parameters);
}
