/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.cache.impl;

import de.hybris.platform.outboundservices.cache.DestinationRestTemplateCacheKey;
import de.hybris.platform.outboundservices.cache.DestinationRestTemplateId;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

/**
 * A cache key that uses the destination and rest template as the unique identifier
 */
public class DefaultDestinationRestTemplateCacheKey implements DestinationRestTemplateCacheKey
{
	private final DestinationRestTemplateId id;

	protected DefaultDestinationRestTemplateCacheKey(final DestinationRestTemplateId id)
	{
		Preconditions.checkArgument(id != null, "DestinationRestTemplatedId cannot be null");
		this.id = id;
	}

	public static DefaultDestinationRestTemplateCacheKey from(final DestinationRestTemplateId id)
	{
		return new DefaultDestinationRestTemplateCacheKey(id);
	}

	@Override
	public DestinationRestTemplateId getId()
	{
		return id;
	}

	@Override
	public String getDestinationId()
	{
		return getId().getDestination().getId();
	}

	@Override
	public String getRestTemplateTypeName()
	{
		return getId().getRestTemplateType().getName();
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

		final DefaultDestinationRestTemplateCacheKey that = (DefaultDestinationRestTemplateCacheKey) o;

		return new EqualsBuilder()
				.append(id, that.id)
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(id)
				.toHashCode();
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "{" +
				"id=" + id +
				'}';
	}
}
