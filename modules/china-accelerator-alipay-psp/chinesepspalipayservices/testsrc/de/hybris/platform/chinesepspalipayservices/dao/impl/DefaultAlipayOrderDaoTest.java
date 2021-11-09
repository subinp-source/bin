/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.dao.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultAlipayOrderDaoTest extends ServicelayerTransactionalTest
{
	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "alipayOrderDao")
	private DefaultAlipayOrderDao defaultAlipayOrderDao;

	@Before
	public void prepare()
	{
		CurrencyModel currencyModel = new CurrencyModel();
		currencyModel.setIsocode("USD");

		OrderModel orderModel = new OrderModel();
		orderModel.setCode("00000001");
		orderModel.setTotalPrice(1.5);
		orderModel.setCurrency(currencyModel);
		orderModel.setDate(new Date());
		orderModel.setUser(userService.getCurrentUser());

		modelService.save(orderModel);
	}

	@Test
	public void testFindOrderByCode()
	{
		OrderModel fundOrder = defaultAlipayOrderDao.findOrderByCode("00000001");
		assertEquals("00000001", fundOrder.getCode());
		assertEquals(1.5, fundOrder.getTotalPrice().doubleValue(), 0.0001);
	}
}
