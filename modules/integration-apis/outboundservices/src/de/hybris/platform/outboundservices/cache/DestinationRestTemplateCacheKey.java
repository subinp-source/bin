/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.cache;

import de.hybris.platform.integrationservices.cache.IntegrationCacheKey;

/**
 * Defines a cache key that uses the destination and rest template as the key
 */
public interface DestinationRestTemplateCacheKey extends IntegrationCacheKey<DestinationRestTemplateId>
{
	/**
	 * Gets the destination id
	 * @return id
	 */
	String getDestinationId();

	/**
	 * Gets the {@link org.springframework.web.client.RestTemplate} type name
	 * @return type name
	 */
	String getRestTemplateTypeName();
}
