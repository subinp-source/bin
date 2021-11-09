/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.strategies;

import de.hybris.platform.core.model.order.OrderModel;


public interface BusinessProcessStrategy
{
	void createB2BBusinessProcess(OrderModel order);
}
