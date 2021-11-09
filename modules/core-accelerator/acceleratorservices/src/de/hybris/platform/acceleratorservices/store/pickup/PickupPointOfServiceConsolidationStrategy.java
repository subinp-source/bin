/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.store.pickup;

import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.core.model.order.CartModel;

import java.util.List;


/**
 * Service that attempts to return a number of PointsOfService that could be used to consolidate pickup
 */
public interface PickupPointOfServiceConsolidationStrategy
{
	List<PointOfServiceDistanceData> getConsolidationOptions(CartModel cart);
}
