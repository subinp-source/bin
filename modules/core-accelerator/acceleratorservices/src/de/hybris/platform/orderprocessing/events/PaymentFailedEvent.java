/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;


/**
 * Event representing a failure to capture payment
 */
public class PaymentFailedEvent extends OrderProcessingEvent
{
	private static final long serialVersionUID = -4143696687348230520L;

	public PaymentFailedEvent(final OrderProcessModel process)
	{
		super(process);
	}
}
