/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.stock.strategies;

import de.hybris.platform.ordersplitting.model.StockLevelModel;

import java.util.Collection;


/**
 * This strategy is designed to consolidate stock level calculations.
 */
public interface CommerceAvailabilityCalculationStrategy
{
	Long calculateAvailability(Collection<StockLevelModel> stockLevels);
}
