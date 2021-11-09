/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.order;

import de.hybris.platform.core.model.order.OrderModel;


/**
 * Provides method to get order
 */
public interface AlipayOrderService
{
	/**
	 * Gets order by order code
	 *
	 * @param code
	 *           the order code
	 * @return OrderModel if order found or returns null otherwise
	 */
	OrderModel getOrderByCode(final String code);

}
