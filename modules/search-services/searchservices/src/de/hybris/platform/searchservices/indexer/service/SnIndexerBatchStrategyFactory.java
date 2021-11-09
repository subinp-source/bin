/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.indexer.SnIndexerException;

/**
 * Implementations of this interface are responsible for getting the applicable instance of
 * {@link SnIndexerBatchStrategy}.
 */
public interface SnIndexerBatchStrategyFactory
{
	/**
	 * Returns an instance of {@link SnIndexerBatchStrategy}.
	 *
	 * @param indexerRequest
	 *           - the indexer request
	 *
	 * @return the indexer batch strategy
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 *
	 */
	SnIndexerBatchStrategy getIndexerBatchStrategy(SnIndexerRequest indexerRequest);
}
