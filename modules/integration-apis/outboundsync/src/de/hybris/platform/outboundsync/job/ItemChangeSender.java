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
package de.hybris.platform.outboundsync.job;

import de.hybris.platform.outboundsync.dto.OutboundItemDTO;

import javax.validation.constraints.NotNull;

/**
 * The ItemChangeSender sends out an {@link OutboundItemDTO}
 */
public interface ItemChangeSender
{
	/**
	 * Send out an {@link ItemChangeSender}
	 *
	 * @param change A change to send
	 */
	void send(@NotNull OutboundItemDTO change);
}
