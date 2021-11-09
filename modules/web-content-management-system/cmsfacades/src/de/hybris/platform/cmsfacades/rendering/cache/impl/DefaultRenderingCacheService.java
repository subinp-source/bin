/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.cache.impl;

import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cmsfacades.rendering.cache.RenderingCacheKeyProvider;
import de.hybris.platform.cmsfacades.rendering.cache.RenderingCacheService;
import de.hybris.platform.cmsfacades.rendering.cache.dto.RenderingCacheValueLoader;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Suppliers;


/**
 * Default implementation of {@link RenderingCacheService}
 */
public class DefaultRenderingCacheService<T extends Serializable> implements RenderingCacheService<T>
{
	public static final String CMS_RENDERING_CACHE_ENABLED_KEY = "cms.rendering.cache.enabled";

	private RenderingCacheKeyProvider<ItemModel> cacheKeyProvider;
	private CacheController cacheController;
	private ConfigurationService configurationService;
	private SessionService sessionService;

	private final Supplier<Boolean> useCache = Suppliers
			.memoizeWithExpiration(() -> getConfigurationService().getConfiguration()
					.getBoolean(CMS_RENDERING_CACHE_ENABLED_KEY, false), 1, TimeUnit.MINUTES);

	@Override
	public boolean cacheEnabled()
	{
		return useCache.get() && Objects.isNull(getSessionService().getAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM));
	}

	@Override
	public void put(final CacheKey key, final Serializable item)
	{
		if (this.cacheEnabled())
		{
			getCacheController().getWithLoader(key, new RenderingCacheValueLoader(item));
		}
	}

	@Override
	public Optional<T> get(final CacheKey key)
	{
		if (this.cacheEnabled())
		{
			return Optional.ofNullable(getCacheController().get(key));
		}
		else
		{
			return Optional.empty();
		}
	}

	@Override
	public Optional<CacheKey> getKey(final ItemModel item)
	{
		if (this.cacheEnabled())
		{
			return getCacheKeyProvider().getKey(item);
		}
		else
		{
			return Optional.empty();
		}
	}

	@Override
	public T cacheOrElse(final ItemModel item, final Supplier<T> converter)
	{
		final Optional<CacheKey> key = this.getKey(item);

		if (this.cacheEnabled() && key.isPresent())
		{
			final Optional<T> optionalCachedItem = this.get(key.get());
			if (optionalCachedItem.isEmpty())
			{
				final T convertedItem = converter.get();
				this.put(key.get(), convertedItem);
				return convertedItem;
			}
			return optionalCachedItem.get();
		}
		else
		{
			return converter.get();
		}
	}

	public RenderingCacheKeyProvider<ItemModel> getCacheKeyProvider()
	{
		return cacheKeyProvider;
	}

	@Required
	public void setCacheKeyProvider(
			final RenderingCacheKeyProvider<ItemModel> cacheKeyProvider)
	{
		this.cacheKeyProvider = cacheKeyProvider;
	}

	public CacheController getCacheController()
	{
		return cacheController;
	}

	@Required
	public void setCacheController(final CacheController cacheController)
	{
		this.cacheController = cacheController;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
