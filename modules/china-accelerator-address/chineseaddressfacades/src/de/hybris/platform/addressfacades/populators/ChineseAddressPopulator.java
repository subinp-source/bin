/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressfacades.populators;

import de.hybris.platform.addressfacades.data.CityData;
import de.hybris.platform.addressfacades.data.DistrictData;
import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.addressservices.model.DistrictModel;
import de.hybris.platform.addressservices.strategies.NameWithTitleFormatStrategy;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;


public class ChineseAddressPopulator implements Populator<AddressModel, AddressData>
{
	private I18NService i18NService;

	private final Converter<CityModel, CityData> cityConverter;

	private final Converter<DistrictModel, DistrictData> districtConverter;

	private NameWithTitleFormatStrategy nameWithTitleFormatStrategy;
	
	public ChineseAddressPopulator(final I18NService i18NService, final Converter<CityModel, CityData> cityConverter,
								   final Converter<DistrictModel, DistrictData> districtConverter, final NameWithTitleFormatStrategy nameWithTitleFormatStrategy)
	{
		this.i18NService = i18NService;
		this.cityConverter = cityConverter;
		this.districtConverter = districtConverter;
		this.nameWithTitleFormatStrategy = nameWithTitleFormatStrategy;
	}

	protected I18NService getI18NService()
	{
		return this.i18NService;
	}

	/**
	 * @deprecated Since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
	}

	protected NameWithTitleFormatStrategy getNameWithTitleFormatStrategy()
	{
		return this.nameWithTitleFormatStrategy;
	}

	/**
	 * @deprecated Since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	public void setNameWithTitleFormatStrategy(final NameWithTitleFormatStrategy nameWithTitleFormatStrategy)
	{
		this.nameWithTitleFormatStrategy = nameWithTitleFormatStrategy;
	}

	public Converter<CityModel, CityData> getCityConverter()
	{
		return this.cityConverter;
	}

	public Converter<DistrictModel, DistrictData> getDistrictConverter()
	{
		return this.districtConverter;
	}

	@Override
	public void populate(final AddressModel source, final AddressData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		populateCity(source, target);
		populateDistrict(source, target);
		populateFullname(source, target);

	}

	private void populateFullname(final AddressModel source, final AddressData target)
	{
		final String fullname = source.getFullname();
		if (StringUtils.isEmpty(fullname))
		{
			target.setFullnameWithTitle(getNameWithTitleFormatStrategy().getFullnameWithTitle(source.getFirstname(),
					source.getLastname(), extractTitleName(source.getTitle())));
		}
		else
		{
			target.setFullname(fullname);
			target.setFullnameWithTitle(getNameWithTitleFormatStrategy().getFullnameWithTitle(fullname,
					extractTitleName(source.getTitle())));
		}
	}

	protected void populateCity(final AddressModel source, final AddressData target)
	{
		if (source.getCity() != null)
		{
			target.setTown(source.getCity().getIsocode());
			target.setCity(getCityConverter().convert(source.getCity()));
		}
	}

	protected void populateDistrict(final AddressModel source, final AddressData target)
	{
		if (source.getCityDistrict() != null)
		{
			target.setDistrict(source.getCityDistrict().getIsocode());
			target.setCityDistrict(getDistrictConverter().convert(source.getCityDistrict()));

		}
	}

	/**
	 * if address.getTitle() is null, return reverent by default
	 *
	 * @param title
	 * @return title name
	 */
	protected String extractTitleName(final TitleModel title)
	{
		if (title == null)
		{
			return StringUtils.EMPTY;
		}
		return title.getName(getI18NService().getCurrentLocale());
	}
}
