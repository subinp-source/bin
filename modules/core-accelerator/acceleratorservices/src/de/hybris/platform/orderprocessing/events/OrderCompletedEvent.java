/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;


/**
 * Event representing completion of order process.
 */
public class OrderCompletedEvent extends OrderProcessingEvent
{
	private static final long serialVersionUID = -293422455711438189L;

	public OrderCompletedEvent(final OrderProcessModel process)
	{
		super(process);
	}
}
