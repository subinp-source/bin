/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.services;

import de.hybris.platform.core.model.order.OrderModel;


/**
 * @deprecated Since 6.3. Please see quote functionality from commerce.
 *
 *             The Interface B2BSaleQuoteService. This service places a customer's order which requests a sales quote
 *             from an account manager.
 *
 * @spring.bean b2bSaleQuoteService
 */
@Deprecated(since = "6.3", forRemoval = true)
public interface B2BSaleQuoteService
{

	/**
	 * Places an order that requests a sales quote from an account manager.
	 *
	 * @param order
	 *           the sales order quote
	 */
	public void placeQuoteOrder(final OrderModel order);

	/**
	 * This will reset the OrderStatus to CREATED and then follow the path of the {@link #placeQuoteOrder(OrderModel)}
	 *
	 * @param order
	 *           the sales order quote
	 */
	public void placeOrderFromRejectedQuote(final OrderModel order);

}
