/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentfacades.checkout.impl;

import de.hybris.platform.acceleratorfacades.order.impl.DefaultAcceleratorCheckoutFacade;
import de.hybris.platform.chinesepaymentfacades.checkout.ChineseCheckoutFacade;
import de.hybris.platform.chinesepaymentfacades.payment.data.ChinesePaymentRequestData;
import de.hybris.platform.chinesepaymentservices.checkout.ChineseCheckoutService;
import de.hybris.platform.chinesepaymentservices.checkout.strategies.ChinesePaymentServicesStrategy;
import de.hybris.platform.chinesepaymentservices.model.ChinesePaymentInfoModel;
import de.hybris.platform.chinesepaymentservices.order.service.impl.DefaultChineseOrderService;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryOrderEntryGroupData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.PaymentModeData;
import de.hybris.platform.commercefacades.order.data.PickupOrderEntryGroupData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.enums.OrderStatus;
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
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.configuration.ConversionException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

/**
 * Implementation for {@link ChineseCheckoutFacade}. Delivers main functionality for chinese checkout.
 */
public class DefaultChineseCheckoutFacade extends DefaultAcceleratorCheckoutFacade implements ChineseCheckoutFacade
{

	private static final String ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE = "Order with guid %s not found for current user in current BaseStore";
	private static final long DEFAULT_TASK_SCHEDULE_DELAY = 2 * 60 * 1000L;
	private static final long DEFAULT_ORDER_EXPIRED_INTERVAL = 1020000L;

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	private Converter<CartModel, CartData> cartConverter;
	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	private EventPublishingSubmitOrderStrategy eventPublishingSubmitOrderStrategy;
	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	private DefaultChineseOrderService chineseOrderService;

	private long scheduleDelay = DEFAULT_TASK_SCHEDULE_DELAY;
	private long orderExpiredInterval = DEFAULT_ORDER_EXPIRED_INTERVAL;

	private OrderFacade orderFacade;

	private ChinesePaymentServicesStrategy chinesePaymentServicesStrategy;

	private ChineseCheckoutService chineseCheckoutService;
	private ConfigurationService configurationService;
	private TaskService taskService;
	private PaymentModeService paymentModeService;

	private Converter<CartModel, CartData> cartChinesePaymentInfoConverter;
	private Converter<PaymentModeModel, PaymentModeData> paymentModeConverter;



	@Override
	public OrderData createOrder() throws BusinessException
	{

		final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			final UserModel currentUser = getCurrentUserForCheckout();
			if (cartModel.getUser().equals(currentUser) || getCheckoutCustomerStrategy().isAnonymousCheckout())
			{
				beforePlaceOrder(cartModel);
				final OrderModel orderModel = placeOrder(cartModel);
				afterPlaceOrder(cartModel, orderModel);
				return reserveStockAfterPlaceOrder(orderModel);
			}
		}

