/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync;

import de.hybris.platform.outboundsync.dto.OutboundItemDTO;

import com.google.common.base.Preconditions;

/**
 * Defines a correlation strategy to be used when aggregating {@link OutboundItemDTO}s.
 * It takes into account the root item PK and the channel configuration PK.
 */
public class RootItemChannelCorrelationStrategy
{
	public String correlationKey(final OutboundItemDTO dto)
	{
		Preconditions.checkArgument(dto != null, "Cannot create correlation key with a null OutboundItemDTO");
		return String.format("%d-%d", dto.getRootItemPK(), dto.getChannelConfigurationPK());
	}
}
