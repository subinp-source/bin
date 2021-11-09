/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservices.core.context;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.commercewebservices.core.exceptions.InvalidResourceException;
import de.hybris.platform.commercewebservices.core.exceptions.RecalculationException;
import de.hybris.platform.commercewebservices.core.exceptions.UnsupportedCurrencyException;
import de.hybris.platform.commercewebservices.core.exceptions.UnsupportedLanguageException;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface for context information loader
 */
public interface ContextInformationLoader
{

	/**
	 * Method set current language base on information from request
	 *
	 * @param request
	 * 		- request from which we should get language information
	 * @return language set as current
	 * @throws UnsupportedLanguageException
	 */
	LanguageModel setLanguageFromRequest(final HttpServletRequest request) throws UnsupportedLanguageException;

	/**
	 * Method set current currency based on information from request and recalculate cart for current session
	 *
	 * @param request
	 * 		- request from which we should get currency information
	 * @return currency set as current
	 * @throws UnsupportedCurrencyException
	 * @throws RecalculationException
	 */
	CurrencyModel setCurrencyFromRequest(final HttpServletRequest request)
			throws UnsupportedCurrencyException, RecalculationException;

}
