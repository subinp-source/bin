/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.util.DiscountValue;

import java.util.List;


/**
 * Strategy that provides read and write access to quote specific discounts of an order.
 */
public interface OrderQuoteDiscountValuesAccessor
{
	/**
	 * Returns a list quote specific discounts for the given order.
	 *
	 * @param order
	 *           the order to return quote discount values for.
	 * @return a list of quote specific discount values
	 * @throws IllegalArgumentException
	 *            in the given order is null
	 */
	List<DiscountValue> getQuoteDiscountValues(AbstractOrderModel order);

	/**
	 * Sets quote specific discount values for the given order and sets the order's calculated flag to false. The change
	 * is not persisted.
	 *
	 * @param order
	 *           the order to set quote discount values for.
	 * @param discountValues
	 *           the list of discount values to set
	 * @throws IllegalArgumentException
	 *            in case any of the parameters is null
	 */
	void setQuoteDiscountValues(AbstractOrderModel order, List<DiscountValue> discountValues);
}
