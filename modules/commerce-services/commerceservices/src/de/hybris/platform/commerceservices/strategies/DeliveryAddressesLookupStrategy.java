/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.List;


/**
 * A strategy to look up delivery addresses based on the {@link AbstractOrderModel}
 */
public interface DeliveryAddressesLookupStrategy
{
	/**
	 * Gets the list of delivery addresses for an order
	 * 
	 * @param abstractOrder the order
	 * @param visibleAddressesOnly include only the visible addresses
	 * @return A list of delivery address for an order.
	 */
	List<AddressModel> getDeliveryAddressesForOrder(AbstractOrderModel abstractOrder, boolean visibleAddressesOnly);
}
