/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.populators;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.hybris.cis.api.model.CisAddress;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populate the CisAddress with the AddressData information
 */
public class CisAddressPopulator implements Populator<AddressData, CisAddress>
{
	@Override
	public void populate(final AddressData source, final CisAddress target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("target", target);

		if (source == null)
		{
			return;
		}

		target.setCompany(source.getCompanyName());
		if (source.getCountry() != null)
		{
			target.setCountry(source.getCountry().getIsocode() == null ? source.getCountry().getName() : source.getCountry()
					.getIsocode());
		}
		target.setEmail(source.getEmail());
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setAddressLine1(source.getLine1());
		target.setAddressLine2(source.getLine2());
		target.setPhone(source.getPhone());
		target.setZipCode(source.getPostalCode());
		if (source.getRegion() != null)
		{
			target.setState(source.getRegion().getName());
		}
		target.setCity(source.getTown());
	}
}
