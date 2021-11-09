/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.addressservices.address.daos.impl;

import de.hybris.platform.addressservices.address.daos.DistrictDao;
import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.addressservices.model.DistrictModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * Implementation of {@link DistrictDao}
 *
 */
public class DefaultDistrictDao extends DefaultGenericDao<DistrictModel> implements DistrictDao
{

	public DefaultDistrictDao()
	{
		super(DistrictModel._TYPECODE);
	}

	@Override
	public List<DistrictModel> findDistrictByCityAndIsocode(final CityModel city, final String isocode)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(DistrictModel.CITY, city);
		params.put(DistrictModel.ISOCODE, isocode);
		return find(params);
	}

}
