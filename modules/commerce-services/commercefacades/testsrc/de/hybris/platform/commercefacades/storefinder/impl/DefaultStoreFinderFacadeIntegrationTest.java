/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storefinder.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.store.data.StoreCountData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import javax.annotation.Resource;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class DefaultStoreFinderFacadeIntegrationTest extends ServicelayerTest
{
	private static final String SITE_NAME = "testSite";
	@Resource
	private DefaultStoreFinderFacade storeFinderFacade;
	@Resource
	private BaseSiteService baseSiteService;
	@Resource
	private BaseStoreService baseStoreService;

	private BaseSiteModel testBaseSite;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/commercefacades/test/testPointOfService.csv", "UTF-8");
		testBaseSite = baseSiteService.getBaseSiteForUID(SITE_NAME);
		baseSiteService.setCurrentBaseSite(testBaseSite, false);
		testBaseSite.setStores(baseStoreService.getAllBaseStores());
	}

	@Test
	public void testGetStoreCounts()
	{
		final List<StoreCountData> result = storeFinderFacade.getStoreCounts();
		assertNotNull(result);
		assertTrue(result.size() == 1);

		final StoreCountData germany = result.stream().filter(scd -> scd.getIsoCode().equalsIgnoreCase("DE")).findFirst().get();
		assertEquals("Germany", germany.getName());
		assertEquals("COUNTRY", germany.getType());
		assertEquals("DE", germany.getIsoCode());
		assertEquals(5, germany.getCount().intValue());


		final StoreCountData storeCountRegionBW = getRegionStoreCount(result, "DE-BW");
		assertNotNull(storeCountRegionBW);
		assertEquals("Baden-Wurttemberg", storeCountRegionBW.getName());
		assertEquals("REGION", storeCountRegionBW.getType());
		assertEquals("DE-BW", storeCountRegionBW.getIsoCode());
		assertEquals(2, storeCountRegionBW.getCount().intValue());

		final StoreCountData storeCountRegionBY = getRegionStoreCount(result, "DE-BY");
		assertNotNull(storeCountRegionBW);
		assertEquals("Bayern", storeCountRegionBY.getName());
		assertEquals("REGION", storeCountRegionBY.getType());
		assertEquals("DE-BY", storeCountRegionBY.getIsoCode());
		assertEquals(3, storeCountRegionBY.getCount().intValue());
	}

	@Test
	public void testCountryNoRegions()
	{
		final List<StoreCountData> result = storeFinderFacade.getStoreCounts();
		assertFalse(result.stream().anyMatch(scd -> scd.getIsoCode().equalsIgnoreCase("CA")));
	}

	@Test
	public void getPointsOfServiceForCountry()
	{
		final	List<PointOfServiceData> result = storeFinderFacade.getPointsOfServiceForCountry("DE");
		assertEquals(5, result.size());
		assertTrue(result.stream().allMatch(pos -> String.valueOf("DE").equals(pos.getAddress().getCountry().getIsocode())));
	}

	@Test
	public void getPointsOfServiceForCountryNoResults()
	{
		final	List<PointOfServiceData> result = storeFinderFacade.getPointsOfServiceForCountry("US");
		assertEquals(0, result.size());
	}

	@Test
	public void getPointsOfServiceForRegion()
	{
		final	List<PointOfServiceData> result = storeFinderFacade.getPointsOfServiceForRegion("DE", "DE-BY");
		assertEquals(3, result.size());
		assertTrue(result.stream().allMatch(pos -> String.valueOf("DE-BY").equals(pos.getAddress().getRegion().getIsocode())));
	}

	@Test
	public void getPointsOfServiceForRegionNoResults()
	{
		final	List<PointOfServiceData> result = storeFinderFacade.getPointsOfServiceForRegion("DE", "DE-BB");
		assertEquals(0, result.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetStoreCountsNullStore()
	{
		testBaseSite.setStores(null);
		storeFinderFacade.getStoreCounts();
	}

	/**
	 * gets {@link StoreCountData} by isocode for a given collection of region store count
	 *
	 * @param regionsStoreCounts
	 * @param isoCode
	 * @return {@link StoreCountData}
	 */
	protected StoreCountData getRegionStoreCount(final List<StoreCountData> regionsStoreCounts, final String isoCode)
	{
		return regionsStoreCounts.stream().findFirst().get().getStoreCountDataList().stream()
				.filter(storeCountData -> storeCountData.getIsoCode().equalsIgnoreCase(isoCode)).findAny().get();
	}

}
