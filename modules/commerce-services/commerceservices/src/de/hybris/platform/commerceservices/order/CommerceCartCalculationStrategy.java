/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;


/**
 * Strategy to calculate cart.
 */
public interface CommerceCartCalculationStrategy
{
	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #calculateCart(de.hybris.platform.commerceservices.service.data.CommerceCartParameter)}
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	boolean calculateCart(final CartModel cartModel);

	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #recalculateCart(de.hybris.platform.commerceservices.service.data.CommerceCartParameter)}
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	boolean recalculateCart(final CartModel cartModel);

	/**
	 * Calculate cart.
	 *
	 * @param parameter
	 *           the parameter
	 * @return true, if successful
	 */
	boolean calculateCart(final CommerceCartParameter parameter);

	/**
	 * Recalculate cart.
	 *
	 * @param parameter
	 *           the parameter
	 * @return true, if successful
	 */
	boolean recalculateCart(final CommerceCartParameter parameter);


}
