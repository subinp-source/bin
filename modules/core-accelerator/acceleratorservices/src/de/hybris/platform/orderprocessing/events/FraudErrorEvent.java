/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;


/**
 * Event representing an error occurring in the Fraud Check
 */
public class FraudErrorEvent extends OrderProcessingEvent
{
	private static final long serialVersionUID = -1624578701505313603L;

	public FraudErrorEvent(final OrderProcessModel process)
	{
		super(process);
	}
}
