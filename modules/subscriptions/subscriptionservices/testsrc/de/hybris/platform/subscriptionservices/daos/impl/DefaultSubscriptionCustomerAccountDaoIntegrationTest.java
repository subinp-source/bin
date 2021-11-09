/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.daos.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.util.BaseCommerceBaseTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commerceservices.customer.dao.CustomerAccountDao;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.subscriptionservices.subscription.SubscriptionCommerceCartService;
import de.hybris.platform.util.AppendSpringConfiguration;
import de.hybris.platform.util.Config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;


/**
 * Integration test suite for CustomerAccountDao with subscriptions parent filter.
 */
@IntegrationTest
@AppendSpringConfiguration("classpath:subscriptionservices-spring-test-context.xml")
public class DefaultSubscriptionCustomerAccountDaoIntegrationTest extends BaseCommerceBaseTest
{
	private static final Logger LOG = Logger.getLogger(DefaultSubscriptionCustomerAccountDaoIntegrationTest.class);
	private static final String TEST_BASESITE_UID = "testSite";
	private static final String FIND_ORDERS_ADDITIONAL_FILTER_ENABLED = "commerceservices.find.orders.additional.filter.enabled";

	@Resource
	private CustomerAccountDao customerAccountDao;

	@Resource
	private UserService userService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private ProductService productService;

	@Resource
	private UnitService unitService;

	@Resource
	private SubscriptionCommerceCartService subscriptionCommerceCartService;

	@Resource
	private CommerceCheckoutService commerceCheckoutService;

	@Resource
	private ModelService modelService;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private PriceFactory testPriceFactory;

	private PriceFactory defaultPriceFactory;

	private ProductModel galaxyNexus;
	private ProductModel planStandard1Y;
	private CartModel telcoMasterCart;
	private UnitModel unitModel;
	private UserModel userTelco;
	private BaseStoreModel baseStore;

	@Before
	public void setUp() throws Exception
	{
		// final Create data for tests
		LOG.info("Creating data for DefaultSubscriptionCustomerAccountDaoIntegrationTest ..");

		defaultPriceFactory = JaloSession.getCurrentSession().getPriceFactory();
		JaloSession.getCurrentSession().setPriceFactory(testPriceFactory);

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
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);

		LOG.info("Finished data for DefaultSubscriptionCustomerAccountDaoIntegrationTest "
				+ (System.currentTimeMillis() - startTime) + "ms");

		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);
		catalogVersionService.setSessionCatalogVersion("testCatalog", "Online");

		modelService.detachAll();

		userTelco = userService.getUserForUID("telco");
		final Collection<CartModel> cartModels = userTelco.getCarts();
		assertEquals("", 1, cartModels.size());
		telcoMasterCart = cartModels.iterator().next();
		unitModel = unitService.getUnitForCode("pieces");

		baseStore = modelService.create(BaseStoreModel.class);
		baseStore.setUid("uid");
		modelService.save(baseStore);

		galaxyNexus = productService.getProductForCode("GALAXY_NEXUS");
		planStandard1Y = productService.getProductForCode("PLAN_STANDARD_1Y");
	}

	@Test
	public void testFindOrdersByCustomerAndStore() throws CommerceCartModificationException, InvalidCartException
	{
		final CommerceCartModification modProduct = subscriptionCommerceCartService.addToCart(telcoMasterCart, galaxyNexus, 1,
				unitModel, false);
		assertNotNull("", modProduct);
		final CommerceCartModification modPlan = subscriptionCommerceCartService.addToCart(telcoMasterCart, planStandard1Y, 1,
				unitModel, false);
		assertNotNull("", modPlan);

		assertEquals("", 1, telcoMasterCart.getChildren().size());
		assertEquals("", 2, telcoMasterCart.getEntries().size());

		final OrderModel masterOrder = commerceCheckoutService.placeOrder(telcoMasterCart);
		assertEquals("", 1, masterOrder.getChildren().size());
		assertEquals("", 2, masterOrder.getEntries().size());
		final OrderModel childOrder = (OrderModel) masterOrder.getChildren().iterator().next();

		// fake the base store
		masterOrder.setStore(baseStore);
		masterOrder.setStatus(OrderStatus.CREATED);
		modelService.save(masterOrder);
		childOrder.setStore(baseStore);
		childOrder.setStatus(OrderStatus.CREATED);
		modelService.save(childOrder);

		final OrderStatus[] orderStatus = {};

		final List<OrderModel> orders = customerAccountDao.findOrdersByCustomerAndStore((CustomerModel) userTelco, baseStore,
				orderStatus);
		assertEquals("", 1, orders.size());
		final OrderModel selectedOrder = orders.iterator().next();
		assertEquals("", masterOrder, selectedOrder);

		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(0);
		pageableData.setSort("sort");
		pageableData.setPageSize(5);

		final SearchPageData<OrderModel> orderHistory = customerAccountDao.findOrdersByCustomerAndStore((CustomerModel) userTelco,
				baseStore, orderStatus, pageableData);
		assertEquals("", 1, orderHistory.getResults().size());
		final OrderModel selectedOrder2 = orderHistory.getResults().iterator().next();
		assertEquals("", masterOrder, selectedOrder2);
	}
	
	@Test
	public void testFindOrdersByCustomerAndStoreWithDisabledAdditionalFilter() throws CommerceCartModificationException, InvalidCartException
	{
		Config.setParameter(FIND_ORDERS_ADDITIONAL_FILTER_ENABLED, "false");
		final CommerceCartModification modProduct = subscriptionCommerceCartService.addToCart(telcoMasterCart, galaxyNexus, 1,
				unitModel, false);
		assertNotNull("", modProduct);
		final CommerceCartModification modPlan = subscriptionCommerceCartService.addToCart(telcoMasterCart, planStandard1Y, 1,
				unitModel, false);
		assertNotNull("", modPlan);

		assertEquals("", 1, telcoMasterCart.getChildren().size());
		assertEquals("", 2, telcoMasterCart.getEntries().size());

		final OrderModel masterOrder = commerceCheckoutService.placeOrder(telcoMasterCart);
		assertEquals("", 1, masterOrder.getChildren().size());
		assertEquals("", 2, masterOrder.getEntries().size());
		final OrderModel childOrder = (OrderModel) masterOrder.getChildren().iterator().next();

		// fake the base store
		masterOrder.setStore(baseStore);
		masterOrder.setStatus(OrderStatus.CREATED);
		modelService.save(masterOrder);
		childOrder.setStore(baseStore);
		childOrder.setStatus(OrderStatus.CREATED);
		modelService.save(childOrder);

		final OrderStatus[] orderStatus = {};

		final List<OrderModel> orders = customerAccountDao.findOrdersByCustomerAndStore((CustomerModel) userTelco, baseStore,
				orderStatus);
		assertEquals("", 2, orders.size());

		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(0);
		pageableData.setSort("sort");
		pageableData.setPageSize(5);

		final SearchPageData<OrderModel> orderHistory = customerAccountDao.findOrdersByCustomerAndStore((CustomerModel) userTelco,
				baseStore, orderStatus, pageableData);
		assertEquals("", 2, orderHistory.getResults().size());
	}

	@After
	public void tearDown()
	{
		JaloSession.getCurrentSession().setPriceFactory(defaultPriceFactory);
	}
}
