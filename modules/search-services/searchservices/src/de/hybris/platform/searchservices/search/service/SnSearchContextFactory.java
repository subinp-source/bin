/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service;

import de.hybris.platform.searchservices.index.service.SnIndexContext;
import de.hybris.platform.searchservices.search.SnSearchException;


/**
 * Implementations of this interface are responsible for creating instances of {@link SnSearchContext}.
 */
public interface SnSearchContextFactory
{
	/**
	 * Creates a new instance of {@link SnIndexContext}.
	 *
	 * @param searchRequest
	 *           - the search request
	 *
	 * @return the new instance of {@link SnIndexContext}
	 *
	 * @throws SnSearchException
	 *            if an error occurs
	 */
	SnSearchContext createSearchContext(SnSearchRequest searchRequest) throws SnSearchException;
}
