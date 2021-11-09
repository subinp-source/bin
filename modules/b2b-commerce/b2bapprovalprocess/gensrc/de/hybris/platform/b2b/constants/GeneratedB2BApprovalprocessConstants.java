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
public class GeneratedB2BApprovalprocessConstants
{
	public static final String EXTENSIONNAME = "b2bapprovalprocess";
	public static class TC
	{
		public static final String B2B2POEPERMISSIONA = "B2B2POEPermissionA".intern();
		public static final String B2B2POEPERMISSIONB = "B2B2POEPermissionB".intern();
		public static final String B2BAPPROVALPROCESS = "B2BApprovalProcess".intern();
		public static final String B2BBUDGETEXCEEDEDPERMISSION = "B2BBudgetExceededPermission".intern();
		public static final String B2BORDERTHRESHOLDPERMISSION = "B2BOrderThresholdPermission".intern();
		public static final String B2BORDERTHRESHOLDTIMESPANPERMISSION = "B2BOrderThresholdTimespanPermission".intern();
		public static final String B2BPERMISSION = "B2BPermission".intern();
		public static final String B2BPERMISSIONRESULT = "B2BPermissionResult".intern();
		public static final String ESCALATIONTASK = "EscalationTask".intern();
		public static final String PERMISSIONSTATUS = "PermissionStatus".intern();
		public static final String PERMISSIONTYPE = "PermissionType".intern();
		public static final String WORKFLOWTEMPLATETYPE = "WorkflowTemplateType".intern();
	}
	public static class Attributes
	{
		public static class AbstractOrder
		{
			public static final String PERMISSIONRESULTS = "PermissionResults".intern();
		}
		public static class AbstractWorkflowAction
		{
			public static final String QUALIFIER = "qualifier".intern();
		}
		public static class AbstractWorkflowDecision
		{
			public static final String QUALIFIER = "qualifier".intern();
		}
		public static class B2BCustomer
		{
			public static final String APPROVERGROUPS = "ApproverGroups".intern();
			public static final String APPROVERS = "Approvers".intern();
			public static final String PERMISSIONGROUPS = "PermissionGroups".intern();
			public static final String PERMISSIONS = "Permissions".intern();
		}
		public static class B2BUnit
		{
			public static final String APPROVALPROCESSCODE = "approvalProcessCode".intern();
			public static final String APPROVERGROUPS = "ApproverGroups".intern();
			public static final String APPROVERS = "Approvers".intern();
			public static final String PERMISSIONS = "Permissions".intern();
		}
		public static class B2BUserGroup
		{
			public static final String PERMISSIONS = "Permissions".intern();
		}
		public static class Order
		{
			public static final String EXHAUSTEDAPPROVERS = "exhaustedApprovers".intern();
		}
	}
	public static class Enumerations
	{
		public static class PermissionStatus
		{
			public static final String APPROVED = "APPROVED".intern();
			public static final String REJECTED = "REJECTED".intern();
			public static final String PENDING_APPROVAL = "PENDING_APPROVAL".intern();
			public static final String OPEN = "OPEN".intern();
			public static final String ERROR = "ERROR".intern();
			public static final String FAILURE = "FAILURE".intern();
		}
		public static class PermissionType
		{
			public static final String ORDERTHRESHOLD = "ORDERTHRESHOLD".intern();
			public static final String BUDGETEXCEEDED = "BUDGETEXCEEDED".intern();
			public static final String AMOUNTTIMESPAN = "AMOUNTTIMESPAN".intern();
		}
		public static class WorkflowTemplateType
		{
			public static final String ORDER_APPROVAL = "ORDER_APPROVAL".intern();
			public static final String CREDIT_LIMIT_ALERT = "CREDIT_LIMIT_ALERT".intern();
			public static final String MERCHANT_CHECK = "MERCHANT_CHECK".intern();
			public static final String SALES_QUOTES = "SALES_QUOTES".intern();
		}
	}
	public static class Relations
	{
		public static final String ABSTRACTORDER2B2BPERMISSIONRESULTS = "AbstractOrder2B2BPermissionResults".intern();
		public static final String B2BCUSTOMER2B2BPERMISSIONS = "B2BCustomer2B2BPermissions".intern();
		public static final String B2BCUSTOMERS2APPROVERGROUPS = "B2BCustomers2ApproverGroups".intern();
		public static final String B2BCUSTOMERS2APPROVERS = "B2BCustomers2Approvers".intern();
		public static final String B2BCUSTOMERS2PERMISSIONGROUPS = "B2BCustomers2PermissionGroups".intern();
		public static final String B2BUNIT2APPROVERGROUP = "B2BUnit2ApproverGroup".intern();
		public static final String B2BUNIT2APPROVERS = "B2BUnit2Approvers".intern();
		public static final String B2BUNIT2B2BPERMISSIONS = "B2BUnit2B2BPermissions".intern();
		public static final String B2BUSERGROUPS2B2BPERMISSIONS = "B2BUserGroups2B2BPermissions".intern();
		public static final String ORDER2EXHAUSTEDAPPROVERS = "Order2ExhaustedApprovers".intern();
	}
	
	protected GeneratedB2BApprovalprocessConstants()
	{
		// private constructor
	}
	
	
}
