/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.cache.impl;

import de.hybris.platform.integrationservices.cache.IntegrationCache;
import de.hybris.platform.integrationservices.cache.IntegrationCacheKey;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;

import java.util.function.Function;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * {@inheritDoc}
 * <p>Base implementation of the {@link de.hybris.platform.integrationservices.cache.IntegrationCache} to be extended
 * for specific generic values.</p>
 */
public abstract class BaseIntegrationCache<K extends IntegrationCacheKey, T> implements IntegrationCache<K, T>
{
	private static final Logger LOG = Log.getLogger(BaseIntegrationCache.class);
	private CacheRegion cacheRegion;

	@Override
	public T get(final K key)
	{
		if (key != null)
		{
			log().debug("Getting cached value for '{}'", key);
			final T value = resolveCachedValue(key, getCache()::get);
			log().debug("Got '{}' for '{}'", value, key);
			return value;
		}
		log().warn("Not getting cached value because key is null");
		return null;
	}

	@Override
	public void put(final K key, final T item)
	{
		if (key != null)
		{
			log().debug("Caching '{}' for '{}'", item, key);
			cacheRegion.getWithLoader(toCacheKey(key), load(item));
		}
		else
		{
			log().warn("Not placing the value into cache because key is null");
		}
	}

	@Override
	public T remove(final K key)
	{
		if (key != null)
		{
			log().debug("Removing cached value for {}", key);
			return resolveCachedValue(key, k -> getCache().invalidate(toCacheKey(key), false));
		}
		log().warn("Not removing the cached value because key is null");
		return null;
	}

	@Override
	public boolean contains(final K key)
	{
		return key != null && cacheRegion.containsKey(toCacheKey(key));
	}

	private T resolveCachedValue(final K key, final Function<CacheKey, Object> cacheOperation)
	{
		final Object item = cacheOperation.apply(toCacheKey(key));
		return getValueType().isInstance(item) ? getValueType().cast(item) : null;
	}

	/**
	 * Converts the {@link IntegrationCacheKey} specific to the implementation to a {@link CacheKey}
	 * @param key an integration key to convert
	 * @return converted cache key
	 */
	protected abstract CacheKey toCacheKey(final K key);

	/**
	 * Retrieves concrete class of the cached values.
	 * @return class of the values stored in the cache.
	 */
	protected abstract Class<T> getValueType();

	/**
	 * Returns a cache value loader for the specified item. This implementation simply loads the specified item without any changes
	 * or transformations done to it.
	 * @param item an item to be placed into the cache.
	 * @return a loader creating a value to be placed in the cache.
	 */
	protected CacheValueLoader load(final T item)
	{
		return key -> item;
	}

	@Required
	public void setCacheRegion(final CacheRegion region)
	{
		cacheRegion = region;
	}

	/**
	 * Retrieves cache regions used by this cache.
	 * @return a region injected into this facade.
	 * @see #setCacheRegion(CacheRegion)
	 */
	protected CacheRegion getCache()
	{
		return cacheRegion;
	}

	/**
	 * Returns a logger for writing in to the log.
	 * @return a logger for writing into the log.
	 */
	protected Logger log()
	{
		return LOG;
	}
}
