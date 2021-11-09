/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;


public class SendNotPickedUpConsignmentCanceledMessageEvent extends ConsignmentProcessingEvent
{
	private static final long serialVersionUID = 1L;

	public SendNotPickedUpConsignmentCanceledMessageEvent(final ConsignmentProcessModel process)
	{
		super(process);
	}
}
