/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartfacades.strategies;

import de.hybris.platform.commercefacades.order.data.CartData;


/**
 * Orders cart entries when displaying the cart page
 */
public interface CartEntriesOrderingStrategy
{

	/**
	 * Orders cart entries
	 *
	 * @param cartData
	 *           the cart data with entries to be sorted
	 * @return the cart data with correct ordering
	 */
	CartData ordering(CartData cartData);
}
