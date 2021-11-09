/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.event;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


public class OrderApprovalRejectionEvent extends AbstractEvent
{
	private final OrderProcessModel process;

	public OrderApprovalRejectionEvent(final OrderProcessModel process)
	{
		this.process = process;
	}

	public OrderProcessModel getProcess()
	{
		return process;
	}
}
