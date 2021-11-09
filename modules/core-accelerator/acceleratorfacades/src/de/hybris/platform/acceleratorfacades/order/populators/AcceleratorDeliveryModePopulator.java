/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.order.populators;

import de.hybris.platform.commercefacades.order.converters.populator.AbstractDeliveryModePopulator;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;



public class AcceleratorDeliveryModePopulator extends AbstractDeliveryModePopulator<DeliveryModeModel, DeliveryModeData>
{

	@Override
	public void populate(final DeliveryModeModel source, final DeliveryModeData target)
	{
		super.populate(source, target);
	}

}
