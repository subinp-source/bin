/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;


public class SendDeliveryMessageEvent extends ConsignmentProcessingEvent
{
	private static final long serialVersionUID = -8586595518929550780L;

	public SendDeliveryMessageEvent(final ConsignmentProcessModel process)
	{
		super(process);
	}
}
