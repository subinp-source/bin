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
import de.hybris.platform.b2b.jalo.B2BPermission;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BPermissionResult B2BPermissionResult}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BPermissionResult extends GenericItem
{
	/** Qualifier of the <code>B2BPermissionResult.permission</code> attribute **/
	public static final String PERMISSION = "permission";
	/** Qualifier of the <code>B2BPermissionResult.permissionTypeCode</code> attribute **/
	public static final String PERMISSIONTYPECODE = "permissionTypeCode";
	/** Qualifier of the <code>B2BPermissionResult.status</code> attribute **/
	public static final String STATUS = "status";
	/** Qualifier of the <code>B2BPermissionResult.approver</code> attribute **/
	public static final String APPROVER = "approver";
	/** Qualifier of the <code>B2BPermissionResult.note</code> attribute **/
	public static final String NOTE = "note";
	/** Qualifier of the <code>B2BPermissionResult.OrderPOS</code> attribute **/
	public static final String ORDERPOS = "OrderPOS";
	/** Qualifier of the <code>B2BPermissionResult.Order</code> attribute **/
	public static final String ORDER = "Order";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n ORDER's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BPermissionResult> ORDERHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BPermissionResult>(
	B2BApprovalprocessConstants.TC.B2BPERMISSIONRESULT,
	false,
	"Order",
	"OrderPOS",
	true,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(PERMISSION, AttributeMode.INITIAL);
		tmp.put(PERMISSIONTYPECODE, AttributeMode.INITIAL);
		tmp.put(STATUS, AttributeMode.INITIAL);
		tmp.put(APPROVER, AttributeMode.INITIAL);
		tmp.put(NOTE, AttributeMode.INITIAL);
		tmp.put(ORDERPOS, AttributeMode.INITIAL);
		tmp.put(ORDER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.approver</code> attribute.
	 * @return the approver
	 */
	public B2BCustomer getApprover(final SessionContext ctx)
	{
		return (B2BCustomer)getProperty( ctx, APPROVER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.approver</code> attribute.
	 * @return the approver
	 */
	public B2BCustomer getApprover()
	{
		return getApprover( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.approver</code> attribute. 
	 * @param value the approver
	 */
	public void setApprover(final SessionContext ctx, final B2BCustomer value)
	{
		setProperty(ctx, APPROVER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.approver</code> attribute. 
	 * @param value the approver
	 */
	public void setApprover(final B2BCustomer value)
	{
		setApprover( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		ORDERHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.note</code> attribute.
	 * @return the note
	 */
	public String getNote(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedB2BPermissionResult.getNote requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NOTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.note</code> attribute.
	 * @return the note
	 */
	public String getNote()
	{
		return getNote( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.note</code> attribute. 
	 * @return the localized note
	 */
	public Map<Language,String> getAllNote(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NOTE,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.note</code> attribute. 
	 * @return the localized note
	 */
	public Map<Language,String> getAllNote()
	{
		return getAllNote( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.note</code> attribute. 
	 * @param value the note
	 */
	public void setNote(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedB2BPermissionResult.setNote requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NOTE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.note</code> attribute. 
	 * @param value the note
	 */
	public void setNote(final String value)
	{
		setNote( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.note</code> attribute. 
	 * @param value the note
	 */
	public void setAllNote(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NOTE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.note</code> attribute. 
	 * @param value the note
	 */
	public void setAllNote(final Map<Language,String> value)
	{
		setAllNote( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.Order</code> attribute.
	 * @return the Order
	 */
	public AbstractOrder getOrder(final SessionContext ctx)
	{
		return (AbstractOrder)getProperty( ctx, ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.Order</code> attribute.
	 * @return the Order
	 */
	public AbstractOrder getOrder()
	{
		return getOrder( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.Order</code> attribute. 
	 * @param value the Order
	 */
	public void setOrder(final SessionContext ctx, final AbstractOrder value)
	{
		ORDERHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.Order</code> attribute. 
	 * @param value the Order
	 */
	public void setOrder(final AbstractOrder value)
	{
		setOrder( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.OrderPOS</code> attribute.
	 * @return the OrderPOS
	 */
	 Integer getOrderPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, ORDERPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.OrderPOS</code> attribute.
	 * @return the OrderPOS
	 */
	 Integer getOrderPOS()
	{
		return getOrderPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.OrderPOS</code> attribute. 
	 * @return the OrderPOS
	 */
	 int getOrderPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getOrderPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.OrderPOS</code> attribute. 
	 * @return the OrderPOS
	 */
	 int getOrderPOSAsPrimitive()
	{
		return getOrderPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.OrderPOS</code> attribute. 
	 * @param value the OrderPOS
	 */
	 void setOrderPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, ORDERPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.OrderPOS</code> attribute. 
	 * @param value the OrderPOS
	 */
	 void setOrderPOS(final Integer value)
	{
		setOrderPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.OrderPOS</code> attribute. 
	 * @param value the OrderPOS
	 */
	 void setOrderPOS(final SessionContext ctx, final int value)
	{
		setOrderPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.OrderPOS</code> attribute. 
	 * @param value the OrderPOS
	 */
	 void setOrderPOS(final int value)
	{
		setOrderPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.permission</code> attribute.
	 * @return the permission
	 */
	public B2BPermission getPermission(final SessionContext ctx)
	{
		return (B2BPermission)getProperty( ctx, PERMISSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.permission</code> attribute.
	 * @return the permission
	 */
	public B2BPermission getPermission()
	{
		return getPermission( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.permission</code> attribute. 
	 * @param value the permission
	 */
	public void setPermission(final SessionContext ctx, final B2BPermission value)
	{
		setProperty(ctx, PERMISSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.permission</code> attribute. 
	 * @param value the permission
	 */
	public void setPermission(final B2BPermission value)
	{
		setPermission( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.permissionTypeCode</code> attribute.
	 * @return the permissionTypeCode - the item type code for the permission the result is
	 * 						holding
	 * 						as defined in items.xml
	 */
	public String getPermissionTypeCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PERMISSIONTYPECODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.permissionTypeCode</code> attribute.
	 * @return the permissionTypeCode - the item type code for the permission the result is
	 * 						holding
	 * 						as defined in items.xml
	 */
	public String getPermissionTypeCode()
	{
		return getPermissionTypeCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.permissionTypeCode</code> attribute. 
	 * @param value the permissionTypeCode - the item type code for the permission the result is
	 * 						holding
	 * 						as defined in items.xml
	 */
	public void setPermissionTypeCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PERMISSIONTYPECODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.permissionTypeCode</code> attribute. 
	 * @param value the permissionTypeCode - the item type code for the permission the result is
	 * 						holding
	 * 						as defined in items.xml
	 */
	public void setPermissionTypeCode(final String value)
	{
		setPermissionTypeCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BPermissionResult.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus()
	{
		return getStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, STATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BPermissionResult.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final EnumerationValue value)
	{
		setStatus( getSession().getSessionContext(), value );
	}
	
}
