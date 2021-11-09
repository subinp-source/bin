/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.event;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


public class MerchantRejectedEvent extends AbstractEvent
{
	private OrderModel order;
	private PrincipalModel manager;

	public MerchantRejectedEvent(final OrderModel order, final PrincipalModel manager)
	{
		this.order = order;
		this.manager = manager;
	}

	public OrderModel getOrder()
	{
		return order;
	}

	public void setOrder(final OrderModel order)
	{
		this.order = order;
	}

	public PrincipalModel getManager()
	{
		return manager;
	}

	public void setManager(final PrincipalModel manager)
	{
		this.manager = manager;
	}
}
