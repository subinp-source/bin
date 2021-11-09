/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.cache;

import de.hybris.platform.integrationservices.cache.IntegrationCache;

import org.springframework.web.client.RestTemplate;

/**
 * Defines the interface for caching {@link RestTemplate}
 */
public interface RestTemplateCache extends IntegrationCache<DestinationRestTemplateCacheKey, RestTemplate>
{
}
