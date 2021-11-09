/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.addressservices.address.daos;

import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.addressservices.model.DistrictModel;

import java.util.List;


/**
 * Dao responsible for {@link DistrictModel} access.
 * 
 */
public interface DistrictDao
{
	/**
	 * Find all districts by city and its code
	 * 
	 * @param city
	 *           the city that the district belongs to
	 * @param isocode
	 *           district code
	 * 
	 * @return districts
	 */
	List<DistrictModel> findDistrictByCityAndIsocode(CityModel city, String isocode);
}
