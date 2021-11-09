/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.strategies.impl;

import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chinesepaymentservices.model.ChinesePaymentInfoModel;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayPaymentInfoStrategy;
import de.hybris.platform.core.model.order.OrderModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultAlipaySubmitOrderStrategyUnitTest
{
	private DefaultAlipaySubmitOrderStrategy defaultAlipaySubmitOrderStrategy;

	@Mock
	private AlipayPaymentInfoStrategy alipayPaymentInfoStrategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		defaultAlipaySubmitOrderStrategy = new DefaultAlipaySubmitOrderStrategy();
		defaultAlipaySubmitOrderStrategy.setAlipayPaymentInfoStrategy(alipayPaymentInfoStrategy);
	}

	@Test
	public void testSubmitOrder()
	{
		OrderModel orderModel = new OrderModel();
		orderModel.setStatus(null);
		ChinesePaymentInfoModel chinesePaymentInfoModel = new ChinesePaymentInfoModel();
		orderModel.setPaymentInfo(chinesePaymentInfoModel);
		defaultAlipaySubmitOrderStrategy.submitOrder(orderModel);
		verify(alipayPaymentInfoStrategy).updatePaymentInfoForPlaceOrder(orderModel);

	}

}
