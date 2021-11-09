/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.languages;

import de.hybris.platform.commercefacades.storesession.data.LanguageData;

import java.util.List;


/**
 * Language facade interface which deals with methods related to language operations.
 */
public interface LanguageFacade
{

	/**
	 * Get all languages for the current active site. The languages are ordered starting with the default language.<br>
	 * For example: Given a site supports [ "EN", "DE", "JP", "IT" ] and <i>German</i> is the default language, the
	 * resulted list would be ordered: [ "<b>DE</b>", "EN", "JP", "IT" ]
	 *
	 * @param siteId
	 *           the current cmsSite-id.
	 * @return list of languages.
	 */
	List<LanguageData> getLanguages();

}
