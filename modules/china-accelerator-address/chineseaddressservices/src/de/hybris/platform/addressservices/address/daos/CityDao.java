/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.address.daos;

import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.core.model.c2l.RegionModel;

import java.util.List;


/**
 * Dao responsible for {@link CityModel} access.
 * 
 */
public interface CityDao
{
	/**
	 * Find all cities by region and its code
	 *
	 * @param region
	 *           the region that the city belongs to
	 * @param isocode
	 *           city code
	 * @return cities
	 */
	List<CityModel> findCityByRegionAndIsocode(RegionModel region, String isocode);
}
