/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.event;

import de.hybris.platform.orderprocessing.events.OrderProcessingEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;


public class OrderRefundEvent extends OrderProcessingEvent
{
	private static final long serialVersionUID = 1L;

	public OrderRefundEvent(final OrderProcessModel process)
	{
		super(process);
	}
}
