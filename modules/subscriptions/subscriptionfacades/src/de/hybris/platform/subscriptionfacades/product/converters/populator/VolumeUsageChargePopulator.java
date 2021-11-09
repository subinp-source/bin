/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionfacades.product.converters.populator;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionfacades.data.VolumeUsageChargeData;
import de.hybris.platform.subscriptionservices.model.VolumeUsageChargeModel;


/**
 * Populate DTO {@link VolumeUsageChargeData} with data from {@link VolumeUsageChargeModel}.
 *
 * @param <SOURCE> source class
 * @param <TARGET> target class
 */
public class VolumeUsageChargePopulator<SOURCE extends VolumeUsageChargeModel, TARGET extends VolumeUsageChargeData> extends
		AbstractUsageChargePopulator<SOURCE, TARGET>
{

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		super.populate(source, target);
	}
}
