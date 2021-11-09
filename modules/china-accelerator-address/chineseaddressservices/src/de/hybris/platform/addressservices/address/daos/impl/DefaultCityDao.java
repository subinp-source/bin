/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.addressservices.address.daos.impl;

import de.hybris.platform.addressservices.address.daos.CityDao;
import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * Implementation of {@link CityDao}
 *
 */
public class DefaultCityDao extends DefaultGenericDao<CityModel> implements CityDao
{

	public DefaultCityDao()
	{
		super(CityModel._TYPECODE);
	}

	@Override
	public List<CityModel> findCityByRegionAndIsocode(final RegionModel region, final String isocode)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(CityModel.REGION, region);
		params.put(CityModel.ISOCODE, isocode);
		return find(params);
	}

}
