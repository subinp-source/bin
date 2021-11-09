/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.cache.impl;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.outboundservices.cache.DestinationRestTemplateId;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Preconditions;

public class DefaultDestinationRestTemplateId implements DestinationRestTemplateId
{
	private final ConsumedDestinationModel destination;

	protected DefaultDestinationRestTemplateId(final ConsumedDestinationModel destination)
	{
		Preconditions.checkArgument(destination != null, "ConsumedDestinationModel cannot be null");
		this.destination = destination;
	}

	public static DefaultDestinationRestTemplateId from(final ConsumedDestinationModel destination)
	{
		return new DefaultDestinationRestTemplateId(destination);
	}

	@Override
	public ConsumedDestinationModel getDestination()
	{
		return destination;
	}

	@Override
	public Class<? extends RestTemplate> getRestTemplateType()
	{
		return RestTemplate.class;
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

		final DefaultDestinationRestTemplateId that = (DefaultDestinationRestTemplateId) o;

		return new EqualsBuilder()
				.append(destination, that.destination)
				.append(getRestTemplateType(), that.getRestTemplateType())
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(destination)
				.append(getRestTemplateType())
				.toHashCode();
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "{" +
				"destination=" + destination +
				",restTemplateType=" + getRestTemplateType().getSimpleName() +
				'}';
	}
}
