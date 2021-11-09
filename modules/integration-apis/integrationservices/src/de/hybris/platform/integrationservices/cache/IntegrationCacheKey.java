/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.integrationservices.cache;

/**
 * Represents a key to the cached item
 * @param <K> Key type
 */
public interface IntegrationCacheKey<K>
{
	/**
	 * Returns the unique identifier for that represents the key
	 * @return Id
	 */
	K getId();
}
