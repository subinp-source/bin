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
package de.hybris.platform.assistedservicefacades.impl;

import static de.hybris.platform.assistedservicefacades.constants.AssistedservicefacadesConstants.ASM_AGENT_SESSION_TIMER;
import static de.hybris.platform.assistedservicefacades.impl.DefaultAssistedServiceFacade.AGENT_TIMER_KEY;
import static de.hybris.platform.assistedservicefacades.impl.DefaultAssistedServiceFacade.ASM_AGENT_STORE;
import static de.hybris.platform.assistedservicefacades.impl.DefaultAssistedServiceFacade.ASM_CUSTOMER_PROFILE_REFERENCES;
import static de.hybris.platform.assistedservicefacades.impl.DefaultAssistedServiceFacade.ASM_ERROR_MESSAGE_ARGS_KEY;
import static de.hybris.platform.assistedservicefacades.impl.DefaultAssistedServiceFacade.ASM_ERROR_MESSAGE_KEY;
import static de.hybris.platform.assistedservicefacades.impl.DefaultAssistedServiceFacade.CREATE_DISABLED_KEY;
import static de.hybris.platform.assistedservicefacades.impl.DefaultAssistedServiceFacade.DEFAULT_SESSION_TIMER;
import static de.hybris.platform.assistedservicefacades.impl.DefaultAssistedServiceFacade.EMULATE_BY_ORDER_KEY;
import static de.hybris.platform.assistedservicefacades.impl.DefaultAssistedServiceFacade.ERROR_NO_CUSTOMER_OR_CART_ID_PROVIDED;
import static de.hybris.platform.assistedservicefacades.impl.DefaultAssistedServiceFacade.SESSION_CART_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.assistedservicefacades.constants.AssistedservicefacadesConstants;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentBadCredentialsException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentBlockedException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceCustomerLoginDisabledException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceWrongCartIdException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceWrongCustomerIdException;
import de.hybris.platform.assistedserviceservices.utils.CustomerEmulationParams;
import de.hybris.platform.assistedservicefacades.user.data.AutoSuggestionCustomerData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.ticket.enums.EventType;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.localization.Localization;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;


/**
 * Additional tests for ASM Facade, for some methods, that was not involved in atdd tests.
 */
