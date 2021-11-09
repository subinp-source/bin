/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.cache.impl;

import de.hybris.platform.integrationservices.cache.impl.BaseIntegrationCache;
import de.hybris.platform.outboundservices.cache.DestinationRestTemplateCacheKey;
import de.hybris.platform.outboundservices.cache.RestTemplateCache;
import de.hybris.platform.regioncache.key.CacheKey;

import org.springframework.web.client.RestTemplate;

public class DestinationRestTemplateCache extends BaseIntegrationCache<DestinationRestTemplateCacheKey, RestTemplate> implements RestTemplateCache
{
	@Override
	protected CacheKey toCacheKey(final DestinationRestTemplateCacheKey key)
	{
		return internalKey(key);
	}

	/**
	 * Converts provided cache key to the key used by the wrapped cache implementation
	 * @param key key used by this class clients
	 * @return key used by the internal cache implementation
	 * @deprecated renamed to {@link #toCacheKey(DestinationRestTemplateCacheKey)}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected CacheKey internalKey(final DestinationRestTemplateCacheKey key)
	{
		return InternalDestinationRestTemplateCacheKey.from(key);
	}

	@Override
	protected Class<RestTemplate> getValueType()
	{
		return RestTemplate.class;
	}
}
