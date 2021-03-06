/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressfacades.populators;

import static org.mockito.Mockito.doNothing;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.addressfacades.data.CityData;
import de.hybris.platform.addressfacades.data.DistrictData;
import de.hybris.platform.addressservices.address.AddressService;
import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.addressservices.model.DistrictModel;
import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import junit.framework.Assert;


@UnitTest
public class ChineseAddressReversePopulatorTest
{
	private static String FIRST_NAME = "Jenny";
	private static String LAST_NAME = "Xu";
	private static String FULL_NAME = "Jenny Xu";
	private static String COUNTRY_ISOCODE = "CN";
	private static String COUNTRY_NAME = "CHINA";
	private ChineseAddressReversePopulator populator;

	private AddressModel addressModel;

	private CityData city;

	private DistrictData district;

	private AddressModel target;

	private CountryData country;

	@Mock
	private AddressData source;

	@Mock
	private AddressService chineseAddressService;

	@Mock
	private CityModel cityModel;

	@Mock
	private CountryModel countryModel;

	@Mock
	private DistrictModel districtModel;

	@Mock
	private AddressReversePopulator superPopulator;

	@Mock
	private CommonI18NService commonI18NService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		populator = new ChineseAddressReversePopulator();
		populator.setChineseAddressService(chineseAddressService);
		populator.setCommonI18NService(commonI18NService);
		addressModel = new AddressModel();
		target = new AddressModel();
		target.setRegion(new RegionModel());

		country = new CountryData();
		country.setIsocode(COUNTRY_ISOCODE);
		country.setName(COUNTRY_NAME);

		district = new DistrictData();
		district.setCode("districtcode");
		district.setName("districtname");

		city = new CityData();
		city.setCode("city");
		city.setName("cityName");

		doNothing().when(superPopulator).populate(Mockito.anyObject(), Mockito.anyObject());

		Mockito.when(chineseAddressService.getCityForRegionAndIsocode(Mockito.anyObject(), Mockito.anyString()))
				.thenReturn(cityModel);
		Mockito.when(chineseAddressService.getDistrictForCityAndIsocode(Mockito.anyObject(), Mockito.anyString()))
				.thenReturn(districtModel);
		Mockito.when(commonI18NService.getCountry(Mockito.anyString())).thenReturn(countryModel);
		Mockito.when(cityModel.getName()).thenReturn("cityName");

	}

	@Test(expected = IllegalArgumentException.class)
	public void test_populator_null_params()
	{
		populator.populate(null, null);
	}

	@Test
	public void test_populator()
	{
		Mockito.when(source.getFirstName()).thenReturn(FIRST_NAME);
		Mockito.when(source.getLastName()).thenReturn(LAST_NAME);
		Mockito.when(source.getCity()).thenReturn(city);
		Mockito.when(source.getCityDistrict()).thenReturn(district);
		Mockito.when(source.getCountry()).thenReturn(country);
		populator.populate(source, target);

		Assert.assertEquals(FIRST_NAME, target.getFirstname());
		Assert.assertEquals(LAST_NAME, target.getLastname());
		Assert.assertEquals(cityModel, target.getCity());
		Assert.assertEquals(districtModel, target.getCityDistrict());
	}

	@Test
	public void test_populator_district()
	{
		Mockito.when(source.getCityDistrict()).thenReturn(district);
		Mockito.when(source.getCountry()).thenReturn(country);
		populator.populate(source, target);

		Assert.assertEquals(districtModel, target.getCityDistrict());
	}

	@Test
	public void test_populator_city()
	{
		Mockito.when(source.getCity()).thenReturn(city);
		Mockito.when(source.getCountry()).thenReturn(country);
		populator.populate(source, target);

		Assert.assertEquals(cityModel, target.getCity());
	}

	@Test
	public void test_populator_fillname()
	{
		addressModel.setFullname(FULL_NAME);
		Mockito.when(source.getFullname()).thenReturn(FULL_NAME);
		populator.populate(source, target);

		Assert.assertEquals(FULL_NAME, target.getFirstname());
		Assert.assertEquals(StringUtils.EMPTY, target.getLastname());
	}

}
