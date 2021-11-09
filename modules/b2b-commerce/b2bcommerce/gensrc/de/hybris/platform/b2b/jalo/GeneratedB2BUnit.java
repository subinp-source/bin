/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.jalo;

import de.hybris.platform.b2b.constants.B2BCommerceConstants;
import de.hybris.platform.b2b.jalo.B2BBudget;
import de.hybris.platform.b2b.jalo.B2BCostCenter;
import de.hybris.platform.b2b.jalo.B2BCreditLimit;
import de.hybris.platform.b2b.jalo.B2BQuoteLimit;
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.b2b.jalo.B2BUserGroup;
import de.hybris.platform.commerceservices.jalo.OrgUnit;
import de.hybris.platform.constants.CoreConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BUnit B2BUnit}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BUnit extends OrgUnit
{
	/** Qualifier of the <code>B2BUnit.reportingOrganization</code> attribute **/
	public static final String REPORTINGORGANIZATION = "reportingOrganization";
	/** Qualifier of the <code>B2BUnit.UserGroups</code> attribute **/
	public static final String USERGROUPS = "UserGroups";
	/** Qualifier of the <code>B2BUnit.CostCenters</code> attribute **/
	public static final String COSTCENTERS = "CostCenters";
	/** Qualifier of the <code>B2BUnit.Budgets</code> attribute **/
	public static final String BUDGETS = "Budgets";
	/** Qualifier of the <code>B2BUnit.Orders</code> attribute **/
	public static final String ORDERS = "Orders";
	/** Qualifier of the <code>B2BUnit.CreditLimit</code> attribute **/
	public static final String CREDITLIMIT = "CreditLimit";
	/** Qualifier of the <code>B2BUnit.QuoteLimit</code> attribute **/
	public static final String QUOTELIMIT = "QuoteLimit";
	/** Qualifier of the <code>B2BUnit.accountManager</code> attribute **/
	public static final String ACCOUNTMANAGER = "accountManager";
	/** Qualifier of the <code>B2BUnit.AccountManagerGroups</code> attribute **/
	public static final String ACCOUNTMANAGERGROUPS = "AccountManagerGroups";
	/** Relation ordering override parameter constants for B2BUnit2AccountManagerGroup from ((b2bcommerce))*/
	protected static String B2BUNIT2ACCOUNTMANAGERGROUP_SRC_ORDERED = "relation.B2BUnit2AccountManagerGroup.source.ordered";
	protected static String B2BUNIT2ACCOUNTMANAGERGROUP_TGT_ORDERED = "relation.B2BUnit2AccountManagerGroup.target.ordered";
	/** Relation disable markmodifed parameter constants for B2BUnit2AccountManagerGroup from ((b2bcommerce))*/
	protected static String B2BUNIT2ACCOUNTMANAGERGROUP_MARKMODIFIED = "relation.B2BUnit2AccountManagerGroup.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n USERGROUPS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<B2BUserGroup> USERGROUPSHANDLER = new OneToManyHandler<B2BUserGroup>(
	B2BCommerceConstants.TC.B2BUSERGROUP,
	false,
	"Unit",
	null,
	false,
	true,
	CollectionType.SET
	);
	/**
	* {@link OneToManyHandler} for handling 1:n COSTCENTERS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<B2BCostCenter> COSTCENTERSHANDLER = new OneToManyHandler<B2BCostCenter>(
	B2BCommerceConstants.TC.B2BCOSTCENTER,
	false,
	"Unit",
	null,
	false,
	true,
	CollectionType.LIST
	);
	/**
	* {@link OneToManyHandler} for handling 1:n BUDGETS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<B2BBudget> BUDGETSHANDLER = new OneToManyHandler<B2BBudget>(
	B2BCommerceConstants.TC.B2BBUDGET,
	false,
	"Unit",
	null,
	false,
	true,
	CollectionType.LIST
	);
	/**
	* {@link OneToManyHandler} for handling 1:n ORDERS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<AbstractOrder> ORDERSHANDLER = new OneToManyHandler<AbstractOrder>(
	CoreConstants.TC.ABSTRACTORDER,
	false,
	"Unit",
	null,
	false,
	true,
	CollectionType.SET
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n CREDITLIMIT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BUnit> CREDITLIMITHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BUnit>(
	B2BCommerceConstants.TC.B2BUNIT,
	false,
	"CreditLimit",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n QUOTELIMIT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BUnit> QUOTELIMITHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BUnit>(
	B2BCommerceConstants.TC.B2BUNIT,
	false,
	"QuoteLimit",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n ACCOUNTMANAGER's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BUnit> ACCOUNTMANAGERHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BUnit>(
	B2BCommerceConstants.TC.B2BUNIT,
	false,
	"accountManager",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(OrgUnit.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(REPORTINGORGANIZATION, AttributeMode.INITIAL);
		tmp.put(CREDITLIMIT, AttributeMode.INITIAL);
		tmp.put(QUOTELIMIT, AttributeMode.INITIAL);
		tmp.put(ACCOUNTMANAGER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.accountManager</code> attribute.
	 * @return the accountManager - Employee-Account Manager associated to a Parent B2BUnit
	 */
	public Employee getAccountManager(final SessionContext ctx)
	{
		return (Employee)getProperty( ctx, ACCOUNTMANAGER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.accountManager</code> attribute.
	 * @return the accountManager - Employee-Account Manager associated to a Parent B2BUnit
	 */
	public Employee getAccountManager()
	{
		return getAccountManager( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.accountManager</code> attribute. 
	 * @param value the accountManager - Employee-Account Manager associated to a Parent B2BUnit
	 */
	public void setAccountManager(final SessionContext ctx, final Employee value)
	{
		ACCOUNTMANAGERHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.accountManager</code> attribute. 
	 * @param value the accountManager - Employee-Account Manager associated to a Parent B2BUnit
	 */
	public void setAccountManager(final Employee value)
	{
		setAccountManager( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.AccountManagerGroups</code> attribute.
	 * @return the AccountManagerGroups - User groups associated to a B2BUnit that hold other employees who can approve quotes
	 */
	public Set<UserGroup> getAccountManagerGroups(final SessionContext ctx)
	{
		final List<UserGroup> items = getLinkedItems( 
			ctx,
			true,
			B2BCommerceConstants.Relations.B2BUNIT2ACCOUNTMANAGERGROUP,
			"UserGroup",
			null,
			Utilities.getRelationOrderingOverride(B2BUNIT2ACCOUNTMANAGERGROUP_SRC_ORDERED, true),
			false
		);
		return new LinkedHashSet<UserGroup>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.AccountManagerGroups</code> attribute.
	 * @return the AccountManagerGroups - User groups associated to a B2BUnit that hold other employees who can approve quotes
	 */
	public Set<UserGroup> getAccountManagerGroups()
	{
		return getAccountManagerGroups( getSession().getSessionContext() );
	}
	
	public long getAccountManagerGroupsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			B2BCommerceConstants.Relations.B2BUNIT2ACCOUNTMANAGERGROUP,
			"UserGroup",
			null
		);
	}
	
	public long getAccountManagerGroupsCount()
	{
		return getAccountManagerGroupsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.AccountManagerGroups</code> attribute. 
	 * @param value the AccountManagerGroups - User groups associated to a B2BUnit that hold other employees who can approve quotes
	 */
	public void setAccountManagerGroups(final SessionContext ctx, final Set<UserGroup> value)
	{
		setLinkedItems( 
			ctx,
			true,
			B2BCommerceConstants.Relations.B2BUNIT2ACCOUNTMANAGERGROUP,
			null,
			value,
			Utilities.getRelationOrderingOverride(B2BUNIT2ACCOUNTMANAGERGROUP_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BUNIT2ACCOUNTMANAGERGROUP_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.AccountManagerGroups</code> attribute. 
	 * @param value the AccountManagerGroups - User groups associated to a B2BUnit that hold other employees who can approve quotes
	 */
	public void setAccountManagerGroups(final Set<UserGroup> value)
	{
		setAccountManagerGroups( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to AccountManagerGroups. 
	 * @param value the item to add to AccountManagerGroups - User groups associated to a B2BUnit that hold other employees who can approve quotes
	 */
	public void addToAccountManagerGroups(final SessionContext ctx, final UserGroup value)
	{
		addLinkedItems( 
			ctx,
			true,
			B2BCommerceConstants.Relations.B2BUNIT2ACCOUNTMANAGERGROUP,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BUNIT2ACCOUNTMANAGERGROUP_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BUNIT2ACCOUNTMANAGERGROUP_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to AccountManagerGroups. 
	 * @param value the item to add to AccountManagerGroups - User groups associated to a B2BUnit that hold other employees who can approve quotes
	 */
	public void addToAccountManagerGroups(final UserGroup value)
	{
		addToAccountManagerGroups( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from AccountManagerGroups. 
	 * @param value the item to remove from AccountManagerGroups - User groups associated to a B2BUnit that hold other employees who can approve quotes
	 */
	public void removeFromAccountManagerGroups(final SessionContext ctx, final UserGroup value)
	{
		removeLinkedItems( 
			ctx,
			true,
			B2BCommerceConstants.Relations.B2BUNIT2ACCOUNTMANAGERGROUP,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BUNIT2ACCOUNTMANAGERGROUP_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BUNIT2ACCOUNTMANAGERGROUP_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from AccountManagerGroups. 
	 * @param value the item to remove from AccountManagerGroups - User groups associated to a B2BUnit that hold other employees who can approve quotes
	 */
	public void removeFromAccountManagerGroups(final UserGroup value)
	{
		removeFromAccountManagerGroups( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.Budgets</code> attribute.
	 * @return the Budgets - Budgets associated to a Parent B2BUnit
	 */
	public List<B2BBudget> getBudgets(final SessionContext ctx)
	{
		return (List<B2BBudget>)BUDGETSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.Budgets</code> attribute.
	 * @return the Budgets - Budgets associated to a Parent B2BUnit
	 */
	public List<B2BBudget> getBudgets()
	{
		return getBudgets( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.Budgets</code> attribute. 
	 * @param value the Budgets - Budgets associated to a Parent B2BUnit
	 */
	public void setBudgets(final SessionContext ctx, final List<B2BBudget> value)
	{
		BUDGETSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.Budgets</code> attribute. 
	 * @param value the Budgets - Budgets associated to a Parent B2BUnit
	 */
	public void setBudgets(final List<B2BBudget> value)
	{
		setBudgets( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Budgets. 
	 * @param value the item to add to Budgets - Budgets associated to a Parent B2BUnit
	 */
	public void addToBudgets(final SessionContext ctx, final B2BBudget value)
	{
		BUDGETSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Budgets. 
	 * @param value the item to add to Budgets - Budgets associated to a Parent B2BUnit
	 */
	public void addToBudgets(final B2BBudget value)
	{
		addToBudgets( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Budgets. 
	 * @param value the item to remove from Budgets - Budgets associated to a Parent B2BUnit
	 */
	public void removeFromBudgets(final SessionContext ctx, final B2BBudget value)
	{
		BUDGETSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Budgets. 
	 * @param value the item to remove from Budgets - Budgets associated to a Parent B2BUnit
	 */
	public void removeFromBudgets(final B2BBudget value)
	{
		removeFromBudgets( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.CostCenters</code> attribute.
	 * @return the CostCenters - CostCenters associated to a Parent B2BUnit
	 */
	public List<B2BCostCenter> getCostCenters(final SessionContext ctx)
	{
		return (List<B2BCostCenter>)COSTCENTERSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.CostCenters</code> attribute.
	 * @return the CostCenters - CostCenters associated to a Parent B2BUnit
	 */
	public List<B2BCostCenter> getCostCenters()
	{
		return getCostCenters( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.CostCenters</code> attribute. 
	 * @param value the CostCenters - CostCenters associated to a Parent B2BUnit
	 */
	public void setCostCenters(final SessionContext ctx, final List<B2BCostCenter> value)
	{
		COSTCENTERSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.CostCenters</code> attribute. 
	 * @param value the CostCenters - CostCenters associated to a Parent B2BUnit
	 */
	public void setCostCenters(final List<B2BCostCenter> value)
	{
		setCostCenters( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to CostCenters. 
	 * @param value the item to add to CostCenters - CostCenters associated to a Parent B2BUnit
	 */
	public void addToCostCenters(final SessionContext ctx, final B2BCostCenter value)
	{
		COSTCENTERSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to CostCenters. 
	 * @param value the item to add to CostCenters - CostCenters associated to a Parent B2BUnit
	 */
	public void addToCostCenters(final B2BCostCenter value)
	{
		addToCostCenters( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from CostCenters. 
	 * @param value the item to remove from CostCenters - CostCenters associated to a Parent B2BUnit
	 */
	public void removeFromCostCenters(final SessionContext ctx, final B2BCostCenter value)
	{
		COSTCENTERSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from CostCenters. 
	 * @param value the item to remove from CostCenters - CostCenters associated to a Parent B2BUnit
	 */
	public void removeFromCostCenters(final B2BCostCenter value)
	{
		removeFromCostCenters( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		CREDITLIMITHANDLER.newInstance(ctx, allAttributes);
		QUOTELIMITHANDLER.newInstance(ctx, allAttributes);
		ACCOUNTMANAGERHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.CreditLimit</code> attribute.
	 * @return the CreditLimit - Merchant Check associated to a Parent B2BUnit
	 */
	public B2BCreditLimit getCreditLimit(final SessionContext ctx)
	{
		return (B2BCreditLimit)getProperty( ctx, CREDITLIMIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.CreditLimit</code> attribute.
	 * @return the CreditLimit - Merchant Check associated to a Parent B2BUnit
	 */
	public B2BCreditLimit getCreditLimit()
	{
		return getCreditLimit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.CreditLimit</code> attribute. 
	 * @param value the CreditLimit - Merchant Check associated to a Parent B2BUnit
	 */
	public void setCreditLimit(final SessionContext ctx, final B2BCreditLimit value)
	{
		CREDITLIMITHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.CreditLimit</code> attribute. 
	 * @param value the CreditLimit - Merchant Check associated to a Parent B2BUnit
	 */
	public void setCreditLimit(final B2BCreditLimit value)
	{
		setCreditLimit( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("UserGroup");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(B2BUNIT2ACCOUNTMANAGERGROUP_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.Orders</code> attribute.
	 * @return the Orders
	 */
	public Set<AbstractOrder> getOrders(final SessionContext ctx)
	{
		return (Set<AbstractOrder>)ORDERSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.Orders</code> attribute.
	 * @return the Orders
	 */
	public Set<AbstractOrder> getOrders()
	{
		return getOrders( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.Orders</code> attribute. 
	 * @param value the Orders
	 */
	public void setOrders(final SessionContext ctx, final Set<AbstractOrder> value)
	{
		ORDERSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.Orders</code> attribute. 
	 * @param value the Orders
	 */
	public void setOrders(final Set<AbstractOrder> value)
	{
		setOrders( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Orders. 
	 * @param value the item to add to Orders
	 */
	public void addToOrders(final SessionContext ctx, final AbstractOrder value)
	{
		ORDERSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Orders. 
	 * @param value the item to add to Orders
	 */
	public void addToOrders(final AbstractOrder value)
	{
		addToOrders( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Orders. 
	 * @param value the item to remove from Orders
	 */
	public void removeFromOrders(final SessionContext ctx, final AbstractOrder value)
	{
		ORDERSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Orders. 
	 * @param value the item to remove from Orders
	 */
	public void removeFromOrders(final AbstractOrder value)
	{
		removeFromOrders( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.QuoteLimit</code> attribute.
	 * @return the QuoteLimit - Minimum Amount on Cart for qualifying for Quote Request
	 */
	public B2BQuoteLimit getQuoteLimit(final SessionContext ctx)
	{
		return (B2BQuoteLimit)getProperty( ctx, QUOTELIMIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.QuoteLimit</code> attribute.
	 * @return the QuoteLimit - Minimum Amount on Cart for qualifying for Quote Request
	 */
	public B2BQuoteLimit getQuoteLimit()
	{
		return getQuoteLimit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.QuoteLimit</code> attribute. 
	 * @param value the QuoteLimit - Minimum Amount on Cart for qualifying for Quote Request
	 */
	public void setQuoteLimit(final SessionContext ctx, final B2BQuoteLimit value)
	{
		QUOTELIMITHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.QuoteLimit</code> attribute. 
	 * @param value the QuoteLimit - Minimum Amount on Cart for qualifying for Quote Request
	 */
	public void setQuoteLimit(final B2BQuoteLimit value)
	{
		setQuoteLimit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.reportingOrganization</code> attribute.
	 * @return the reportingOrganization
	 */
	public B2BUnit getReportingOrganization(final SessionContext ctx)
	{
		return (B2BUnit)getProperty( ctx, REPORTINGORGANIZATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.reportingOrganization</code> attribute.
	 * @return the reportingOrganization
	 */
	public B2BUnit getReportingOrganization()
	{
		return getReportingOrganization( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.reportingOrganization</code> attribute. 
	 * @param value the reportingOrganization
	 */
	public void setReportingOrganization(final SessionContext ctx, final B2BUnit value)
	{
		setProperty(ctx, REPORTINGORGANIZATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.reportingOrganization</code> attribute. 
	 * @param value the reportingOrganization
	 */
	public void setReportingOrganization(final B2BUnit value)
	{
		setReportingOrganization( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.UserGroups</code> attribute.
	 * @return the UserGroups
	 */
	public Set<B2BUserGroup> getUserGroups(final SessionContext ctx)
	{
		return (Set<B2BUserGroup>)USERGROUPSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.UserGroups</code> attribute.
	 * @return the UserGroups
	 */
	public Set<B2BUserGroup> getUserGroups()
	{
		return getUserGroups( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.UserGroups</code> attribute. 
	 * @param value the UserGroups
	 */
	public void setUserGroups(final SessionContext ctx, final Set<B2BUserGroup> value)
	{
		USERGROUPSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.UserGroups</code> attribute. 
	 * @param value the UserGroups
	 */
	public void setUserGroups(final Set<B2BUserGroup> value)
	{
		setUserGroups( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to UserGroups. 
	 * @param value the item to add to UserGroups
	 */
	public void addToUserGroups(final SessionContext ctx, final B2BUserGroup value)
	{
		USERGROUPSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to UserGroups. 
	 * @param value the item to add to UserGroups
	 */
	public void addToUserGroups(final B2BUserGroup value)
	{
		addToUserGroups( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from UserGroups. 
	 * @param value the item to remove from UserGroups
	 */
	public void removeFromUserGroups(final SessionContext ctx, final B2BUserGroup value)
	{
		USERGROUPSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from UserGroups. 
	 * @param value the item to remove from UserGroups
	 */
	public void removeFromUserGroups(final B2BUserGroup value)
	{
		removeFromUserGroups( getSession().getSessionContext(), value );
	}
	
}
