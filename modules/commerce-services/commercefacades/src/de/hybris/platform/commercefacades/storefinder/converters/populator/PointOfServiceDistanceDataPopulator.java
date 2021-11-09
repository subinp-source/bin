/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storefinder.converters.populator;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.helpers.DistanceHelper;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.beans.factory.annotation.Required;


/**
 */
public class PointOfServiceDistanceDataPopulator implements Populator<PointOfServiceDistanceData, PointOfServiceData>
{
	private DistanceHelper distanceHelper;

	protected DistanceHelper getDistanceHelper()
	{
		return distanceHelper;
	}

	@Required
	public void setDistanceHelper(final DistanceHelper distanceHelper)
	{
		this.distanceHelper = distanceHelper;
	}

	@Override
	public void populate(final PointOfServiceDistanceData source, final PointOfServiceData target) throws ConversionException
	{
		if (source != null)
		{
			final String formattedDistance = getDistanceHelper().getDistanceStringForStore(
					source.getPointOfService().getBaseStore(), source.getDistanceKm());
			target.setFormattedDistance(formattedDistance);
		}
	}
}
