/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.facade;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.outboundservices.enums.OutboundSource;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

/**
 * Item synchronization parameters.
 */
public class SyncParameters
{
	private final ItemModel item;
	private final String integrationObjectCode;
	private final String destinationId;
	private final OutboundSource source;
	private final ConsumedDestinationModel destinationModel;
	private final IntegrationObjectModel integrationObjectModel;

	protected SyncParameters(final ItemModel item, final String integrationObjectCode,
	                         final IntegrationObjectModel integrationObjectModel, final String destinationId,
	                         final ConsumedDestinationModel destinationModel,
	                         final OutboundSource source)
	{
		Preconditions.checkArgument(item != null, "itemModel cannot be null");
		this.item = item;
		this.integrationObjectCode = integrationObjectCode;
		this.integrationObjectModel = integrationObjectModel;
		this.destinationId = destinationId;
		this.destinationModel = destinationModel;
		this.source = source == null ? OutboundSource.UNKNOWN : source;
	}

	public ItemModel getItem()
	{
		return item;
	}

	public String getIntegrationObjectCode()
	{
		return getIntegrationObject() != null ? getIntegrationObject().getCode() : integrationObjectCode;
	}

	public String getDestinationId()
	{
		return getDestination() != null ? getDestination().getId() : destinationId;
	}

	public OutboundSource getSource()
	{
		return source;
	}

	public ConsumedDestinationModel getDestination()
	{
		return destinationModel;
	}

	public IntegrationObjectModel getIntegrationObject()
	{
		return integrationObjectModel;
	}

	public static SyncParametersBuilder syncParametersBuilder()
	{
		return new SyncParametersBuilder();
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

		final SyncParameters that = (SyncParameters) o;

		return new EqualsBuilder()
				.append(getItem(), that.getItem())
				.append(getIntegrationObjectCode(), that.getIntegrationObjectCode())
				.append(getDestinationId(), that.getDestinationId())
				.append(getSource(), that.getSource())
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(getItem())
				.append(getIntegrationObjectCode())
				.append(getDestinationId())
				.append(getSource())
				.toHashCode();
	}

	@Override
	public String toString()
	{
		return "SyncParameters{" +
				"item='" + getItem().toString() +
				"', integrationObject='" + getIntegrationObjectCode() +
				"', destination='" + getDestinationId() +
				"', source='" + getSource().getCode() +
				"'}";
	}

	public static class SyncParametersBuilder
	{
		private ItemModel item;
		private String ioCode;
		private String destId;
		private OutboundSource source;
		private ConsumedDestinationModel destinationModel;
		private IntegrationObjectModel integrationObjectModel;

		private SyncParametersBuilder()
		{
			// non-instantiable through constructor
			// use the factory method in SyncParameters
		}

		public SyncParametersBuilder withItem(final ItemModel item)
		{
			this.item = item;
			return this;
		}

		public SyncParametersBuilder withIntegrationObjectCode(final String ioCode)
		{
			this.ioCode = ioCode;
			return this;
		}

		public SyncParametersBuilder withDestinationId(final String destId)
		{
			this.destId = destId;
			return this;
		}

		public SyncParametersBuilder withSource(final OutboundSource src)
		{
			this.source = src;
			return this;
		}

		public SyncParametersBuilder withDestination(final ConsumedDestinationModel destinationModel)
		{
			this.destinationModel = destinationModel;
			return this;
		}

		public SyncParametersBuilder withIntegrationObject(final IntegrationObjectModel integrationObjectModel)
		{
			this.integrationObjectModel = integrationObjectModel;
			return this;
		}

		public SyncParameters build()
		{
			return new SyncParameters(item, ioCode, integrationObjectModel, destId, destinationModel, source);
		}
	}
}
