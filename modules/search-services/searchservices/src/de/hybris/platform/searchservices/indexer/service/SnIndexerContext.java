/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.enums.SnIndexerOperationType;
import de.hybris.platform.searchservices.index.service.SnIndexContext;

import java.util.List;


/**
 * Represents an indexer context.
 */
public interface SnIndexerContext extends SnIndexContext
{
	/**
	 * Returns the indexer request.
	 *
	 * @return the indexer request
	 */
	SnIndexerRequest getIndexerRequest();

	/**
	 * Returns the indexer response.
	 *
	 * @return the indexer response
	 */
	SnIndexerResponse getIndexerResponse();

	/**
	 * Sets the indexer response.
	 *
	 * @param indexerResponse
	 *           - the indexer response
	 */
	void setIndexerResponse(final SnIndexerResponse indexerResponse);

	/**
	 * Returns the indexer operation type.
	 *
	 * @return the indexer operation type
	 */
	SnIndexerOperationType getIndexerOperationType();

	/**
	 * Returns the indexer item source operations.
	 *
	 * @return the indexer item source operations
	 */
	List<SnIndexerItemSourceOperation> getIndexerItemSourceOperations();

	/**
	 * Returns the indexer operation id.
	 *
	 * @return the indexer operation id
	 */
	String getIndexerOperationId();

	/**
	 * Sets the indexer operation id.
	 *
	 * @param indexerOperationId
	 *           - the indexer operation id
	 */
	void setIndexerOperationId(final String indexerOperationId);
}
