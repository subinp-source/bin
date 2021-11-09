/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.event;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class ReplenishmentOrderConfirmationEvent extends AbstractEvent
{
	private final OrderModel order;

	public ReplenishmentOrderConfirmationEvent(final OrderModel order)
	{
		this.order = order;
	}

	public OrderModel getOrder()
	{
		return order;
	}
}
