/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressfacades.address;


import de.hybris.platform.addressfacades.data.CityData;
import de.hybris.platform.addressfacades.data.DistrictData;

import java.util.List;


/**
 * 
 * Provide method related to address data
 *
 */
public interface AddressFacade
{
	/**
	 * Find city by its code
	 *
	 * @param isocode
	 *           city code
	 * @return city data
	 */
	CityData getCityForIsocode(String isocode);

	/**
	 * Find district by its code
	 *
	 * @param isocode
	 *           district code
	 * @return district data
	 */
	DistrictData getDistrcitForIsocode(String isocode);

	/**
	 * Find cities by region code
	 *
	 * @param regionCode
	 *           region Code
	 * @return city list in the region
	 */
	List<CityData> getCitiesForRegion(String regionCode);

	/**
	 * Find districts by city code
	 *
	 * @param cityCode
	 *           city Code
	 * @return district list in the city
	 */
	List<DistrictData> getDistrictsForCity(String cityCode);


	/**
	 * Validate the specific postcode
	 *
	 * @param postcode
	 *           the specific postcode
	 * @return validated result
	 */
	boolean validatePostcode(String postcode);

	/**
	 * Validate the specific cell phone
	 *
	 * @param cellphone
	 *           the specific cell phone
	 * @return validated result
	 */
	boolean isInvalidCellphone(String cellphone);
}
