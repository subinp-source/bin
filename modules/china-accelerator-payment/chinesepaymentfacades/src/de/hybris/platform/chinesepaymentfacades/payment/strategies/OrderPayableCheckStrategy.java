/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentfacades.payment.strategies;

import de.hybris.platform.commercefacades.order.data.OrderData;


/**
 * Checks if order is payable.
 */
public interface OrderPayableCheckStrategy
{
	/**
	 * Checks if order is payable.
	 *
	 * @param orderData
	 *           the order data
	 */
	boolean isOrderPayable(final OrderData orderData);

}
