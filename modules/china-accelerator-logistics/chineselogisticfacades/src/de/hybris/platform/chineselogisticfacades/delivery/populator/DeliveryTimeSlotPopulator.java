/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticfacades.delivery.populator;

import de.hybris.platform.chineselogisticfacades.delivery.data.DeliveryTimeSlotData;
import de.hybris.platform.chineselogisticservices.model.DeliveryTimeSlotModel;
import de.hybris.platform.converters.Populator;


/**
 * Populating from DeliveryTimeSlotModel to DeliveryTimeSlotData
 */
public class DeliveryTimeSlotPopulator implements Populator<DeliveryTimeSlotModel, DeliveryTimeSlotData>
{

	@Override
	public void populate(final DeliveryTimeSlotModel source, final DeliveryTimeSlotData target)
	{
		target.setCode(source.getCode());
		target.setName(source.getName());
	}

}
