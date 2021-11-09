/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.indexer.SnIndexerException;

/**
 * Listener for indexer operations.
 */
public interface SnIndexerListener
{
	/**
	 * Handles a notification that the indexer operation is about to begin execution.
	 *
	 * @param context
	 *           - the indexer context
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	void beforeIndex(SnIndexerContext context) throws SnIndexerException;

	/**
	 * Handles a notification that the indexer operation has just completed.
	 *
	 * @param context
	 *           - the indexer context
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	void afterIndex(SnIndexerContext context) throws SnIndexerException;

	/**
	 * Handles a notification that the indexer operation failed (this may also be due to listeners failing).
	 *
	 * @param context
	 *           - the indexer context
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	void afterIndexError(SnIndexerContext context) throws SnIndexerException;
}
