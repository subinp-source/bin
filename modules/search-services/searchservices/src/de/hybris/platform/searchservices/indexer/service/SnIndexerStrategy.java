/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.indexer.SnIndexerException;

/**
 * Strategy for indexer batch requests.
 */
public interface SnIndexerStrategy
{
	/**
	 * Executes the indexer batch request.
	 *
	 * @param indexerRequest
	 *           - the indexer request
	 *
	 * @return the indexer response
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	SnIndexerResponse execute(SnIndexerRequest indexerRequest) throws SnIndexerException;
}
