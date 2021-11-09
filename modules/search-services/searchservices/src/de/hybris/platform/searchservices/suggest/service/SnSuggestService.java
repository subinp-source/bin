/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service;


import de.hybris.platform.searchservices.suggest.SnSuggestException;
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery;


/**
 * Service for suggest operations.
 */
public interface SnSuggestService
{
	/**
	 * Creates a suggest request.
	 *
	 * @param indexTypeId
	 *           - the index type id
	 * @param suggestQuery
	 *           - the suggest query
	 *
	 * @return the new suggest request
	 */
	SnSuggestRequest createSuggestRequest(String indexTypeId, SnSuggestQuery suggestQuery);

	/**
	 * Starts a new suggest operation.
	 *
	 * @param suggestRequest
	 *           - the suggest request
	 *
	 * @return the suggest response
	 *
	 * @throws SnSuggestException
	 *            if an error occurs
	 */
	SnSuggestResponse suggest(SnSuggestRequest suggestRequest) throws SnSuggestException;
}
