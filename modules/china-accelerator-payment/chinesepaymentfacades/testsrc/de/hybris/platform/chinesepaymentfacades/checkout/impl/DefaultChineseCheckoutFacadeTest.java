/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentfacades.checkout.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chinesepaymentfacades.payment.data.ChinesePaymentInfoData;
import de.hybris.platform.chinesepaymentfacades.payment.data.ChinesePaymentRequestData;
import de.hybris.platform.chinesepaymentservices.checkout.ChineseCheckoutService;
import de.hybris.platform.chinesepaymentservices.checkout.strategies.ChinesePaymentServicesStrategy;
import de.hybris.platform.chinesepaymentservices.model.ChinesePaymentInfoModel;
import de.hybris.platform.chinesepaymentservices.order.service.impl.DefaultChineseOrderService;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.PaymentModeData;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.PaymentModeService;
import de.hybris.platform.order.strategies.impl.EventPublishingSubmitOrderStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConversionException;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.microsoft.sqlserver.jdbc.StringUtils;


@UnitTest
public class DefaultChineseCheckoutFacadeTest
{
	private static final String TEST_ORDER_CODE = "testOrderCode";
	private static String PAYMENT_MODE_CODE = "testPaymentModeCode";
	private static final String CHINESE_PAYMENT_REQUEST_URL = "testUrl";
	private static final long DEFAULT_ORDER_EXPIRED_INTERVAL = 1020000L;
	@Mock
	private Converter<CartModel, CartData> cartConverter;
	@Mock
	private ChineseCheckoutService chineseCheckoutService;
	@Mock
	private DefaultChineseOrderService chineseOrderService;
	@Mock
	private Converter<CartModel, CartData> cartChinesePaymentInfoConverter;
	@Mock
	private EventPublishingSubmitOrderStrategy eventPublishingSubmitOrderStrategy;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private TaskService taskService;
	@Mock
	private CheckoutCustomerStrategy checkoutCustomerStrategy;
	@Mock
	private ChinesePaymentServicesStrategy chinesePaymentServicesStrategy;
	@Mock
	private ChinesePaymentService paymentService;
	@Mock
	private ModelService modelService;
	@Mock
	private Converter<PaymentModeModel, PaymentModeData> paymentModeConverter;
	@Mock
	private PaymentModeService paymentModeService;
	@Mock
	private OrderFacade orderFacade;
	@Mock
	private ChinesePaymentInfoModel chinesePaymentInfoModel;

	private DefaultChineseCheckoutFacade chineseCheckoutFacade;
	private CartModel cartModel;
	private CartData cartData;
	private UserModel userModel;
	private OrderModel orderModel;
	private OrderData orderData;
	private PaymentModeModel paymentMode;
	private PaymentModeData paymentModeData;
	private TaskModel taskModel;
	private ChinesePaymentInfoData chinesePaymentInfoData;
	private Date date;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		chineseCheckoutFacade = Mockito.spy(new DefaultChineseCheckoutFacade());
		chineseCheckoutFacade.setCartConverter(cartConverter);
		chineseCheckoutFacade.setChineseCheckoutService(chineseCheckoutService);
		chineseCheckoutFacade.setChineseOrderService(chineseOrderService);
		chineseCheckoutFacade.setCartChinesePaymentInfoConverter(cartChinesePaymentInfoConverter);
		chineseCheckoutFacade.setEventPublishingSubmitOrderStrategy(eventPublishingSubmitOrderStrategy);
		chineseCheckoutFacade.setConfigurationService(configurationService);
		chineseCheckoutFacade.setTaskService(taskService);
		chineseCheckoutFacade.setCheckoutCustomerStrategy(checkoutCustomerStrategy);
		chineseCheckoutFacade.setChinesePaymentServicesStrategy(chinesePaymentServicesStrategy);
		chineseCheckoutFacade.setModelService(modelService);
		chineseCheckoutFacade.setPaymentModeConverter(paymentModeConverter);
		chineseCheckoutFacade.setPaymentModeService(paymentModeService);
		chineseCheckoutFacade.setOrderFacade(orderFacade);
		chineseCheckoutFacade.setOrderExpiredInterval(DEFAULT_ORDER_EXPIRED_INTERVAL);

