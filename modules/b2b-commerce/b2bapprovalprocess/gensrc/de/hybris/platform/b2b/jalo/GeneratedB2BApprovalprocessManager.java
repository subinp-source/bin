/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.jalo;

import de.hybris.platform.b2b.constants.B2BApprovalprocessConstants;
import de.hybris.platform.b2b.jalo.B2B2POEPermissionA;
import de.hybris.platform.b2b.jalo.B2B2POEPermissionB;
import de.hybris.platform.b2b.jalo.B2BBudgetExceededPermission;
import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.b2b.jalo.B2BOrderThresholdPermission;
import de.hybris.platform.b2b.jalo.B2BOrderThresholdTimespanPermission;
import de.hybris.platform.b2b.jalo.B2BPermission;
import de.hybris.platform.b2b.jalo.B2BPermissionResult;
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.b2b.jalo.B2BUserGroup;
import de.hybris.platform.b2b.jalo.EscalationTask;
import de.hybris.platform.b2b.process.approval.jalo.B2BApprovalProcess;
import de.hybris.platform.commerceservices.jalo.OrgUnit;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.workflow.jalo.AbstractWorkflowAction;
import de.hybris.platform.workflow.jalo.AbstractWorkflowDecision;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type <code>B2BApprovalprocessManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BApprovalprocessManager extends Extension
{
	/** Relation ordering override parameter constants for B2BUnit2ApproverGroup from ((b2bapprovalprocess))*/
	protected static String B2BUNIT2APPROVERGROUP_SRC_ORDERED = "relation.B2BUnit2ApproverGroup.source.ordered";
	protected static String B2BUNIT2APPROVERGROUP_TGT_ORDERED = "relation.B2BUnit2ApproverGroup.target.ordered";
	/** Relation disable markmodifed parameter constants for B2BUnit2ApproverGroup from ((b2bapprovalprocess))*/
	protected static String B2BUNIT2APPROVERGROUP_MARKMODIFIED = "relation.B2BUnit2ApproverGroup.markmodified";
	/** Relation ordering override parameter constants for B2BUnit2Approvers from ((b2bapprovalprocess))*/
	protected static String B2BUNIT2APPROVERS_SRC_ORDERED = "relation.B2BUnit2Approvers.source.ordered";
	protected static String B2BUNIT2APPROVERS_TGT_ORDERED = "relation.B2BUnit2Approvers.target.ordered";
	/** Relation disable markmodifed parameter constants for B2BUnit2Approvers from ((b2bapprovalprocess))*/
	protected static String B2BUNIT2APPROVERS_MARKMODIFIED = "relation.B2BUnit2Approvers.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n PERMISSIONS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<B2BPermission> B2BUNIT2B2BPERMISSIONSPERMISSIONSHANDLER = new OneToManyHandler<B2BPermission>(
	B2BApprovalprocessConstants.TC.B2BPERMISSION,
	false,
	"Unit",
	"UnitPOS",
	true,
	true,
	CollectionType.SET
	);
	/** Relation ordering override parameter constants for B2BCustomers2ApproverGroups from ((b2bapprovalprocess))*/
	protected static String B2BCUSTOMERS2APPROVERGROUPS_SRC_ORDERED = "relation.B2BCustomers2ApproverGroups.source.ordered";
	protected static String B2BCUSTOMERS2APPROVERGROUPS_TGT_ORDERED = "relation.B2BCustomers2ApproverGroups.target.ordered";
	/** Relation disable markmodifed parameter constants for B2BCustomers2ApproverGroups from ((b2bapprovalprocess))*/
	protected static String B2BCUSTOMERS2APPROVERGROUPS_MARKMODIFIED = "relation.B2BCustomers2ApproverGroups.markmodified";
	/** Relation ordering override parameter constants for B2BCustomers2Approvers from ((b2bapprovalprocess))*/
	protected static String B2BCUSTOMERS2APPROVERS_SRC_ORDERED = "relation.B2BCustomers2Approvers.source.ordered";
	protected static String B2BCUSTOMERS2APPROVERS_TGT_ORDERED = "relation.B2BCustomers2Approvers.target.ordered";
	/** Relation disable markmodifed parameter constants for B2BCustomers2Approvers from ((b2bapprovalprocess))*/
	protected static String B2BCUSTOMERS2APPROVERS_MARKMODIFIED = "relation.B2BCustomers2Approvers.markmodified";
	/** Relation ordering override parameter constants for B2BCustomers2PermissionGroups from ((b2bapprovalprocess))*/
	protected static String B2BCUSTOMERS2PERMISSIONGROUPS_SRC_ORDERED = "relation.B2BCustomers2PermissionGroups.source.ordered";
	protected static String B2BCUSTOMERS2PERMISSIONGROUPS_TGT_ORDERED = "relation.B2BCustomers2PermissionGroups.target.ordered";
	/** Relation disable markmodifed parameter constants for B2BCustomers2PermissionGroups from ((b2bapprovalprocess))*/
	protected static String B2BCUSTOMERS2PERMISSIONGROUPS_MARKMODIFIED = "relation.B2BCustomers2PermissionGroups.markmodified";
	/** Relation ordering override parameter constants for B2BCustomer2B2BPermissions from ((b2bapprovalprocess))*/
	protected static String B2BCUSTOMER2B2BPERMISSIONS_SRC_ORDERED = "relation.B2BCustomer2B2BPermissions.source.ordered";
	protected static String B2BCUSTOMER2B2BPERMISSIONS_TGT_ORDERED = "relation.B2BCustomer2B2BPermissions.target.ordered";
	/** Relation disable markmodifed parameter constants for B2BCustomer2B2BPermissions from ((b2bapprovalprocess))*/
	protected static String B2BCUSTOMER2B2BPERMISSIONS_MARKMODIFIED = "relation.B2BCustomer2B2BPermissions.markmodified";
	/** Relation ordering override parameter constants for B2BUserGroups2B2BPermissions from ((b2bapprovalprocess))*/
	protected static String B2BUSERGROUPS2B2BPERMISSIONS_SRC_ORDERED = "relation.B2BUserGroups2B2BPermissions.source.ordered";
	protected static String B2BUSERGROUPS2B2BPERMISSIONS_TGT_ORDERED = "relation.B2BUserGroups2B2BPermissions.target.ordered";
	/** Relation disable markmodifed parameter constants for B2BUserGroups2B2BPermissions from ((b2bapprovalprocess))*/
	protected static String B2BUSERGROUPS2B2BPERMISSIONS_MARKMODIFIED = "relation.B2BUserGroups2B2BPermissions.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n PERMISSIONRESULTS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<B2BPermissionResult> ABSTRACTORDER2B2BPERMISSIONRESULTSPERMISSIONRESULTSHANDLER = new OneToManyHandler<B2BPermissionResult>(
	B2BApprovalprocessConstants.TC.B2BPERMISSIONRESULT,
	true,
	"Order",
	"OrderPOS",
	true,
	true,
	CollectionType.COLLECTION
	);
	/** Relation ordering override parameter constants for Order2ExhaustedApprovers from ((b2bapprovalprocess))*/
	protected static String ORDER2EXHAUSTEDAPPROVERS_SRC_ORDERED = "relation.Order2ExhaustedApprovers.source.ordered";
	protected static String ORDER2EXHAUSTEDAPPROVERS_TGT_ORDERED = "relation.Order2ExhaustedApprovers.target.ordered";
	/** Relation disable markmodifed parameter constants for Order2ExhaustedApprovers from ((b2bapprovalprocess))*/
	protected static String ORDER2EXHAUSTEDAPPROVERS_MARKMODIFIED = "relation.Order2ExhaustedApprovers.markmodified";
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("qualifier", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.workflow.jalo.AbstractWorkflowAction", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("qualifier", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.workflow.jalo.AbstractWorkflowDecision", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("approvalProcessCode", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.b2b.jalo.B2BUnit", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.approvalProcessCode</code> attribute.
	 * @return the approvalProcessCode - A name of the process to be consumed by process engine for
	 * 						b2b order approval as defined in the process definition file.
	 */
	public String getApprovalProcessCode(final SessionContext ctx, final B2BUnit item)
	{
		return (String)item.getProperty( ctx, B2BApprovalprocessConstants.Attributes.B2BUnit.APPROVALPROCESSCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.approvalProcessCode</code> attribute.
	 * @return the approvalProcessCode - A name of the process to be consumed by process engine for
	 * 						b2b order approval as defined in the process definition file.
	 */
	public String getApprovalProcessCode(final B2BUnit item)
	{
		return getApprovalProcessCode( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.approvalProcessCode</code> attribute. 
	 * @param value the approvalProcessCode - A name of the process to be consumed by process engine for
	 * 						b2b order approval as defined in the process definition file.
	 */
	public void setApprovalProcessCode(final SessionContext ctx, final B2BUnit item, final String value)
	{
		item.setProperty(ctx, B2BApprovalprocessConstants.Attributes.B2BUnit.APPROVALPROCESSCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.approvalProcessCode</code> attribute. 
	 * @param value the approvalProcessCode - A name of the process to be consumed by process engine for
	 * 						b2b order approval as defined in the process definition file.
	 */
	public void setApprovalProcessCode(final B2BUnit item, final String value)
	{
		setApprovalProcessCode( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.ApproverGroups</code> attribute.
	 * @return the ApproverGroups - User groups associated to a B2BUnit that hold other employees who can approve orders
	 */
	public Set<B2BUserGroup> getApproverGroups(final SessionContext ctx, final B2BUnit item)
	{
		final List<B2BUserGroup> items = item.getLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUNIT2APPROVERGROUP,
			"B2BUserGroup",
			null,
			Utilities.getRelationOrderingOverride(B2BUNIT2APPROVERGROUP_SRC_ORDERED, true),
			false
		);
		return new LinkedHashSet<B2BUserGroup>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.ApproverGroups</code> attribute.
	 * @return the ApproverGroups - User groups associated to a B2BUnit that hold other employees who can approve orders
	 */
	public Set<B2BUserGroup> getApproverGroups(final B2BUnit item)
	{
		return getApproverGroups( getSession().getSessionContext(), item );
	}
	
	public long getApproverGroupsCount(final SessionContext ctx, final B2BUnit item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUNIT2APPROVERGROUP,
			"B2BUserGroup",
			null
		);
	}
	
	public long getApproverGroupsCount(final B2BUnit item)
	{
		return getApproverGroupsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.ApproverGroups</code> attribute. 
	 * @param value the ApproverGroups - User groups associated to a B2BUnit that hold other employees who can approve orders
	 */
	public void setApproverGroups(final SessionContext ctx, final B2BUnit item, final Set<B2BUserGroup> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUNIT2APPROVERGROUP,
			null,
			value,
			Utilities.getRelationOrderingOverride(B2BUNIT2APPROVERGROUP_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BUNIT2APPROVERGROUP_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.ApproverGroups</code> attribute. 
	 * @param value the ApproverGroups - User groups associated to a B2BUnit that hold other employees who can approve orders
	 */
	public void setApproverGroups(final B2BUnit item, final Set<B2BUserGroup> value)
	{
		setApproverGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to ApproverGroups. 
	 * @param value the item to add to ApproverGroups - User groups associated to a B2BUnit that hold other employees who can approve orders
	 */
	public void addToApproverGroups(final SessionContext ctx, final B2BUnit item, final B2BUserGroup value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUNIT2APPROVERGROUP,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BUNIT2APPROVERGROUP_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BUNIT2APPROVERGROUP_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to ApproverGroups. 
	 * @param value the item to add to ApproverGroups - User groups associated to a B2BUnit that hold other employees who can approve orders
	 */
	public void addToApproverGroups(final B2BUnit item, final B2BUserGroup value)
	{
		addToApproverGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from ApproverGroups. 
	 * @param value the item to remove from ApproverGroups - User groups associated to a B2BUnit that hold other employees who can approve orders
	 */
	public void removeFromApproverGroups(final SessionContext ctx, final B2BUnit item, final B2BUserGroup value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUNIT2APPROVERGROUP,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BUNIT2APPROVERGROUP_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BUNIT2APPROVERGROUP_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from ApproverGroups. 
	 * @param value the item to remove from ApproverGroups - User groups associated to a B2BUnit that hold other employees who can approve orders
	 */
	public void removeFromApproverGroups(final B2BUnit item, final B2BUserGroup value)
	{
		removeFromApproverGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.ApproverGroups</code> attribute.
	 * @return the ApproverGroups
	 */
	public Set<B2BUserGroup> getApproverGroups(final SessionContext ctx, final B2BCustomer item)
	{
		final List<B2BUserGroup> items = item.getLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2APPROVERGROUPS,
			"B2BUserGroup",
			null,
			false,
			false
		);
		return new LinkedHashSet<B2BUserGroup>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.ApproverGroups</code> attribute.
	 * @return the ApproverGroups
	 */
	public Set<B2BUserGroup> getApproverGroups(final B2BCustomer item)
	{
		return getApproverGroups( getSession().getSessionContext(), item );
	}
	
	public long getApproverGroupsCount(final SessionContext ctx, final B2BCustomer item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2APPROVERGROUPS,
			"B2BUserGroup",
			null
		);
	}
	
	public long getApproverGroupsCount(final B2BCustomer item)
	{
		return getApproverGroupsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.ApproverGroups</code> attribute. 
	 * @param value the ApproverGroups
	 */
	public void setApproverGroups(final SessionContext ctx, final B2BCustomer item, final Set<B2BUserGroup> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2APPROVERGROUPS,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMERS2APPROVERGROUPS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.ApproverGroups</code> attribute. 
	 * @param value the ApproverGroups
	 */
	public void setApproverGroups(final B2BCustomer item, final Set<B2BUserGroup> value)
	{
		setApproverGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to ApproverGroups. 
	 * @param value the item to add to ApproverGroups
	 */
	public void addToApproverGroups(final SessionContext ctx, final B2BCustomer item, final B2BUserGroup value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2APPROVERGROUPS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMERS2APPROVERGROUPS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to ApproverGroups. 
	 * @param value the item to add to ApproverGroups
	 */
	public void addToApproverGroups(final B2BCustomer item, final B2BUserGroup value)
	{
		addToApproverGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from ApproverGroups. 
	 * @param value the item to remove from ApproverGroups
	 */
	public void removeFromApproverGroups(final SessionContext ctx, final B2BCustomer item, final B2BUserGroup value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2APPROVERGROUPS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMERS2APPROVERGROUPS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from ApproverGroups. 
	 * @param value the item to remove from ApproverGroups
	 */
	public void removeFromApproverGroups(final B2BCustomer item, final B2BUserGroup value)
	{
		removeFromApproverGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.Approvers</code> attribute.
	 * @return the Approvers - Approvers associated to a customer
	 */
	public Set<B2BCustomer> getApprovers(final SessionContext ctx, final B2BUnit item)
	{
		final List<B2BCustomer> items = item.getLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUNIT2APPROVERS,
			"B2BCustomer",
			null,
			Utilities.getRelationOrderingOverride(B2BUNIT2APPROVERS_SRC_ORDERED, true),
			false
		);
		return new LinkedHashSet<B2BCustomer>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.Approvers</code> attribute.
	 * @return the Approvers - Approvers associated to a customer
	 */
	public Set<B2BCustomer> getApprovers(final B2BUnit item)
	{
		return getApprovers( getSession().getSessionContext(), item );
	}
	
	public long getApproversCount(final SessionContext ctx, final B2BUnit item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUNIT2APPROVERS,
			"B2BCustomer",
			null
		);
	}
	
	public long getApproversCount(final B2BUnit item)
	{
		return getApproversCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.Approvers</code> attribute. 
	 * @param value the Approvers - Approvers associated to a customer
	 */
	public void setApprovers(final SessionContext ctx, final B2BUnit item, final Set<B2BCustomer> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUNIT2APPROVERS,
			null,
			value,
			Utilities.getRelationOrderingOverride(B2BUNIT2APPROVERS_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BUNIT2APPROVERS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.Approvers</code> attribute. 
	 * @param value the Approvers - Approvers associated to a customer
	 */
	public void setApprovers(final B2BUnit item, final Set<B2BCustomer> value)
	{
		setApprovers( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Approvers. 
	 * @param value the item to add to Approvers - Approvers associated to a customer
	 */
	public void addToApprovers(final SessionContext ctx, final B2BUnit item, final B2BCustomer value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUNIT2APPROVERS,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BUNIT2APPROVERS_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BUNIT2APPROVERS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Approvers. 
	 * @param value the item to add to Approvers - Approvers associated to a customer
	 */
	public void addToApprovers(final B2BUnit item, final B2BCustomer value)
	{
		addToApprovers( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Approvers. 
	 * @param value the item to remove from Approvers - Approvers associated to a customer
	 */
	public void removeFromApprovers(final SessionContext ctx, final B2BUnit item, final B2BCustomer value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUNIT2APPROVERS,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BUNIT2APPROVERS_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BUNIT2APPROVERS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Approvers. 
	 * @param value the item to remove from Approvers - Approvers associated to a customer
	 */
	public void removeFromApprovers(final B2BUnit item, final B2BCustomer value)
	{
		removeFromApprovers( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.Approvers</code> attribute.
	 * @return the Approvers
	 */
	public Set<B2BCustomer> getApprovers(final SessionContext ctx, final B2BCustomer item)
	{
		final List<B2BCustomer> items = item.getLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2APPROVERS,
			"B2BCustomer",
			null,
			false,
			false
		);
		return new LinkedHashSet<B2BCustomer>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.Approvers</code> attribute.
	 * @return the Approvers
	 */
	public Set<B2BCustomer> getApprovers(final B2BCustomer item)
	{
		return getApprovers( getSession().getSessionContext(), item );
	}
	
	public long getApproversCount(final SessionContext ctx, final B2BCustomer item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2APPROVERS,
			"B2BCustomer",
			null
		);
	}
	
	public long getApproversCount(final B2BCustomer item)
	{
		return getApproversCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.Approvers</code> attribute. 
	 * @param value the Approvers
	 */
	public void setApprovers(final SessionContext ctx, final B2BCustomer item, final Set<B2BCustomer> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2APPROVERS,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMERS2APPROVERS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.Approvers</code> attribute. 
	 * @param value the Approvers
	 */
	public void setApprovers(final B2BCustomer item, final Set<B2BCustomer> value)
	{
		setApprovers( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Approvers. 
	 * @param value the item to add to Approvers
	 */
	public void addToApprovers(final SessionContext ctx, final B2BCustomer item, final B2BCustomer value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2APPROVERS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMERS2APPROVERS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Approvers. 
	 * @param value the item to add to Approvers
	 */
	public void addToApprovers(final B2BCustomer item, final B2BCustomer value)
	{
		addToApprovers( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Approvers. 
	 * @param value the item to remove from Approvers
	 */
	public void removeFromApprovers(final SessionContext ctx, final B2BCustomer item, final B2BCustomer value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2APPROVERS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMERS2APPROVERS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Approvers. 
	 * @param value the item to remove from Approvers
	 */
	public void removeFromApprovers(final B2BCustomer item, final B2BCustomer value)
	{
		removeFromApprovers( getSession().getSessionContext(), item, value );
	}
	
	public B2B2POEPermissionA createB2B2POEPermissionA(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( B2BApprovalprocessConstants.TC.B2B2POEPERMISSIONA );
			return (B2B2POEPermissionA)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2B2POEPermissionA : "+e.getMessage(), 0 );
		}
	}
	
	public B2B2POEPermissionA createB2B2POEPermissionA(final Map attributeValues)
	{
		return createB2B2POEPermissionA( getSession().getSessionContext(), attributeValues );
	}
	
	public B2B2POEPermissionB createB2B2POEPermissionB(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( B2BApprovalprocessConstants.TC.B2B2POEPERMISSIONB );
			return (B2B2POEPermissionB)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2B2POEPermissionB : "+e.getMessage(), 0 );
		}
	}
	
	public B2B2POEPermissionB createB2B2POEPermissionB(final Map attributeValues)
	{
		return createB2B2POEPermissionB( getSession().getSessionContext(), attributeValues );
	}
	
	public B2BApprovalProcess createB2BApprovalProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( B2BApprovalprocessConstants.TC.B2BAPPROVALPROCESS );
			return (B2BApprovalProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BApprovalProcess : "+e.getMessage(), 0 );
		}
	}
	
	public B2BApprovalProcess createB2BApprovalProcess(final Map attributeValues)
	{
		return createB2BApprovalProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public B2BBudgetExceededPermission createB2BBudgetExceededPermission(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( B2BApprovalprocessConstants.TC.B2BBUDGETEXCEEDEDPERMISSION );
			return (B2BBudgetExceededPermission)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BBudgetExceededPermission : "+e.getMessage(), 0 );
		}
	}
	
	public B2BBudgetExceededPermission createB2BBudgetExceededPermission(final Map attributeValues)
	{
		return createB2BBudgetExceededPermission( getSession().getSessionContext(), attributeValues );
	}
	
	public B2BOrderThresholdPermission createB2BOrderThresholdPermission(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( B2BApprovalprocessConstants.TC.B2BORDERTHRESHOLDPERMISSION );
			return (B2BOrderThresholdPermission)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BOrderThresholdPermission : "+e.getMessage(), 0 );
		}
	}
	
	public B2BOrderThresholdPermission createB2BOrderThresholdPermission(final Map attributeValues)
	{
		return createB2BOrderThresholdPermission( getSession().getSessionContext(), attributeValues );
	}
	
	public B2BOrderThresholdTimespanPermission createB2BOrderThresholdTimespanPermission(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( B2BApprovalprocessConstants.TC.B2BORDERTHRESHOLDTIMESPANPERMISSION );
			return (B2BOrderThresholdTimespanPermission)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BOrderThresholdTimespanPermission : "+e.getMessage(), 0 );
		}
	}
	
	public B2BOrderThresholdTimespanPermission createB2BOrderThresholdTimespanPermission(final Map attributeValues)
	{
		return createB2BOrderThresholdTimespanPermission( getSession().getSessionContext(), attributeValues );
	}
	
	public B2BPermissionResult createB2BPermissionResult(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( B2BApprovalprocessConstants.TC.B2BPERMISSIONRESULT );
			return (B2BPermissionResult)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BPermissionResult : "+e.getMessage(), 0 );
		}
	}
	
	public B2BPermissionResult createB2BPermissionResult(final Map attributeValues)
	{
		return createB2BPermissionResult( getSession().getSessionContext(), attributeValues );
	}
	
	public EscalationTask createEscalationTask(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( B2BApprovalprocessConstants.TC.ESCALATIONTASK );
			return (EscalationTask)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating EscalationTask : "+e.getMessage(), 0 );
		}
	}
	
	public EscalationTask createEscalationTask(final Map attributeValues)
	{
		return createEscalationTask( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.exhaustedApprovers</code> attribute.
	 * @return the exhaustedApprovers
	 */
	public Set<B2BCustomer> getExhaustedApprovers(final SessionContext ctx, final Order item)
	{
		final List<B2BCustomer> items = item.getLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.ORDER2EXHAUSTEDAPPROVERS,
			"B2BCustomer",
			null,
			false,
			false
		);
		return new LinkedHashSet<B2BCustomer>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.exhaustedApprovers</code> attribute.
	 * @return the exhaustedApprovers
	 */
	public Set<B2BCustomer> getExhaustedApprovers(final Order item)
	{
		return getExhaustedApprovers( getSession().getSessionContext(), item );
	}
	
	public long getExhaustedApproversCount(final SessionContext ctx, final Order item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.ORDER2EXHAUSTEDAPPROVERS,
			"B2BCustomer",
			null
		);
	}
	
	public long getExhaustedApproversCount(final Order item)
	{
		return getExhaustedApproversCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.exhaustedApprovers</code> attribute. 
	 * @param value the exhaustedApprovers
	 */
	public void setExhaustedApprovers(final SessionContext ctx, final Order item, final Set<B2BCustomer> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.ORDER2EXHAUSTEDAPPROVERS,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(ORDER2EXHAUSTEDAPPROVERS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.exhaustedApprovers</code> attribute. 
	 * @param value the exhaustedApprovers
	 */
	public void setExhaustedApprovers(final Order item, final Set<B2BCustomer> value)
	{
		setExhaustedApprovers( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to exhaustedApprovers. 
	 * @param value the item to add to exhaustedApprovers
	 */
	public void addToExhaustedApprovers(final SessionContext ctx, final Order item, final B2BCustomer value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.ORDER2EXHAUSTEDAPPROVERS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ORDER2EXHAUSTEDAPPROVERS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to exhaustedApprovers. 
	 * @param value the item to add to exhaustedApprovers
	 */
	public void addToExhaustedApprovers(final Order item, final B2BCustomer value)
	{
		addToExhaustedApprovers( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from exhaustedApprovers. 
	 * @param value the item to remove from exhaustedApprovers
	 */
	public void removeFromExhaustedApprovers(final SessionContext ctx, final Order item, final B2BCustomer value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.ORDER2EXHAUSTEDAPPROVERS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ORDER2EXHAUSTEDAPPROVERS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from exhaustedApprovers. 
	 * @param value the item to remove from exhaustedApprovers
	 */
	public void removeFromExhaustedApprovers(final Order item, final B2BCustomer value)
	{
		removeFromExhaustedApprovers( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return B2BApprovalprocessConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.PermissionGroups</code> attribute.
	 * @return the PermissionGroups
	 */
	public Set<B2BUserGroup> getPermissionGroups(final SessionContext ctx, final B2BCustomer item)
	{
		final List<B2BUserGroup> items = item.getLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2PERMISSIONGROUPS,
			"B2BUserGroup",
			null,
			false,
			false
		);
		return new LinkedHashSet<B2BUserGroup>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.PermissionGroups</code> attribute.
	 * @return the PermissionGroups
	 */
	public Set<B2BUserGroup> getPermissionGroups(final B2BCustomer item)
	{
		return getPermissionGroups( getSession().getSessionContext(), item );
	}
	
	public long getPermissionGroupsCount(final SessionContext ctx, final B2BCustomer item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2PERMISSIONGROUPS,
			"B2BUserGroup",
			null
		);
	}
	
	public long getPermissionGroupsCount(final B2BCustomer item)
	{
		return getPermissionGroupsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.PermissionGroups</code> attribute. 
	 * @param value the PermissionGroups
	 */
	public void setPermissionGroups(final SessionContext ctx, final B2BCustomer item, final Set<B2BUserGroup> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2PERMISSIONGROUPS,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMERS2PERMISSIONGROUPS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.PermissionGroups</code> attribute. 
	 * @param value the PermissionGroups
	 */
	public void setPermissionGroups(final B2BCustomer item, final Set<B2BUserGroup> value)
	{
		setPermissionGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to PermissionGroups. 
	 * @param value the item to add to PermissionGroups
	 */
	public void addToPermissionGroups(final SessionContext ctx, final B2BCustomer item, final B2BUserGroup value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2PERMISSIONGROUPS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMERS2PERMISSIONGROUPS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to PermissionGroups. 
	 * @param value the item to add to PermissionGroups
	 */
	public void addToPermissionGroups(final B2BCustomer item, final B2BUserGroup value)
	{
		addToPermissionGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from PermissionGroups. 
	 * @param value the item to remove from PermissionGroups
	 */
	public void removeFromPermissionGroups(final SessionContext ctx, final B2BCustomer item, final B2BUserGroup value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMERS2PERMISSIONGROUPS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMERS2PERMISSIONGROUPS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from PermissionGroups. 
	 * @param value the item to remove from PermissionGroups
	 */
	public void removeFromPermissionGroups(final B2BCustomer item, final B2BUserGroup value)
	{
		removeFromPermissionGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.PermissionResults</code> attribute.
	 * @return the PermissionResults
	 */
	public Collection<B2BPermissionResult> getPermissionResults(final SessionContext ctx, final AbstractOrder item)
	{
		return ABSTRACTORDER2B2BPERMISSIONRESULTSPERMISSIONRESULTSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.PermissionResults</code> attribute.
	 * @return the PermissionResults
	 */
	public Collection<B2BPermissionResult> getPermissionResults(final AbstractOrder item)
	{
		return getPermissionResults( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.PermissionResults</code> attribute. 
	 * @param value the PermissionResults
	 */
	public void setPermissionResults(final SessionContext ctx, final AbstractOrder item, final Collection<B2BPermissionResult> value)
	{
		ABSTRACTORDER2B2BPERMISSIONRESULTSPERMISSIONRESULTSHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.PermissionResults</code> attribute. 
	 * @param value the PermissionResults
	 */
	public void setPermissionResults(final AbstractOrder item, final Collection<B2BPermissionResult> value)
	{
		setPermissionResults( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to PermissionResults. 
	 * @param value the item to add to PermissionResults
	 */
	public void addToPermissionResults(final SessionContext ctx, final AbstractOrder item, final B2BPermissionResult value)
	{
		ABSTRACTORDER2B2BPERMISSIONRESULTSPERMISSIONRESULTSHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to PermissionResults. 
	 * @param value the item to add to PermissionResults
	 */
	public void addToPermissionResults(final AbstractOrder item, final B2BPermissionResult value)
	{
		addToPermissionResults( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from PermissionResults. 
	 * @param value the item to remove from PermissionResults
	 */
	public void removeFromPermissionResults(final SessionContext ctx, final AbstractOrder item, final B2BPermissionResult value)
	{
		ABSTRACTORDER2B2BPERMISSIONRESULTSPERMISSIONRESULTSHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from PermissionResults. 
	 * @param value the item to remove from PermissionResults
	 */
	public void removeFromPermissionResults(final AbstractOrder item, final B2BPermissionResult value)
	{
		removeFromPermissionResults( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.Permissions</code> attribute.
	 * @return the Permissions - Permissions associated to a unit B2BUnit
	 */
	public Set<B2BPermission> getPermissions(final SessionContext ctx, final B2BUnit item)
	{
		return (Set<B2BPermission>)B2BUNIT2B2BPERMISSIONSPERMISSIONSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.Permissions</code> attribute.
	 * @return the Permissions - Permissions associated to a unit B2BUnit
	 */
	public Set<B2BPermission> getPermissions(final B2BUnit item)
	{
		return getPermissions( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.Permissions</code> attribute. 
	 * @param value the Permissions - Permissions associated to a unit B2BUnit
	 */
	public void setPermissions(final SessionContext ctx, final B2BUnit item, final Set<B2BPermission> value)
	{
		B2BUNIT2B2BPERMISSIONSPERMISSIONSHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.Permissions</code> attribute. 
	 * @param value the Permissions - Permissions associated to a unit B2BUnit
	 */
	public void setPermissions(final B2BUnit item, final Set<B2BPermission> value)
	{
		setPermissions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Permissions. 
	 * @param value the item to add to Permissions - Permissions associated to a unit B2BUnit
	 */
	public void addToPermissions(final SessionContext ctx, final B2BUnit item, final B2BPermission value)
	{
		B2BUNIT2B2BPERMISSIONSPERMISSIONSHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Permissions. 
	 * @param value the item to add to Permissions - Permissions associated to a unit B2BUnit
	 */
	public void addToPermissions(final B2BUnit item, final B2BPermission value)
	{
		addToPermissions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Permissions. 
	 * @param value the item to remove from Permissions - Permissions associated to a unit B2BUnit
	 */
	public void removeFromPermissions(final SessionContext ctx, final B2BUnit item, final B2BPermission value)
	{
		B2BUNIT2B2BPERMISSIONSPERMISSIONSHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Permissions. 
	 * @param value the item to remove from Permissions - Permissions associated to a unit B2BUnit
	 */
	public void removeFromPermissions(final B2BUnit item, final B2BPermission value)
	{
		removeFromPermissions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.Permissions</code> attribute.
	 * @return the Permissions - Permissions associated to a B2BCustomer
	 */
	public Set<B2BPermission> getPermissions(final SessionContext ctx, final B2BCustomer item)
	{
		final List<B2BPermission> items = item.getLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMER2B2BPERMISSIONS,
			"B2BPermission",
			null,
			Utilities.getRelationOrderingOverride(B2BCUSTOMER2B2BPERMISSIONS_SRC_ORDERED, true),
			false
		);
		return new LinkedHashSet<B2BPermission>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.Permissions</code> attribute.
	 * @return the Permissions - Permissions associated to a B2BCustomer
	 */
	public Set<B2BPermission> getPermissions(final B2BCustomer item)
	{
		return getPermissions( getSession().getSessionContext(), item );
	}
	
	public long getPermissionsCount(final SessionContext ctx, final B2BCustomer item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMER2B2BPERMISSIONS,
			"B2BPermission",
			null
		);
	}
	
	public long getPermissionsCount(final B2BCustomer item)
	{
		return getPermissionsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.Permissions</code> attribute. 
	 * @param value the Permissions - Permissions associated to a B2BCustomer
	 */
	public void setPermissions(final SessionContext ctx, final B2BCustomer item, final Set<B2BPermission> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMER2B2BPERMISSIONS,
			null,
			value,
			Utilities.getRelationOrderingOverride(B2BCUSTOMER2B2BPERMISSIONS_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMER2B2BPERMISSIONS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.Permissions</code> attribute. 
	 * @param value the Permissions - Permissions associated to a B2BCustomer
	 */
	public void setPermissions(final B2BCustomer item, final Set<B2BPermission> value)
	{
		setPermissions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Permissions. 
	 * @param value the item to add to Permissions - Permissions associated to a B2BCustomer
	 */
	public void addToPermissions(final SessionContext ctx, final B2BCustomer item, final B2BPermission value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMER2B2BPERMISSIONS,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BCUSTOMER2B2BPERMISSIONS_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMER2B2BPERMISSIONS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Permissions. 
	 * @param value the item to add to Permissions - Permissions associated to a B2BCustomer
	 */
	public void addToPermissions(final B2BCustomer item, final B2BPermission value)
	{
		addToPermissions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Permissions. 
	 * @param value the item to remove from Permissions - Permissions associated to a B2BCustomer
	 */
	public void removeFromPermissions(final SessionContext ctx, final B2BCustomer item, final B2BPermission value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMER2B2BPERMISSIONS,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BCUSTOMER2B2BPERMISSIONS_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMER2B2BPERMISSIONS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Permissions. 
	 * @param value the item to remove from Permissions - Permissions associated to a B2BCustomer
	 */
	public void removeFromPermissions(final B2BCustomer item, final B2BPermission value)
	{
		removeFromPermissions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUserGroup.Permissions</code> attribute.
	 * @return the Permissions
	 */
	public List<B2BPermission> getPermissions(final SessionContext ctx, final B2BUserGroup item)
	{
		final List<B2BPermission> items = item.getLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUSERGROUPS2B2BPERMISSIONS,
			"B2BPermission",
			null,
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUserGroup.Permissions</code> attribute.
	 * @return the Permissions
	 */
	public List<B2BPermission> getPermissions(final B2BUserGroup item)
	{
		return getPermissions( getSession().getSessionContext(), item );
	}
	
	public long getPermissionsCount(final SessionContext ctx, final B2BUserGroup item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUSERGROUPS2B2BPERMISSIONS,
			"B2BPermission",
			null
		);
	}
	
	public long getPermissionsCount(final B2BUserGroup item)
	{
		return getPermissionsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUserGroup.Permissions</code> attribute. 
	 * @param value the Permissions
	 */
	public void setPermissions(final SessionContext ctx, final B2BUserGroup item, final List<B2BPermission> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUSERGROUPS2B2BPERMISSIONS,
			null,
			value,
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(B2BUSERGROUPS2B2BPERMISSIONS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUserGroup.Permissions</code> attribute. 
	 * @param value the Permissions
	 */
	public void setPermissions(final B2BUserGroup item, final List<B2BPermission> value)
	{
		setPermissions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Permissions. 
	 * @param value the item to add to Permissions
	 */
	public void addToPermissions(final SessionContext ctx, final B2BUserGroup item, final B2BPermission value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUSERGROUPS2B2BPERMISSIONS,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(B2BUSERGROUPS2B2BPERMISSIONS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Permissions. 
	 * @param value the item to add to Permissions
	 */
	public void addToPermissions(final B2BUserGroup item, final B2BPermission value)
	{
		addToPermissions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Permissions. 
	 * @param value the item to remove from Permissions
	 */
	public void removeFromPermissions(final SessionContext ctx, final B2BUserGroup item, final B2BPermission value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			B2BApprovalprocessConstants.Relations.B2BUSERGROUPS2B2BPERMISSIONS,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(B2BUSERGROUPS2B2BPERMISSIONS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Permissions. 
	 * @param value the item to remove from Permissions
	 */
	public void removeFromPermissions(final B2BUserGroup item, final B2BPermission value)
	{
		removeFromPermissions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.qualifier</code> attribute.
	 * @return the qualifier - Used to qualify an action by a unique name within a context of a workflow
	 */
	public String getQualifier(final SessionContext ctx, final AbstractWorkflowAction item)
	{
		return (String)item.getProperty( ctx, B2BApprovalprocessConstants.Attributes.AbstractWorkflowAction.QUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowAction.qualifier</code> attribute.
	 * @return the qualifier - Used to qualify an action by a unique name within a context of a workflow
	 */
	public String getQualifier(final AbstractWorkflowAction item)
	{
		return getQualifier( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractWorkflowAction.qualifier</code> attribute. 
	 * @param value the qualifier - Used to qualify an action by a unique name within a context of a workflow
	 */
	public void setQualifier(final SessionContext ctx, final AbstractWorkflowAction item, final String value)
	{
		item.setProperty(ctx, B2BApprovalprocessConstants.Attributes.AbstractWorkflowAction.QUALIFIER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractWorkflowAction.qualifier</code> attribute. 
	 * @param value the qualifier - Used to qualify an action by a unique name within a context of a workflow
	 */
	public void setQualifier(final AbstractWorkflowAction item, final String value)
	{
		setQualifier( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowDecision.qualifier</code> attribute.
	 * @return the qualifier - Used to qualify a Desicion by name
	 */
	public String getQualifier(final SessionContext ctx, final AbstractWorkflowDecision item)
	{
		return (String)item.getProperty( ctx, B2BApprovalprocessConstants.Attributes.AbstractWorkflowDecision.QUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractWorkflowDecision.qualifier</code> attribute.
	 * @return the qualifier - Used to qualify a Desicion by name
	 */
	public String getQualifier(final AbstractWorkflowDecision item)
	{
		return getQualifier( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractWorkflowDecision.qualifier</code> attribute. 
	 * @param value the qualifier - Used to qualify a Desicion by name
	 */
	public void setQualifier(final SessionContext ctx, final AbstractWorkflowDecision item, final String value)
	{
		item.setProperty(ctx, B2BApprovalprocessConstants.Attributes.AbstractWorkflowDecision.QUALIFIER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractWorkflowDecision.qualifier</code> attribute. 
	 * @param value the qualifier - Used to qualify a Desicion by name
	 */
	public void setQualifier(final AbstractWorkflowDecision item, final String value)
	{
		setQualifier( getSession().getSessionContext(), item, value );
	}
	
}
