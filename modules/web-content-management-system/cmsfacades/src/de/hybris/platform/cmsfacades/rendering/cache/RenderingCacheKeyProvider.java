/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.cache;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.regioncache.key.CacheKey;

import java.util.Optional;


/**
 * Provides a cache key for {@link ItemModel}
 *
 * @param <T>
 * 		the subtype of the {@link ItemModel}
 */
public interface RenderingCacheKeyProvider<T extends ItemModel>
{
	/**
	 * Returns the cache key.
	 *
	 * @param item
	 * 		the item to cache.
	 * @return the optional key for the item. It returns Optional.empty()
	 * if the cache key provider is not available for the item type.
	 */
	Optional<CacheKey> getKey(T item);
}