		cartModel = new CartModel();
		userModel = new UserModel();
		cartModel.setUser(userModel);
		cartData = new CartData();
		orderModel = new OrderModel();
		orderData = new OrderData();
		paymentMode = new PaymentModeModel();
		paymentModeData = new PaymentModeData();
		taskModel = new TaskModel();
		chinesePaymentInfoData = new ChinesePaymentInfoData();
		date = new Date();


		Mockito.when(cartConverter.convert(cartModel)).thenReturn(cartData);
		Mockito.doReturn(cartModel).when(chineseCheckoutFacade).getCart();
		Mockito.when(chinesePaymentServicesStrategy.getPaymentService(Mockito.anyString())).thenReturn(paymentService);
		Mockito.when(paymentService.setPaymentInfo(Mockito.eq(cartModel), Mockito.any(ChinesePaymentInfoModel.class)))
				.thenReturn(true);
		Mockito.when(paymentModeConverter.convert(paymentMode)).thenReturn(paymentModeData);
		Mockito.when(paymentService.getPaymentRequestUrl(TEST_ORDER_CODE)).thenReturn(CHINESE_PAYMENT_REQUEST_URL);
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Test
	public void testConvertCart()
	{
		final CartData result = chineseCheckoutFacade.convertCart(cartModel);
		Assert.assertEquals(result, cartData);
	}

	@Test
	public void testSetPaymentMode()
	{
		Mockito.doNothing().when(chineseCheckoutService).setPaymentMode(paymentMode, cartModel);

		chineseCheckoutFacade.setPaymentMode(paymentMode);
		Mockito.verify(chineseCheckoutService, Mockito.times(1)).setPaymentMode(paymentMode, cartModel);
	}

