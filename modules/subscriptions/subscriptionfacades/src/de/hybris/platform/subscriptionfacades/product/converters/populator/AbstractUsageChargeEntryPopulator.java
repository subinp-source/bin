/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionfacades.product.converters.populator;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;
import de.hybris.platform.subscriptionservices.model.UsageChargeEntryModel;


/**
 * Populate DTO {@link UsageChargeEntryData} with data from {@link UsageChargeEntryModel}.
 *
 * @param <SOURCE> source class
 * @param <TARGET> target class
 */
public abstract class AbstractUsageChargeEntryPopulator<SOURCE extends UsageChargeEntryModel, TARGET extends UsageChargeEntryData>
		extends AbstractChargeEntryPopulator<SOURCE, TARGET>
{

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		super.populate(source, target);
	}

}
