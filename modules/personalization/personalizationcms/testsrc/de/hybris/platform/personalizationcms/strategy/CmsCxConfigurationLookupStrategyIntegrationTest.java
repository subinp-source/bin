/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationcms.strategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.site.BaseSiteService;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CmsCxConfigurationLookupStrategyIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String CONTENT_CATALOG_WITH_SINGLE_BASE_SITE = "testContentCatalog";
	private static final String CONTENT_CATALOG_WITH_MULTIPLE_BASE_SITE = "contentCatalogWithMultipleBaseSite";
	private static final String CATALOG_VERSION = "Online";

	@Resource(name = "cmsCxConfigurationLookupStrategy")
	private CmsCxConfigurationLookupStrategy strategy;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private CatalogVersionService catalogVersionService;


	@Before
	public void setupTest() throws Exception
	{
		createCoreData();
		importData(new ClasspathImpExResource("/personalizationcms/test/testdata_cxconfig.impex", "UTF-8"));
	}

	@Test
	public void getConfigurationForCatalogVersionTest()
	{
		//given
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(CONTENT_CATALOG_WITH_SINGLE_BASE_SITE,
				CATALOG_VERSION);

		//when
		final Set<CxConfigModel> configuration = strategy.getConfigurations(catalogVersion);

		//then
		assertNotNull(configuration);
		assertEquals(1, configuration.size());
		assertTrue(isCatalogVersionRelated(configuration.iterator().next(), catalogVersion));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getConfigurationWithNullCatalogVersionTest()
	{
		//when
		strategy.getConfigurations(null);
	}

	@Test
	public void getConfigurationForCatalogVersionWithMultipleSitesTest()
	{
		//given
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(CONTENT_CATALOG_WITH_MULTIPLE_BASE_SITE,
				CATALOG_VERSION);

		//when
		final Set<CxConfigModel> configuration = strategy.getConfigurations(catalogVersion);

		//then
		assertNotNull(configuration);
		assertEquals(3, configuration.size());
		assertTrue(isCatalogVersionRelated(configuration.iterator().next(), catalogVersion));
	}

	private boolean isCatalogVersionRelated(final CxConfigModel config, final CatalogVersionModel catalogVersion)
	{
		return config.getBaseSites().stream()//
				.filter(site -> site instanceof CMSSiteModel)//
				.map(cmsSite -> (CMSSiteModel) cmsSite)//
				.map(cmsSite -> cmsSite.getContentCatalogs())//
				.flatMap(catalogs -> catalogs.stream())//
				.map(c -> c.getCatalogVersions())//
				.flatMap(catalogVersions -> catalogVersions.stream())//
				.anyMatch(cv -> cv.equals(catalogVersion));
	}
}
