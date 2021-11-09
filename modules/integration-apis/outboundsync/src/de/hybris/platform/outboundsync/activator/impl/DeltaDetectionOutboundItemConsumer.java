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
package de.hybris.platform.outboundsync.activator.impl;

import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.platform.outboundsync.activator.OutboundItemConsumer;
import de.hybris.platform.outboundsync.dto.OutboundItemDTO;
import de.hybris.platform.outboundsync.dto.impl.DeltaDetectionOutboundItemChange;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Required;

/**
 * Delta Detection implementation for consuming an OutboundItemDTO that uses the ChangeDetectionService for the consumption.
 */
public class DeltaDetectionOutboundItemConsumer implements OutboundItemConsumer
{
	private ChangeDetectionService changeDetectionService;

	@Override
	public void consume(final OutboundItemDTO outboundItemDTO)
	{
		final DeltaDetectionOutboundItemChange outboundItem = (DeltaDetectionOutboundItemChange) outboundItemDTO.getItem();
		final ItemChangeDTO itemChanged = outboundItem.getItemChangeDTO();
		getChangeDetectionService().consumeChanges(Collections.singletonList(itemChanged));
	}

	protected ChangeDetectionService getChangeDetectionService()
	{
		return changeDetectionService;
	}

	@Required
	public void setChangeDetectionService(final ChangeDetectionService changeDetectionService)
	{
		this.changeDetectionService = changeDetectionService;
	}
}
