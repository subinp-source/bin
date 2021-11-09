/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;


/**
 * Event representing failure to authorise a payment.
 */
public class AuthorizationFailedEvent extends OrderProcessingEvent
{
	private static final long serialVersionUID = 8181864059445399549L;

	public AuthorizationFailedEvent(final OrderProcessModel process)
	{
		super(process);
	}
}
