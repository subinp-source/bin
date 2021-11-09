/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.user.converters.populator;

import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;


/**
 *
 */
public class RegionPopulator implements Populator<RegionModel, RegionData>
{
	@Override
	public void populate(final RegionModel source, final RegionData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setName(source.getName());
		target.setIsocode(source.getIsocode());
		target.setIsocodeShort(source.getIsocodeShort());
		target.setCountryIso(source.getCountry().getIsocode());
	}
}
