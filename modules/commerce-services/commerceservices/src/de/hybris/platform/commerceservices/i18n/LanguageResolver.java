/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.i18n;

import de.hybris.platform.core.model.c2l.LanguageModel;


/**
 * Abstraction for the resolving mechanism of Language for a given iso code.
 */
public interface LanguageResolver
{
	LanguageModel getLanguage(String isoCode);
}
