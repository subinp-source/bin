/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpayservices.dao;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Optional;


/**
 * Provide method to find order model with given params
 */
public interface WeChatPayOrderDao
{


	/**
	 * Get OrderModel by code
	 *
	 * @param code
	 *           The order code of the wanted order
	 * @return OrderModel if found and an empty Optional otherwise
	 */
	Optional<OrderModel> findOrderByCode(final String code);
}
