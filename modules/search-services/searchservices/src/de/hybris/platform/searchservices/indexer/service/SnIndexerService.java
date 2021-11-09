/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;


import de.hybris.platform.searchservices.indexer.SnIndexerException;

import java.util.List;


/**
 * Service for indexer operations.
 */
public interface SnIndexerService
{
	/**
	 * Creates a full indexer request.
	 *
	 * @param indexTypeId
	 *           - the index type id
	 * @param indexerItemSource
	 *           - the indexer item source
	 *
	 * @return the new indexer request
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	SnIndexerRequest createFullIndexerRequest(String indexTypeId, SnIndexerItemSource indexerItemSource) throws SnIndexerException;

	/**
	 * Creates an incremental indexer request.
	 *
	 * @param indexTypeId
	 *           - the index type id
	 * @param indexerOperationType
	 *           - the indexer operation type
	 * @param indexerItemSourceOperations
	 *           - the indexer item source operations
	 *
	 * @return the new indexer request
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	SnIndexerRequest createIncrementalIndexerRequest(String indexTypeId,
			List<SnIndexerItemSourceOperation> indexerItemSourceOperations) throws SnIndexerException;

	/**
	 * Starts a new indexer operation.
	 *
	 * @param indexerRequest
	 *           - the indexer request
	 *
	 * @return the indexer response
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	SnIndexerResponse index(SnIndexerRequest indexerRequest) throws SnIndexerException;
}
