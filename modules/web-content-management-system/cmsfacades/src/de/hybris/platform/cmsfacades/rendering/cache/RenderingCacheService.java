/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.cache;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.regioncache.key.CacheKey;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Supplier;


/**
 * Service to cache {@link ItemModel} data representation for rendering.
 * The type T can be represented by any Data or Model type.
 */
public interface RenderingCacheService<T extends Serializable>
{

	/**
	 * Verifies whether the cache is enabled or not.
	 *
	 * @return true if cache is enabled, false otherwise
	 */
	boolean cacheEnabled();

	/**
	 * Returns cached {@link Serializable}.
	 *
	 * @param key
	 * 		the key used to retrieve cached {@link Serializable}
	 * @return optional cached {@link Serializable}.
	 */
	Optional<T> get(CacheKey key);

	/**
	 * Puts the {@link Serializable} into the cache using provided key.
	 *
	 * @param key
	 * 		the key used to save {@link Serializable}
	 * @param item
	 * 		the {@link Serializable} that must be cached.
	 */
	void put(CacheKey key, T item);

	/**
	 * Retrieves the key that must be used to cache {@link Serializable}. The key is retrieved by {@link ItemModel}.
	 *
	 * @param item
	 * 		the {@link ItemModel} for which to retrieve the key.
	 * @return the key
	 */
	Optional<CacheKey> getKey(ItemModel item);


	/**
	 * Methods takes the {@link ItemModel}, verifies that the {@link Serializable} representation is cached and returns it.
	 * If the item is not in the cache the converter is called and {@link Serializable} representation is saved into the cache.
	 *
	 * @param item
	 * 		the {@link ItemModel} to cache.
	 * @param converter
	 * 		the converter to use if the {@link ItemModel} is not in the cache.
	 * @return the {@link Serializable} representation for rendering.
	 */
	T cacheOrElse(final ItemModel item, final Supplier<T> converter);
}
