/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated(since = "ages", forRemoval = false)
@SuppressWarnings({"unused","cast"})
public class GeneratedCustomercouponservicesConstants
{
	public static final String EXTENSIONNAME = "customercouponservices";
	public static class TC
	{
		public static final String COUPONNOTIFICATION = "CouponNotification".intern();
		public static final String COUPONNOTIFICATIONPROCESS = "couponNotificationProcess".intern();
		public static final String COUPONNOTIFICATIONSTATUS = "couponNotificationStatus".intern();
		public static final String CUSTOMERCOUPON = "CustomerCoupon".intern();
		public static final String CUSTOMERCOUPONFORPROMOTIONSOURCERULE = "CustomerCouponForPromotionSourceRule".intern();
	}
	public static class Attributes
	{
		public static class Customer
		{
			public static final String CUSTOMERCOUPONS = "customerCoupons".intern();
		}
	}
	public static class Enumerations
	{
		public static class CouponNotificationStatus
		{
			public static final String INIT = "INIT".intern();
			public static final String EFFECTIVESENT = "EFFECTIVESENT".intern();
			public static final String EXPIRESENT = "EXPIRESENT".intern();
		}
		public static class NotificationType
		{
			public static final String COUPON_EFFECTIVE = "COUPON_EFFECTIVE".intern();
			public static final String COUPON_EXPIRE = "COUPON_EXPIRE".intern();
		}
	}
	public static class Relations
	{
		public static final String CUSTOMERCOUPON2CUSTOMER = "CustomerCoupon2Customer".intern();
	}
	
	protected GeneratedCustomercouponservicesConstants()
	{
		// private constructor
	}
	
	
}
