/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpayservices.order.impl;

import de.hybris.platform.chinesepspwechatpayservices.dao.WeChatPayOrderDao;
import de.hybris.platform.chinesepspwechatpayservices.order.WeChatPayOrderService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.impl.DefaultOrderService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Global class for Chinesepspwechatpayservices order impl.
 */
public class DefaultWeChatPayOrderService extends DefaultOrderService implements WeChatPayOrderService
{
	private transient WeChatPayOrderDao weChatPayOrderDao;

	@Override
	public Optional<OrderModel> getOrderByCode(final String code)
	{
		return weChatPayOrderDao.findOrderByCode(code);
	}

	@Required
	public void setWeChatPayOrderDao(final WeChatPayOrderDao weChatPayOrderDao)
	{
		this.weChatPayOrderDao = weChatPayOrderDao;
	}

	protected WeChatPayOrderDao getWeChatPayOrderDao()
	{
		return weChatPayOrderDao;
	}


}
