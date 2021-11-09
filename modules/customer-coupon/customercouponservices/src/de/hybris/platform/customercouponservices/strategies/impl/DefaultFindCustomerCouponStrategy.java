/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.strategies.impl;

import static java.util.Optional.empty;

import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.strategies.impl.AbstractFindCouponStrategy;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;

import java.util.Optional;


/**
 * Validates CustomerCouponModel, otherwise customer coupon can't be applied
 */
public class DefaultFindCustomerCouponStrategy extends AbstractFindCouponStrategy
{

	@Override
	protected Optional<AbstractCouponModel> couponValidation(final AbstractCouponModel coupon)
	{
		return (coupon instanceof CustomerCouponModel) ? super.couponValidation(coupon) : empty();
	}

	@Override
	protected String getCouponId(final String couponCode)
	{
		return couponCode;
	}
}
