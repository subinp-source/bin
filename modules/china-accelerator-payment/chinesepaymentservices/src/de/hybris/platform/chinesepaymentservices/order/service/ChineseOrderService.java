/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentservices.order.service;

import de.hybris.platform.core.model.order.OrderModel;

/**
 * The service of ChineseOrder
 */
public interface ChineseOrderService {
	/**
	 * Cancel the order
	 *
	 * @param code
	 *           The code of the order
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	void cancelOrder(final String code);

	/**
	 * Call back for refund service to update order status
	 *
	 * @param orderModel
	 *            The order model to be updated
	 * @param refundSucceed
	 *            True if the order has been refunded successfully and false
	 *            otherwise
	 */
	void updateOrderForRefund(final OrderModel orderModel, boolean refundSucceed);
}
