/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.dao.SaveCartDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


public class DefaultSaveCartDaoIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String USER = "abrode";
	private static final String CART_CODE = "abrodeCart";
	private static final String TEST_BASESITE1_UID = "testSite";
	private static final String TEST_BASESITE2_UID = "testSite2";

	@Resource
	private SaveCartDao saveCartDao;
	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;
	@Resource
	private BaseSiteService baseSiteService;
	@Resource
	private TimeService timeService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private CartService cartService;

	@Before
	public void setUp() throws Exception
	{
		// importing test csv
		importCsv("/commerceservices/test/testCommerceCart.csv", "utf-8");
	}

	@Test
	public void getSavedCartsCountForSiteAndUserTest()
	{
		final UserModel user = userService.getUserForUID(USER);
		final BaseSiteModel baseSite1 = baseSiteService.getBaseSiteForUID(TEST_BASESITE1_UID);
		final BaseSiteModel baseSite2 = baseSiteService.getBaseSiteForUID(TEST_BASESITE2_UID);

		final int originalCountWithNullBaseSite = saveCartDao.getSavedCartsCountForSiteAndUser(null, user).intValue();
		final int originalCountWithBaseSite1 = saveCartDao.getSavedCartsCountForSiteAndUser(baseSite1, user).intValue();
		final int originalCountWithBaseSite2 = saveCartDao.getSavedCartsCountForSiteAndUser(baseSite2, user).intValue();

		final CartModel modelByExample = new CartModel();
		modelByExample.setCode(CART_CODE);

		CartModel cartToBeSaved = flexibleSearchService.getModelByExample(modelByExample);
		populateCartModel(cartToBeSaved, user, null);

		//save 1 cart with save time is null
		cartToBeSaved.setSaveTime(null);
		modelService.save(cartToBeSaved);
		int countWithNullBaseSite = saveCartDao.getSavedCartsCountForSiteAndUser(null, user).intValue();
		assertEquals(originalCountWithNullBaseSite, countWithNullBaseSite);

		//save 1 cart with baseSite1
		cartToBeSaved.setSaveTime(timeService.getCurrentTime());
		cartToBeSaved.setSite(baseSite1);
		modelService.save(cartToBeSaved);

		countWithNullBaseSite = saveCartDao.getSavedCartsCountForSiteAndUser(null, user).intValue();
		final int countWithBaseSite1 = saveCartDao.getSavedCartsCountForSiteAndUser(baseSite1, user).intValue();
		assertEquals(originalCountWithBaseSite1 + 1, countWithBaseSite1);
		assertEquals(originalCountWithNullBaseSite + 1, countWithNullBaseSite);

		//save 1 session cart with baseSite2
		userService.setCurrentUser(user);
		cartToBeSaved = cartService.getSessionCart();
		populateCartModel(cartToBeSaved, user, baseSite2);
		modelService.save(cartToBeSaved);

		countWithNullBaseSite = saveCartDao.getSavedCartsCountForSiteAndUser(null, user).intValue();
		final int countWithBaseSite2 = saveCartDao.getSavedCartsCountForSiteAndUser(baseSite2, user).intValue();
		assertEquals(originalCountWithBaseSite2 + 1, countWithBaseSite2);
		assertEquals(originalCountWithNullBaseSite + 2, countWithNullBaseSite);
	}

	@Test
	public void getSavedCartsForUserAndStatusTest()
	{
		final UserModel lesley = userService.getUserForUID("lesley");

		final List<CartModel> lesleyCarts = flexibleSearchService.<CartModel>search("GET {Cart} WHERE {user}=?user",
				  Map.of("user", lesley)).getResult();
		assertThat(lesleyCarts).isNotNull().hasSize(2).doesNotContainNull();

		final CartModel createdCart = lesleyCarts.get(0);
		final CartModel suspendedCart = lesleyCarts.get(1);

		markAsSavedCartWithStatus(createdCart, OrderStatus.CREATED);
		markAsSavedCartWithStatus(suspendedCart, OrderStatus.SUSPENDED);
		modelService.saveAll();

		final List<CartModel> lesleyAllSavedCarts = getSavedCartsForUserAndStatus(lesley);
		assertThat(lesleyAllSavedCarts).isNotNull().hasSize(2).doesNotContainNull().containsOnly(createdCart, suspendedCart);

		final List<CartModel> lesleyCreatedSavedCarts = getSavedCartsForUserAndStatus(lesley, OrderStatus.CREATED);
		assertThat(lesleyCreatedSavedCarts).isNotNull().hasSize(1).doesNotContainNull().containsOnly(createdCart);

		final List<CartModel> lesleySuspendedSavedCarts = getSavedCartsForUserAndStatus(lesley, OrderStatus.SUSPENDED);
		assertThat(lesleySuspendedSavedCarts).isNotNull().hasSize(1).doesNotContainNull().containsOnly(suspendedCart);

		final List<CartModel> lesleyCreatedOrSuspendedSavedCarts = getSavedCartsForUserAndStatus(lesley, OrderStatus.CREATED, OrderStatus.SUSPENDED);
		assertThat(lesleyCreatedOrSuspendedSavedCarts).isNotNull().hasSize(2).doesNotContainNull().containsOnly(createdCart, suspendedCart);
	}

	private List<CartModel> getSavedCartsForUserAndStatus(final UserModel user, OrderStatus ... statuses)
	{
		final PageableData pd = new PageableData();
		pd.setPageSize(1000);
		return saveCartDao.getSavedCartsForSiteAndUser(pd, null, user, List.of(statuses))
					  .getResults();
	}

	private void markAsSavedCartWithStatus(CartModel cart, OrderStatus status) {
		cart.setStatus(status);
		cart.setSavedBy(cart.getUser());
		cart.setSaveTime(timeService.getCurrentTime());
	}

	private void populateCartModel(final CartModel cart, final UserModel user, final BaseSiteModel baseSite)
	{
		cart.setName("name");
		cart.setDescription("description");
		cart.setSavedBy(user);
		cart.setSite(baseSite);

		final Date currentDate = timeService.getCurrentTime();
		cart.setSaveTime(currentDate);
	}

}
