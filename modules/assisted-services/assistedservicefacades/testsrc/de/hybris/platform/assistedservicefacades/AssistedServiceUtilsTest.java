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
package de.hybris.platform.assistedservicefacades;

import static de.hybris.platform.assistedservicefacades.util.AssistedServiceUtils.CREATION_DATE_FORMAT;
import static de.hybris.platform.assistedservicefacades.util.AssistedServiceUtils.getTimeSince;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedservicefacades.constants.AssistedservicefacadesConstants;
import de.hybris.platform.assistedservicefacades.util.AssistedServiceUtils;
import de.hybris.platform.assistedservicefacades.util.TimeSince;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.util.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


@UnitTest
public class AssistedServiceUtilsTest
{

	private static final String TEST_COOKIE_NAME = "testCookieName";
	private static final String TEST_VALUE = "testValue";
	private static final String BASE_SITE_UID_1 = "testBaseSiteUid1";
	private static final String ORDER_GUID_1 = "testOrderGuid1";
	private static final String USER_UID = "testUserUid";
	private static final String BASESITE_URL = "url1";
	private static final String BASE_SITE_UID_2 = "testBaseSiteUid2";
	private static final String DEEPLINK_PARAM = "/assisted-service/emulate";

	@BeforeClass
	public static void beforeClass()
	{
		Registry.setCurrentTenantByID("junit");
	}

	@Test
	public void shouldCutCardNumberCorrectly()
	{
		final String number = "4444444444442424";
		final String accepted = "2424";

		final CreditCardPaymentInfoModel creditCardPaymentInfoModel = new CreditCardPaymentInfoModel();
		creditCardPaymentInfoModel.setNumber(number);
		final CustomerModel customerModel = new CustomerModel();
		customerModel.setDefaultPaymentInfo(creditCardPaymentInfoModel);

		assertEquals(accepted, AssistedServiceUtils.getCardLastFourDigits(customerModel));
	}

	@Test
	public void getLastFourDigitsWithNullNumber()
	{
		CreditCardPaymentInfoModel creditCardPaymentInfoModel = new CreditCardPaymentInfoModel();
		creditCardPaymentInfoModel.setNumber(null);
		CustomerModel customerModel = new CustomerModel();
		customerModel.setDefaultPaymentInfo(creditCardPaymentInfoModel);

		assertEquals("----", AssistedServiceUtils.getCardLastFourDigits(customerModel));
	}

	@Test
	public void getLastFourDigitsWithNumberLenghtLessThanFour()
	{
		final String number = "444";

		final CreditCardPaymentInfoModel creditCardPaymentInfoModel = new CreditCardPaymentInfoModel();
		creditCardPaymentInfoModel.setNumber(number);
		final CustomerModel customerModel = new CustomerModel();
		customerModel.setDefaultPaymentInfo(creditCardPaymentInfoModel);

		assertEquals(number, AssistedServiceUtils.getCardLastFourDigits(customerModel));
	}

	@Test
	public void getCreateionDateTest()
	{
		final CustomerModel customer = new CustomerModel();
		assertEquals("--/--/----", AssistedServiceUtils.getCreationDate(customer));

		final Date date = new Date();
		customer.setCreationtime(date);
		assertEquals(new SimpleDateFormat(CREATION_DATE_FORMAT).format(date), AssistedServiceUtils.getCreationDate(customer));
	}

	@Test
	public void eraseSamlCookieTest()
	{
		final HttpServletResponse response = new MockHttpServletResponse();

		AssistedServiceUtils.eraseSamlCookie(response);
		assertEquals(0, ((MockHttpServletResponse) response).getCookies().length);
		Config.setParameter(AssistedservicefacadesConstants.SSO_COOKIE_NAME, TEST_COOKIE_NAME);
		AssistedServiceUtils.eraseSamlCookie(response);
		final Cookie cookie = ((MockHttpServletResponse) response).getCookie(TEST_COOKIE_NAME);

		assertNotNull(cookie);
		assertEquals(0, cookie.getMaxAge());
		assertEquals("/", cookie.getPath());
		assertTrue(cookie.isHttpOnly());
		assertTrue(cookie.getSecure());
	}

	@Test
	public void getSamlCookieTest()
	{
		final HttpServletRequest request = new MockHttpServletRequest();
		assertNull(AssistedServiceUtils.getSamlCookie(request));
		Config.setParameter(AssistedservicefacadesConstants.SSO_COOKIE_NAME, TEST_COOKIE_NAME);
		final Cookie c = new Cookie(TEST_COOKIE_NAME, TEST_VALUE);
		((MockHttpServletRequest) request).setCookies(c);
		final Cookie cookie = AssistedServiceUtils.getSamlCookie(request);
		assertEquals(c, cookie);
	}

	@Test
	public void populateCartorOrderUrlTest()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setSite(null);
		assertEquals("", AssistedServiceUtils.populateCartorOrderUrl(order, null));

		final BaseSiteModel baseSite = new BaseSiteModel();
		baseSite.setUid(BASE_SITE_UID_1);
		order.setSite(baseSite);
		order.setUser(null);
		order.setGuid(ORDER_GUID_1);
		assertEquals("", AssistedServiceUtils.populateCartorOrderUrl(order, baseSite));

