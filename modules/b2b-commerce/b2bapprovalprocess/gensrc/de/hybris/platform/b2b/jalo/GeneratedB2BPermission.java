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
import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.b2b.jalo.B2BUserGroup;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BPermission B2BPermission}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BPermission extends GenericItem
{
	/** Qualifier of the <code>B2BPermission.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>B2BPermission.active</code> attribute **/
	public static final String ACTIVE = "active";
	/** Qualifier of the <code>B2BPermission.message</code> attribute **/
	public static final String MESSAGE = "message";
	/** Qualifier of the <code>B2BPermission.UserGroups</code> attribute **/
	public static final String USERGROUPS = "UserGroups";
	/** Relation ordering override parameter constants for B2BUserGroups2B2BPermissions from ((b2bapprovalprocess))*/
	protected static String B2BUSERGROUPS2B2BPERMISSIONS_SRC_ORDERED = "relation.B2BUserGroups2B2BPermissions.source.ordered";
	protected static String B2BUSERGROUPS2B2BPERMISSIONS_TGT_ORDERED = "relation.B2BUserGroups2B2BPermissions.target.ordered";
	/** Relation disable markmodifed parameter constants for B2BUserGroups2B2BPermissions from ((b2bapprovalprocess))*/
	protected static String B2BUSERGROUPS2B2BPERMISSIONS_MARKMODIFIED = "relation.B2BUserGroups2B2BPermissions.markmodified";
	/** Qualifier of the <code>B2BPermission.UnitPOS</code> attribute **/
	public static final String UNITPOS = "UnitPOS";
	/** Qualifier of the <code>B2BPermission.Unit</code> attribute **/
	public static final String UNIT = "Unit";
	/** Qualifier of the <code>B2BPermission.Customers</code> attribute **/
	public static final String CUSTOMERS = "Customers";
	/** Relation ordering override parameter constants for B2BCustomer2B2BPermissions from ((b2bapprovalprocess))*/
	protected static String B2BCUSTOMER2B2BPERMISSIONS_SRC_ORDERED = "relation.B2BCustomer2B2BPermissions.source.ordered";
	protected static String B2BCUSTOMER2B2BPERMISSIONS_TGT_ORDERED = "relation.B2BCustomer2B2BPermissions.target.ordered";
	/** Relation disable markmodifed parameter constants for B2BCustomer2B2BPermissions from ((b2bapprovalprocess))*/
	protected static String B2BCUSTOMER2B2BPERMISSIONS_MARKMODIFIED = "relation.B2BCustomer2B2BPermissions.markmodified";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n UNIT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BPermission> UNITHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BPermission>(
	B2BApprovalprocessConstants.TC.B2BPERMISSION,
	false,
	"Unit",
	"UnitPOS",
	true,
	true,
	CollectionType.SET
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(ACTIVE, AttributeMode.INITIAL);
		tmp.put(MESSAGE, AttributeMode.INITIAL);
		tmp.put(UNITPOS, AttributeMode.INITIAL);
		tmp.put(UNIT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.active</code> attribute.
	 * @return the active
	 */
	public Boolean isActive(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.active</code> attribute.
	 * @return the active
	 */
	public Boolean isActive()
	{
		return isActive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.active</code> attribute. 
	 * @return the active
	 */
	public boolean isActiveAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isActive( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.active</code> attribute. 
	 * @return the active
	 */
	public boolean isActiveAsPrimitive()
	{
		return isActiveAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ACTIVE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final Boolean value)
	{
		setActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final SessionContext ctx, final boolean value)
	{
		setActive( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final boolean value)
	{
		setActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		UNITHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.Customers</code> attribute.
	 * @return the Customers
	 */
	public Collection<B2BCustomer> getCustomers(final SessionContext ctx)
	{
		final List<B2BCustomer> items = getLinkedItems( 
			ctx,
			false,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMER2B2BPERMISSIONS,
			"B2BCustomer",
			null,
			Utilities.getRelationOrderingOverride(B2BCUSTOMER2B2BPERMISSIONS_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.Customers</code> attribute.
	 * @return the Customers
	 */
	public Collection<B2BCustomer> getCustomers()
	{
		return getCustomers( getSession().getSessionContext() );
	}
	
	public long getCustomersCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMER2B2BPERMISSIONS,
			"B2BCustomer",
			null
		);
	}
	
	public long getCustomersCount()
	{
		return getCustomersCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.Customers</code> attribute. 
	 * @param value the Customers
	 */
	public void setCustomers(final SessionContext ctx, final Collection<B2BCustomer> value)
	{
		setLinkedItems( 
			ctx,
			false,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMER2B2BPERMISSIONS,
			null,
			value,
			Utilities.getRelationOrderingOverride(B2BCUSTOMER2B2BPERMISSIONS_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMER2B2BPERMISSIONS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.Customers</code> attribute. 
	 * @param value the Customers
	 */
	public void setCustomers(final Collection<B2BCustomer> value)
	{
		setCustomers( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Customers. 
	 * @param value the item to add to Customers
	 */
	public void addToCustomers(final SessionContext ctx, final B2BCustomer value)
	{
		addLinkedItems( 
			ctx,
			false,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMER2B2BPERMISSIONS,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BCUSTOMER2B2BPERMISSIONS_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMER2B2BPERMISSIONS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to Customers. 
	 * @param value the item to add to Customers
	 */
	public void addToCustomers(final B2BCustomer value)
	{
		addToCustomers( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Customers. 
	 * @param value the item to remove from Customers
	 */
	public void removeFromCustomers(final SessionContext ctx, final B2BCustomer value)
	{
		removeLinkedItems( 
			ctx,
			false,
			B2BApprovalprocessConstants.Relations.B2BCUSTOMER2B2BPERMISSIONS,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BCUSTOMER2B2BPERMISSIONS_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(B2BCUSTOMER2B2BPERMISSIONS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from Customers. 
	 * @param value the item to remove from Customers
	 */
	public void removeFromCustomers(final B2BCustomer value)
	{
		removeFromCustomers( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("B2BUserGroup");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(B2BUSERGROUPS2B2BPERMISSIONS_MARKMODIFIED);
		}
		ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("B2BCustomer");
		if(relationSecondEnd1.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(B2BCUSTOMER2B2BPERMISSIONS_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.message</code> attribute.
	 * @return the message
	 */
	public String getMessage(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedB2BPermission.getMessage requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, MESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.message</code> attribute.
	 * @return the message
	 */
	public String getMessage()
	{
		return getMessage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.message</code> attribute. 
	 * @return the localized message
	 */
	public Map<Language,String> getAllMessage(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,MESSAGE,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.message</code> attribute. 
	 * @return the localized message
	 */
	public Map<Language,String> getAllMessage()
	{
		return getAllMessage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.message</code> attribute. 
	 * @param value the message
	 */
	public void setMessage(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedB2BPermission.setMessage requires a session language", 0 );
		}
		setLocalizedProperty(ctx, MESSAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.message</code> attribute. 
	 * @param value the message
	 */
	public void setMessage(final String value)
	{
		setMessage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.message</code> attribute. 
	 * @param value the message
	 */
	public void setAllMessage(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,MESSAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.message</code> attribute. 
	 * @param value the message
	 */
	public void setAllMessage(final Map<Language,String> value)
	{
		setAllMessage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.Unit</code> attribute.
	 * @return the Unit - The Parent B2BUnit
	 */
	public B2BUnit getUnit(final SessionContext ctx)
	{
		return (B2BUnit)getProperty( ctx, UNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.Unit</code> attribute.
	 * @return the Unit - The Parent B2BUnit
	 */
	public B2BUnit getUnit()
	{
		return getUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.Unit</code> attribute. 
	 * @param value the Unit - The Parent B2BUnit
	 */
	public void setUnit(final SessionContext ctx, final B2BUnit value)
	{
		UNITHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.Unit</code> attribute. 
	 * @param value the Unit - The Parent B2BUnit
	 */
	public void setUnit(final B2BUnit value)
	{
		setUnit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.UnitPOS</code> attribute.
	 * @return the UnitPOS
	 */
	 Integer getUnitPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, UNITPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.UnitPOS</code> attribute.
	 * @return the UnitPOS
	 */
	 Integer getUnitPOS()
	{
		return getUnitPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.UnitPOS</code> attribute. 
	 * @return the UnitPOS
	 */
	 int getUnitPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getUnitPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.UnitPOS</code> attribute. 
	 * @return the UnitPOS
	 */
	 int getUnitPOSAsPrimitive()
	{
		return getUnitPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.UnitPOS</code> attribute. 
	 * @param value the UnitPOS
	 */
	 void setUnitPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, UNITPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.UnitPOS</code> attribute. 
	 * @param value the UnitPOS
	 */
	 void setUnitPOS(final Integer value)
	{
		setUnitPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.UnitPOS</code> attribute. 
	 * @param value the UnitPOS
	 */
	 void setUnitPOS(final SessionContext ctx, final int value)
	{
		setUnitPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.UnitPOS</code> attribute. 
	 * @param value the UnitPOS
	 */
	 void setUnitPOS(final int value)
	{
		setUnitPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.UserGroups</code> attribute.
	 * @return the UserGroups
	 */
	public List<B2BUserGroup> getUserGroups(final SessionContext ctx)
	{
		final List<B2BUserGroup> items = getLinkedItems( 
			ctx,
			false,
			B2BApprovalprocessConstants.Relations.B2BUSERGROUPS2B2BPERMISSIONS,
			"B2BUserGroup",
			null,
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermission.UserGroups</code> attribute.
	 * @return the UserGroups
	 */
	public List<B2BUserGroup> getUserGroups()
	{
		return getUserGroups( getSession().getSessionContext() );
	}
	
	public long getUserGroupsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			B2BApprovalprocessConstants.Relations.B2BUSERGROUPS2B2BPERMISSIONS,
			"B2BUserGroup",
			null
		);
	}
	
	public long getUserGroupsCount()
	{
		return getUserGroupsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.UserGroups</code> attribute. 
	 * @param value the UserGroups
	 */
	public void setUserGroups(final SessionContext ctx, final List<B2BUserGroup> value)
	{
		setLinkedItems( 
			ctx,
			false,
			B2BApprovalprocessConstants.Relations.B2BUSERGROUPS2B2BPERMISSIONS,
			null,
			value,
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(B2BUSERGROUPS2B2BPERMISSIONS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermission.UserGroups</code> attribute. 
	 * @param value the UserGroups
	 */
	public void setUserGroups(final List<B2BUserGroup> value)
	{
		setUserGroups( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to UserGroups. 
	 * @param value the item to add to UserGroups
	 */
	public void addToUserGroups(final SessionContext ctx, final B2BUserGroup value)
	{
		addLinkedItems( 
			ctx,
			false,
			B2BApprovalprocessConstants.Relations.B2BUSERGROUPS2B2BPERMISSIONS,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(B2BUSERGROUPS2B2BPERMISSIONS_MARKMODIFIED)
		);
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
		removeLinkedItems( 
			ctx,
			false,
			B2BApprovalprocessConstants.Relations.B2BUSERGROUPS2B2BPERMISSIONS,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(B2BUSERGROUPS2B2BPERMISSIONS_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(B2BUSERGROUPS2B2BPERMISSIONS_MARKMODIFIED)
		);
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
