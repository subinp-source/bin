/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems;

/**
 * Interface responsible for storing (in a stack-like data structure) context information per transaction.
 */
public interface CMSItemContextProvider<T>
{
	/**
	 * Initializes and stores a new instance for this transaction.
	 *
	 * @param item
	 *           the value to store
	 */
	void initializeItem(final T item);

	/**
	 * Provides the current instance for this transaction.
	 *
	 * @return the current item
	 */
	T getCurrentItem();

	/**
	 * Finalizes the latest instance for this transaction.
	 */
	void finalizeItem();
}
