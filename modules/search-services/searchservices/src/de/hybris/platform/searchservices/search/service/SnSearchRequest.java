/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service;

import de.hybris.platform.searchservices.core.service.SnRequest;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;


/**
 * Represents a search request.
 */
public interface SnSearchRequest extends SnRequest
{
	/**
	 * Returns the search query.
	 *
	 * @return the search query
	 */
	SnSearchQuery getSearchQuery();
}
