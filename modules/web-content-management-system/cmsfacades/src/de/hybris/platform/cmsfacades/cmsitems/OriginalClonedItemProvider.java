/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems;

import de.hybris.platform.core.model.ItemModel;

/**
 * Interface responsible for storing (in a stack-like data structure) and cloning {@link ItemModel} instances per transaction.
 */
public interface OriginalClonedItemProvider<T extends ItemModel> extends CMSItemContextProvider<T>
{
	@Override
	/**
	 * Initializes and stores a new {@link ItemModel} instance for this transaction.
	 */
	void initializeItem(final T item);

	@Override
	/**
	 * Provides the current {@link ItemModel} instance for this transaction.
	 * @return the current {@link ItemModel}
	 */
	T getCurrentItem();

	@Override
	/**
	 * Finalizes the latest {@link ItemModel} instance for this transaction.
	 */
	void finalizeItem();
}
