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
package de.hybris.platform.outboundsync.dto.impl;

import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.platform.outboundsync.dto.OutboundChangeType;
import de.hybris.platform.outboundsync.dto.OutboundItemChange;

import java.util.EnumMap;
import java.util.function.Supplier;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

/**
 * An implementation of an {@link OutboundItemChange} for items provided by deltadetection extension
 */
public class DeltaDetectionOutboundItemChange implements OutboundItemChange
{
	private final ItemChangeDTO itemChangeDTO;

	private EnumMap<ChangeType, Supplier<OutboundChangeType>> changeTypeMap = new EnumMap<>(ChangeType.class);


	public DeltaDetectionOutboundItemChange(final ItemChangeDTO item)
	{
		Preconditions.checkArgument(item != null, "ItemChangeDTO cannot be null");
		itemChangeDTO = item;

		changeTypeMap.put(ChangeType.NEW, () -> OutboundChangeType.CREATED);
		changeTypeMap.put(ChangeType.MODIFIED, () -> OutboundChangeType.MODIFIED);
		changeTypeMap.put(ChangeType.DELETED, () -> OutboundChangeType.DELETED);
	}

	public ItemChangeDTO getItemChangeDTO()
	{
		return itemChangeDTO;
	}

	@Override
	public Long getPK()
	{
		return itemChangeDTO.getItemPK();
	}

	@Override
	public OutboundChangeType getChangeType()
	{
		final ChangeType changeType = itemChangeDTO.getChangeType();
		if (changeType == null || !changeTypeMap.containsKey(changeType))
		{
			throw new IllegalArgumentException(String.format("Change type %s is not supported", changeType));
		}
		return changeTypeMap.get(changeType).get();
	}

	@Override
	public String toString()
	{
		return "DeltaDetectionOutboundItemChange{" +
				"itemChangeDTO=" + itemChangeDTO +
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
		final DeltaDetectionOutboundItemChange that = (DeltaDetectionOutboundItemChange) o;

		return new EqualsBuilder()
				.append(itemChangeDTO.getItemPK(), that.itemChangeDTO.getItemPK())
				.append(itemChangeDTO.getChangeType(), that.itemChangeDTO.getChangeType())
				.append(itemChangeDTO.getInfo(), that.itemChangeDTO.getInfo())
				.append(itemChangeDTO.getItemComposedType(), that.itemChangeDTO.getItemComposedType())
				.append(itemChangeDTO.getStreamId(), that.itemChangeDTO.getStreamId())
				.append(itemChangeDTO.getVersionValue(), that.itemChangeDTO.getVersionValue())
				.append(itemChangeDTO.getVersion(), that.itemChangeDTO.getVersion())
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
				.append(itemChangeDTO.getItemPK())
				.append(itemChangeDTO.getChangeType())
				.append(itemChangeDTO.getInfo())
				.append(itemChangeDTO.getItemComposedType())
				.append(itemChangeDTO.getStreamId())
				.append(itemChangeDTO.getVersionValue())
				.append(itemChangeDTO.getVersion())
				.toHashCode();
	}
}
