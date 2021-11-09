/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;


public class PickupConfirmationEvent extends ConsignmentProcessingEvent
{
	private static final long serialVersionUID = 7445256807120364650L;

	public PickupConfirmationEvent(final ConsignmentProcessModel process)
	{
		super(process);
	}
}