	@Test
	public void testAuthorizePayment()
	{
		final String securityCode = "securityCode";
		Mockito.when(chineseCheckoutService.authorizePayment(securityCode, cartModel)).thenReturn(true);

		final boolean result = chineseCheckoutFacade.authorizePayment(securityCode);
		Mockito.verify(chineseCheckoutService, Mockito.times(1)).authorizePayment(securityCode, cartModel);
		Assert.assertTrue(result);
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Test
	public void testReserveStock() throws InsufficientStockLevelException
	{
		final String orderCode = "orderCode";
		final String productCode = "productCode";
		final int quantity = 0;
		final Optional<PointOfServiceModel> pos = Optional.empty();
		Mockito.when(chineseCheckoutService.reserveStock(orderCode, productCode, quantity, pos)).thenReturn(true);

		final boolean result = chineseCheckoutFacade.reserveStock(orderCode, productCode, quantity, pos);
		Assert.assertTrue(result);
	}

	@Test
	public void testSubmitOrder_taskExists()
	{
		final String orderCode = "orderCode";
		Mockito.when(chineseCheckoutService.getSubmitOrderEventTask(orderCode)).thenReturn(Optional.of(taskModel));

		chineseCheckoutFacade.submitOrder(orderCode);
		Mockito.verify(chineseCheckoutService, Mockito.times(0)).getOrderByCode(Mockito.any());
		Mockito.verify(chineseCheckoutFacade, Mockito.times(0)).createSubmitOrderEventTask(Mockito.any());
	}

	@Test
	public void testSubmitOrder_taskNotExists()
	{
		final String orderCode = "orderCode";
		Mockito.when(chineseCheckoutService.getSubmitOrderEventTask(orderCode)).thenReturn(Optional.empty());
		Mockito.when(chineseCheckoutService.getOrderByCode(orderCode)).thenReturn(orderModel);
		Mockito.doReturn(taskModel).when(chineseCheckoutFacade).createSubmitOrderEventTask(orderModel);
		Mockito.doNothing().when(taskService).scheduleTask(taskModel);

		chineseCheckoutFacade.submitOrder(orderCode);
		Mockito.verify(chineseCheckoutService, Mockito.times(1)).getOrderByCode(orderCode);
		Mockito.verify(chineseCheckoutFacade, Mockito.times(1)).createSubmitOrderEventTask(orderModel);
		Mockito.verify(taskService, Mockito.times(1)).scheduleTask(taskModel);
	}

	@Test
	public void testSetPaymentInfo()
	{
		final CustomerModel customer = Mockito.mock(CustomerModel.class);
		cartModel.setUser(customer);
		Mockito.when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(customer);

		chineseCheckoutFacade.setPaymentInfo(null);
		Mockito.verify(paymentService, Mockito.times(0)).setPaymentInfo(Mockito.eq(cartModel),
				Mockito.any(ChinesePaymentInfoModel.class));

		chineseCheckoutFacade.setPaymentInfo(PAYMENT_MODE_CODE);
		Mockito.verify(paymentService, Mockito.times(1)).setPaymentInfo(Mockito.eq(cartModel),
				Mockito.isA(ChinesePaymentInfoModel.class));

	}

	@Test
	public void testSetPaymentInfo_nullService()
	{
		final CustomerModel customer = Mockito.mock(CustomerModel.class);
		cartModel.setUser(customer);
		Mockito.when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(customer);
		Mockito.when(chinesePaymentServicesStrategy.getPaymentService(Mockito.anyString())).thenReturn(null);
		
		chineseCheckoutFacade.setPaymentInfo(PAYMENT_MODE_CODE);
		Mockito.verify(paymentService, Mockito.times(0)).setPaymentInfo(Mockito.eq(cartModel),
				Mockito.any(ChinesePaymentInfoModel.class));
	}

	@Test
	public void testGetPaymentModeByNullCode()
	{
		Mockito.when(paymentModeService.getPaymentModeForCode(null)).thenThrow(UnknownIdentifierException.class);
		Assert.assertNull(chineseCheckoutFacade.getPaymentModeByCode(null));

		Mockito.when(paymentModeService.getPaymentModeForCode(Mockito.anyString())).thenReturn(paymentMode);
		Assert.assertEquals(paymentModeData, chineseCheckoutFacade.getPaymentModeByCode(Mockito.anyString()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetPaymentModeByCode_validCode()
	{
		Mockito.when(paymentModeService.getPaymentModeForCode(Mockito.anyString())).thenReturn(null);
		Mockito.when(paymentModeConverter.convert(null)).thenThrow(IllegalArgumentException.class);
		chineseCheckoutFacade.getPaymentModeByCode(PAYMENT_MODE_CODE);
	}



	@Test
	public void testBuildPaymentRequestUrl()
	{
		Mockito.when(orderFacade.getOrderDetailsForCode(TEST_ORDER_CODE)).thenReturn(orderData);
		Assert.assertEquals(StringUtils.EMPTY, chineseCheckoutFacade.buildPaymentRequestUrl(TEST_ORDER_CODE));

		orderData.setChinesePaymentInfo(null);
		Assert.assertEquals(StringUtils.EMPTY, chineseCheckoutFacade.buildPaymentRequestUrl(TEST_ORDER_CODE));

		orderData.setChinesePaymentInfo(chinesePaymentInfoData);
		Assert.assertEquals(StringUtils.EMPTY, chineseCheckoutFacade.buildPaymentRequestUrl(TEST_ORDER_CODE));

		chinesePaymentInfoData.setPaymentProvider("TestProvider");
		Assert.assertEquals(CHINESE_PAYMENT_REQUEST_URL, chineseCheckoutFacade.buildPaymentRequestUrl(TEST_ORDER_CODE));

		Mockito.when(chinesePaymentServicesStrategy.getPaymentService(Mockito.anyString())).thenReturn(null);
		Assert.assertEquals(StringUtils.EMPTY, chineseCheckoutFacade.buildPaymentRequestUrl(TEST_ORDER_CODE));
	}

	@Test
	public void testBuildChinesePaymentRequestData()
	{
		Mockito.when(orderFacade.getOrderDetailsForCodeWithoutUser(TEST_ORDER_CODE)).thenReturn(orderData);

		ChinesePaymentRequestData chinesePaymentRequest = chineseCheckoutFacade.buildChinesePaymentRequestData(null, null,
				TEST_ORDER_CODE);
		Assert.assertNull(chinesePaymentRequest.getUrl());
		Assert.assertNull(chinesePaymentRequest.getExpiredDate());
		Assert.assertNull(chinesePaymentRequest.getOrder());

		chinesePaymentRequest = chineseCheckoutFacade.buildChinesePaymentRequestData(CHINESE_PAYMENT_REQUEST_URL, null,
				TEST_ORDER_CODE);
		Assert.assertNull(chinesePaymentRequest.getUrl());
		Assert.assertNull(chinesePaymentRequest.getExpiredDate());
		Assert.assertNull(chinesePaymentRequest.getOrder());

		chinesePaymentRequest = chineseCheckoutFacade.buildChinesePaymentRequestData(null, date, TEST_ORDER_CODE);
		Assert.assertNull(chinesePaymentRequest.getUrl());
		Assert.assertNull(chinesePaymentRequest.getExpiredDate());
		Assert.assertNull(chinesePaymentRequest.getOrder());

		chinesePaymentRequest = chineseCheckoutFacade.buildChinesePaymentRequestData(CHINESE_PAYMENT_REQUEST_URL, date, null);
		Assert.assertNull(chinesePaymentRequest.getUrl());
		Assert.assertNull(chinesePaymentRequest.getExpiredDate());
		Assert.assertNull(chinesePaymentRequest.getOrder());

		chinesePaymentRequest = chineseCheckoutFacade.buildChinesePaymentRequestData(CHINESE_PAYMENT_REQUEST_URL, date,
				TEST_ORDER_CODE);
		Assert.assertEquals(CHINESE_PAYMENT_REQUEST_URL, chinesePaymentRequest.getUrl());
		Assert.assertEquals(date, chinesePaymentRequest.getExpiredDate());
		Assert.assertEquals(orderData, chinesePaymentRequest.getOrder());
	}

	@Test
	public void testCreateChinesePaymentRequestData()
	{
		taskModel.setCreationtime(date);
		Mockito.when(chineseCheckoutService.getSubmitOrderEventTask(TEST_ORDER_CODE)).thenReturn(Optional.of(taskModel));

		orderData.setChinesePaymentInfo(chinesePaymentInfoData);
		chinesePaymentInfoData.setPaymentProvider("TestProvider");
		Mockito.when(orderFacade.getOrderDetailsForCode(TEST_ORDER_CODE)).thenReturn(orderData);

		final ChinesePaymentRequestData chinesePaymentRequest = chineseCheckoutFacade.createChinesePaymentRequestData(TEST_ORDER_CODE);
		Assert.assertEquals(CHINESE_PAYMENT_REQUEST_URL, chinesePaymentRequest.getUrl());
		Assert.assertNotNull(chinesePaymentRequest.getExpiredDate());
		Assert.assertTrue(new DateTime(chinesePaymentRequest.getExpiredDate().getTime()).isAfterNow());
	}

	@Test
	public void testIfOpenNewWindowForPayment()
	{
		final Configuration configuration = Mockito.mock(Configuration.class);
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);

		Mockito.when(configuration.getBoolean("chinesepayment.pay.in.new.window", true)).thenReturn(true);
		Assert.assertTrue(chineseCheckoutFacade.needPayInNewWindow());

		Mockito.when(configuration.getBoolean("chinesepayment.pay.in.new.window", true)).thenReturn(false);
		Assert.assertFalse(chineseCheckoutFacade.needPayInNewWindow());

		Mockito.when(configuration.getBoolean("chinesepayment.pay.in.new.window", true)).thenThrow(ConversionException.class);
		Assert.assertTrue(chineseCheckoutFacade.needPayInNewWindow());
	}

	@Test
	public void testSyncPaymentStatusForOrder()
	{
		orderData.setPaymentStatus(PaymentStatus.PAID);
		Mockito.when(orderFacade.getOrderDetailsForCode(TEST_ORDER_CODE)).thenReturn(orderData);
		Assert.assertEquals(orderData, chineseCheckoutFacade.syncPaymentStatusForOrder(TEST_ORDER_CODE));

		orderData.setPaymentStatus(PaymentStatus.NOTPAID);
		orderData.setChinesePaymentInfo(chinesePaymentInfoData);
		chinesePaymentInfoData.setPaymentProvider("TestProvider");
		Mockito.doNothing().when(paymentService).syncPaymentStatus(TEST_ORDER_CODE);

		Assert.assertEquals(orderData, chineseCheckoutFacade.syncPaymentStatusForOrder(TEST_ORDER_CODE));
		Mockito.verify(paymentService, Mockito.times(1)).syncPaymentStatus(TEST_ORDER_CODE);
	}

	@Test
	public void testSyncPaymentStatusForOrder_nullService()
	{
		orderData.setPaymentStatus(PaymentStatus.NOTPAID);
		orderData.setChinesePaymentInfo(chinesePaymentInfoData);
		chinesePaymentInfoData.setPaymentProvider("TestProvider");
		Mockito.when(orderFacade.getOrderDetailsForCode(TEST_ORDER_CODE)).thenReturn(orderData);
		Mockito.doNothing().when(paymentService).syncPaymentStatus(TEST_ORDER_CODE);
		Mockito.when(chinesePaymentServicesStrategy.getPaymentService(Mockito.anyString())).thenReturn(null);

		Assert.assertEquals(orderData, chineseCheckoutFacade.syncPaymentStatusForOrder(TEST_ORDER_CODE));
		Mockito.verify(paymentService, Mockito.times(0)).syncPaymentStatus(TEST_ORDER_CODE);
	}

	@Test
	public void testUpdatePaymentInfoForPlacingOrder()
	{
		orderData.setCode(TEST_ORDER_CODE);
		chineseCheckoutFacade.updatePaymentInfoForPlacingOrder(orderData);
		Mockito.doNothing().when(paymentService).updatePaymentInfoForPlaceOrder(TEST_ORDER_CODE);

		orderData.setChinesePaymentInfo(null);
		chineseCheckoutFacade.updatePaymentInfoForPlacingOrder(orderData);
		Mockito.verify(paymentService, Mockito.times(0)).updatePaymentInfoForPlaceOrder(TEST_ORDER_CODE);

		orderData.setChinesePaymentInfo(chinesePaymentInfoData);
		chineseCheckoutFacade.updatePaymentInfoForPlacingOrder(orderData);
		Mockito.verify(paymentService, Mockito.times(0)).updatePaymentInfoForPlaceOrder(TEST_ORDER_CODE);

		chinesePaymentInfoData.setPaymentProvider("TestProvider");
		chineseCheckoutFacade.updatePaymentInfoForPlacingOrder(orderData);
		Mockito.verify(paymentService, Mockito.times(1)).updatePaymentInfoForPlaceOrder(TEST_ORDER_CODE);
	}

	@Test
	public void testUpdatePaymentInfoForPlacingOrder_nullService()
	{
		orderData.setCode(TEST_ORDER_CODE);
		orderData.setChinesePaymentInfo(chinesePaymentInfoData);
		chinesePaymentInfoData.setPaymentProvider("TestProvider");
		Mockito.doNothing().when(paymentService).updatePaymentInfoForPlaceOrder(TEST_ORDER_CODE);
		Mockito.when(chinesePaymentServicesStrategy.getPaymentService(Mockito.anyString())).thenReturn(null);

		chineseCheckoutFacade.updatePaymentInfoForPlacingOrder(orderData);
		Mockito.verify(paymentService, Mockito.times(0)).updatePaymentInfoForPlaceOrder(TEST_ORDER_CODE);
	}
}
