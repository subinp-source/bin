/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.order.populators;

import de.hybris.platform.commercefacades.order.converters.populator.GroupOrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.OrderModel;


/**
 * Set supported actions for grouped entry.
 */
public class AcceleratorGroupOrderEntryPopulator extends GroupOrderEntryPopulator<OrderModel, OrderData>
{
	@Override
	protected OrderEntryData createGroupedOrderEntry(final OrderEntryData firstEntry)
	{
		final OrderEntryData groupedEntry = super.createGroupedOrderEntry(firstEntry);
		groupedEntry.setSupportedActions(firstEntry.getSupportedActions());
		return groupedEntry;
	}
}
