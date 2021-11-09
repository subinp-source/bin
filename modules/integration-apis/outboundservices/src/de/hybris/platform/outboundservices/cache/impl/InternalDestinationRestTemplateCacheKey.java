/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.cache.impl;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.outboundservices.cache.DestinationRestTemplateCacheKey;
import de.hybris.platform.regioncache.key.AbstractCacheKey;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

final class InternalDestinationRestTemplateCacheKey extends AbstractCacheKey
{
	private final DestinationRestTemplateCacheKey key;
	private final int hashCode;

	private InternalDestinationRestTemplateCacheKey(final DestinationRestTemplateCacheKey key)
	{
		super(key.getId().getRestTemplateType().getName(), getCurrentTenantId());
		this.key = key;
		hashCode = new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(key)
				.toHashCode();
	}

	public static InternalDestinationRestTemplateCacheKey from(final DestinationRestTemplateCacheKey key)
	{
		return new InternalDestinationRestTemplateCacheKey(key);
	}

	private static String getCurrentTenantId()
	{
		return JaloSession.getCurrentSession().getTenant().getTenantID();
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}

		if (o == null || getClass() != o.getClass() || this.hashCode != o.hashCode())
		{
			return false;
		}

		final InternalDestinationRestTemplateCacheKey that = (InternalDestinationRestTemplateCacheKey) o;

		return new EqualsBuilder()
				.appendSuper(super.equals(o))
				.append(key, that.key)
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return hashCode;
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "{" +
				"key='" + key + '\'' +
				'}';
	}
}
