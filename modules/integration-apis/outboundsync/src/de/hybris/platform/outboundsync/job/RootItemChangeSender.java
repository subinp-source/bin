/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job;

import de.hybris.platform.outboundsync.dto.OutboundItemDTO;

import javax.validation.constraints.NotNull;

public interface RootItemChangeSender
{
	/**
	 * Consume the {@link OutboundItemDTO}
	 *
	 * @param change A change that is not eligible for sending to the
	 */
	void sendPopulatedItem(@NotNull OutboundItemDTO change);
}
