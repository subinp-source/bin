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
package de.hybris.platform.outboundsync.activator;

import de.hybris.platform.outboundsync.dto.OutboundItemDTO;

/**
 * Service that takes care of consuming an outbound DTO after the item has been processed.
 */
public interface OutboundItemConsumer
{
	/**
	 * Consumes the items in the outbound item DTO.
	 *
	 * @param outboundItemDTO that contains the items to consume
	 */
	void consume(OutboundItemDTO outboundItemDTO);
}
