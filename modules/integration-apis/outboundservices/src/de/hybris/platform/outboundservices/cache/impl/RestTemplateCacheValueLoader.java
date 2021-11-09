/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.cache.impl;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.springframework.web.client.RestTemplate;

/**
 * @deprecated no alternatives available
 */
@Deprecated(since = "1905.08-CEP", forRemoval = true)
public class RestTemplateCacheValueLoader implements CacheValueLoader<RestTemplate>
{
	private static final Logger LOG = Log.getLogger(RestTemplateCacheValueLoader.class);

	private final RestTemplate restTemplate;

	private RestTemplateCacheValueLoader(final RestTemplate restTemplate)
	{
		this.restTemplate = restTemplate;
	}

	public static RestTemplateCacheValueLoader from(final RestTemplate restTemplate)
	{
		return new RestTemplateCacheValueLoader(restTemplate);
	}

	@Override
	public RestTemplate load(final CacheKey key)
	{
		LOG.debug("Loading rest template for key {}", key);
		return restTemplate;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}

		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final RestTemplateCacheValueLoader that = (RestTemplateCacheValueLoader) o;

		return new EqualsBuilder()
				.append(restTemplate, that.restTemplate)
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(restTemplate)
				.toHashCode();
	}
}
