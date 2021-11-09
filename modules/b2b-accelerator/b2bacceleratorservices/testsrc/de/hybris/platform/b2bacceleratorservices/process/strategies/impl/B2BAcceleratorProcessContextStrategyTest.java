/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.process.strategies.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2bacceleratorservices.model.process.ReplenishmentProcessModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;


/**
 * Test for class B2BAcceleratorProcessContextStrategy
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class B2BAcceleratorProcessContextStrategyTest
{
	@Mock
	private ReplenishmentProcessModel replenishmentProcessModel;
	@Mock
	private CartToOrderCronJobModel cartToOrderCronJobModel;
	@Mock
	private CartModel cartModel;

	@InjectMocks
	private B2BAcceleratorProcessContextStrategy contextStrategy = new B2BAcceleratorProcessContextStrategy();

	@Before
	public void setUp() throws Exception
	{
		given(replenishmentProcessModel.getCartToOrderCronJob()).willReturn(cartToOrderCronJobModel);
		given(cartToOrderCronJobModel.getCart()).willReturn(cartModel);
	}

	@Test
	public void testGetCmsSite() throws Exception
	{
		assertSame(cartModel, contextStrategy.getOrderModel(replenishmentProcessModel).get());
	}
}
