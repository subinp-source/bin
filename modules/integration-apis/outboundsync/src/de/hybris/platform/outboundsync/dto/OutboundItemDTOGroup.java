/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.dto;

import de.hybris.platform.core.PK;
import de.hybris.platform.outboundsync.job.OutboundItemFactory;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

/**
 * The {@code OutboundItemDTOGroup} class encapsulates the collection of {@link OutboundItemDTO}s.
 * It also provides some convenience accessors.
 */
@Immutable
public class OutboundItemDTOGroup
{
	private final Collection<OutboundItemDTO> outboundItemDTOs;
	private final Long rootItemPk;
	private final OutboundItem outboundItem;
	private final PK cronJobPk;

	private OutboundItemDTOGroup(final Collection<OutboundItemDTO> outboundItemDTOs, final OutboundItemFactory itemFactory)
	{
		Preconditions.checkArgument(CollectionUtils.isNotEmpty(outboundItemDTOs), "OutboundItemDTOs can't be null or empty");
		Preconditions.checkArgument(itemFactory != null, "OutboundItemFactory can't be null");

		this.outboundItemDTOs = outboundItemDTOs;
		rootItemPk = getRootItemPkInternal();
		outboundItem = itemFactory.createItem(getFirstItem());
		cronJobPk = getFirstItem().getCronJobPK();
	}

	private OutboundItemDTO getFirstItem()
	{
		final Iterator<OutboundItemDTO> itr = outboundItemDTOs.iterator();
		return itr.next();
	}

	private Long getRootItemPkInternal()
	{
		return outboundItemDTOs.stream()
				.filter(dto -> dto.getRootItemPK() != null)
				.findFirst()
				.map(OutboundItemDTO::getRootItemPK).orElse(null);
	}

	/**
	 * Creates an {@code OutboundItemDTOGroup} from the given collection of {@link OutboundItemDTO}
	 *
	 * @param outboundItemDTOs Collection of OutboundItemDTOs
	 * @param itemFactory a factory for {@link OutboundItem}
	 * @return new instance of this group
	 */
	public static OutboundItemDTOGroup from(final Collection<OutboundItemDTO> outboundItemDTOs, final OutboundItemFactory itemFactory)
	{
		return new OutboundItemDTOGroup(outboundItemDTOs, itemFactory);
	}

	/**
	 * Retrieves a copy of the collection of OutboundItemDTOs
	 *
	 * @return Collection of OutboundItemDTOs
	 */
	public Collection<OutboundItemDTO> getOutboundItemDTOs()
	{
		return Collections.unmodifiableCollection(outboundItemDTOs);
	}

	/**
	 * Gets the root item PK. If the items in the OutboundItemDTO collection have a root,
	 * the root item PK belongs to the root. Otherwise, the root item PK is the same
	 * as the item's PK.
	 *
	 * @return Root item PK
	 */
	public Long getRootItemPk()
	{
		return rootItemPk;
	}

	/**
	 * Gets the Integration Object's code associated with the changed item
	 *
	 * @return Code
	 */
	public String getIntegrationObjectCode()
	{
		return outboundItem.getIntegrationObject().getCode();
	}

	/**
	 * Gets the ConsumedDestination id associated with the changed item
	 *
	 * @return Destination id
	 */
	public String getDestinationId()
	{
		return outboundItem.getChannelConfiguration().getDestination().getId();
	}

	/**
	 * Gets the {@link OutboundChannelConfigurationModel} associated with the changed item
	 *
	 * @return Channel configuration model
	 */
	public OutboundChannelConfigurationModel getChannelConfiguration()
	{
		return outboundItem.getChannelConfiguration();
	}

	/**
	 * Gets the PK of the CronJob that was triggered
	 *
	 * @return CronJob's primary key
	 */
	public PK getCronJobPk()
	{
		return cronJobPk;
	}

	@Override
	public String toString()
	{
		return "OutboundItemDTOGroup{" +
				"outboundItemDTOS=" + outboundItemDTOs +
				", rootItemPk=" + rootItemPk +
				", cronJobPk=" + cronJobPk +
				'}';
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

		final OutboundItemDTOGroup that = (OutboundItemDTOGroup) o;

		return new EqualsBuilder()
				.append(outboundItemDTOs, that.outboundItemDTOs)
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(outboundItemDTOs)
				.toHashCode();
	}
}
