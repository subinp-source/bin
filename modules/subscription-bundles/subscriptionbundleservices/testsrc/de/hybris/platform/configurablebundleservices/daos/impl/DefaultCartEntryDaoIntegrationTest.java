/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.daos.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.configurablebundleservices.daos.OrderEntryDao;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test suite for {@link DefaultCartEntryDao}
 * 
 */
@IntegrationTest
public class DefaultCartEntryDaoIntegrationTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(DefaultCartEntryDaoIntegrationTest.class);
	private static final String TEST_BASESITE_UID = "testSite";

	@Resource
	private CommerceCartService commerceCartService;

	@Resource
	private UserService userService;

	@Resource
	private ProductService productService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private UnitService unitService;

	@Resource
	private OrderEntryDao bundleCartEntryDao;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private ModelService modelService;

	private static final String PRODUCT_MODEL_CODE_GALAXY_NEXUS = "GALAXY_NEXUS";
	private static final String PRODUCT_MODEL_CODE_IPHONE = "APPLE_IPHONE_4S_16GB";
	private static final String SUBSCRIPTION_PRODUCT_CODE_MONTHLY = "Y_STARTER_100_1Y";
	private static final String SUBSCRIPTION_PRODUCT_CODE_QUARTERLY = "Y_STARTER_200_2Y";
	private static final String SUBSCRIPTION_PRODUCT_CODE_YEARLY = "Y_STARTER_300_2Y";
	private static final String SUBSCRIPTION_PRODUCT_CODE_PAYNOW = "Y_STARTER_100_2Y";

	private ProductModel productModelGalaxyNexus;
	private ProductModel productModelIPhone;
	private ProductModel subscriptionProductModelMonthly;
	private ProductModel subscriptionProductModelYearly;
	private ProductModel subscriptionProductModelPaynow;
	private ProductModel subscriptionProductModelQuarterly;
	private UnitModel unitModel;


	private BundleTemplateModel bundleSmartPhoneDeviceSelection;
	private BundleTemplateModel bundleSmartPhonePlanSelection;
	private BundleTemplateModel bundleSmartPhoneValuePackSelection2;
	private BundleTemplateModel bundleIPhoneDeviceSelection;
	private BundleTemplateModel bundleIPhonePlanSelection;
	private BundleTemplateModel bundleIPhoneAddonSelection;

	@Before
	public void setUp() throws Exception
	{
		// final Create data for tests
		LOG.info("Creating data for DefaultCartEntryDaoIntegrationTest ...");
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

		LOG.info("Finished data for DefaultCartEntryDaoIntegrationTest " + (System.currentTimeMillis() - startTime) + "ms");

		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);
		catalogVersionService.setSessionCatalogVersion("testCatalog", "Online");

		modelService.detachAll();
	}

	@Test
	public void testFindEntries() throws CommerceCartModificationException
	{
		setupProducts();
		setupBundleTemplates();
		final UserModel telco = userService.getUserForUID("telco");
		final Collection<CartModel> cartModels = telco.getCarts();
		assertEquals(1, cartModels.size());
		final CartModel telcoMasterCart = cartModels.iterator().next();

		// Add new entry (device product, standalone)
		addToCart(telcoMasterCart, productModelGalaxyNexus, 2, unitModel, 0, null);
		// Add new entry (device product, new bundle (number=1))
		addToCart(telcoMasterCart, productModelGalaxyNexus, 1, unitModel,  -1,
				bundleSmartPhoneDeviceSelection);
		// Add new entry (plan product with billing freq = monthly, existing bundle (number=1))
		addToCart(telcoMasterCart, subscriptionProductModelMonthly, 1, unitModel, 1,
				bundleSmartPhonePlanSelection);
		// Add new entry (plan product with billing freq = yearly, existing bundle (number=1))
		addToCart(telcoMasterCart, subscriptionProductModelYearly, 1, unitModel, 1,
				bundleSmartPhoneValuePackSelection2);
		// Add new entry (device product, new bundle (number=2))
		addToCart(telcoMasterCart, productModelIPhone, 1, unitModel, -1, bundleIPhoneDeviceSelection);
		// Add new entry (plan product with billing freq = paynow, existing bundle (number=2))
		addToCart(telcoMasterCart, subscriptionProductModelPaynow, 1, unitModel, 2,
				bundleIPhoneAddonSelection);
		// Add new entry (plan product with billing freq = quarterly, existing bundle (number=2))
		addToCart(telcoMasterCart, subscriptionProductModelQuarterly, 1, unitModel, 2,
				bundleIPhonePlanSelection);
		// Add new entry (plan product with billing freq = quarterly, standalone)
		addToCart(telcoMasterCart, subscriptionProductModelQuarterly, 1, unitModel, 0, null);

		//Multi cart looks like this now :
		//@formatter:off
		//	  
		//	  - master cart (pay now):
		//	    - 2 PRODUCT_MODEL_CODE (Standalone)
		//	    - 1 PRODUCT_MODEL_CODE (bundle 1)
		//     - 1 PRODUCT_MODEL_CODE (bundle 2)
		//     - 1 SUBSCRIPTION_PRODUCT_CODE_PAYNOW (bundle 2)
		//	  - child cart (monthly):
		//	    - 1 SUBSCRIPTION_PRODUCT_CODE_MONTHLY (bundle 1)
		//	  - child cart (yearly):
		//	    - 1 SUBSCRIPTION_PRODUCT_CODE_YEARLY (bundle 1)
		//   - child cart (quarterly):
		//     - 1 SUBSCRIPTION_PRODUCT_CODE_QUARTERLY (bundle 2)
		//     - 1 SUBSCRIPTION_PRODUCT_CODE_QUARTERLY (Standalone)
		//
		//@formatter:on

		final List<CartEntryModel> bundleEntries = bundleCartEntryDao.findEntriesByMasterCartAndBundleNo(telcoMasterCart, 1);

		assertEquals(8, bundleEntries.size());

		for (final CartEntryModel cartEntry : bundleEntries)
		{
			//YTODO assertEquals(telcoMasterCart, cartEntry.getMasterAbstractOrder());
		}

		final List<CartEntryModel> bundleTemplateEntries = bundleCartEntryDao.findEntriesByMasterCartAndBundleNoAndTemplate(
				telcoMasterCart, 2, bundleIPhonePlanSelection);

		assertEquals(8, bundleTemplateEntries.size());

	}

	private void setupProducts()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		productModelGalaxyNexus = productService.getProductForCode(catalogVersionModel, PRODUCT_MODEL_CODE_GALAXY_NEXUS);
		productModelIPhone = productService.getProductForCode(catalogVersionModel, PRODUCT_MODEL_CODE_IPHONE);
		subscriptionProductModelMonthly = productService.getProductForCode(catalogVersionModel, SUBSCRIPTION_PRODUCT_CODE_MONTHLY);
		subscriptionProductModelYearly = productService.getProductForCode(catalogVersionModel, SUBSCRIPTION_PRODUCT_CODE_YEARLY);
		subscriptionProductModelPaynow = productService.getProductForCode(catalogVersionModel, SUBSCRIPTION_PRODUCT_CODE_PAYNOW);
		subscriptionProductModelQuarterly = productService.getProductForCode(catalogVersionModel, SUBSCRIPTION_PRODUCT_CODE_QUARTERLY);
		unitModel = unitService.getUnitForCode("pieces");
	}

	private void setupBundleTemplates()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getSessionCatalogVersions().iterator().next();

		bundleSmartPhoneDeviceSelection = getBundleTemplateByIdAndCatalogVersion("SmartPhoneDeviceSelection", catalogVersionModel);
		bundleSmartPhonePlanSelection = getBundleTemplateByIdAndCatalogVersion("SmartPhonePlanSelection", catalogVersionModel);
		//bundleSmartPhoneValuePackSelection1 = getBundleTemplateByIdAndCatalogVersion("SmartPhoneValuePackSelection1", catalogVersionModel);
		bundleSmartPhoneValuePackSelection2 = getBundleTemplateByIdAndCatalogVersion("SmartPhoneValuePackSelection2",
				catalogVersionModel);
		bundleIPhoneDeviceSelection = getBundleTemplateByIdAndCatalogVersion("IPhoneDeviceSelection", catalogVersionModel);
		bundleIPhonePlanSelection = getBundleTemplateByIdAndCatalogVersion("IPhonePlanSelection", catalogVersionModel);
		bundleIPhoneAddonSelection = getBundleTemplateByIdAndCatalogVersion("IPhoneAddonSelection", catalogVersionModel);
	}

	private BundleTemplateModel getBundleTemplateByIdAndCatalogVersion(final String bundleId,
			final CatalogVersionModel catalogVersionModel)
	{
		final BundleTemplateModel exampleModel = new BundleTemplateModel();
		exampleModel.setId(bundleId);
		exampleModel.setCatalogVersion(catalogVersionModel);

		return flexibleSearchService.getModelByExample(exampleModel);
	}

	private CommerceCartModification addToCart(final CartModel cart, final ProductModel productModel, final long quantity,
											   final UnitModel unit, int bundleNo,
											   BundleTemplateModel bundleTemplateModel) throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setCart(cart);
		parameter.setProduct(productModel);
		parameter.setQuantity(quantity);
		parameter.setUnit(unit);
		parameter.setBundleTemplate(bundleTemplateModel);
		parameter.setEnableHooks(true);
		return commerceCartService.addToCart(parameter);
	}

}