/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.job;

import de.hybris.platform.outboundsync.dto.OutboundItem;
import de.hybris.platform.outboundsync.dto.OutboundItemDTO;

/**
 * A factory for {@link de.hybris.platform.outboundsync.dto.OutboundItem}s
 */
public interface OutboundItemFactory
{
	/**
	 * Creates new instance of an {@link OutboundItem}
	 * @param itemDto a data transfer object describing the item to create
	 * @return a created item object. This object should not be {@code null}, instead an exception can be thrown by the factory
	 * implementation.
	 */
	OutboundItem createItem(OutboundItemDTO itemDto);
}
