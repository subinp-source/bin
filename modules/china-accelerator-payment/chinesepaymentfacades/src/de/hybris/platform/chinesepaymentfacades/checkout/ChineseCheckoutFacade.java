/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentfacades.checkout;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.chinesepaymentfacades.payment.data.ChinesePaymentRequestData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.PaymentModeData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Optional;


/**
 * The facade of ChineseCheckout
 */
public interface ChineseCheckoutFacade extends AcceleratorCheckoutFacade
{
	/**
	 * Gets the SessionCart.
	 *
	 * @return CartModel
	 * @deprecated since 1905. Use super implementation instead.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	CartModel getCart();

	/**
	 * Merges the cart.
	 *
	 * @param cartModel
	 *           cart model
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	void mergeCart(final CartModel cartModel);

	/**
	 * Converts the CartModel into CartData.
	 *
	 * @param cartModel
	 *           The CartModel to be converted
	 * @return cart data converted from cart model
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	CartData convertCart(final CartModel cartModel);

	/**
	 * Saves the PaymentMode in the cart.
	 *
	 * @param paymentMode
	 *           The selected PaymentMode
	 */
	void setPaymentMode(final PaymentModeModel paymentMode);

	/**
	 * Reserves the stock after placing order.
	 *
	 * @param orderCode
	 *           The code of the order
	 * @param productCode
	 *           The code of the product in the order
	 * @param quantity
	 *           The quantity to be reserved
	 * @param pos
	 *           The point of service to find stock
	 * @return true if reserve stock successfully, false otherwise
	 * @throws InsufficientStockLevelException
	 *            when stock level is insufficient
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	boolean reserveStock(final String orderCode, final String productCode, final int quantity,
			final Optional<PointOfServiceModel> pos) throws InsufficientStockLevelException;

	/**
	 * Gets the OrderDetails for code.
	 *
	 * @param code
	 *           The code of the order
	 * @return order data
	 * @deprecated since 1905. Use de.hybris.platform.commercefacades.order.OrderFacade.getOrderDetailsForCode(String)
	 *             instead
	 */
	@Deprecated(since = "1905", forRemoval= true )
	OrderData getOrderDetailsForCode(final String code);

	/**
	 * Delete StockLevelReservationHistoryEntry after the user pay the order successfully.
	 *
	 * @param code
	 *           The code of the order
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	void deleteStockLevelReservationHistoryEntry(final String code);

	/**
	 * Checks whether the cart has the ChinesePaymentInfo.
	 *
	 * @return false if the cart has chinese payment info, true otherwise
	 */
	boolean hasNoChinesePaymentInfo();

	/**
	 * Creates an order.
	 *
	 * @return order data after place order
	 * @throws BusinessException
	 *            when business error both before place order and after place order
	 */
	OrderData createOrder() throws BusinessException;

	/**
	 * Publishes the SubmitOrderEvent.
	 *
	 * @param orderCode
	 *           The code of the order
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	void publishSubmitOrderEvent(final String orderCode);

	/**
	 * Gets the OrderData by code.
	 *
	 * @param code
	 *           The code of the order
	 * @return order data
	 * @deprecated since 1905. Use de.hybris.platform.commercefacades.order.OrderFacade.getOrderDetailsForCode(String)
	 */
	@Deprecated(since = "1905", forRemoval= true )
	OrderData getOrderByCode(final String code);

	/**
	 * Submits order when paying order.
	 *
	 * @param orderCode
	 *           the order code
	 */
	void submitOrder(String orderCode);

	/**
	 * Saves the ChinesePaymentInfo in the cart.
	 *
	 * @param paymentModeCode
	 *           The code of PaymentMode which is set in ChinesePaymentInfo
	 */
	void setPaymentInfo(final String paymentModeCode);

	/**
	 * Gets the PaymentMode by code.
	 *
	 * @param paymentModeCode
	 *           the code of payment mode
	 * @return payment mode data
	 */
	PaymentModeData getPaymentModeByCode(String paymentModeCode);

	/**
	 * Creates Chinese payment request data.
	 *
	 * @param orderCode
	 *           the order code
	 * @return chinese payment request data
	 */
	ChinesePaymentRequestData createChinesePaymentRequestData(String orderCode);

	/**
	 * Builds order payment request url.
	 *
	 * @param orderCode
	 *           the order code
	 * @return chinese payment request url
	 */
	String buildPaymentRequestUrl(final String orderCode);

	/**
	 * Updates Payment info before placing order.
	 *
	 * @param orderData
	 *           the order data
	 */
	void updatePaymentInfoForPlacingOrder(OrderData orderData);

	/**
	 * Checks whether to open a new window for payment.
	 * 
	 * @return true if requires opening a new browser window for payment, false otherwise
	 */
	boolean needPayInNewWindow();

	/**
	 * Checks latest payment status.
	 * 
	 * @param orderCode
	 *           the order code
	 * @return order data with latest payment status
	 */
	OrderData syncPaymentStatusForOrder(String orderCode);
}
