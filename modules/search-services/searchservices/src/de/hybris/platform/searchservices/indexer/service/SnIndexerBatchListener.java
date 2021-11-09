/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.indexer.SnIndexerException;

/**
 * Listener for indexer batch operations.
 */
public interface SnIndexerBatchListener
{
	/**
	 * Handles a notification that the indexer batch operation is about to begin execution.
	 *
	 * @param context
	 *           - the indexer context
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	void beforeIndexBatch(SnIndexerContext context) throws SnIndexerException;

	/**
	 * Handles a notification that the indexer batch operation has just completed.
	 *
	 * @param context
	 *           - the indexer context
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	void afterIndexBatch(SnIndexerContext context) throws SnIndexerException;

	/**
	 * Handles a notification that the indexer batch operation failed (this may also be due to listeners failing).
	 *
	 * @param context
	 *           - the indexer context
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	void afterIndexBatchError(SnIndexerContext context) throws SnIndexerException;
}
