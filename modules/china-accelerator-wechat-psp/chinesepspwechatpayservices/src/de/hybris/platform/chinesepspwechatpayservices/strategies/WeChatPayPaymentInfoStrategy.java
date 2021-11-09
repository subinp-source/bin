/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpayservices.strategies;

import de.hybris.platform.chinesepaymentservices.model.ChinesePaymentInfoModel;
import de.hybris.platform.core.model.order.OrderModel;


/**
 * Methods to update Payment info
 */
public interface WeChatPayPaymentInfoStrategy
{
	/**
	 * update paymentinfo once payment method is chosen.
	 *
	 * @param chinesePaymentInfoModel
	 *           ChinesePaymentInfoModel to be updated {@link ChinesePaymentInfoModel}
	 * @return updated ChinesePaymentInfoModel
	 */
	ChinesePaymentInfoModel updatePaymentInfoForPayemntMethod(ChinesePaymentInfoModel chinesePaymentInfoModel);

	/**
	 * update paymentinfo once order is placed.
	 *
	 * @param order
	 *           Placed order {@link OrderModel}
	 */
	void updatePaymentInfoForPlaceOrder(OrderModel order);

}
