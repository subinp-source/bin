/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.cache.impl;

import de.hybris.platform.cmsfacades.rendering.cache.RenderingCacheKeyProvider;
import de.hybris.platform.cmsfacades.rendering.cache.dto.RenderingCacheKey;
import de.hybris.platform.cmsfacades.rendering.cache.keyprovider.RenderingItemCacheKeyProvider;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link RenderingCacheKeyProvider}
 */
public class DefaultRenderingCacheKeyProvider implements RenderingCacheKeyProvider<ItemModel>
{
	private Map<String, RenderingItemCacheKeyProvider<ItemModel>> cacheKeyProviders;
	private TypeService typeService;

	@Override
	public Optional<CacheKey> getKey(final ItemModel item)
	{
		final String internalKey = getInternalKey(item);
		if ("".equals(internalKey))
		{
			return Optional.empty();
		}
		else
		{
			return Optional.of(new RenderingCacheKey(internalKey, this.getTenantId()));
		}
	}

	/**
	 * Returns current tenant id.
	 *
	 * @return the tenant id.
	 */
	protected String getTenantId()
	{
		return Registry.getCurrentTenant().getTenantID();
	}

	/**
	 * Generates the key for an item.
	 *
	 * @param item
	 * 		the item to analyze.
	 * @return the key
	 */
	protected String getInternalKey(final ItemModel item)
	{
		return getCacheKeyProviders().entrySet().stream()
				.filter(provider -> getTypeService().isAssignableFrom(provider.getKey(), item.getItemtype()))
				.map(provider -> provider.getValue().getKey(item))
				.collect(Collectors.joining("-"));
	}

	public Map<String, RenderingItemCacheKeyProvider<ItemModel>> getCacheKeyProviders()
	{
		return cacheKeyProviders;
	}

	@Required
	public void setCacheKeyProviders(
			final Map<String, RenderingItemCacheKeyProvider<ItemModel>> cacheKeyProviders)
	{
		this.cacheKeyProviders = cacheKeyProviders;
	}

	public TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}
}
