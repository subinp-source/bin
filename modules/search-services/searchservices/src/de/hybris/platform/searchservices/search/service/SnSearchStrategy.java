/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service;

import de.hybris.platform.searchservices.search.SnSearchException;


/**
 * Strategy for search requests.
 */
public interface SnSearchStrategy
{
	/**
	 * Executes the search request.
	 *
	 * @param searchRequest
	 *           - the search request
	 *
	 * @return the search response
	 *
	 * @throws SnSearchException
	 *            if an error occurs
	 */
	SnSearchResponse execute(SnSearchRequest searchRequest) throws SnSearchException;
}
