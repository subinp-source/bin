/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratoraddon.event;

import de.hybris.platform.b2b.event.MerchantRejectedEvent;
import de.hybris.platform.b2bacceleratorservices.event.AbstractMerchantEventListener;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;


public class MerchantRejectedEventListener extends AbstractMerchantEventListener<MerchantRejectedEvent>
{
	@Override
	protected void onEvent(final MerchantRejectedEvent event)
	{
		final OrderModel order = event.getOrder();
		setSessionLocaleForOrder(order);
		createOrderHistoryEntry(event.getManager(), order, "", OrderStatus.REJECTED_BY_MERCHANT);
	}
}
