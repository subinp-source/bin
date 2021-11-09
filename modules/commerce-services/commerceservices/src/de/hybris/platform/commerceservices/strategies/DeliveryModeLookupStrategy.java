/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import java.util.List;


/**
 * A strategy to look up delivery modes based on the {@link AbstractOrderModel}
 * 
 */
public interface DeliveryModeLookupStrategy
{

	/**
	 * Gets the list of delivery modes for given order/cart
	 * 
	 * @param abstractOrderModel
	 * @return sorted list of delivery modes
	 */
	List<DeliveryModeModel> getSelectableDeliveryModesForOrder(AbstractOrderModel abstractOrderModel);
}
