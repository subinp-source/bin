/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.address;


import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.addressservices.model.DistrictModel;
import de.hybris.platform.core.model.c2l.RegionModel;

import java.util.List;


/**
 * 
 * Provide methods to handle address related request
 *
 */
public interface AddressService extends de.hybris.platform.servicelayer.user.AddressService //NOSONAR
{
	/**
	 * Find cities by region code
	 *
	 * @param regionCode
	 *           region code
	 * @return city list in the region
	 */
	List<CityModel> getCitiesForRegion(String regionCode);

	/**
	 * Find districts by city code
	 *
	 * @param cityCode
	 *           city code
	 * @return district list in the city
	 */
	List<DistrictModel> getDistrictsForCity(String cityCode);

	/**
	 * Find city by its code
	 *
	 * @param isocode
	 *           city code
	 * @return city model
	 */
	CityModel getCityForIsocode(String isocode);

	/**
	 * Find district by its code
	 *
	 * @param isocode
	 *           district code
	 * @return district model
	 */
	DistrictModel getDistrictForIsocode(String isocode);

	/**
	 * Gets city by region and its code
	 *
	 * @param region
	 *           the region that the city belongs to
	 * @param isocode
	 *           city code
	 * @return city model
	 */
	CityModel getCityForRegionAndIsocode(RegionModel region, String isocode);

	/**
	 * Gets district by city and its code
	 *
	 * @param city
	 *           the city that the district belongs to
	 * @param isocode
	 *           district code
	 * @return district model
	 */
	DistrictModel getDistrictForCityAndIsocode(CityModel city, String isocode);

}
