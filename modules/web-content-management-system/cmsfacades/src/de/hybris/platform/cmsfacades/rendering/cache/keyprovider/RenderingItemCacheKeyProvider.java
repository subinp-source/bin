/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.cache.keyprovider;

import de.hybris.platform.core.model.ItemModel;


/**
 * Key provider for the {@link ItemModel}.
 *
 * @param <T>
 */
public interface RenderingItemCacheKeyProvider<T extends ItemModel>
{
	/**
	 * Returns key for the provided {@link ItemModel}
	 *
	 * @param item
	 * 		the {@link ItemModel}
	 * @return the key.
	 */
	String getKey(T item);
}
