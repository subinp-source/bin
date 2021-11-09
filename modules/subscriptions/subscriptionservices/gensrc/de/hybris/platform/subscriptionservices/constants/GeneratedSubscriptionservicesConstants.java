/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated(since = "ages", forRemoval = false)
@SuppressWarnings({"unused","cast"})
public class GeneratedSubscriptionservicesConstants
{
	public static final String EXTENSIONNAME = "subscriptionservices";
	public static class TC
	{
		public static final String BILLINGCYCLETYPE = "BillingCycleType".intern();
		public static final String BILLINGEVENT = "BillingEvent".intern();
		public static final String BILLINGFREQUENCY = "BillingFrequency".intern();
		public static final String BILLINGPLAN = "BillingPlan".intern();
		public static final String BILLINGTIME = "BillingTime".intern();
		public static final String CHARGEENTRY = "ChargeEntry".intern();
		public static final String ONETIMECHARGEENTRY = "OneTimeChargeEntry".intern();
		public static final String OVERAGEUSAGECHARGEENTRY = "OverageUsageChargeEntry".intern();
		public static final String PERUNITUSAGECHARGE = "PerUnitUsageCharge".intern();
		public static final String PRICEROWSVALIDCONSTRAINT = "PriceRowsValidConstraint".intern();
		public static final String PROMOTIONBILLINGTIMERESTRICTION = "PromotionBillingTimeRestriction".intern();
		public static final String RECURRINGCHARGEENTRY = "RecurringChargeEntry".intern();
		public static final String SUBSCRIPTION = "Subscription".intern();
		public static final String SUBSCRIPTIONPRICEPLAN = "SubscriptionPricePlan".intern();
		public static final String SUBSCRIPTIONPRODUCT = "SubscriptionProduct".intern();
		public static final String SUBSCRIPTIONSTATUS = "SubscriptionStatus".intern();
		public static final String SUBSCRIPTIONTERM = "SubscriptionTerm".intern();
		public static final String TERMOFSERVICEFREQUENCY = "TermOfServiceFrequency".intern();
		public static final String TERMOFSERVICERENEWAL = "TermOfServiceRenewal".intern();
		public static final String TIERUSAGECHARGEENTRY = "TierUsageChargeEntry".intern();
		public static final String USAGECHARGE = "UsageCharge".intern();
		public static final String USAGECHARGEENTRY = "UsageChargeEntry".intern();
		public static final String USAGECHARGETYPE = "UsageChargeType".intern();
		public static final String USAGEUNIT = "UsageUnit".intern();
		public static final String VOLUMEUSAGECHARGE = "VolumeUsageCharge".intern();
	}
	public static class Attributes
	{
		public static class AbstractOrder
		{
			public static final String BILLINGTIME = "billingTime".intern();
			public static final String CHILDREN = "children".intern();
			public static final String PARENT = "parent".intern();
		}
		public static class AbstractOrderEntry
		{
			public static final String CHILDENTRIES = "childEntries".intern();
			public static final String MASTERENTRY = "masterEntry".intern();
			public static final String ORIGINALORDERENTRY = "originalOrderEntry".intern();
			public static final String ORIGINALSUBSCRIPTIONID = "originalSubscriptionId".intern();
			public static final String XMLPRODUCT = "xmlProduct".intern();
		}
		public static class CreditCardPaymentInfo
		{
			public static final String SUBSCRIPTIONSERVICEID = "subscriptionServiceId".intern();
		}
		public static class Product
		{
			public static final String PRICEROWSVALID = "priceRowsValid".intern();
			public static final String SUBSCRIPTIONTERM = "subscriptionTerm".intern();
		}
	}
	public static class Enumerations
	{
		public static class BillingCycleType
		{
			public static final String END_OF_MONTH = "end_of_month".intern();
			public static final String DAY_OF_MONTH = "day_of_month".intern();
			public static final String SUBSCRIPTION_START = "subscription_start".intern();
		}
		public static class SubscriptionStatus
		{
			public static final String ACTIVE = "active".intern();
			public static final String EXPIRED = "expired".intern();
			public static final String CANCELLED = "cancelled".intern();
		}
		public static class TermOfServiceFrequency
		{
			public static final String NONE = "none".intern();
			public static final String MONTHLY = "monthly".intern();
			public static final String QUARTERLY = "quarterly".intern();
			public static final String ANNUALLY = "annually".intern();
		}
		public static class TermOfServiceRenewal
		{
			public static final String NON_RENEWING = "non_renewing".intern();
			public static final String AUTO_RENEWING = "auto_renewing".intern();
			public static final String RENEWS_ONCE = "renews_once".intern();
			public static final String RENEWS_TWICE = "renews_twice".intern();
			public static final String RENEWS_THREE_TIMES = "renews_three_times".intern();
		}
		public static class UsageChargeType
		{
			public static final String HIGHEST_APPLICABLE_TIER = "highest_applicable_tier".intern();
			public static final String EACH_RESPECTIVE_TIER = "each_respective_tier".intern();
		}
	}
	public static class Relations
	{
		public static final String ABSTRACTMASTERORDER2ABSTRACTCHILDORDERRELATION = "AbstractMasterOrder2AbstractChildOrderRelation".intern();
		public static final String MASTERABSTRACTORDERENTRY2CHILDABSTRACTORDERENTRIESRELATION = "MasterAbstractOrderEntry2ChildAbstractOrderEntriesRelation".intern();
		public static final String SUBSCRIPTIONPRICEPLAN2ONETIMECHARGEENTRYRELATION = "SubscriptionPricePlan2OneTimeChargeEntryRelation".intern();
		public static final String SUBSCRIPTIONPRICEPLAN2RECURRINGCHARGEENTRYRELATION = "SubscriptionPricePlan2RecurringChargeEntryRelation".intern();
		public static final String SUBSCRIPTIONPRICEPLAN2USAGECHARGERELATION = "SubscriptionPricePlan2UsageChargeRelation".intern();
		public static final String SUBSCRIPTIONTERM2SUBSCRIPTIONPRODUCTRELATION = "SubscriptionTerm2SubscriptionProductRelation".intern();
		public static final String USAGECHARGE2USAGECHARGEENTRYRELATION = "UsageCharge2UsageChargeEntryRelation".intern();
	}
	
	protected GeneratedSubscriptionservicesConstants()
	{
		// private constructor
	}
	
	
}
