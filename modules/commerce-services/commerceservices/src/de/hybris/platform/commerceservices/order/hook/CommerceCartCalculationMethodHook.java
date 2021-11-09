/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.hook;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * A hook interface into before and after cart calculation lifecycle
 */
public interface CommerceCartCalculationMethodHook
{
	/**
	 * Executed after commerce cart calculation
	 *
	 * @param parameter
	 *           a parameter object holding the cart data. parameter.cart may not be null.
	 *
	 * @throws IllegalArgumentException
	 *            in case parameter is null or parameter.cart is null.
	 */
	void afterCalculate(final CommerceCartParameter parameter);

	/**
	 * Executed before commerce cart calculation
	 *
	 * @param parameter
	 *           a parameter object holding the cart data. parameter.cart may not be null.
	 *
	 * @throws IllegalArgumentException
	 *            in case parameter is null or parameter.cart is null.
	 */
	void beforeCalculate(final CommerceCartParameter parameter);
}
