/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressfacades.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.addressfacades.data.CityData;
import de.hybris.platform.addressfacades.data.DistrictData;
import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.addressservices.model.DistrictModel;
import de.hybris.platform.addressservices.strategies.NameWithTitleFormatStrategy;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ChineseAddressPopulatorTest
{
	private static final String CITY_ISOCODE = "CN-11-1";
	private static final String DISTRICT_ISOCODE = "CN-11-1-1";
	private static final String CITY_NAME = "Beijing";
	private static final String DISTRICT_NAME = "Dongcheng";
	private static final String CHINESE_FIRST_NAME = "建中";
	private static final String CHINESE_LAST_NAME = "张";
	private static final String CHINESE_FULLNAME = "张建中";
	private static final String MR_TITLE_NAME = "Mr";
	private static final String ENGLISH_ISOCODE = "en";
	private final AddressModel addressModel = new AddressModel();
	@Mock
	private I18NService i18NService;
	@Mock
	private Converter<CityModel, CityData> cityConverter;
	@Mock
	private Converter<DistrictModel, DistrictData> districtConverter;
	@Mock
	private NameWithTitleFormatStrategy nameWithTitleFormatStrategy;

	private ChineseAddressPopulator chineseAddressPopulator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		chineseAddressPopulator = new ChineseAddressPopulator(i18NService, cityConverter, districtConverter,
				nameWithTitleFormatStrategy);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateAddressDataWithSourceNull()
	{
		final AddressModel addressModel = null;
		final AddressData addressData = new AddressData();
		chineseAddressPopulator.populate(addressModel, addressData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateAddressDataWithTargetNull()
	{
		final AddressData addressData = null;
		chineseAddressPopulator.populate(addressModel, addressData);
	}

	@Test
	public void testPopulateAddressDataWithNullFullName()
	{
		final CityModel cityModel = Mockito.spy(new CityModel());
		final DistrictModel districtModel = Mockito.spy(new DistrictModel());
		final Locale currentLocale = new Locale(ENGLISH_ISOCODE);
		initializeCommonData(cityModel, districtModel, addressModel, currentLocale);

		final CityData cityData = new CityData();
		cityData.setCode(CITY_ISOCODE);
		cityData.setName(CITY_NAME);

		final DistrictData districtData = new DistrictData();
		districtData.setCode(DISTRICT_ISOCODE);
		districtData.setName(DISTRICT_NAME);
		addressModel.setFirstname(CHINESE_FIRST_NAME);
		addressModel.setLastname(CHINESE_LAST_NAME);

		final AddressData addressData = new AddressData();
		final String expectedFullName = MR_TITLE_NAME + " " + CHINESE_LAST_NAME + " " + CHINESE_FIRST_NAME;

		Mockito.when(i18NService.getCurrentLocale()).thenReturn(currentLocale);
		Mockito.when(nameWithTitleFormatStrategy.getFullnameWithTitle(CHINESE_FIRST_NAME, CHINESE_LAST_NAME, MR_TITLE_NAME))
				.thenReturn(expectedFullName);
		Mockito.doReturn(CITY_NAME).when(cityModel).getName();
		Mockito.doReturn(DISTRICT_NAME).when(districtModel).getName();
		Mockito.when(cityConverter.convert(cityModel)).thenReturn(cityData);
		Mockito.when(districtConverter.convert(districtModel)).thenReturn(districtData);
		chineseAddressPopulator.populate(addressModel, addressData);

		Assert.assertEquals(CITY_ISOCODE, addressData.getCity().getCode());
		Assert.assertEquals(CITY_NAME, addressData.getCity().getName());
		Assert.assertEquals(DISTRICT_ISOCODE, addressData.getCityDistrict().getCode());
		Assert.assertEquals(DISTRICT_NAME, addressData.getCityDistrict().getName());
		Assert.assertEquals(expectedFullName, addressData.getFullnameWithTitle());
	}

	@Test
	public void testPopulateAddressDataWithExistingFullName()
	{
		final CityModel cityModel = Mockito.spy(new CityModel());
		final DistrictModel districtModel = Mockito.spy(new DistrictModel());
		final Locale currentLocale = new Locale(ENGLISH_ISOCODE);
		initializeCommonData(cityModel, districtModel, addressModel, currentLocale);

		final CityData cityData = new CityData();
		cityData.setCode(CITY_ISOCODE);
		cityData.setName(CITY_NAME);

		final DistrictData districtData = new DistrictData();
		districtData.setCode(DISTRICT_ISOCODE);
		districtData.setName(DISTRICT_NAME);

		addressModel.setFullname(CHINESE_FULLNAME);

		final AddressData addressData = new AddressData();
		final String expectedFullName = MR_TITLE_NAME + CHINESE_FULLNAME;

		Mockito.when(i18NService.getCurrentLocale()).thenReturn(currentLocale);
		Mockito.when(nameWithTitleFormatStrategy.getFullnameWithTitle(CHINESE_FULLNAME, MR_TITLE_NAME))
				.thenReturn(expectedFullName);
		Mockito.doReturn(CITY_NAME).when(cityModel).getName();
		Mockito.doReturn(DISTRICT_NAME).when(districtModel).getName();
		Mockito.when(cityConverter.convert(cityModel)).thenReturn(cityData);
		Mockito.when(districtConverter.convert(districtModel)).thenReturn(districtData);

		chineseAddressPopulator.populate(addressModel, addressData);

		Assert.assertEquals(CITY_ISOCODE, addressData.getCity().getCode());
		Assert.assertEquals(CITY_NAME, addressData.getCity().getName());
		Assert.assertEquals(DISTRICT_ISOCODE, addressData.getCityDistrict().getCode());
		Assert.assertEquals(DISTRICT_NAME, addressData.getCityDistrict().getName());
		Assert.assertEquals(expectedFullName, addressData.getFullnameWithTitle());
	}

	@Test
	public void testExtractCityWithCityNull()
	{
		final AddressData addressData = new AddressData();
		chineseAddressPopulator.populateCity(addressModel, addressData);
		Assert.assertNull(addressData.getCity());
	}

	@Test
	public void testExtractCityWithCityNotNull()
	{
		final AddressData addressData = new AddressData();
		final CityData cityData = new CityData();
		cityData.setCode(CITY_ISOCODE);
		cityData.setName(CITY_NAME);
		final CityModel cityModel = Mockito.spy(new CityModel());
		addressModel.setCity(cityModel);

		Mockito.when(cityModel.getIsocode()).thenReturn(CITY_ISOCODE);
		Mockito.when(cityConverter.convert(cityModel)).thenReturn(cityData);
		chineseAddressPopulator.populateCity(addressModel, addressData);

		Assert.assertEquals(CITY_ISOCODE, addressData.getCity().getCode());
		Assert.assertEquals(CITY_NAME, addressData.getCity().getName());
	}

	@Test
	public void testExtractDistrictWithDistrictNull()
	{
		final AddressData addressData = new AddressData();
		chineseAddressPopulator.populateDistrict(addressModel, addressData);
		Assert.assertNull(addressData.getCityDistrict());
	}

	@Test
	public void testExtractDistrictWithDistrictNotNull()
	{
		final AddressData addressData = new AddressData();
		final DistrictData districtData = new DistrictData();
		districtData.setCode(DISTRICT_ISOCODE);
		districtData.setName(DISTRICT_NAME);
		final DistrictModel districtModel = Mockito.spy(new DistrictModel());
		addressModel.setCityDistrict(districtModel);

		Mockito.when(districtModel.getIsocode()).thenReturn(DISTRICT_ISOCODE);
		Mockito.when(districtConverter.convert(districtModel)).thenReturn(districtData);
		chineseAddressPopulator.populateDistrict(addressModel, addressData);

		Assert.assertEquals(DISTRICT_ISOCODE, addressData.getCityDistrict().getCode());
		Assert.assertEquals(DISTRICT_NAME, addressData.getCityDistrict().getName());
	}

	@Test
	public void testExtractTitleName()
	{
		final TitleModel titleModel = new TitleModel();
		final Locale locale = new Locale(ENGLISH_ISOCODE);
		titleModel.setName(MR_TITLE_NAME, locale);

		Mockito.when(i18NService.getCurrentLocale()).thenReturn(locale);

		Assert.assertEquals(MR_TITLE_NAME, chineseAddressPopulator.extractTitleName(titleModel));
	}

	protected void initializeCommonData(final CityModel cityModel, final DistrictModel districtModel, final AddressModel addressModel, final Locale locale)
	{
		cityModel.setIsocode(CITY_ISOCODE);
		districtModel.setIsocode(DISTRICT_ISOCODE);

		final TitleModel titleModel = new TitleModel();
		titleModel.setName(MR_TITLE_NAME, locale);

		addressModel.setTitle(titleModel);
		addressModel.setCity(cityModel);
		addressModel.setCityDistrict(districtModel);
	}
}
