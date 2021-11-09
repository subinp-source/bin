/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.basesites.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commercefacades.basesite.data.BaseSiteData;
import de.hybris.platform.commercefacades.basesites.BaseSiteFacade;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import javax.annotation.Resource;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;



/**
 * Integration test suite for {@link de.hybris.platform.commercefacades.basesites.impl.DefaultBaseSiteFacade}
 */
@IntegrationTest
public class DefaultBaseSiteFacadeIntegrationTest extends ServicelayerTest
{
	private static final String LANG_EN = "en";
	private static final String BASE_SITE_UID = "storetemplate";
	private static final String BASE_STORE_NAME = "Default Store";
	private static final String BASE_SITE_THEME =  null;
	private static final String BASE_SITE_CHANNEL = "B2C";

	@Resource
	private BaseSiteFacade baseSiteFacade;

	@Resource
	private CommonI18NService commonI18NService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		importCsv("/commercefacades/test/testBaseSite.csv", "UTF-8");
		commonI18NService.setCurrentLanguage(commonI18NService.getLanguage(LANG_EN));
	}

	@Test
	public void getAllBaseSites()
	{
		final List<BaseSiteData> baseSites = baseSiteFacade.getAllBaseSites();

		assertNotNull(baseSites);
		assertTrue(baseSites.stream().anyMatch(baseSite -> baseSite.getUid().equals(BASE_SITE_UID)));

		final Optional<BaseSiteData> baseSiteData = baseSites.stream().filter(baseSite -> baseSite.getUid().equals(BASE_SITE_UID))
				.findFirst();
		assertTrue(baseSiteData.isPresent());
		assertEquals(BASE_SITE_UID, baseSiteData.get().getUid());
		assertTrue(baseSiteData.get().getStores().stream().anyMatch(baseStore -> baseStore.getName().equals(BASE_STORE_NAME)));
		assertEquals(BASE_SITE_THEME, baseSiteData.get().getTheme());
		assertEquals(LANG_EN, baseSiteData.get().getDefaultLanguage().getIsocode());
		assertEquals(BASE_SITE_CHANNEL, baseSiteData.get().getChannel());
	}
}
