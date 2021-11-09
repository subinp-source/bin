/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.marketplaceservices.strategies.VendorOrderTotalPriceCalculationStrategy;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;


public class DefaultVendorOrderTotalPriceCalculationStrategy implements VendorOrderTotalPriceCalculationStrategy
{

	@Override
	public double calculateTotalPrice(ConsignmentModel consignment)
	{
		if (consignment.getConsignmentEntries() == null)
		{
			return 0;
		}
		else
		{
			return consignment.getConsignmentEntries().stream().map(ConsignmentEntryModel::getOrderEntry)
					.mapToDouble(AbstractOrderEntryModel::getTotalPrice).sum();
		}
	}

}