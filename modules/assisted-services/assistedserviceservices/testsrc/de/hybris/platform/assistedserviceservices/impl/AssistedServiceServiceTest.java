/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedserviceservices.impl;

import static de.hybris.platform.assistedserviceservices.impl.DefaultAssistedServiceService.NOT_ANONYMOUS_CART_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Test class for {@link DefaultAssistedServiceService}
 */
@IntegrationTest
public class AssistedServiceServiceTest extends ServicelayerTransactionalTest
{
	private final String customerUID = "ascustomer";
	private final String firstCart = "00000001";
	private final String secondCart = "00000002";
	@Resource
	private DefaultAssistedServiceService assistedServiceService;
	@Resource
	private BaseSiteService baseSiteService;
	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;
	@Resource
	private CartService cartService;

	@Before
	public void setup() throws Exception
	{
		importCsv("/assistedserviceservices/test/cart_data.impex", "UTF-8");
		baseSiteService.setCurrentBaseSite("testSite", true);
	}

	@Test
	public void latestModifiedCartTest()
	{
		assertEquals("00000002", assistedServiceService.getLatestModifiedCart(userService.getUserForUID("ascustomer")).getCode());
		assertEquals("00000003", assistedServiceService.getLatestModifiedCart(userService.getUserForUID("ascustomer2")).getCode());
		assertNull(assistedServiceService.getLatestModifiedCart(userService.getUserForUID("ascustomer3")));
	}

	@Test
	public void testGetCartByCode()
	{
		final CartModel firstCartModel = assistedServiceService.getCartByCode(firstCart, userService.getUserForUID(customerUID));
		assertTrue(firstCartModel.getCode().equals(firstCart));

		final CartModel secondCartModel = assistedServiceService.getCartByCode(secondCart, userService.getUserForUID(customerUID));
		assertTrue(secondCartModel.getCode().equals(secondCart));
	}

	@Test
	public void testGetCarts()
	{
		final Collection<CartModel> cartsForCustomer = assistedServiceService
				.getCartsForCustomer((CustomerModel) userService.getUserForUID(customerUID));
		assertTrue(cartsForCustomer.stream().anyMatch(cartModel -> cartModel.getCode().equals(firstCart)));
		assertTrue(cartsForCustomer.stream().anyMatch(cartModel -> cartModel.getCode().equals(secondCart)));
	}

	@Test
	public void restoreCartToUserTest()
	{
		final CartService spyService = spy(cartService);
		final ModelService modelServiceSpy = spy(modelService);
		final UserModel user = userService.getUserForUID(customerUID);
		final CartModel cart = cartService.getSessionCart();
		cart.setEntries(new ArrayList<>());
		assistedServiceService.setCartService(spyService);
		assistedServiceService.setModelService(modelServiceSpy);
		assistedServiceService.restoreCartToUser(null, null);
		assistedServiceService.restoreCartToUser(cart, null);
		assistedServiceService.restoreCartToUser(cart, user);
		verify(spyService, never()).changeCurrentCartUser(any());
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(new AbstractOrderEntryModel());
		cart.setEntries(entries);
		doNothing().when(spyService).changeCurrentCartUser(any());
		doNothing().when(modelServiceSpy).refresh(any());
		assistedServiceService.restoreCartToUser(cart, user);
		verify(spyService).changeCurrentCartUser(any());
		verify(modelServiceSpy).refresh(any());
	}

	@Test
	public void bindCustomerToCartTest()
	{
		final UserModel user = userService.getUserForUID(customerUID);
		final CartModel cart = assistedServiceService.getCartByCode(firstCart, user);
		cartService.setSessionCart(cart);

		try
		{
			assistedServiceService.bindCustomerToCart(null, firstCart);
		}
		catch (final AssistedServiceException e)
		{
			assertEquals(Localization.getLocalizedString(NOT_ANONYMOUS_CART_ERROR), e.getMessage());
		}
	}
}
