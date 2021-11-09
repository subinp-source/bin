/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;


/**
 * A strategy for vendor order total price calculation
 */
public interface VendorOrderTotalPriceCalculationStrategy
{
	/**
	 * Calculate total price of a given consignment
	 *
	 * @param consignment
	 *           given consignment
	 * @return total price of the consignment
	 */
	double calculateTotalPrice(ConsignmentModel consignment);
}
