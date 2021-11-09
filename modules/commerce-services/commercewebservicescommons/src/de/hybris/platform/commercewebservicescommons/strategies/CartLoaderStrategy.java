/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.strategies;

/**
 * Strategy loading user cart for current session
 */
public interface CartLoaderStrategy
{
	/**
	 * Loads cart for current session
	 *
	 * @param cartId
	 * 		Cart identifier (can be guid or code)
	 */
	void loadCart(final String cartId);

	/**
	 * Loads cart for current session
	 *
	 * @param cartId
	 * 		Cart identifier (can be guid or code)
	 * @param refresh
	 * 		Define if cart should be refreshed (recalculated). Refreshing cart can change it.
	 */
	default void loadCart(final String cartId, final boolean refresh)
	{
		loadCart(cartId);
	}
}