@IntegrationTest
public class AssistedServiceFacadeIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String ASCUSTOMER_UID = "ascustomer";
	private static final String ASAGENT_UID = "asagent";
	private static final String ASAGENT_PWD = "1234";
	@Resource
	private UserService userService;
	@Resource
	private DefaultAssistedServiceFacade assistedServiceFacade;
	@Resource
	private BaseSiteService baseSiteService;
	@Resource
	private CartService cartService;
	@Resource
	private ProductService productService;
	@Resource
	private CommerceCartService commerceCartService;
	@Resource
	private CommerceCheckoutService commerceCheckoutService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/assistedservicefacades/test/asm.impex", "UTF-8");
		baseSiteService.setCurrentBaseSite("testSite", true);
	}

	@Test
	public void testLoginAgentSAML() throws AssistedServiceException
	{
		final EmployeeModel customer = userService.getUserForUID(ASAGENT_UID, EmployeeModel.class);
		// shouldn't throw an exception
		assistedServiceFacade.loginAssistedServiceAgentSAML(customer.getUid(), customer.getEncodedPassword());
	}

	@Test(expected = AssistedServiceAgentBadCredentialsException.class)
	public void testLoginCustomerSAMLFail() throws AssistedServiceException
	{
		// should throw an exception
		assistedServiceFacade.loginAssistedServiceAgentSAML(ASCUSTOMER_UID, "wrong password");
	}

	@Test
	public void testGetSuggestedCustomerList() throws AssistedServiceException
	{
		assistedServiceFacade.launchAssistedServiceMode();
		assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD);

		final List<CustomerData> customerListWithOne = assistedServiceFacade.getSuggestedCustomerList("bre");
		assertEquals(1, customerListWithOne.size());

		final List<CustomerData> customerListWith2 = assistedServiceFacade.getSuggestedCustomerList("joh");
		assertEquals(4, customerListWith2.size());

		final List<CustomerData> emptyCustomerList = assistedServiceFacade.getSuggestedCustomerList("afasfaf");
		assertEquals(0, emptyCustomerList.size());
	}

	@Test
	public void testGetSuggestedCustomerData() throws AssistedServiceException
	{
		assistedServiceFacade.launchAssistedServiceMode();
		assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD);

		final List<AutoSuggestionCustomerData> customerListWithOne = assistedServiceFacade.getSuggestedCustomerData("bre");
		assertEquals(1, customerListWithOne.size());
		assertNotNull(customerListWithOne.get(0).getValue());

		final List<AutoSuggestionCustomerData> customerListWith2 = assistedServiceFacade.getSuggestedCustomerData("joh");
		assertEquals(4, customerListWith2.size());

		final List<AutoSuggestionCustomerData> emptyCustomerList = assistedServiceFacade.getSuggestedCustomerData("afasfaf");
		assertEquals(1, emptyCustomerList.size());
		assertNull(emptyCustomerList.get(0).getValue());
	}

	@Test
	public void testGetCustomerSuggestions()
	{
		final List<AutoSuggestionCustomerData> customerListWithOne = assistedServiceFacade.getCustomerSuggestions("bre");
		assertEquals(1, customerListWithOne.size());
		assertNotNull(customerListWithOne.get(0).getValue());

		final List<AutoSuggestionCustomerData> customerListWith2 = assistedServiceFacade.getCustomerSuggestions("joh");
		assertEquals(4, customerListWith2.size());

		final List<AutoSuggestionCustomerData> emptyCustomerList = assistedServiceFacade.getCustomerSuggestions("afasfaf");
		assertEquals(1, emptyCustomerList.size());
		assertNull(emptyCustomerList.get(0).getValue());
	}


	@Test
	public void testCreateCustomer() throws AssistedServiceException
	{
		final String customerID = "customerID";

		final CustomerData data = assistedServiceFacade.createCustomer(customerID, "John Doe");
		final UserModel customer = userService.getUserForUID(data.getUid());

		assertEquals(StringUtils.lowerCase(customerID), customer.getUid());
		assertEquals("John Doe", customer.getName());
	}

	@Test(expected = AssistedServiceException.class)
	public void testCreateCustomerWithSameUID() throws AssistedServiceException
	{
		final String customerID = "customerID";

		final CustomerData data = assistedServiceFacade.createCustomer(customerID, "John Doe");
		final UserModel customer = userService.getUserForUID(data.getUid());

		assertEquals(StringUtils.lowerCase(customerID), customer.getUid());
		assertEquals("John Doe", customer.getName());

		assistedServiceFacade.createCustomer(customerID, "John Doe");
	}

	@Test(expected = AssistedServiceException.class)
	public void testCreateCustomerWhenCreateDisabled() throws AssistedServiceException
	{
		Config.setParameter(AssistedservicefacadesConstants.CREATE_DISABLED_PROPERTY, "true");
		final String customerID = "customerID";

		assistedServiceFacade.createCustomer(customerID, "John Doe");
	}

	@Test(expected = AssistedServiceWrongCustomerIdException.class)
	public void testEmulateCustomerAndCreateAnOrdu() throws AssistedServiceException
	{
		assistedServiceFacade.launchAssistedServiceMode();
		assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD);
		assistedServiceFacade.emulateCustomer("user1_asagentgroup", null);
	}

	@Test
	public void testEmulateCustomerAndCreateAnOrder()
			throws AssistedServiceException, CommerceCartModificationException, InvalidCartException
	{
		assistedServiceFacade.launchAssistedServiceMode();
		assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD);
		assistedServiceFacade.emulateCustomer(ASCUSTOMER_UID, null);

		assertEquals(cartService.getSessionCart().getCode(), "ascustomerCart"); // check that latest cart being picked up

		// add something to cart
		final CommerceCartParameter cartParameter = new CommerceCartParameter();
		cartParameter.setBaseSite(baseSiteService.getCurrentBaseSite());
		cartParameter.setCart(cartService.getSessionCart());
		cartParameter.setEnableHooks(true);
		cartParameter.setProduct(productService.getProductForCode("HW1210-3422"));
		cartParameter.setQuantity(4);

		commerceCartService.addToCart(cartParameter);
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartService.getSessionCart());
		parameter.setSalesApplication(SalesApplication.WEB);
		final OrderModel result = commerceCheckoutService.placeOrder(parameter).getOrder();

		assertEquals(result.getPlacedBy().getUid(), ASAGENT_UID);
	}


	@Test(expected = AssistedServiceCustomerLoginDisabledException.class)
	public void testEmulateInactiveCustomer() throws AssistedServiceException
	{
		assistedServiceFacade.launchAssistedServiceMode();
		assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD);
		assistedServiceFacade.emulateCustomer("inactivecustomer", null, null);
	}

	@Test(expected = AssistedServiceWrongCustomerIdException.class)
	public void testEmulateNotCustomer() throws AssistedServiceException
	{
		assistedServiceFacade.launchAssistedServiceMode();
		assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD);
		assistedServiceFacade.emulateCustomer("notcustomer", null, null);
	}

	@Test
	public void isCustomerAccountActiveTest()
	{
		final UserModel customer = userService.getUserForUID(ASCUSTOMER_UID);
		customer.setLoginDisabled(true);
		customer.setDeactivationDate(new Date(System.currentTimeMillis() - 1000));
		assertFalse(assistedServiceFacade.isCustomerAccountActive(customer));
		customer.setLoginDisabled(false);
		assertFalse(assistedServiceFacade.isCustomerAccountActive(customer));
		customer.setDeactivationDate(new Date(System.currentTimeMillis() + 10000));
		assertTrue(assistedServiceFacade.isCustomerAccountActive(customer));
		customer.setDeactivationDate(null);
		assertTrue(assistedServiceFacade.isCustomerAccountActive(customer));
	}

	@Test
	public void attachCartToSessionTest() throws AssistedServiceWrongCartIdException
	{
		try
		{
			assistedServiceFacade.attachCartToSession(null, null);
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("user cannot be null", e.getMessage());
		}


		final UserModel user = userService.getUserForUID(ASCUSTOMER_UID);

		assistedServiceFacade.attachCartToSession(null, user);
		try
		{
			assistedServiceFacade.attachCartToSession("invalid_cart_id", user);
		}
		catch (final AssistedServiceWrongCartIdException e)
		{
			assertEquals("Cart ID/Order ID not found", e.getMessage());
		}


		try
		{
			assistedServiceFacade.attachCartToSession(cartService.getSessionCart().getCode(), null);
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("user cannot be null", e.getMessage());
		}

		assertEquals(user, assistedServiceFacade.attachCartToSession(cartService.getSessionCart().getCode(), user));
	}

	@Test
	public void isAssistedServiceModeLaunchedTest()
	{
		assertFalse(assistedServiceFacade.isAssistedServiceModeLaunched());
	}


	@Test
	public void testEmulateCustomerInvalidSession()
	{
		assistedServiceFacade.launchAssistedServiceMode();
		try
		{
			assistedServiceFacade.emulateCustomer(ASCUSTOMER_UID, null);
		}
		catch (final AssistedServiceException e)
		{
			final CustomerEmulationParams params = assistedServiceFacade.getAsmSession().getSavedEmulationData();
			assertEquals(ASCUSTOMER_UID, params.getUserId());
			assertNull(params.getCartId());
		}
		assistedServiceFacade.launchAssistedServiceMode();
		try
		{
			assistedServiceFacade.emulateCustomer(ASCUSTOMER_UID, null);
		}
		catch (final AssistedServiceException e)
		{
			final CustomerEmulationParams params = assistedServiceFacade.getAsmSession().getSavedEmulationData();
			assertEquals(ASCUSTOMER_UID, params.getUserId());
			assertNull(params.getCartId());
		}
		try
		{
			assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD);
		}
		catch (final AssistedServiceException e)
		{
			//do nothing
		}

		try
		{
			assistedServiceFacade.emulateCustomer(null, null);
		}
		catch (final AssistedServiceException e)
		{
			assertEquals(Localization.getLocalizedString(ERROR_NO_CUSTOMER_OR_CART_ID_PROVIDED), e.getMessage());
		}

		final CartModel cart = cartService.getSessionCart();

		try
		{
			assistedServiceFacade.emulateCustomer(null, cart.getCode());
		}
		catch (final AssistedServiceException e)
		{
			assertEquals("Unknown customer id. Will not add customer and/or cart to the session.", e.getMessage());
		}

	}

	@Test
	public void testBindCustomerToCart() throws AssistedServiceException
	{
		// run mode, login agent, we are anonym
		assistedServiceFacade.launchAssistedServiceMode();
		assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD);
		// we have session cart
		final CartModel sessionCart = cartService.getSessionCart();

		// ascustomer as ascustomerCart, but we will bing new cart to him
		assistedServiceFacade.bindCustomerToCart(ASCUSTOMER_UID, sessionCart.getCode());
		// we still use that cart
		assertTrue(cartService.getSessionCart().getCode().equals(sessionCart.getCode()));
		assertTrue(cartService.getSessionCart().getUser().getUid().equals(ASCUSTOMER_UID));
	}

	@Test(expected = AssistedServiceAgentBlockedException.class)
	public void testBruteForce() throws Exception
	{
		assistedServiceFacade.launchAssistedServiceMode();
		final EmployeeModel agent = userService.getUserForUID(ASAGENT_UID, EmployeeModel.class);
		for (int i = 0; i < 3; i++)
		{
			try
			{ //3rd time will throw AssistedServiceAgentBlockedException
				assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD + i);
			}
			catch (final AssistedServiceAgentBadCredentialsException e)
			{
				/* that is fine */ }
		}
	}

	@Test
	public void testAssistedServiceAgentStore()
	{
		final EmployeeModel agent = userService.getUserForUID(ASAGENT_UID, EmployeeModel.class);
		final String agentStore = assistedServiceFacade.getAssistedServiceAgentStore(agent);
		assertTrue("asm.emulate.no_stores".equals(agentStore));
	}

	@Test
	public void testLogoutAssistedServiceAgent() throws AssistedServiceException
	{
		assistedServiceFacade.launchAssistedServiceMode();
		assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD);
		assistedServiceFacade.emulateCustomer(ASCUSTOMER_UID, null);

		assistedServiceFacade.logoutAssistedServiceAgent();

		assertFalse(assistedServiceFacade.isAssistedServiceAgent(userService.getCurrentUser()));
	}

	@Test(expected = AssistedServiceException.class)
	public void testVerifyAssistedServiceAgent() throws Exception
	{
		assistedServiceFacade.verifyAssistedServiceAgent(userService.getUserForUID(ASCUSTOMER_UID));
	}


	@Test
	public void testAssistedServiceSessionTimeout() throws Exception
	{
		final Integer timeout = 100;
		Config.setParameter(AssistedservicefacadesConstants.ASM_AGENT_SESSION_TIMEOUT, timeout.toString());
		assertTrue(timeout == assistedServiceFacade.getAssistedServiceSessionTimeout());
	}

	@Test
	public void testAssistedServiceSessionTimerValue() throws Exception
	{
		final Integer timer = 100;
		Config.setParameter(ASM_AGENT_SESSION_TIMER, timer.toString());
		assertTrue(timer == assistedServiceFacade.getAssistedServiceSessionTimerValue());
	}

	@Test
	public void testQuitASM() throws AssistedServiceException
	{
		assistedServiceFacade.launchAssistedServiceMode();
		assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD);
		assistedServiceFacade.emulateCustomer(ASCUSTOMER_UID, null);

		assistedServiceFacade.quitAssistedServiceMode();

		assertFalse(assistedServiceFacade.isAssistedServiceAgent(userService.getCurrentUser()));
	}

	@Test
	public void testEmulateAfterLogin() throws AssistedServiceException
	{
		assistedServiceFacade.launchAssistedServiceMode();
		assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD);
		assistedServiceFacade.emulateAfterLogin();

		assistedServiceFacade.quitAssistedServiceMode();

		assertFalse(assistedServiceFacade.isAssistedServiceAgent(userService.getCurrentUser()));
	}

	@Test
	public void testStopEmulateCustomer() throws AssistedServiceException, CommerceCartModificationException, InvalidCartException
	{
		final DefaultAssistedServiceFacade assistedServiceFacadeSpy = spy(assistedServiceFacade);
		testEmulateCustomerAndCreateAnOrder();
		final UserModel expectedAgent = assistedServiceFacade.getAsmSession().getAgent();

		final ArgumentCaptor<UserModel> agent = ArgumentCaptor.forClass(UserModel.class);
		final ArgumentCaptor<UserModel> customer = ArgumentCaptor.forClass(UserModel.class);
		final ArgumentCaptor<EventType> type = ArgumentCaptor.forClass(EventType.class);

		assistedServiceFacadeSpy.stopEmulateCustomer();

		verify(assistedServiceFacadeSpy).createSessionEvent(agent.capture(), customer.capture(), type.capture());
		assertEquals(expectedAgent, agent.getValue());
		assertEquals(null, customer.getValue());
		assertEquals(EventType.END_SESSION_EVENT, type.getValue());

		assertNull(assistedServiceFacadeSpy.getAsmSession().getEmulatedCustomer());
		assertNull(assistedServiceFacadeSpy.getSessionService().getAttribute(ASM_CUSTOMER_PROFILE_REFERENCES));

		assertTrue(CollectionUtils.isEmpty(assistedServiceFacadeSpy.getCartService().getSessionCart().getEntries()));
		assertTrue(assistedServiceFacadeSpy.getUserService()
				.isAnonymousUser(assistedServiceFacadeSpy.getUserService().getCurrentUser()));
	}

	@Test
	public void testGetAssistedServiceSessionAttributes() throws AssistedServiceException
	{
		assistedServiceFacade.launchAssistedServiceMode();
		assistedServiceFacade.loginAssistedServiceAgent(ASAGENT_UID, ASAGENT_PWD);
		assistedServiceFacade.getAsmSession().setFlashErrorMessage("testMessage");
		assistedServiceFacade.getAsmSession().setFlashErrorMessageArgs("testArgs");
		// we have session cart
		final CartModel sessionCart = cartService.getSessionCart();

		// ascustomer as ascustomerCart, but we will bing new cart to him
		assistedServiceFacade.bindCustomerToCart(ASCUSTOMER_UID, sessionCart.getCode());

		Map<String, Object> attributes = assistedServiceFacade.getAssistedServiceSessionAttributes();
		assertEquals(sessionCart, attributes.get(SESSION_CART_KEY));
		assertEquals(String.valueOf(Config.getInt(ASM_AGENT_SESSION_TIMER, DEFAULT_SESSION_TIMER)),
				attributes.get(AGENT_TIMER_KEY));
		assertEquals(Config.getParameter(AssistedservicefacadesConstants.CREATE_DISABLED_PROPERTY),
				attributes.get(CREATE_DISABLED_KEY));
		assertEquals(Config.getParameter(AssistedservicefacadesConstants.EMULATE_BY_ORDER_PROPERTY),
				attributes.get(EMULATE_BY_ORDER_KEY));
		assertEquals(assistedServiceFacade.getAssistedServiceAgentStore(assistedServiceFacade.getAsmSession().getAgent()),
				attributes.get(ASM_AGENT_STORE));
		assertEquals("testMessage", attributes.get(ASM_ERROR_MESSAGE_KEY));
		assertEquals("testArgs", attributes.get(ASM_ERROR_MESSAGE_ARGS_KEY));

		assistedServiceFacade.getAsmSession().setFlashErrorMessage(null);
		attributes = assistedServiceFacade.getAssistedServiceSessionAttributes();
		assertFalse(attributes.containsKey(ASM_ERROR_MESSAGE_ARGS_KEY));
		assertFalse(attributes.containsKey(ASM_ERROR_MESSAGE_ARGS_KEY));
	}
}
