/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.entitlementfacades.entitlement.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.entitlementfacades.data.EntitlementData;
import de.hybris.platform.entitlementservices.model.EntitlementModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.util.ServicesUtil;


/**
 * Populate DTO {@link de.hybris.platform.entitlementfacades.data.EntitlementData} with data
 * from {@link de.hybris.platform.entitlementservices.model.EntitlementModel}.
 */
public class EntitlementModelToDataPopulator<SOURCE extends EntitlementModel, TARGET extends EntitlementData>
        implements Populator<SOURCE, TARGET>
{
    @Override
    public void populate(final SOURCE source, final TARGET target) throws ConversionException
    {
		ServicesUtil.validateParameterNotNullStandardMessage("source", source);
		ServicesUtil.validateParameterNotNullStandardMessage("target", target);

		target.setId(source.getId());
		target.setName(source.getName());
		target.setDescription(source.getDescription());
	}
}
