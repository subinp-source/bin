/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.event;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


public class OrderApprovedEvent extends AbstractEvent
{
	private OrderModel order;
	private PrincipalModel approver;

	public OrderApprovedEvent(final OrderModel order, final PrincipalModel approver)
	{
		this.order = order;
		this.approver = approver;
	}

	public OrderModel getOrder()
	{
		return order;
	}

	public void setOrder(final OrderModel order)
	{
		this.order = order;
	}

	public PrincipalModel getApprover()
	{
		return approver;
	}

	public void setApprover(final PrincipalModel approver)
	{
		this.approver = approver;
	}
}