		final UserModel user = new UserModel();
		user.setUid(USER_UID);

		order.setUser(user);
		assertEquals("", AssistedServiceUtils.populateCartorOrderUrl(order, baseSite));

		Config.setParameter("website." + BASE_SITE_UID_1 + ".https", BASESITE_URL);
		Config.setParameter(AssistedservicefacadesConstants.ASM_DEEPLINK_PARAM, DEEPLINK_PARAM);


		final BaseSiteModel currentSite = new BaseSiteModel();
		currentSite.setUid(BASE_SITE_UID_2);
		assertEquals(BASESITE_URL + "/assisted-service/emulate?customerId=" + USER_UID + "&AbstractOrderId=" + ORDER_GUID_1,
				AssistedServiceUtils.populateCartorOrderUrl(order, currentSite));


		final CartModel cart = new CartModel();
		cart.setSite(baseSite);
		cart.setUser(user);
		cart.setSaveTime(new Date());

		assertEquals(
				BASESITE_URL + "/assisted-service/emulate?customerId=" + USER_UID + "&fwd=/my-account/saved-carts/" + cart.getCode(),
				AssistedServiceUtils.populateCartorOrderUrl(cart, currentSite));
	}


	@Test
	public void populateTicketUrlTest()
	{
		final BaseSiteModel baseSite = new BaseSiteModel();
		baseSite.setUid(BASE_SITE_UID_1);
		final BaseSiteModel currentSite = new BaseSiteModel();
		currentSite.setUid(BASE_SITE_UID_2);
		final UserModel user = new UserModel();
		user.setUid(USER_UID);
		final CsTicketModel ticket = new CsTicketModel();
		ticket.setTicketID("testTicketId1");
		assertEquals("", AssistedServiceUtils.populateTicketUrl(ticket, null));

		ticket.setBaseSite(baseSite);
		assertEquals("", AssistedServiceUtils.populateTicketUrl(ticket, baseSite));

		assertEquals("", AssistedServiceUtils.populateTicketUrl(ticket, currentSite));

		Config.setParameter("website." + BASE_SITE_UID_1 + ".https", BASESITE_URL);
		Config.setParameter(AssistedservicefacadesConstants.ASM_DEEPLINK_PARAM, DEEPLINK_PARAM);
		ticket.setCustomer(user);
		assertEquals(BASESITE_URL + "/assisted-service/emulate?customerId=testUserUid&&fwd=/my-account/support-ticket/"
				+ ticket.getTicketID(), AssistedServiceUtils.populateTicketUrl(ticket, currentSite));


	}

	@Test
	public void getTimeSinceTest()
	{
		final Date time = new Date();

		DateTimeUtils.setCurrentMillisFixed(time.getTime());

		assertEquals(getTimeSince(null), TimeSince.MOMENT);

		// moments
		TimeSince timeSince = getTimeSince(new Date(time.getTime() - 1));
		assertEquals(timeSince, TimeSince.MOMENT);

		// second
		timeSince = getTimeSince(new Date(time.getTime() - TimeUnit.SECONDS.toMillis(1)));
		assertEquals(timeSince, TimeSince.SECOND);

		// seconds
		timeSince = getTimeSince(new Date(time.getTime() - TimeUnit.SECONDS.toMillis(59)));
		assertEquals(timeSince, TimeSince.SECONDS);
		assertEquals(timeSince.getValue(), 59);

		// minute
		timeSince = getTimeSince(new Date(time.getTime() - TimeUnit.MINUTES.toMillis(1)));
		assertEquals(timeSince, TimeSince.MINUTE);

		// minutes
		timeSince = getTimeSince(new Date(time.getTime() - TimeUnit.MINUTES.toMillis(59)));
		assertEquals(timeSince, TimeSince.MINUTES);
		assertEquals(timeSince.getValue(), 59);

		// day
		timeSince = getTimeSince(new Date(time.getTime() - TimeUnit.DAYS.toMillis(1)));
		assertEquals(timeSince, TimeSince.DAY);

		// days
		timeSince = getTimeSince(new Date(time.getTime() - TimeUnit.DAYS.toMillis(29)));
		assertEquals(timeSince, TimeSince.DAYS);
		assertEquals(timeSince.getValue(), 29);

		// month
		timeSince = getTimeSince(new Date(time.getTime() - TimeUnit.DAYS.toMillis(33)));
		assertEquals(timeSince, TimeSince.MONTH);

		// months
		timeSince = getTimeSince(new Date(time.getTime() - TimeUnit.DAYS.toMillis(65)));
		assertEquals(timeSince, TimeSince.MONTHS);
		assertEquals(timeSince.getValue(), 2);

		// year
		timeSince = getTimeSince(new Date(time.getTime() - TimeUnit.DAYS.toMillis(380)));
		assertEquals(timeSince, TimeSince.YEAR);

		// years
		timeSince = getTimeSince(new Date(time.getTime() - TimeUnit.DAYS.toMillis(1100)));
		assertEquals(timeSince, TimeSince.YEARS);
		assertEquals(timeSince.getValue(), 3);
	}

	@After
	public void tearDown()
	{
		DateTimeUtils.setCurrentMillisSystem();
	}
}
