/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.event;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import java.util.Date;


public abstract class AbstractMerchantEventListener<T extends AbstractEvent> extends AbstractOrderEventListener<T>
{
	protected void createOrderHistoryEntry(final PrincipalModel owner, final OrderModel order, final String description,
			final OrderStatus status)
	{
		final OrderHistoryEntryModel historyEntry = getModelService().create(OrderHistoryEntryModel.class);
		historyEntry.setTimestamp(new Date());
		historyEntry.setOrder(order);
		historyEntry.setDescription(description);
		historyEntry.setOwner(owner);
		if (this.isCreateSnapshot())
		{
			final OrderModel snapshot = getOrderHistoryService().createHistorySnapshot(order);
			snapshot.setStatus(status);
			historyEntry.setPreviousOrderVersion(snapshot);
			getOrderHistoryService().saveHistorySnapshot(snapshot);
		}
		getModelService().save(historyEntry);
	}
}
