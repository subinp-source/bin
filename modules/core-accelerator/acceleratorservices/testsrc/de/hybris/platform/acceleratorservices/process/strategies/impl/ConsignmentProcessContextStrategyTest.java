/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.process.strategies.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;

/**
 * Test class for ConsignmentProcessContextStrategy
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ConsignmentProcessContextStrategyTest
{
	@Mock
	private AbstractOrderModel abstractOrderModel;

	@Mock
	private ConsignmentProcessModel businessProcessModel;

	@Mock
	private ConsignmentModel consignmentModel;

	@InjectMocks
	private ConsignmentProcessContextStrategy strategy = new ConsignmentProcessContextStrategy();

	@Test
	public void testGetOrderModel() throws Exception
	{
		given(businessProcessModel.getConsignment()).willReturn(consignmentModel);
		given(consignmentModel.getOrder()).willReturn(abstractOrderModel);

		final Optional<AbstractOrderModel> orderModelOptional = strategy.getOrderModel(businessProcessModel);

		assertSame(abstractOrderModel, orderModelOptional.get());
	}
}
