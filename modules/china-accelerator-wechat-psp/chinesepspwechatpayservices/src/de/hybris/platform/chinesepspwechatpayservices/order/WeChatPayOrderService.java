/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpayservices.order;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Optional;




/**
 * Provide method to get order model
 */
public interface WeChatPayOrderService
{
	/**
	 * Get OrderModel by OrderCode
	 *
	 * @param code
	 *           The order code of the wanted order
	 * @return OrderModel if found and empty option otherwise
	 */
	Optional<OrderModel> getOrderByCode(String code);

}
