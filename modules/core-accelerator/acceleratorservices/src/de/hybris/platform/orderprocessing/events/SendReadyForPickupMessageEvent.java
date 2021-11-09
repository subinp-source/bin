/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;


public class SendReadyForPickupMessageEvent extends ConsignmentProcessingEvent
{
	private static final long serialVersionUID = -1664757371801260365L;

	public SendReadyForPickupMessageEvent(final ConsignmentProcessModel process)
	{
		super(process);
	}
}
