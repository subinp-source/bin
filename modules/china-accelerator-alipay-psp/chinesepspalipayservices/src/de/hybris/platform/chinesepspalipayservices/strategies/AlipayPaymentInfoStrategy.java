/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.strategies;

import de.hybris.platform.chinesepaymentservices.model.ChinesePaymentInfoModel;
import de.hybris.platform.core.model.order.OrderModel;


/**
 * Updates payment information after payment method is changed or order is placed
 */
public interface AlipayPaymentInfoStrategy
{
	/**
	 * Updates payment information once payment method is chosen
	 *
	 * @param chinesePaymentInfoModel
	 *           ChinesePaymentInfoModel to be updated {@link ChinesePaymentInfoModel}
	 * @return updated ChinesePaymentInfoModel
	 */
	ChinesePaymentInfoModel updatePaymentInfoForPayemntMethod(ChinesePaymentInfoModel chinesePaymentInfoModel);

	/**
	 * Updates payment information once order is placed
	 *
	 * @param order
	 *           the placed order {@link OrderModel}
	 */
	void updatePaymentInfoForPlaceOrder(OrderModel order);

}
