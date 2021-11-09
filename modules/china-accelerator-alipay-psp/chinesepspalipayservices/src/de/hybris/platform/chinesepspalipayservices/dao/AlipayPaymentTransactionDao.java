/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.dao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.AlipayPaymentTransactionModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;


/**
 * Looks up items related to {@link AlipayPaymentTransactionModel}
 */
public interface AlipayPaymentTransactionDao extends GenericDao<AlipayPaymentTransactionModel>
{
	/**
	 * Gets AlipayPaymentTransaction of the given order which satisfies these conditions: 1. There is only one entry with
	 * type code = 'REQUEST' in this transaction. 2. This entry is the latest among all transactions' entries.
	 *
	 * @param orderModel
	 *           the order
	 * @return AlipayPaymentTransactionModel if found or returns null otherwise
	 *
	 */
	AlipayPaymentTransactionModel findTransactionByLatestRequestEntry(OrderModel orderModel, boolean limit);

	/**
	 * Gets AlipayPaymentTransaction by alipay code
	 *
	 * @param alipayCode
	 *           the alipay code
	 * @return AlipayPaymentTransactionModel if found or returns null otherwise
	 *
	 */
	AlipayPaymentTransactionModel findTransactionByAlipayCode(String alipayCode);
}
