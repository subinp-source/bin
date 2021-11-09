/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

/**
 * Represents an indexer batch request.
 */
public interface SnIndexerBatchRequest extends SnIndexerRequest
{
	/**
	 * Returns the index id.
	 *
	 * @return the index id
	 */
	String getIndexId();

	/**
	 * Returns the indexer operation type.
	 *
	 * @return the indexer operation type
	 */
	String getIndexerOperationId();

	/**
	 * Returns the indexer batch id.
	 *
	 * @return the indexer batch id
	 */
	String getIndexerBatchId();
}
