/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.services.impl;

import de.hybris.platform.b2b.services.B2BSaleQuoteService;
import de.hybris.platform.b2b.strategies.PlaceQuoteOrderStrategy;
import de.hybris.platform.core.model.order.OrderModel;


/**
 * @deprecated Since 6.3. Please see quote functionality from commerce.
 *
 *             Default implementation for {@link B2BSaleQuoteService}
 *
 * @spring.bean b2bSaleQuoteService
 */
@Deprecated(since = "6.3", forRemoval = true)
public class DefaultB2BSaleQuoteService implements B2BSaleQuoteService
{

	private PlaceQuoteOrderStrategy placeQuoteOrderStrategy;


	@Override
	public void placeQuoteOrder(final OrderModel order)
	{
		getPlaceQuoteOrderStrategy().placeQuoteOrder(order);
	}

	@Override
	public void placeOrderFromRejectedQuote(final OrderModel order)
	{
		getPlaceQuoteOrderStrategy().placeOrderFromRejectedQuote(order);
	}

	protected PlaceQuoteOrderStrategy getPlaceQuoteOrderStrategy()
	{
		return placeQuoteOrderStrategy;
	}


	public void setPlaceQuoteOrderStrategy(final PlaceQuoteOrderStrategy placeQuoteOrderStrategy)
	{
		this.placeQuoteOrderStrategy = placeQuoteOrderStrategy;
	}

}
