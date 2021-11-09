/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.cache.dto;

import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;

import java.io.Serializable;


/**
 * Loader to provide a missing cache value.
 */
public class RenderingCacheValueLoader implements CacheValueLoader<Serializable>
{
	private final Serializable item;

	public RenderingCacheValueLoader(final Serializable item)
	{
		this.item = item;
	}

	@Override
	public Serializable load(final CacheKey key) throws CacheValueLoadException
	{
		return item;
	}
}
