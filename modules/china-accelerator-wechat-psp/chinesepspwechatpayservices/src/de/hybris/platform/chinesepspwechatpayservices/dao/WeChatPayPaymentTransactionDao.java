/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpayservices.dao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.WeChatPayPaymentTransactionModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.Optional;



/**
 * Provide method to find WeChatPay Payment Transaction with given params
 */
public interface WeChatPayPaymentTransactionDao extends GenericDao<WeChatPayPaymentTransactionModel>
{
	/**
	 * Get WeChatPayPaymentTransaction of the given order which satisfy these conditions: 1. There is only one entry with
	 * type Request in this transaction. 2. This entry is the latest among all transactions' entries.
	 *
	 * @param orderModel
	 *           The order contains the WeChatPayPaymentTransactionModel
	 * @return WeChatPayPaymentTransactionModel if found and an empty Optional otherwise
	 *
	 */
	Optional<WeChatPayPaymentTransactionModel> findTransactionByLatestRequestEntry(OrderModel orderModel, boolean limit);

	/**
	 * Get WeChatPayPaymentTransaction by WeChatPayCode
	 *
	 * @param WeChatPayCode
	 *           The WeChatPayCode of the wanted WeChatPayPaymentTransactionModel
	 * @return WeChatPayPaymentTransactionModel if found and an empty Optional otherwise
	 *
	 */
	Optional<WeChatPayPaymentTransactionModel> findTransactionByWeChatPayCode(String weChatPayCode);
}
