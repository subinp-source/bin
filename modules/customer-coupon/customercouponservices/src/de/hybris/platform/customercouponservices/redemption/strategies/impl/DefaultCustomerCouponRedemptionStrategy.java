/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.redemption.strategies.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.CouponServiceException;
import de.hybris.platform.couponservices.redemption.strategies.CouponRedemptionStrategy;
import de.hybris.platform.customercouponservices.daos.CustomerCouponDao;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.couponservices.constants.CouponServicesConstants.COUPON_CODE_INVALID_ERROR_CODE;


/**
 * Checks if customer coupon is redeemable when placing an order
 */
public class DefaultCustomerCouponRedemptionStrategy implements CouponRedemptionStrategy<CustomerCouponModel>
{
	private CustomerCouponDao customerCouponDao;

	@Override
	public boolean isRedeemable(final CustomerCouponModel coupon, final AbstractOrderModel abstractOrder, final String couponCode)
	{
		return isCouponRedeemable(coupon, abstractOrder.getUser(), couponCode);
	}

	@Override
	public boolean isCouponRedeemable(final CustomerCouponModel coupon, final UserModel user, final String couponCode)
	{
		if (!coupon.getCustomers().contains(user))
		{
			throw new CouponServiceException(COUPON_CODE_INVALID_ERROR_CODE);
		}
		return getCustomerCouponDao().checkCustomerCouponAvailableForCustomer(couponCode, (CustomerModel) user);
	}

	protected CustomerCouponDao getCustomerCouponDao()
	{
		return customerCouponDao;
	}

	@Required
	public void setCustomerCouponDao(final CustomerCouponDao customerCouponDao)
	{
		this.customerCouponDao = customerCouponDao;
	}

}
