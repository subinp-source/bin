/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service;

import de.hybris.platform.searchservices.suggest.SnSuggestException;

/**
 * Strategy for suggest requests.
 */
public interface SnSuggestStrategy
{
	/**
	 * Executes the suggest request.
	 *
	 * @param suggestRequest
	 *           - the suggest request
	 *
	 * @return the suggest response
	 *
	 * @throws SnSuggestException
	 *            if an error occurs
	 */
	SnSuggestResponse execute(SnSuggestRequest suggestRequest) throws SnSuggestException;
}