		return null;
	}

	/**
	 * Reserve stock after place order
	 * @param orderModel
	 * @return
	 * @throws BusinessException
	 */
	private OrderData reserveStockAfterPlaceOrder(final OrderModel orderModel) throws BusinessException{
		OrderData orderData = null;
		if (orderModel != null)
		{
			orderData = getOrderConverter().convert(orderModel);

			if (getConfigurationService().getConfiguration().getBoolean("order.reserve.stock.enabled", false))
			{
				if (!CollectionUtils.isEmpty(orderData.getPickupOrderGroups()))
				{
					processPickupReserveStock(orderData);
				}
				if (!CollectionUtils.isEmpty(orderData.getDeliveryOrderGroups()))
				{
					processDeliveryReserveStock(orderData);
				}
			}
		}
		return orderData;
	}

	/**
	 * Process pickup in store reserve stock
	 * @param orderData
	 * @throws BusinessException
	 */
	private void processPickupReserveStock(final OrderData orderData) throws BusinessException{
		for (final PickupOrderEntryGroupData pickupOrderEntryGroupData : orderData.getPickupOrderGroups())
		{
			final PointOfServiceData pointOfServiceData = pickupOrderEntryGroupData.getDeliveryPointOfService();
			final PointOfServiceModel pointOfServiceModel = getPointOfServiceService().getPointOfServiceForName(
					pointOfServiceData.getName());
			if (!CollectionUtils.isEmpty(pickupOrderEntryGroupData.getEntries()))
			{
				for (final OrderEntryData entry : pickupOrderEntryGroupData.getEntries())
				{
					reserveStock(orderData.getCode(), entry.getProduct().getCode(), entry.getQuantity().intValue(),
							Optional.of(pointOfServiceModel));
				}
			}
		}
	}

	/**
	 * Process delivery reserve stock
	 * @param orderData
	 * @throws BusinessException
	 */
	private void processDeliveryReserveStock(final OrderData orderData) throws BusinessException{
		for (final DeliveryOrderEntryGroupData deliveryOrderEntryGroupData : orderData.getDeliveryOrderGroups())
		{
			if (!CollectionUtils.isEmpty(deliveryOrderEntryGroupData.getEntries()))
			{
				for (final OrderEntryData entry : deliveryOrderEntryGroupData.getEntries())
				{
					reserveStock(orderData.getCode(), entry.getProduct().getCode(), entry.getQuantity().intValue(),
							Optional.<PointOfServiceModel> empty());
				}
			}
		}
	}

	/**
	 * @deprecated since 1905. Use super implementation instead.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Override
	public CartModel getCart()
	{
		if (getCartFacade().hasSessionCart())
		{
			return getCartService().getSessionCart();
		}
		return null;
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Override
	public void mergeCart(final CartModel cartModel)
	{
		getCartService().saveOrder(cartModel);
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Override
	public CartData convertCart(final CartModel cartModel)
	{
		return getCartConverter().convert(cartModel);
	}

	@Override
	public void setPaymentMode(final PaymentModeModel paymentMode)
	{
		if (getCart() != null)
		{
			getChineseCheckoutService().setPaymentMode(paymentMode, getCart());
		}
	}

	@Override
	public boolean authorizePayment(final String securityCode)
	{
		return getChineseCheckoutService().authorizePayment(securityCode, getCart());
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Override
	public boolean reserveStock(final String orderCode, final String productCode, final int quantity,
			final Optional<PointOfServiceModel> pos) throws InsufficientStockLevelException
	{
		return getChineseCheckoutService().reserveStock(orderCode, productCode, quantity, pos);
	}

	@Override
	public CartData getCheckoutCart()
	{
		return getCartChinesePaymentInfoConverter().convert(getCart(), super.getCheckoutCart());
	}

	/**
	 * @deprecated since 1905. Use de.hybris.platform.commercefacades.order.OrderFacade.getOrderDetailsForCode(String)
	 *             instead
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Override
	public OrderData getOrderDetailsForCode(final String code)
	{
		final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();

		OrderModel orderModel = null;
		if (getCheckoutCustomerStrategy().isAnonymousCheckout())
		{
			orderModel = getCustomerAccountService().getOrderForCode(code, baseStoreModel);
		}
		else
		{
			try
			{
				orderModel = getCustomerAccountService().getOrderForCode((CustomerModel) getUserService().getCurrentUser(), code,
						baseStoreModel);
			}
			catch (final ModelNotFoundException e)
			{
				throw new UnknownIdentifierException(String.format(ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE, code));
			}
		}

		if (orderModel == null)
		{
			throw new UnknownIdentifierException(String.format(ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE, code));
		}
		final OrderData orderData = getOrderConverter().convert(orderModel);
		return orderData;
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Override
	public void deleteStockLevelReservationHistoryEntry(final String code)
	{
		getChineseCheckoutService().deleteStockLevelReservationHistoryEntry(code);
	}

	/**
	 * @deprecated since 1905. Use de.hybris.platform.commercefacades.order.OrderFacade.getOrderDetailsForCode(String)
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Override
	public OrderData getOrderByCode(final String code)
	{
		final OrderModel orderModel = getChineseCheckoutService().getOrderByCode(code);
		final OrderData orderData = getOrderConverter().convert(orderModel);
		return orderData;
	}

	@Override
	public boolean hasNoChinesePaymentInfo()
	{
		return getCheckoutCart().getChinesePaymentInfo() == null;
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Override
	public void publishSubmitOrderEvent(final String orderCode)
	{
		final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();

		OrderModel orderModel = null;
		if (getCheckoutCustomerStrategy().isAnonymousCheckout())
		{
			orderModel = getCustomerAccountService().getOrderForCode(orderCode, baseStoreModel);
		}
		else
		{
			try
			{
				orderModel = getCustomerAccountService().getOrderForCode((CustomerModel) getUserService().getCurrentUser(),
						orderCode, baseStoreModel);
			}
			catch (final ModelNotFoundException e)
			{
				throw new UnknownIdentifierException(String.format(ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE, orderCode));
			}
		}

		if (orderModel == null)
		{
			throw new UnknownIdentifierException(String.format(ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE, orderCode));
		}

		if (OrderStatus.CREATED.equals(orderModel.getStatus()))
		{
			getEventPublishingSubmitOrderStrategy().submitOrder(orderModel);
		}
	}

	/**
	 * @deprecated since 2005
	 */
	@Deprecated(since = "2005", forRemoval = true)
	protected ChinesePaymentRequestData buildChinesePaymentRequestData(final String url, final Date expiredDate)
	{
		final ChinesePaymentRequestData chinesePaymentRequest = new ChinesePaymentRequestData();
		if (StringUtils.isNotBlank(url))
		{
			chinesePaymentRequest.setUrl(url);
			chinesePaymentRequest.setExpiredDate(expiredDate);
		}
		return chinesePaymentRequest;
	}

	@Override
	public void submitOrder(final String orderCode)
	{
		final Optional<TaskModel> optional = getChineseCheckoutService().getSubmitOrderEventTask(orderCode);
		if (!optional.isPresent())
		{
			final OrderModel orderModel = getChineseCheckoutService().getOrderByCode(orderCode);
			final TaskModel task = createSubmitOrderEventTask(orderModel);
			if (Objects.nonNull(task))
			{
				getTaskService().scheduleTask(task);
			}
		}
	}

	@Override
	public void setPaymentInfo(final String paymentModeCode)
	{
		final CartModel cartModel = getCart();
		if (Objects.nonNull(cartModel) && StringUtils.isNotBlank(paymentModeCode)
				&& cartModel.getUser().equals(getCheckoutCustomerStrategy().getCurrentUserForCheckout()))
		{
			final ChinesePaymentInfoModel chinesePaymentInfoModel = new ChinesePaymentInfoModel();
			chinesePaymentInfoModel.setPaymentProvider(paymentModeCode);
			final ChinesePaymentService chinesePaymentService = getChinesePaymentServicesStrategy()
					.getPaymentService(paymentModeCode);
			if (Objects.nonNull(chinesePaymentService))
			{
				chinesePaymentService.setPaymentInfo(cartModel, chinesePaymentInfoModel);
			}
		}
	}

	@Override
	public PaymentModeData getPaymentModeByCode(final String paymentModeCode)
	{
		try
		{
			return getPaymentModeConverter().convert(getPaymentModeService().getPaymentModeForCode(paymentModeCode));
		}
		catch (final UnknownIdentifierException e)
		{
			return null;
		}
	}

	@Override
	public String buildPaymentRequestUrl(final String orderCode)
	{
		final OrderData orderData = orderFacade.getOrderDetailsForCode(orderCode);
		if (orderData.getChinesePaymentInfo() != null
				&& StringUtils.isNotBlank(orderData.getChinesePaymentInfo().getPaymentProvider()))
		{
			final ChinesePaymentService chinesePaymentService = chinesePaymentServicesStrategy
					.getPaymentService(orderData.getChinesePaymentInfo().getPaymentProvider());
			return Objects.nonNull(chinesePaymentService) ? chinesePaymentService.getPaymentRequestUrl(orderCode)
					: StringUtils.EMPTY;
		}
		return StringUtils.EMPTY;
	}

	@Override
	public ChinesePaymentRequestData createChinesePaymentRequestData(final String orderCode)
	{
		submitOrder(orderCode);
		final String url = buildPaymentRequestUrl(orderCode);
		final Optional<TaskModel> orderEventTask = getChineseCheckoutService().getSubmitOrderEventTask(orderCode);



		final Date expiredDate = orderEventTask.isPresent()
				? new Date(orderEventTask.get().getCreationtime().getTime() + getOrderExpiredInterval())
				: null;

		return buildChinesePaymentRequestData(url, expiredDate, orderCode);

	}

	@Override
	public void updatePaymentInfoForPlacingOrder(final OrderData orderData)
	{

		if (Objects.nonNull(orderData.getChinesePaymentInfo())
				&& StringUtils.isNotBlank(orderData.getChinesePaymentInfo().getPaymentProvider()))
		{
			final ChinesePaymentService chinesePaymentService = chinesePaymentServicesStrategy
					.getPaymentService(orderData.getChinesePaymentInfo().getPaymentProvider());
			if (Objects.nonNull(chinesePaymentService))
			{
				chinesePaymentService.updatePaymentInfoForPlaceOrder(orderData.getCode());
			}
		}
	}

	@Override
	public boolean needPayInNewWindow()
	{
		try
		{
			return getConfigurationService().getConfiguration().getBoolean("chinesepayment.pay.in.new.window", true);
		}
		catch (final ConversionException e)
		{
			return true;
		}
	}

	@Override
	public OrderData syncPaymentStatusForOrder(final String orderCode)
	{
		final OrderData orderData = getOrderFacade().getOrderDetailsForCode(orderCode);
		if (!PaymentStatus.PAID.equals(orderData.getPaymentStatus()))
		{
			final ChinesePaymentService chinesePaymentService = chinesePaymentServicesStrategy
					.getPaymentService(orderData.getChinesePaymentInfo().getPaymentProvider());
			if (Objects.nonNull(chinesePaymentService))
			{
				chinesePaymentService.syncPaymentStatus(orderCode);
			}
		}
		return getOrderFacade().getOrderDetailsForCode(orderCode);
	}

	protected TaskModel createSubmitOrderEventTask(final OrderModel orderModel)
	{
		TaskModel task = null;
		if (Objects.nonNull(orderModel))
		{
			task = getModelService().create(TaskModel.class);
			task.setRunnerBean("submitOrderEventTask");
			task.setExecutionDate(new Date(System.currentTimeMillis() + getScheduleDelay()));
			task.setContextItem(orderModel);
			final Map<String, Object> baseData = buildContextDataForSubmitOrderEventTask();
			task.setContext(baseData);
		}
		return task;
	}

	protected Map<String, Object> buildContextDataForSubmitOrderEventTask()
	{
		final Map<String, Object> contextData = new HashMap();
		contextData.put("baseStore", getBaseStoreService().getCurrentBaseStore());
		contextData.put("currentUser", getUserService().getCurrentUser());
		return contextData;
	}

	protected ChinesePaymentRequestData buildChinesePaymentRequestData(final String url, final Date expiredDate,
			final String orderCode)
	{
		final ChinesePaymentRequestData chinesePaymentRequest = new ChinesePaymentRequestData();
		if (StringUtils.isNotBlank(url) && Objects.nonNull(expiredDate) && StringUtils.isNotBlank(orderCode))
		{
			chinesePaymentRequest.setUrl(url);
			chinesePaymentRequest.setExpiredDate(expiredDate);
			chinesePaymentRequest.setOrder(orderFacade.getOrderDetailsForCodeWithoutUser(orderCode));
		}
		return chinesePaymentRequest;
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	protected Converter<CartModel, CartData> getCartConverter()
	{
		return cartConverter;
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Required
	public void setCartConverter(final Converter<CartModel, CartData> cartConverter)
	{
		this.cartConverter = cartConverter;
	}

	protected ChineseCheckoutService getChineseCheckoutService()
	{
		return chineseCheckoutService;
	}

	@Required
	public void setChineseCheckoutService(final ChineseCheckoutService chineseCheckoutService)
	{
		this.chineseCheckoutService = chineseCheckoutService;
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	protected DefaultChineseOrderService getChineseOrderService()
	{
		return chineseOrderService;
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Required
	public void setChineseOrderService(final DefaultChineseOrderService chineseOrderService)
	{
		this.chineseOrderService = chineseOrderService;
	}

	protected Converter<CartModel, CartData> getCartChinesePaymentInfoConverter()
	{
		return cartChinesePaymentInfoConverter;
	}

	@Required
	public void setCartChinesePaymentInfoConverter(final Converter<CartModel, CartData> cartChinesePaymentInfoConverter)
	{
		this.cartChinesePaymentInfoConverter = cartChinesePaymentInfoConverter;
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	protected EventPublishingSubmitOrderStrategy getEventPublishingSubmitOrderStrategy()
	{
		return eventPublishingSubmitOrderStrategy;
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Required
	public void setEventPublishingSubmitOrderStrategy(final EventPublishingSubmitOrderStrategy eventPublishingSubmitOrderStrategy)
	{
		this.eventPublishingSubmitOrderStrategy = eventPublishingSubmitOrderStrategy;
	}

	protected ConfigurationService getConfigurationService() {
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	protected TaskService getTaskService()
	{
		return taskService;
	}

	@Required
	public void setTaskService(final TaskService taskService)
	{
		this.taskService = taskService;
	}

	protected long getScheduleDelay()
	{
		return scheduleDelay;
	}

	@Required
	public void setScheduleDelay(final long scheduleDelay)
	{
		this.scheduleDelay = scheduleDelay;
	}

	protected ChinesePaymentServicesStrategy getChinesePaymentServicesStrategy()
	{
		return chinesePaymentServicesStrategy;
	}

	@Required
	public void setChinesePaymentServicesStrategy(final ChinesePaymentServicesStrategy chinesePaymentServicesStrategy)
	{
		this.chinesePaymentServicesStrategy = chinesePaymentServicesStrategy;
	}

	protected PaymentModeService getPaymentModeService()
	{
		return paymentModeService;
	}

	@Required
	public void setPaymentModeService(final PaymentModeService paymentModeService)
	{
		this.paymentModeService = paymentModeService;
	}

	protected Converter<PaymentModeModel, PaymentModeData> getPaymentModeConverter()
	{
		return paymentModeConverter;
	}

	@Required
	public void setPaymentModeConverter(final Converter<PaymentModeModel, PaymentModeData> paymentModeConverter)
	{
		this.paymentModeConverter = paymentModeConverter;
	}

	protected OrderFacade getOrderFacade()
	{
		return orderFacade;
	}

	@Required
	public void setOrderFacade(final OrderFacade orderFacade)
	{
		this.orderFacade = orderFacade;
	}

	protected long getOrderExpiredInterval()
	{
		return orderExpiredInterval;
	}

	@Required
	public void setOrderExpiredInterval(final long orderExpiredInterval)
	{
		this.orderExpiredInterval = orderExpiredInterval;
	}

}
