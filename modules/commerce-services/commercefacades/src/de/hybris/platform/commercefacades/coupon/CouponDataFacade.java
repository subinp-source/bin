/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.coupon;

import de.hybris.platform.commercefacades.coupon.data.CouponData;

import java.util.Optional;


/**
 * Coupon Data facade interface. Manages populating {@link CouponData} from AbstractCouponModel.
 */
public interface CouponDataFacade
{
	/**
	 * Get {@link CouponData} object based on its code.
	 *
	 * @param couponCode
	 *           coupon identifier
	 * @return the {@link CouponData}
	 *
	 * @throws IllegalArgumentException
	 *            if parameter code is <code>null</code> or empty
	 */
	Optional<CouponData> getCouponDetails(String couponCode);
}
