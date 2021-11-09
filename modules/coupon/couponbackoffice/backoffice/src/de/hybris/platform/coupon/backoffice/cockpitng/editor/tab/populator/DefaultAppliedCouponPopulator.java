/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.coupon.backoffice.cockpitng.editor.tab.populator;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.coupon.backoffice.data.AppliedCouponData;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.services.CouponService;


/**
 * Populator that provides more details about coupon based on the provided coupon code
 */
public class DefaultAppliedCouponPopulator implements Populator<String, AppliedCouponData>
{
	private CouponService couponService;

	@Override
	public void populate(final String code, final AppliedCouponData target)
	{
		target.setCode(code);

		final Optional<AbstractCouponModel> coupon = getCouponService().getCouponForCode(code);
		if (coupon.isPresent())
		{
			target.setName(coupon.get().getName());
		}
	}

	protected CouponService getCouponService()
	{
		return couponService;
	}

	@Required
	public void setCouponService(final CouponService couponService)
	{
		this.couponService = couponService;
	}
}
