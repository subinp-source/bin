/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service;

import de.hybris.platform.searchservices.search.SnSearchException;


/**
 * Implementations of this interface are responsible for getting the applicable instance of {@link SnSearchStrategy}.
 */
public interface SnSearchStrategyFactory
{
	/**
	 * Returns an instance of {@link SnSearchStrategy}.
	 *
	 * @param searchRequest
	 *           - the search request
	 *
	 * @return the search strategy
	 *
	 * @throws SnSearchException
	 *            if an error occurs
	 *
	 */
	SnSearchStrategy getSearchStrategy(SnSearchRequest searchRequest) throws SnSearchException;
}
