/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service;


import de.hybris.platform.searchservices.search.SnSearchException;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;


/**
 * Service for search operations.
 */
public interface SnSearchService
{
	/**
	 * Creates a search request.
	 *
	 * @param indexTypeId
	 *           - the index type id
	 * @param searchQuery
	 *           - the search query
	 *
	 * @return the new search request
	 */
	SnSearchRequest createSearchRequest(String indexTypeId, SnSearchQuery searchQuery);

	/**
	 * Starts a new search operation.
	 *
	 * @param searchRequest
	 *           - the search request
	 *
	 * @return the search response
	 *
	 * @throws SnSearchException
	 *            if an error occurs
	 */
	SnSearchResponse search(SnSearchRequest searchRequest) throws SnSearchException;
}
