/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service;

import de.hybris.platform.searchservices.index.service.SnIndexContext;


/**
 * Represents a search context.
 */
public interface SnSearchContext extends SnIndexContext
{
	/**
	 * Returns the search request.
	 */
	SnSearchRequest getSearchRequest();

	/**
	 * Returns the search response.
	 */
	SnSearchResponse getSearchResponse();

	/**
	 * Sets the search response.
	 *
	 * @param searchResponse
	 *           - the search response
	 */
	void setSearchResponse(final SnSearchResponse searchResponse);
}
