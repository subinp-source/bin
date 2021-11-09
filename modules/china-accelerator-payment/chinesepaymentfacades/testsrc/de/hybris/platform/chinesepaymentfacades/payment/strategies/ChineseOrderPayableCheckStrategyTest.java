/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentfacades.payment.strategies;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chinesepaymentfacades.payment.strategies.impl.ChineseOrderPayableCheckStrategy;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.enums.PaymentStatus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ChineseOrderPayableCheckStrategyTest
{
	private ChineseOrderPayableCheckStrategy chineseOrderPayableCheckStrategy;
	private OrderData order1;
	private OrderData order2;
	private OrderData order3;
	private OrderData order4;
	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		chineseOrderPayableCheckStrategy = new ChineseOrderPayableCheckStrategy();
		order1 = new OrderData();
		order2 = new OrderData();
		order3 = new OrderData();
		order4 = new OrderData();

		order1.setPaymentStatus(PaymentStatus.NOTPAID);

		order2.setStatus(OrderStatus.CREATED);

		order3.setStatus(OrderStatus.CANCELLED);
		order3.setPaymentStatus(PaymentStatus.NOTPAID);

		order4.setStatus(OrderStatus.CREATED);
		order4.setPaymentStatus(PaymentStatus.NOTPAID);


	}

	@Test
	public void testOrderPayable()
	{
		Assert.assertFalse(chineseOrderPayableCheckStrategy.isOrderPayable(null));
		Assert.assertFalse(chineseOrderPayableCheckStrategy.isOrderPayable(order1));
		Assert.assertFalse(chineseOrderPayableCheckStrategy.isOrderPayable(order2));
		Assert.assertFalse(chineseOrderPayableCheckStrategy.isOrderPayable(order3));
		Assert.assertTrue(chineseOrderPayableCheckStrategy.isOrderPayable(order4));

	}
}
