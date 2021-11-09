/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;


/**
 * Event representing a fraud notification to the customer.
 */
public class OrderFraudCustomerNotificationEvent extends OrderProcessingEvent
{
	private static final long serialVersionUID = -2122981030584865668L;

	public OrderFraudCustomerNotificationEvent(final OrderProcessModel process)
	{
		super(process);
	}
}
