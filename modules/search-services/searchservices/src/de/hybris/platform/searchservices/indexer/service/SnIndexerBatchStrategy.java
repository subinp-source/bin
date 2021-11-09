/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.indexer.SnIndexerException;

/**
 * Strategy for indexer requests.
 */
public interface SnIndexerBatchStrategy
{
	/**
	 * Executes the indexer request.
	 *
	 * @param indexerRequest
	 *           - the indexer request
	 *
	 * @return the indexer batch response
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	SnIndexerBatchResponse execute(SnIndexerBatchRequest indexerBatchRequest) throws SnIndexerException, InterruptedException;
}
