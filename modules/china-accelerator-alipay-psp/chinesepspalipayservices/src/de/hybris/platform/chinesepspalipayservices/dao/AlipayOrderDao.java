/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.dao;

import de.hybris.platform.core.model.order.OrderModel;


/**
 * Looks up items related to {@link OrderModel}
 */
public interface AlipayOrderDao
{
	/**
	 * Gets order by order code
	 *
	 * @param code
	 *           the order code
	 * @return OrderModel if order found or returns null otherwise
	 */
	OrderModel findOrderByCode(final String code);
}
