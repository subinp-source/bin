/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated(since = "ages", forRemoval = false)
@SuppressWarnings({"unused","cast"})
public class GeneratedB2BCommerceConstants
{
	public static final String EXTENSIONNAME = "b2bcommerce";
	public static class TC
	{
		public static final String B2BBOOKINGLINEENTRY = "B2BBookingLineEntry".intern();
		public static final String B2BBOOKINGLINESTATUS = "B2BBookingLineStatus".intern();
		public static final String B2BBUDGET = "B2BBudget".intern();
		public static final String B2BCOMMENT = "B2BComment".intern();
		public static final String B2BCOSTCENTER = "B2BCostCenter".intern();
		public static final String B2BCREDITCHECKRESULT = "B2BCreditCheckResult".intern();
		public static final String B2BCREDITLIMIT = "B2BCreditLimit".intern();
		public static final String B2BCUSTOMER = "B2BCustomer".intern();
		public static final String B2BCUSTOMEREMAILCONSTRAINT = "B2BCustomerEmailConstraint".intern();
		public static final String B2BGROUPENUM = "B2BGroupEnum".intern();
		public static final String B2BMERCHANTCHECK = "B2BMerchantCheck".intern();
		public static final String B2BMERCHANTCHECKRESULT = "B2BMerchantCheckResult".intern();
		public static final String B2BPERIODRANGE = "B2BPeriodRange".intern();
		public static final String B2BPERMISSIONTYPEENUM = "B2BPermissionTypeEnum".intern();
		public static final String B2BQUOTELIMIT = "B2BQuoteLimit".intern();
		public static final String B2BRATETYPE = "B2BRateType".intern();
		public static final String B2BREPORTINGSET = "B2BReportingSet".intern();
		public static final String B2BUNIT = "B2BUnit".intern();
		public static final String B2BUSERGROUP = "B2BUserGroup".intern();
		public static final String BOOKINGTYPE = "BookingType".intern();
		public static final String FUTURESTOCK = "FutureStock".intern();
		public static final String MERCHANTCHECKSTATUS = "MerchantCheckStatus".intern();
		public static final String MERCHANTCHECKSTATUSEMAIL = "MerchantCheckStatusEmail".intern();
		public static final String MERCHANTCHECKTYPE = "MerchantCheckType".intern();
	}
	public static class Attributes
	{
		public static class AbstractOrder
		{
			public static final String B2BCOMMENTS = "b2bcomments".intern();
			public static final String LOCALE = "locale".intern();
			public static final String QUOTEEXPIRATIONDATE = "quoteExpirationDate".intern();
			public static final String UNIT = "Unit".intern();
			public static final String WORKFLOW = "workflow".intern();
		}
		public static class AbstractOrderEntry
		{
			public static final String COSTCENTER = "costCenter".intern();
		}
		public static class Employee
		{
			public static final String UNIT = "Unit".intern();
		}
		public static class Product
		{
			public static final String FUTURESTOCKS = "futureStocks".intern();
		}
	}
	public static class Enumerations
	{
		public static class B2BBookingLineStatus
		{
			public static final String OPEN = "OPEN".intern();
			public static final String INVOICED = "INVOICED".intern();
			public static final String PENDINGINVOICE = "PENDINGINVOICE".intern();
			public static final String DISABLED = "DISABLED".intern();
		}
		public static class B2BGroupEnum
		{
			public static final String B2BADMINGROUP = "b2badmingroup".intern();
			public static final String B2BCUSTOMERGROUP = "b2bcustomergroup".intern();
			public static final String B2BAPPROVERGROUP = "b2bapprovergroup".intern();
			public static final String B2BMANAGERGROUP = "b2bmanagergroup".intern();
		}
		public static class B2BPeriodRange
		{
			public static final String DAY = "DAY".intern();
			public static final String WEEK = "WEEK".intern();
			public static final String MONTH = "MONTH".intern();
			public static final String QUARTER = "QUARTER".intern();
			public static final String YEAR = "YEAR".intern();
		}
		public static class B2BPermissionTypeEnum
		{
			public static final String B2BORDERTHRESHOLDPERMISSION = "B2BOrderThresholdPermission".intern();
			public static final String B2BORDERTHRESHOLDTIMESPANPERMISSION = "B2BOrderThresholdTimespanPermission".intern();
			public static final String B2BBUDGETEXCEEDEDPERMISSION = "B2BBudgetExceededPermission".intern();
		}
		public static class B2BRateType
		{
			public static final String CURRENCY = "CURRENCY".intern();
			public static final String PERCENTAGE = "PERCENTAGE".intern();
		}
		public static class BookingType
		{
			public static final String ENTRY = "ENTRY".intern();
			public static final String SHIPPING = "SHIPPING".intern();
		}
		public static class MerchantCheckStatus
		{
			public static final String APPROVED = "APPROVED".intern();
			public static final String REJECTED = "REJECTED".intern();
			public static final String OPEN = "OPEN".intern();
			public static final String ERROR = "ERROR".intern();
			public static final String FAILURE = "FAILURE".intern();
		}
		public static class MerchantCheckStatusEmail
		{
			public static final String ALERT = "ALERT".intern();
			public static final String NO_ALERT = "NO_ALERT".intern();
		}
		public static class MerchantCheckType
		{
			public static final String CREDITLIMIT = "CREDITLIMIT".intern();
		}
		public static class OrderStatus
		{
			public static final String OPEN = "OPEN".intern();
			public static final String PENDING_APPROVAL = "PENDING_APPROVAL".intern();
			public static final String PENDING_APPROVAL_FROM_MERCHANT = "PENDING_APPROVAL_FROM_MERCHANT".intern();
			public static final String PENDING_QUOTE = "PENDING_QUOTE".intern();
			public static final String APPROVED_QUOTE = "APPROVED_QUOTE".intern();
			public static final String REJECTED_QUOTE = "REJECTED_QUOTE".intern();
			public static final String APPROVED = "APPROVED".intern();
			public static final String REJECTED = "REJECTED".intern();
			public static final String APPROVED_BY_MERCHANT = "APPROVED_BY_MERCHANT".intern();
			public static final String REJECTED_BY_MERCHANT = "REJECTED_BY_MERCHANT".intern();
			public static final String ASSIGNED_TO_ADMIN = "ASSIGNED_TO_ADMIN".intern();
			public static final String B2B_PROCESSING_ERROR = "B2B_PROCESSING_ERROR".intern();
		}
	}
	public static class Relations
	{
		public static final String ABSTRACTORDER2B2BCOMMENT = "AbstractOrder2B2BComment".intern();
		public static final String B2BBUDGETS2COSTCENTERS = "B2BBudgets2CostCenters".intern();
		public static final String B2BREPORTINGENTRY = "B2BReportingEntry".intern();
		public static final String B2BUNIT2ABSTRACTORDERS = "B2BUnit2AbstractOrders".intern();
		public static final String B2BUNIT2ACCOUNTMANAGERGROUP = "B2BUnit2AccountManagerGroup".intern();
		public static final String B2BUNIT2B2BBUDGET = "B2BUnit2B2BBudget".intern();
		public static final String B2BUNIT2B2BCOSTCENTER = "B2BUnit2B2BCostCenter".intern();
		public static final String B2BUNIT2B2BCREDITLIMIT = "B2BUnit2B2BCreditLimit".intern();
		public static final String B2BUNIT2B2BQUOTELIMIT = "B2BUnit2B2BQuoteLimit".intern();
		public static final String B2BUNIT2B2BUSERGROUPS = "B2BUnit2B2BUserGroups".intern();
		public static final String B2BUNIT2EMPLOYEE = "B2BUnit2Employee".intern();
		public static final String FUTURESTOCKPRODUCTRELATION = "FutureStockProductRelation".intern();
	}
	
	protected GeneratedB2BCommerceConstants()
	{
		// private constructor
	}
	
	
}
