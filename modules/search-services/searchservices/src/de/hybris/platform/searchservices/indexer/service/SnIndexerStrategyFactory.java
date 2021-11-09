/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.suggest.service.SnSuggestStrategy;


/**
 * Implementations of this interface are responsible for getting the applicable instance of {@link SnSuggestStrategy}.
 */
public interface SnIndexerStrategyFactory
{
	/**
	 * Returns an instance of {@link SnIndexerStrategy}.
	 *
	 * @param indexerRequest
	 *           - the indexer request
	 *
	 * @return the indexer strategy
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 *
	 */
	SnIndexerStrategy getIndexerStrategy(SnIndexerRequest indexerRequest) throws SnIndexerException;
}
