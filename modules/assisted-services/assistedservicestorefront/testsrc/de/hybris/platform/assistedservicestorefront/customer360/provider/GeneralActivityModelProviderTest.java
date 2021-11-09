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
package de.hybris.platform.assistedservicestorefront.customer360.provider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedservicestorefront.customer360.GeneralActivityData;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.CommerceSaveCartService;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.ticket.service.TicketService;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class GeneralActivityModelProviderTest
{
	@Mock
	private OrderFacade orderFacade;
	@Mock
	private TicketService ticketService;
	@Mock
	private TicketBusinessService ticketBusinessService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private UserService userService;
	@Mock
	private CartFacade cartFacade;
	@Mock
	private Converter<CsTicketModel, GeneralActivityData> ticketConverter;
	@Mock
	private CommerceCartService commerceCartService;
	@Mock
	private CommerceSaveCartService commerceSaveCartService;
	@Mock
	private CustomerAccountService customerAccountService;
	@Mock
	private BaseStoreService baseStoreService;

	@InjectMocks
	private GeneralActivityModelProvider provider = new GeneralActivityModelProvider();

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testDataProviding()
	{
		final CustomerModel currentCustomer = Mockito.mock(CustomerModel.class);
		final CartData sessionCart = Mockito.mock(CartData.class);
		final CartModel cartModel = Mockito.mock(CartModel.class);
		final CurrencyModel currency = Mockito.mock(CurrencyModel.class);
		when(currency.getSymbol()).thenReturn("");
		when(cartModel.getEntries()).thenReturn(Collections.emptyList());
		when(cartModel.getCurrency()).thenReturn(currency);
		when(cartModel.getModifiedtime()).thenReturn(new Date());
		when(cartModel.getCode()).thenReturn("ascustomerCart");

		final CsTicketModel ticketModel = Mockito.mock(CsTicketModel.class);

		when(baseStoreService.getCurrentBaseStore()).thenReturn(null);
		when(userService.getCurrentUser()).thenReturn(currentCustomer);
		when(baseSiteService.getCurrentBaseSite()).thenReturn(null);
		when(cartFacade.getMiniCart()).thenReturn(sessionCart);
		when(ticketConverter.convert(any())).thenReturn(new GeneralActivityData());
		when(commerceCartService.getCartsForSiteAndUser(any(), any())).thenReturn(Collections.singletonList(cartModel));

		final SearchPageData<CartModel> cartData = new SearchPageData<>();
		cartData.setResults(Collections.singletonList(cartModel));

		when(commerceSaveCartService.getSavedCartsForSiteAndUser(any(), any(), any(), any())).thenReturn(cartData);

		when(ticketService.getTicketsForCustomer(Mockito.anyObject())).thenReturn(Collections.emptyList());


		final OrderModel orderModel = Mockito.mock(OrderModel.class);
		when(orderModel.getEntries()).thenReturn(Collections.emptyList());
		when(orderModel.getCurrency()).thenReturn(currency);
		when(orderModel.getModifiedtime()).thenReturn(new Date());
		when(orderModel.getCode()).thenReturn("ascustomerOrder");

		final SearchPageData<OrderModel> orderData = new SearchPageData<>();
		orderData.setResults(Collections.singletonList(orderModel));
		when(customerAccountService.getOrderList(any(), any(), any(), any())).thenReturn(orderData);

		final String name = "name";
		final String tId = "123124";
		final String cardId = "cartId";
		final Integer cartSize = Integer.valueOf(2);
		when(sessionCart.getEntries()).thenReturn(null);
		when(sessionCart.getTotalUnitCount()).thenReturn(cartSize);
		when(sessionCart.getCode()).thenReturn(cardId);
		when(currentCustomer.getName()).thenReturn(name);
		when(currentCustomer.getProfilePicture()).thenReturn(null);
		when(ticketModel.getTicketID()).thenReturn(tId);
		final GeneralActivityData resultCart = provider.getModel(new HashMap<>()).get(1);
		final GeneralActivityData resultOrder = provider.getModel(new HashMap<>()).get(0);

		assertEquals("text.customer360.activity.general.cart", resultCart.getType());
		assertEquals("ascustomerCart", resultCart.getId());
		assertEquals("text.customer360.activity.general.order", resultOrder.getType());
		assertEquals("ascustomerOrder", resultOrder.getId());
	}
}
