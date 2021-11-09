/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.address.daos.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.addressservices.model.DistrictModel;
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
 * Tests for {@link DefaultDistrictDao}
 */
@IntegrationTest
public class DefaultDistrictDaoTest extends ServicelayerTransactionalTest
{
	@Resource
	private DefaultDistrictDao districtDao;

	@Resource
	private ModelService modelService;

	private DistrictModel district1;
	private DistrictModel district2;

	private CityModel city;

	private final String COUNTRY_ISOCODE = "CN";
	private final String REGION_ISOCODE = "CN-11";
	private final String CITY_ISOCODE = "CN-11-1";
	private final String CITY_ISOCODE2 = "CN-12-1";
	private final String CITY = "Beijing";
	private final String CITY2 = "Tianjing";
	private final String DISTRICT_ISOCODE1 = "CN-11-1-3";
	private final String DISTRICT_ISOCODE2 = "CN-11-1-1";
	private final String DISTRICT1 = "DaXing";
	private final String DISTRICT2 = "DongCheng";

	@Before
	public void prepare()
	{
		final CountryModel country = new CountryModel();
		country.setIsocode(COUNTRY_ISOCODE);

		final RegionModel region = new RegionModel();
		region.setIsocode(REGION_ISOCODE);
		region.setCountry(country);

		city = new CityModel();
		city.setRegion(region);
		city.setIsocode(CITY_ISOCODE);
		city.setName(CITY, Locale.ENGLISH);

		final CityModel city2 = new CityModel();
		city2.setRegion(region);
		city2.setIsocode(CITY_ISOCODE2);
		city2.setName(CITY, Locale.ENGLISH);

		final DistrictModel district1 = new DistrictModel();
		district1.setCity(city);
		district1.setName(DISTRICT1, Locale.ENGLISH);
		district1.setIsocode(DISTRICT_ISOCODE1);

		final DistrictModel district2 = new DistrictModel();
		district2.setCity(city2);
		district2.setName(DISTRICT2, Locale.ENGLISH);
		district2.setIsocode(DISTRICT_ISOCODE2);

		modelService.save(district1);
		modelService.save(district2);
		modelService.refresh(city);
		modelService.refresh(region);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindDistrictByNullCityAndIsocode() 
	{
		districtDao.findDistrictByCityAndIsocode(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindDistrictByExistingCityAndNullIsocode()
	{
		districtDao.findDistrictByCityAndIsocode(city, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindDistrictByNullCityAndExistingIsocode()
	{
		districtDao.findDistrictByCityAndIsocode(null, DISTRICT_ISOCODE1);
	}

	@Test
	public void testFindDistrictByExistingCityAndExistingIsocode()
	{
		final List<DistrictModel> districts = districtDao.findDistrictByCityAndIsocode(city, DISTRICT_ISOCODE1);

		Assert.assertNotNull(districts);
		Assert.assertEquals(1, districts.size());
	}

	@Test
	public void testFindDistrictByExistingCityAndExistingIsocode2()
	{
		final List<DistrictModel> districts = districtDao.findDistrictByCityAndIsocode(city, DISTRICT_ISOCODE2);

		Assert.assertNotNull(districts);
		Assert.assertEquals(0, districts.size());
	}
}
