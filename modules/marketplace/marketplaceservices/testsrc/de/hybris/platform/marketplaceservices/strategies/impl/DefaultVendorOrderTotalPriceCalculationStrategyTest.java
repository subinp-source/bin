/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DefaultVendorOrderTotalPriceCalculationStrategyTest
{
	private DefaultVendorOrderTotalPriceCalculationStrategy defaultConsignmentTotalPriceCalculationStrategy;

	private ConsignmentModel consignment;

	@Before
	public void prepare()
	{
		defaultConsignmentTotalPriceCalculationStrategy = new DefaultVendorOrderTotalPriceCalculationStrategy();
		consignment = new ConsignmentModel();
		final AbstractOrderEntryModel orderEntry1 = new OrderEntryModel();
		orderEntry1.setTotalPrice(0.01);
		final AbstractOrderEntryModel orderEntry2 = new OrderEntryModel();
		orderEntry2.setTotalPrice(0.02);
		final ConsignmentEntryModel consignmentEntry1 = new ConsignmentEntryModel();
		consignmentEntry1.setOrderEntry(orderEntry1);
		final ConsignmentEntryModel consignmentEntry2 = new ConsignmentEntryModel();
		consignmentEntry2.setOrderEntry(orderEntry2);
		consignment.setConsignmentEntries(new HashSet<>(Arrays.asList(consignmentEntry1, consignmentEntry2)));
	}

	@Test
	public void testCalculateTotalPrice()
	{
		final double totalPrice = defaultConsignmentTotalPriceCalculationStrategy.calculateTotalPrice(consignment);
		Assert.assertEquals(0.03, totalPrice, 0.000001);
	}

}
