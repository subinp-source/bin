/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.strategies;

import de.hybris.platform.core.model.order.OrderModel;


/**
 * @deprecated Since 6.3. Please see quote functionality from commerce.
 */
@Deprecated(since = "6.3", forRemoval = true)
public interface PlaceQuoteOrderStrategy
{
	public void placeQuoteOrder(final OrderModel order);

	public void placeOrderFromRejectedQuote(OrderModel order);
}
