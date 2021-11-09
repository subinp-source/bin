/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponfacades.strategies;


/**
 * Checks if the specific customer coupon can be removed the when removing it
 */
public interface CustomerCouponRemovableStrategy
{

	/**
	 * Checks if the specific customer coupon can be removed from the current customer
	 *
	 * @param couponCode
	 *           the coupon code
	 * @return true if the coupon can be removed and false otherwise
	 */
	default boolean checkRemovable(final String couponCode)
	{
		return true;
	}

}
