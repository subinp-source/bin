/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.setup.impl;

import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.commerceservices.setup.SetupImpexService;
import de.hybris.platform.servicelayer.ServicelayerTest;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Test suite for {@link DefaultSetupImpexService}
 */
@IntegrationTest
public class DefaultSetupImpexServiceTest extends ServicelayerTest
{
	private static final String TEST_PRODUCT_CATALOG = "productCatalog";

	@Resource
	private SetupImpexService setupImpexService;

	@Resource
	private CatalogService catalogService;


	@Before
	public void setUp() throws Exception
	{
		createCoreData();
	}


	@Test
	public void testImportImpexFile() throws Exception
	{
		setupImpexService.importImpexFile("/commerceservices/test/testSystemSetup.impex", true);
		assertNotNull("Catalog was null", catalogService.getCatalogForId(TEST_PRODUCT_CATALOG));
	}
}
