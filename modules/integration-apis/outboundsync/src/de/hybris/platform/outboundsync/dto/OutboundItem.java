/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundsync.dto;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.model.impl.DefaultIntegrationObjectDescriptor;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;

import java.util.Optional;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Preconditions;

@Immutable
public class OutboundItem
{
	private OutboundItemChange item;
	private IntegrationObjectDescriptor integrationObject;
	private OutboundChannelConfigurationModel channelConfiguration;
	private ItemModel changedItemModel;

	/**
	 * @deprecated use {@link #outboundItem()} instead.
	 */
	@Deprecated(since = "1905.01-CEP", forRemoval = true)
	public static Builder item()
	{
		return outboundItem();
	}
	public static Builder outboundItem(){ return new Builder();}

	public OutboundItemChange getItem()
	{
		return item;
	}

	public IntegrationObjectDescriptor getIntegrationObject()
	{
		return integrationObject;
	}

	public OutboundChannelConfigurationModel getChannelConfiguration()
	{
		return channelConfiguration;
	}

	/**
	 * Retrieves changed item model.
	 * @return an {@code Optional} containing the item model, if the item was changed or created; and {@code Optional.empty()}, if
	 * the item was deleted.
	 */
	public Optional<ItemModel> getChangedItemModel()
	{
		return Optional.ofNullable(changedItemModel);
	}

	/**
	 * Retrieves item type descriptor corresponding to this item in the {@link IntegrationObjectModel}.
	 * @return type descriptor or empty value, if this item has no corresponding type descriptor in the {@link IntegrationObjectModel}
	 * model.
	 */
	public Optional<TypeDescriptor> getTypeDescriptor()
	{
		return getChangedItemModel()
				.flatMap(it -> getIntegrationObject().getItemTypeDescriptor(it));
	}

	@Override
	public String toString()
	{
		return "OutboundItemChange{" +
				"item=" + item +
				", integrationObject=" + integrationObject +
				", channelConfiguration=" + channelConfiguration +
				'}';
	}

	public static final class Builder
	{
		private OutboundItemChange itemChange;
		private ItemModel changedItemModel;
		private IntegrationObjectModel integrationObject;
		private OutboundChannelConfigurationModel channelConfiguration;

		private Builder()
		{
		}

		public Builder withItemChange(final OutboundItemChange change)
		{
			Preconditions.checkArgument(change != null, "item change cannot be null");

			itemChange = change;
			return this;
		}

		public Builder withChangedItemModel(final ItemModel itemModel)
		{
			changedItemModel = itemModel;
			return this;
		}

		public Builder withIntegrationObject(final IntegrationObjectModel model)
		{
			Preconditions.checkArgument(model != null, "integration object cannot be null");

			integrationObject = model;
			return this;
		}

		public Builder withChannelConfiguration(final OutboundChannelConfigurationModel configuration)
		{
			Preconditions.checkArgument(configuration != null, "channel configuration cannot be null");

			channelConfiguration = configuration;
			return this;
		}

		public OutboundItem build()
		{
			final OutboundItem outboundItem = new OutboundItem();
			outboundItem.item = itemChange;
			outboundItem.changedItemModel = changedItemModel;
			outboundItem.integrationObject = DefaultIntegrationObjectDescriptor.create(integrationObject);
			outboundItem.channelConfiguration = channelConfiguration;
			return outboundItem;
		}
	}
}
