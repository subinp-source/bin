/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressfacades.populators;

import de.hybris.platform.addressfacades.data.CityData;
import de.hybris.platform.addressfacades.data.DistrictData;
import de.hybris.platform.addressservices.address.AddressService;
import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.addressservices.model.DistrictModel;
import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


public class ChineseAddressReversePopulator extends AddressReversePopulator
{
	private static final String FOUND = " found.";
	private AddressService chineseAddressService;

	@Override
	public void populate(final AddressData addressData, final AddressModel addressModel)
	{
		Assert.notNull(addressData, "Parameter addressData cannot be null.");
		Assert.notNull(addressModel, "Parameter addressModel cannot be null.");

		super.populate(addressData, addressModel);
		addressModel.setFullname(addressData.getFullname());

		populateFirstAndLastName(addressModel);
		if (Objects.nonNull(addressData.getCountry()) && "CN".equals(addressData.getCountry().getIsocode()))
		{
			populateCity(addressData, addressModel);
			populateCityDistrict(addressData, addressModel);
		}

	}

	protected void populateCityDistrict(final AddressData addressData, final AddressModel addressModel)
	{
		String districtCode = addressData.getDistrict();
		if (StringUtils.isEmpty(districtCode))
		{
			final DistrictData district = addressData.getCityDistrict();
			if (Objects.nonNull(district))
			{
				districtCode = district.getCode();
			}
		}
		setDistrictForAddressModel(addressModel, districtCode);
	}

	protected void populateCity(final AddressData addressData, final AddressModel addressModel)
	{
		String cityCode = addressData.getTown();
		if (StringUtils.isEmpty(cityCode))
		{
			final CityData city = addressData.getCity();
			if (Objects.nonNull(city))
			{
				cityCode = city.getCode();
			}
		}
		setCityForAddressModel(addressModel, cityCode);
	}

	protected void setCityForAddressModel(final AddressModel addressModel, final String cityCode)
	{
		try
		{
			if (Objects.nonNull(addressModel.getRegion()))
			{
				final CityModel cityModel = getChineseAddressService().getCityForRegionAndIsocode(addressModel.getRegion(), cityCode);
				addressModel.setCity(cityModel);
			}
		}
		catch (final UnknownIdentifierException e)
		{
			throw new ConversionException("No city with the code " + cityCode + FOUND, e);
		}
		catch (final AmbiguousIdentifierException e)
		{
			throw new ConversionException("More than one city with the code " + cityCode + FOUND, e);
		}
	}

	protected void setDistrictForAddressModel(final AddressModel addressModel, final String districtCode)
	{
		try
		{
			if (Objects.nonNull(addressModel.getCity()))
			{
				final DistrictModel districtModel = getChineseAddressService().getDistrictForCityAndIsocode(addressModel.getCity(),
						districtCode);
				addressModel.setCityDistrict(districtModel);
				addressModel.setDistrict(districtModel.getName());
			}
		}
		catch (final UnknownIdentifierException e)
		{
			throw new ConversionException("No city district with the code " + districtCode + FOUND, e);
		}
		catch (final AmbiguousIdentifierException e)
		{
			throw new ConversionException("More than one city district with the code " + districtCode + FOUND, e);
		}
	}

	/**
	 * If first and last name are empty, try to copy full name to it, this is needed when guest checks out and creates
	 * new account based on address
	 *
	 * @param addressData
	 * @param addressModel
	 */
	protected void populateFirstAndLastName(final AddressModel addressModel)
	{
		final String fullName = addressModel.getFullname();
		if (StringUtils.isEmpty(addressModel.getFirstname()) && StringUtils.isEmpty(addressModel.getLastname())
				&& StringUtils.isNotEmpty(fullName))
		{
			addressModel.setFirstname(fullName);
			addressModel.setLastname(StringUtils.EMPTY);
		}
	}

	protected AddressService getChineseAddressService()
	{
		return chineseAddressService;
	}

	@Required
	public void setChineseAddressService(final AddressService chineseAddressService)
	{
		this.chineseAddressService = chineseAddressService;
	}

}
