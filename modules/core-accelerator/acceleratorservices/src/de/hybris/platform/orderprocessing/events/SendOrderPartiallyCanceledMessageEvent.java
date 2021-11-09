/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.acceleratorservices.orderprocessing.model.OrderModificationProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


public class SendOrderPartiallyCanceledMessageEvent extends AbstractEvent
{
	private static final long serialVersionUID = 1L;

	private final OrderModificationProcessModel process;

	public SendOrderPartiallyCanceledMessageEvent(final OrderModificationProcessModel process)
	{
		this.process = process;
	}

	public OrderModificationProcessModel getProcess()
	{
		return process;
	}

}
