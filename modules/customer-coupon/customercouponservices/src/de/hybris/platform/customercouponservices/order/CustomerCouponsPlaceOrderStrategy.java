/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.order;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;


/**
 * Deals with customer coupon for the customer after placing order
 */
public interface CustomerCouponsPlaceOrderStrategy
{
	/**
	 * Removes the coupons from the customer and resets the notification status
	 *
	 * @param currentUser
	 *           the current user used for removing from related user group
	 * @param order
	 *           the order used for finding the applied coupons
	 */
	void removeCouponsForCustomer(UserModel currentUser, OrderModel order);
	
	/**
	 * Redirects the continue url to the Open-Catalogue if there is coupon code in continue url
	 */
	void updateContinueUrl();
}
