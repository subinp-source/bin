/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticfacades.delivery.populator;

import de.hybris.platform.chineselogisticfacades.delivery.data.DeliveryTimeSlotData;
import de.hybris.platform.chineselogisticservices.model.DeliveryTimeSlotModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;

import org.springframework.util.Assert;


/**
 * Populating from CartModel to CartData and adding DeliveryTimeSlot into CartData
 */
public class CartDeliveryTimeSlotPopulator implements Populator<CartModel, CartData>
{
	@Override
	public void populate(final CartModel source, final CartData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		final DeliveryTimeSlotModel deliveryTimeSlotModel = source.getDeliveryTimeSlot();
		if (deliveryTimeSlotModel != null)
		{
			final DeliveryTimeSlotData deliveryTimeSlotData = new DeliveryTimeSlotData();
			deliveryTimeSlotData.setCode(deliveryTimeSlotModel.getCode());
			deliveryTimeSlotData.setName(deliveryTimeSlotModel.getName());
			target.setDeliveryTimeSlot(deliveryTimeSlotData);
		}
	}

}
