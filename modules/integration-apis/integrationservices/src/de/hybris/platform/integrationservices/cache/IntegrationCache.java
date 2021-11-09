/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.integrationservices.cache;

/**
 * Defines operations on a cache
 * @param <K> Type of the cache key
 * @param <T> Type of the cached item
 */
public interface IntegrationCache<K extends IntegrationCacheKey, T>
{
	/**
	 * Gets the item from the cache by the key
	 * @param key Key to the cached item
	 * @return The object if the key exists, otherwise null
	 */
	T get(K key);

	/**
	 * Puts the item in the cache, referenced by the key
	 * @param key Key to the cached item
	 * @param item Item to cache
	 */
	void put(K key, T item);

	/**
	 * Removes the item from the cache by the key
	 * @param key Key to the item to invalidate
	 * @return The invalidated item, otherwise null
	 */
	T remove(K key);

	/**
	 * Indicates whether the key exists in the cache
	 * @param key Key to test for existence
	 * @return True if the key exists, otherwise false
	 */
	boolean contains(K key);
}
