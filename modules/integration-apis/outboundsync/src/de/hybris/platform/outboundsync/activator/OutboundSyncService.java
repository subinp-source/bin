/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundsync.activator;

import de.hybris.platform.outboundsync.dto.OutboundItemDTO;

import java.util.Collection;

/**
 * Service Activator interface that handles synchronization of Item changes detected and placed in a spring integration channel.
 */
public interface OutboundSyncService
{
	/**
	 * Consumes a DTO with the item changed information and handles the message based on the change type.
	 *
	 * @param outboundItemDTOs A collection of DTOs with the information about the changes in the item.
	 */
	void sync(Collection<OutboundItemDTO> outboundItemDTOs);
}
