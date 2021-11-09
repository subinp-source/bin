/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.itemcollector;

import de.hybris.platform.core.model.ItemModel;

import java.util.Optional;

/**
 * Generic Item Collector Registry interface that should be responsible for managing (memory cache) and retrieving the corresponding 
 * {@link ItemCollector} for a given {@link ItemModel}. 
 */
public interface ItemCollectorRegistry
{
	/**
	 * Returns the corresponding {@link ItemCollector} for a given {@link ItemModel}
	 * @param itemModel the item model that will be used to retrieve the {@link ItemCollector} instance. 
	 * @return the {@code Optional<ItemCollector>} object for the given item model passed.   
	 */
	Optional<ItemCollector> getItemCollector(final ItemModel itemModel);
}
