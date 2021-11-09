/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.strategies;

import de.hybris.platform.customercouponservices.model.CustomerCouponModel;


/**
 * Generates the customer coupon campaign URL when creating a new customer coupon
 *
 * @deprecated Since 2005      
 */
@Deprecated(since = "2005", forRemoval= true )
public interface CouponCampaignURLGenerationStrategy
{

	/**
	 * Generates the customer coupon campaign URL
	 * 
	 * @param coupon
	 *           the customer coupon model
	 * @return the campaign URL of the coupon
	 */
	String generate(CustomerCouponModel coupon);
}
