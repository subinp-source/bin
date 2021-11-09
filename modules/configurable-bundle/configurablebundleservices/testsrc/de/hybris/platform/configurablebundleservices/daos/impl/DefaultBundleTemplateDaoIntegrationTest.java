/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.configurablebundleservices.daos.BundleTemplateDao;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/**
 * Integration test suite for {@link DefaultBundleTemplateDao}
 * 
 */
@IntegrationTest
public class DefaultBundleTemplateDaoIntegrationTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(DefaultBundleTemplateDaoIntegrationTest.class);
	private static final String TEST_BASESITE_UID = "testSite";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Resource
	private UserService userService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private BundleTemplateDao bundleTemplateDao;

	@Resource
	private ProductService productService;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private ModelService modelService;

	private ProductModel galaxyNexus;
	private ProductModel noBundleProduct;
	private ProductModel planStandard1Y;

	@Before
	public void setUp() throws Exception
	{
		// final Create data for tests
		LOG.info("Creating data for DefaultBundleTemplateDaoIntegrationTest ...");
		userService.setCurrentUser(userService.getAdminUser());
		final long startTime = System.currentTimeMillis();
		createCoreData();
		createDefaultCatalog();

		// importing test csv
		final String legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		LOG.info("Existing value for " + ImpExConstants.Params.LEGACY_MODE_KEY + " :" + legacyModeBackup);
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		importCsv("/commerceservices/test/testCommerceCart.csv", "utf-8");
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "false");
		importCsv("/configurablebundleservices/test/testBundleCommerceCartService.impex", "utf-8");
		importCsv("/configurablebundleservices/test/testApproveAllBundleTemplates.impex", "utf-8");
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);

		LOG.info("Finished data for DefaultBundleTemplateDaoIntegrationTest " + (System.currentTimeMillis() - startTime) + "ms");

		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);
		catalogVersionService.setSessionCatalogVersion("testCatalog", "Online");

		modelService.detachAll();

		galaxyNexus = productService.getProductForCode("GALAXY_NEXUS");
		noBundleProduct = productService.getProductForCode("2047052");
		planStandard1Y = productService.getProductForCode("PLAN_STANDARD_1Y");
	}

	@Test
	public void testFindBundleTemplatesByProductNoResults()
	{
		final List<BundleTemplateModel> actual = bundleTemplateDao.findBundleTemplatesByProduct(noBundleProduct);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void testFindBundleTemplatesByProductOneResult()
	{
		final List<BundleTemplateModel> actual = bundleTemplateDao.findBundleTemplatesByProduct(galaxyNexus);
		assertEquals(1, actual.size());

		BundleTemplateModel template = actual.get(0);
		assertFalse(template.getProducts().isEmpty());
		assertTrue(template.getProducts().contains(galaxyNexus));
	}

	@Test
	public void testFindBundleTemplatesByProduct()
	{
		final List<BundleTemplateModel> actual = bundleTemplateDao.findBundleTemplatesByProduct(planStandard1Y);
		assertEquals(2, actual.size());

		for (final BundleTemplateModel template : actual)
		{
			assertFalse(template.getProducts().isEmpty());
			assertTrue(template.getProducts().contains(planStandard1Y));
		}

		Set<String> bundleTemplateIds = actual.stream()
				.map(BundleTemplateModel::getId)
				.collect(Collectors.toSet());

		assertTrue(bundleTemplateIds.contains("IPhonePlanSelection"));
		assertTrue(bundleTemplateIds.contains("SmartPhonePlanSelection"));
	}

	@Test
	public void testFindBundleByIdAndVersion()
	{
		final BundleTemplateModel validSmartPhoneDeviceSelection = bundleTemplateDao.findBundleTemplateByIdAndVersion(
				"SmartPhonePlanSelection", "1.0");
		assertNotNull(validSmartPhoneDeviceSelection);
	}

	@Test
	public void testCannotFindBundleByIdAndVersion()
	{
		thrown.expect(ModelNotFoundException.class);
		bundleTemplateDao.findBundleTemplateByIdAndVersion("SmartPhonePlanSelection", "1.1");
	}
}
