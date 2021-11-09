/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;


public class SendPickedUpMessageEvent extends ConsignmentProcessingEvent
{
	private static final long serialVersionUID = -7948058666923007102L;

	public SendPickedUpMessageEvent(final ConsignmentProcessModel process)
	{
		super(process);
	}
}
