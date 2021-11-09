/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.router;

import de.hybris.platform.outboundsync.dto.OutboundItemDTO;

/**
 * This class routes {@link OutboundItemDTO} based on whether it should get consumed immediately or be sent along
 * to the spring integration channel.
 */
public interface OutboundItemDTORouter
{
	void route(OutboundItemDTO itemDTO);
}
