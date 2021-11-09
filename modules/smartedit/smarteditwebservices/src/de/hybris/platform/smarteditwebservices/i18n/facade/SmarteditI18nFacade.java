/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.i18n.facade;

import de.hybris.platform.smarteditwebservices.data.SmarteditLanguageData;

import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Facade to retrieve internationalization data
 */
public interface SmarteditI18nFacade
{
	/**
	 * Retrieve the translation keys and the translated values for the given locale.
	 *
	 * @param locale locale
	 * @return Map of attribute key and translated values
	 */
	Map<String, String> getTranslationMap(Locale locale);

	/**
	 * Retrieve the supported smartedit languages
	 *
	 * @return a {@link List} of {@link SmarteditLanguageData}
	 */
	List<SmarteditLanguageData> getSupportedLanguages();
}
