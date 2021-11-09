/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated(since = "ages", forRemoval = false)
@SuppressWarnings({"unused","cast"})
public class GeneratedB2BAcceleratorServicesConstants
{
	public static final String EXTENSIONNAME = "b2bacceleratorservices";
	public static class TC
	{
		public static final String CHECKOUTPAYMENTTYPE = "CheckoutPaymentType".intern();
		public static final String ORDERTHRESHOLDDISCOUNTPERCENTAGEPROMOTION = "OrderThresholdDiscountPercentagePromotion".intern();
		public static final String PRODUCTPRICEDISCOUNTPROMOTIONBYPAYMENTTYPE = "ProductPriceDiscountPromotionByPaymentType".intern();
		public static final String PRODUCTTHRESHOLDPRICEDISCOUNTPROMOTION = "ProductThresholdPriceDiscountPromotion".intern();
		public static final String REPLENISHMENTPROCESS = "ReplenishmentProcess".intern();
	}
	public static class Attributes
	{
		public static class AbstractOrder
		{
			public static final String PAYMENTTYPE = "paymentType".intern();
			public static final String PURCHASEORDERNUMBER = "purchaseOrderNumber".intern();
		}
		public static class CartToOrderCronJob
		{
			public static final String ORDERS = "orders".intern();
		}
		public static class Order
		{
			public static final String SCHEDULINGCRONJOB = "schedulingCronJob".intern();
		}
	}
	public static class Enumerations
	{
		public static class CheckoutFlowEnum
		{
			public static final String SINGLE = "SINGLE".intern();
		}
		public static class CheckoutPaymentType
		{
			public static final String CARD = "CARD".intern();
			public static final String ACCOUNT = "ACCOUNT".intern();
		}
	}
	public static class Relations
	{
		public static final String ORDER2CARTTOORDERCRONJOB = "Order2CartToOrderCronJob".intern();
	}
	
	protected GeneratedB2BAcceleratorServicesConstants()
	{
		// private constructor
	}
	
	
}
