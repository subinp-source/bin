/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.coupon.impl;

import de.hybris.platform.commercefacades.coupon.CouponDataFacade;
import de.hybris.platform.commercefacades.coupon.data.CouponData;

import java.util.Optional;


/**
 * Default Implementation of {@link CouponDataFacade} returning an Optional.empty()
 *
 */
public class DefaultCouponDataFacade implements CouponDataFacade
{

	@Override
	public Optional<CouponData> getCouponDetails(final String couponCode)
	{
		return Optional.empty();
	}

}
