/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.address.daos.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for {@link DefaultCityDao}
 */
@IntegrationTest
public class DefaultCityDaoTest extends ServicelayerTransactionalTest
{
	@Resource
	private DefaultCityDao cityDao;

	@Resource
	private ModelService modelService;

	private RegionModel region;

	private final String COUNTRY_ISOCODE = "CN";
	private final String REGION_ISOCODE = "CN-11";
	private final String CITY_ISOCODE1 = "CN-11-1";
	private final String CITY_ISOCODE2 = "CN-12-1";
	private final String CITY1 = "Beijing";
	private final String CITY2 = "Tianjing";
	@Before
	public void prepare()
	{
		final CountryModel country = new CountryModel();
		country.setIsocode(COUNTRY_ISOCODE);

		region = new RegionModel();
		region.setIsocode(REGION_ISOCODE);
		region.setCountry(country);

		final CityModel city1 = new CityModel();
		city1.setRegion(region);
		city1.setIsocode(CITY_ISOCODE1);
		city1.setName(CITY1, Locale.ENGLISH);

		final CityModel city2 = new CityModel();
		city2.setRegion(region);
		city2.setIsocode(CITY_ISOCODE2);
		city2.setName(CITY2, Locale.ENGLISH);

		modelService.save(city1);
		modelService.save(city2);
		modelService.refresh(region);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindCityByNullRegionAndIsocode()
	{
		cityDao.findCityByRegionAndIsocode(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindCityByExistingRegionAndNullIsocode()
	{
		cityDao.findCityByRegionAndIsocode(region, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindCityByNullRegionAndExistingIsocode()
	{
		cityDao.findCityByRegionAndIsocode(null, CITY_ISOCODE1);
	}

	@Test
	public void testFindCityByExistingRegionAndExistingIsocode()
	{
		final List<CityModel> cities = cityDao.findCityByRegionAndIsocode(region, CITY_ISOCODE1);

		Assert.assertNotNull(cities);
		Assert.assertEquals(1, cities.size());
	}

	@Test
	public void testFindCityByExistingRegionAndExistingIsocode2()
	{
		final List<CityModel> cities = cityDao.findCityByRegionAndIsocode(region, CITY_ISOCODE2);

		Assert.assertNotNull(cities);
		Assert.assertEquals(1, cities.size());
	}
}
