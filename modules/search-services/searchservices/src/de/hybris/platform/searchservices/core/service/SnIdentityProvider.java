/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.indexer.SnIndexerException;


/**
 * Creates identifiers for items.
 */
public interface SnIdentityProvider<T extends ItemModel>
{
	/**
	 * Provides a unique identifier for the given item.
	 *
	 * @param context
	 *           - the context
	 * @param item
	 *           - the item
	 *
	 * @return the identifier
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	String getIdentifier(SnContext context, T item) throws SnException;
}
