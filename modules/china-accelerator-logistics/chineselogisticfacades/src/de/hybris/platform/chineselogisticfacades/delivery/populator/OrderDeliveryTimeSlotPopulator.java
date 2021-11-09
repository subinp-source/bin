/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticfacades.delivery.populator;

import de.hybris.platform.chineselogisticfacades.delivery.data.DeliveryTimeSlotData;
import de.hybris.platform.commercefacades.order.converters.populator.AbstractOrderPopulator;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.OrderModel;

import org.springframework.util.Assert;

/**
 * Populating from OrderModel to OrderData and adding DeliveryTimeSlot into OrderData
 */
public class OrderDeliveryTimeSlotPopulator extends AbstractOrderPopulator<OrderModel, OrderData>
{

	@Override
	public void populate(final OrderModel source, final OrderData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getDeliveryTimeSlot() != null)
		{
			final DeliveryTimeSlotData deliveryTimeSlotData = new DeliveryTimeSlotData();
			deliveryTimeSlotData.setCode(source.getDeliveryTimeSlot().getCode());
			deliveryTimeSlotData.setName(source.getDeliveryTimeSlot().getName());
			target.setDeliveryTimeSlot(deliveryTimeSlotData);
		}
	}
}
