/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.order.impl;

import de.hybris.platform.chinesepspalipayservices.dao.AlipayOrderDao;
import de.hybris.platform.chinesepspalipayservices.order.AlipayOrderService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.impl.DefaultOrderService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link AlipayOrderService}
 */
public class DefaultAlipayOrderService extends DefaultOrderService implements AlipayOrderService
{
	private transient AlipayOrderDao alipayOrderDao;

	@Required
	public void setAlipayOrderDao(final AlipayOrderDao alipayOrderDao)
	{
		this.alipayOrderDao = alipayOrderDao;
	}

	@Override
	public OrderModel getOrderByCode(final String code)
	{
		return alipayOrderDao.findOrderByCode(code);
	}

	protected AlipayOrderDao getAlipayOrderDao()
	{
		return alipayOrderDao;
	}


}
