/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.factory;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;

import java.util.Map;


/**
 * The factory interface generates client instances
 */
public interface ClientFactory
{

	/**
	 * Generates a client proxy for the given client type
	 *
	 * @param cacheKey
	 *           the id of the client
	 * @param clientType
	 *           the class of client type
	 * @param clientConfig
	 *           the map consist of configurations of client
	 * @param <T>
	 *           the type of the client
	 * 
	 * @return the client proxy
	 */
	<T> T client(final String cacheKey, final Class<T> clientType, final Map<String, String> clientConfig);

	/**
	 * Invalidate the cache for the given key
	 *
	 * @param key
	 *           the key of client
	 */
	void inValidateCache(final String key);

	/**
	 * Remove all clients from the cache
	 */
	void clearCache();

	/**
	 * Helper method to build cache key which holds client proxy.
	 *
	 * @param destination
	 * 		consumed destination of the client
	 * @return cache key string
	 */
	String buildCacheKey(final ConsumedDestinationModel destination);
}
