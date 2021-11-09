/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webservicescommons.util;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.testframework.Assert;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

//import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



@IntegrationTest
public class LocalViewExecutorIntegrationTest extends ServicelayerTest
{
	@Resource
	LocalViewExecutor localViewExecutor;

	@Resource
	CatalogVersionService catalogVersionService;

	@Before
	public void setup() throws Exception
	{
		createCoreData();
		createDefaultUsers();
		createDefaultCatalog();
		createHardwareCatalog();
	}

	@Test
	public void testExecuteInLocalView()
	{
		//given
		catalogVersionService.setSessionCatalogVersions(Collections.emptyList());
		Assert.assertCollection(Collections.emptyList(), catalogVersionService.getSessionCatalogVersions());

		//when
		localViewExecutor.executeInLocalView(() -> {
			final Collection<CatalogVersionModel> allCatalogVersions = catalogVersionService.getAllCatalogVersions();
			catalogVersionService.setSessionCatalogVersions(allCatalogVersions);

			Assert.assertCollection(allCatalogVersions, catalogVersionService.getSessionCatalogVersions());

			return null;
		});

		//then
		Assert.assertCollection(Collections.emptyList(), catalogVersionService.getSessionCatalogVersions());
	}

	@Test
	public void testExecuteWithCatalogs()
	{
		//given
		catalogVersionService.setSessionCatalogVersions(Collections.emptyList());
		Assert.assertCollection(Collections.emptyList(), catalogVersionService.getSessionCatalogVersions());
		//when
		localViewExecutor.executeWithAllCatalogs(() -> {

			final Collection<CatalogVersionModel> allCatalogVersions = catalogVersionService.getAllCatalogVersions();
			Assert.assertCollection(allCatalogVersions, catalogVersionService.getSessionCatalogVersions());

			return null;
		});

		//then
		Assert.assertCollection(Collections.emptyList(), catalogVersionService.getSessionCatalogVersions());
	}
}
