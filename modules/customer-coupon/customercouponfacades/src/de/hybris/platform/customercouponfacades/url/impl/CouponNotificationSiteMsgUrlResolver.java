/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponfacades.url.impl;

import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.notificationfacades.url.SiteMessageUrlResolver;


/**
 * Resolves the URL related to coupon notification in site message
 */
public class CouponNotificationSiteMsgUrlResolver extends SiteMessageUrlResolver<CustomerCouponModel>
{

	@Override
	public String resolve(final CustomerCouponModel source)
	{
		return getDefaultUrl();
	}

}
