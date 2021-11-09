/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service;

import de.hybris.platform.searchservices.core.service.SnResponse;
import de.hybris.platform.searchservices.search.data.SnSearchResult;


/**
 * Represents a search response.
 */
public interface SnSearchResponse extends SnResponse
{
	/**
	 * Returns the search result.
	 *
	 * @return the search result
	 */
	SnSearchResult getSearchResult();
}
