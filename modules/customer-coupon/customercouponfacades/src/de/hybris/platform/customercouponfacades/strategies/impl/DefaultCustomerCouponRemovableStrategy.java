/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponfacades.strategies.impl;

import de.hybris.platform.customercouponfacades.strategies.CustomerCouponRemovableStrategy;
import de.hybris.platform.customercouponservices.CustomerCouponService;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link CustomerCouponRemovableStrategy}
 */
public class DefaultCustomerCouponRemovableStrategy implements CustomerCouponRemovableStrategy
{

	private CustomerCouponService customerCouponService;


	@Override
	public boolean checkRemovable(final String couponCode)
	{
		return getCustomerCouponService()
				.getCouponForCode(couponCode)
				.map(coupon -> Boolean.valueOf(coupon.getEndDate() != null
						&& coupon.getEndDate().after(Calendar.getInstance().getTime()))).orElse(Boolean.FALSE).booleanValue();
	}


	protected CustomerCouponService getCustomerCouponService()
	{
		return customerCouponService;
	}

	@Required
	public void setCustomerCouponService(final CustomerCouponService customerCouponService)
	{
		this.customerCouponService = customerCouponService;
	}

}
