/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.languages.impl;

import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link LanguageFacade}.
 */
public class DefaultLanguageFacade implements LanguageFacade
{
	private StoreSessionFacade storeSessionFacade;

	@Override
	public List<LanguageData> getLanguages()
	{
		final List<LanguageData> languageDataList = new ArrayList<>();

		final LanguageData defaultLanguage = getStoreSessionFacade().getDefaultLanguage();

		for (final LanguageData language : getStoreSessionFacade().getAllLanguages())
		{
			if (defaultLanguage.getIsocode().equalsIgnoreCase(language.getIsocode()))
			{
				language.setRequired(true);
				languageDataList.add(0, language);
			}
			else
			{
				languageDataList.add(language);
			}
		}

		return languageDataList;
	}

	protected StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	@Required
	public void setStoreSessionFacade(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}
}
