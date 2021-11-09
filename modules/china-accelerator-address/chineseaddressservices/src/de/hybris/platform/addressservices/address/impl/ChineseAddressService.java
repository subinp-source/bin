/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.address.impl;

import de.hybris.platform.addressservices.address.AddressService;
import de.hybris.platform.addressservices.address.daos.AddressDao;
import de.hybris.platform.addressservices.address.daos.CityDao;
import de.hybris.platform.addressservices.address.daos.DistrictDao;
import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.addressservices.model.DistrictModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.user.impl.DefaultAddressService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Collections;
import java.util.List;


/**
 * 
 * Implementation of {@link AddressService}
 *
 */
public class ChineseAddressService extends DefaultAddressService implements AddressService
{
	private transient AddressDao chineseAddressDao;
	private transient CityDao cityDao;
	private transient DistrictDao districtDao;

	/**
	 * Constructor of ChineseAddressService
	 *
	 * @param chineseAddressDao
	 *           dao of chinese address
	 * @param cityDao
	 *           dao of city
	 * @param districtDao
	 *           dao of district
	 */
	public ChineseAddressService(final AddressDao chineseAddressDao, final CityDao cityDao, final DistrictDao districtDao)
	{
		super();
		this.chineseAddressDao = chineseAddressDao;
		this.cityDao = cityDao;
		this.districtDao = districtDao;
	}

	@Override
	public List<CityModel> getCitiesForRegion(final String regionCode)
	{
		final List<CityModel> result = chineseAddressDao.getCitiesForRegion(regionCode);
		return result.isEmpty() ? Collections.emptyList() : result;
	}

	@Override
	public List<DistrictModel> getDistrictsForCity(final String cityCode)
	{
		final List<DistrictModel> result = chineseAddressDao.getDistrictsForCity(cityCode);
		return result.isEmpty() ? Collections.emptyList() : result;
	}

	@Override
	public CityModel getCityForIsocode(final String isocode)
	{
		return chineseAddressDao.getCityForIsocode(isocode);
	}

	@Override
	public DistrictModel getDistrictForIsocode(final String isocode)
	{
		return chineseAddressDao.getDistrictForIsocode(isocode);
	}

	@Override
	public CityModel getCityForRegionAndIsocode(final RegionModel region, final String isocode)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("region", region);
		final List<CityModel> result = getCityDao().findCityByRegionAndIsocode(region, isocode);
		ServicesUtil.validateIfSingleResult(result, CityModel.class, CityModel.REGION + ", " + CityModel.ISOCODE,
				region.getIsocode() + ", " + isocode);
		return result.get(0);
	}

	@Override
	public DistrictModel getDistrictForCityAndIsocode(final CityModel city, final String isocode)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("city", city);
		final List<DistrictModel> result = getDistrictDao().findDistrictByCityAndIsocode(city, isocode);
		ServicesUtil.validateIfSingleResult(result, DistrictModel.class, DistrictModel.CITY + ", " + DistrictModel.ISOCODE,
				city.getIsocode() + ", " + isocode);
		return result.get(0);
	}

	protected AddressDao getChineseAddressDao()
	{
		return chineseAddressDao;
	}

	protected CityDao getCityDao()
	{
		return cityDao;
	}

	protected DistrictDao getDistrictDao()
	{
		return districtDao;
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	public void setChineseAddressDao(final AddressDao chineseAddressDao)
	{
		this.chineseAddressDao = chineseAddressDao;
	}

}
