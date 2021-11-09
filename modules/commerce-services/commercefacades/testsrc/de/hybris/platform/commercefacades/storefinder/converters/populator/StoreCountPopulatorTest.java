/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storefinder.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.store.data.StoreCountData;
import de.hybris.platform.store.pojo.StoreCountInfo;
import de.hybris.platform.store.pojo.StoreCountType;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class StoreCountPopulatorTest
{
	public static final String COUNTRY = "country";
	public static final String COUNTRY2 = "country2";
	public static final String CO_1 = "co1";
	public static final String CO_2 = "co2";
	public static final String REGION_1 = "region1";
	public static final String RE_1 = "re1";
	public static final String REGION_2 = "region2";
	public static final String RE_2 = "re2";

	private StoreCountPopulator populatorUnderTest = new StoreCountPopulator();
	@Mock
	StoreCountInfo country1Stores, country2Stores, region1Stores, region2Stores;

	@Before
	public void setUp()
	{
		when(country1Stores.getName()).thenReturn(COUNTRY);
		when(country1Stores.getIsoCode()).thenReturn(CO_1);
		when(country1Stores.getCount()).thenReturn(20);
		when(country1Stores.getType()).thenReturn(StoreCountType.COUNTRY);

		when(region1Stores.getName()).thenReturn(REGION_1);
		when(region1Stores.getIsoCode()).thenReturn(RE_1);
		when(region1Stores.getCount()).thenReturn(11);
		when(region1Stores.getType()).thenReturn(StoreCountType.REGION);

		when(region2Stores.getName()).thenReturn(REGION_2);
		when(region2Stores.getIsoCode()).thenReturn(RE_2);
		when(region2Stores.getCount()).thenReturn(9);
		when(region2Stores.getType()).thenReturn(StoreCountType.REGION);

		when(country1Stores.getStoreCountInfoList()).thenReturn(Arrays.asList(region1Stores, region2Stores));

		when(country2Stores.getName()).thenReturn(COUNTRY2);
		when(country2Stores.getIsoCode()).thenReturn(CO_2);
		when(country2Stores.getCount()).thenReturn(50);
		when(country2Stores.getType()).thenReturn(StoreCountType.COUNTRY);
	}

	@Test
	public void shouldPopulateNestedRegions()
	{
		//given
		final StoreCountData storeCountData = new StoreCountData();
		//when
		populatorUnderTest.populate(country1Stores, storeCountData);
		//then check country1 counts
		assertEquals(20, storeCountData.getCount().intValue());
		assertEquals(CO_1, storeCountData.getIsoCode());
		assertEquals(COUNTRY, storeCountData.getName());
		assertEquals(StoreCountType.COUNTRY.toString(), storeCountData.getType());
		assertFalse(storeCountData.getStoreCountDataList().isEmpty());
		//then check region1 counts
		final StoreCountData storeCountDataRe1 = storeCountData.getStoreCountDataList().stream()
				.filter(scd -> scd.getIsoCode().equalsIgnoreCase(RE_1)).findFirst().get();
		assertEquals(RE_1, storeCountDataRe1.getIsoCode());
		assertEquals(REGION_1, storeCountDataRe1.getName());
		assertEquals(StoreCountType.REGION.toString(), storeCountDataRe1.getType());
		assertNull(storeCountDataRe1.getStoreCountDataList());
		//then check region2 counts
		final StoreCountData storeCountDataRe2 = storeCountData.getStoreCountDataList().stream()
				.filter(scd -> scd.getIsoCode().equalsIgnoreCase(RE_2)).findFirst().get();
		assertEquals(RE_2, storeCountDataRe2.getIsoCode());
		assertEquals(REGION_2, storeCountDataRe2.getName());
		assertEquals(StoreCountType.REGION.toString(), storeCountDataRe2.getType());
		assertNull(storeCountDataRe2.getStoreCountDataList());
	}

	@Test
	public void shouldPopulateCountry()
	{
		//given
		final StoreCountData storeCountData = new StoreCountData();
		//when
		populatorUnderTest.populate(country2Stores, storeCountData);
		//then
		assertEquals(50, storeCountData.getCount().intValue());
		assertEquals(CO_2, storeCountData.getIsoCode());
		assertEquals(COUNTRY2, storeCountData.getName());
		assertEquals(StoreCountType.COUNTRY.toString(), storeCountData.getType());
		assertNull(storeCountData.getStoreCountDataList());
	}

	@Test
	public void shouldNotPopulateNull()
	{
		//given
		final StoreCountData storeCountData = new StoreCountData();
		//when
		populatorUnderTest.populate(null, storeCountData);
		//then
		assertNull(storeCountData.getCount());
		assertNull(storeCountData.getIsoCode());
		assertNull(storeCountData.getName());
		assertNull(storeCountData.getStoreCountDataList());
	}
}
