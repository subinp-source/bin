/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.cronjob;

import de.hybris.platform.core.model.ItemModel;

import java.util.Map;


/**
 * Creates a new coupon notification tast
 */
public class CouponNotificationJob extends AbstractCouponNotificationJob
{
	@Override
	protected CouponNotificationTask createTask(final Map<String, ItemModel> data)
	{
		return new CouponNotificationTask(getNotificationService(), data);
	}
}
