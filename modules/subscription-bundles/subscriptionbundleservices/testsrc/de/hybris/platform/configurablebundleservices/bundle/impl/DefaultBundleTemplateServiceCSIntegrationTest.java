/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.bundle.impl;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

import java.util.Collections;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test suite for {@link BundleTemplateService}. Contains tests moved from
 * {@link DefaultBundleTemplateServiceIntegrationTest}.
 */
@IntegrationTest
public class DefaultBundleTemplateServiceCSIntegrationTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(DefaultBundleTemplateServiceCSIntegrationTest.class);
	private static final String TEST_BASESITE_UID = "testSite";

	@Resource
	private UserService userService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private BundleTemplateService bundleTemplateService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private ProductService productService;

	@Resource
	private UnitService unitService;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private ModelService modelService;

	private BundleTemplateModel bundleSmartPhoneDeviceSelection;
	private BundleTemplateModel bundleSmartPhonePlanSelection;
	private ProductModel galaxyNexus;
	private ProductModel planStandard1Y;

	@Before
	public void setUp() throws Exception
	{
		// final Create data for tests
		LOG.info("Creating data for DefaultBundleTemplateServiceCSIntegrationTest ...");
		userService.setCurrentUser(userService.getAdminUser());
		final long startTime = System.currentTimeMillis();
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
		// importing test csv
		final String legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		LOG.info("Existing value for " + ImpExConstants.Params.LEGACY_MODE_KEY + " :" + legacyModeBackup);
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		importCsv("/commerceservices/test/testCommerceCart.csv", "utf-8");
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "false");
		importCsv("/subscriptionservices/test/testSubscriptionCommerceCartService.impex", "utf-8");
		importCsv("/configurablebundleservices/test/testBundleCommerceCartService.impex", "utf-8");
		importCsv("/configurablebundleservices/test/testApproveAllBundleTemplates.impex", "utf-8");
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);

		LOG.info("Finished data for DefaultBundleTemplateServiceCSIntegrationTest " + (System.currentTimeMillis() - startTime) + "ms");

		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);
		catalogVersionService.setSessionCatalogVersion("testCatalog", "Online");

		modelService.detachAll();
	}

	private void setupBundleTemplates()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getSessionCatalogVersions().iterator().next();

		bundleSmartPhonePlanSelection = getBundleTemplateByIdAndCatalogVersion("SmartPhonePlanSelection", catalogVersionModel);
		bundleSmartPhoneDeviceSelection = getBundleTemplateByIdAndCatalogVersion("SmartPhoneDeviceSelection", catalogVersionModel);
	}

	private void setupProducts()
	{
		galaxyNexus = productService.getProductForCode("GALAXY_NEXUS");
		planStandard1Y = productService.getProductForCode("PLAN_STANDARD_1Y");
	}

	private BundleTemplateModel getBundleTemplateByIdAndCatalogVersion(final String bundleId,
			final CatalogVersionModel catalogVersionModel)
	{
		final BundleTemplateModel exampleModel = new BundleTemplateModel();
		exampleModel.setId(bundleId);
		exampleModel.setCatalogVersion(catalogVersionModel);

		return flexibleSearchService.getModelByExample(exampleModel);
	}

	@Test
	public void testGetBundleTemplateById()
	{
		setupBundleTemplates();
		final BundleTemplateModel bundleTemplate = bundleTemplateService.getBundleTemplateForCode("SmartPhonePackage");
		Assert.assertNotNull(bundleTemplate);
	}

}