/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.cache.dto;

import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;


/**
 * Default implementation of rendering cache key.
 */
public class RenderingCacheKey implements CacheKey
{
	private static final String CMS_CACHE_UNIT_CODE = "__CMS_RENDERING_CACHE__";
	private final String key;
	private final String tenantId;

	public RenderingCacheKey(final String key, final String tenantId)
	{
		this.key = key;
		this.tenantId = tenantId;
	}

	@Override
	public CacheUnitValueType getCacheValueType()
	{
		return CacheUnitValueType.SERIALIZABLE;
	}

	@Override
	public Object getTypeCode()
	{
		return CMS_CACHE_UNIT_CODE;
	}

	@Override
	public String getTenantId()
	{
		return tenantId;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result = 31 * result + (key == null ? 0 : key.hashCode());
		result = 31 * result + (tenantId == null ? 0 : tenantId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (super.getClass() != obj.getClass())
		{
			return false;
		}
		final RenderingCacheKey other = (RenderingCacheKey) obj;
		if (tenantId == null)
		{
			if (other.tenantId != null)
			{
				return false;
			}
		}
		else if (!tenantId.equals(other.tenantId))
		{
			return false;
		}
		if (key == null)
		{
			if (other.key != null)
			{
				return false;
			}
		}
		else if (!key.equals(other.key))
		{
			return false;
		}
		return true;
	}
}
