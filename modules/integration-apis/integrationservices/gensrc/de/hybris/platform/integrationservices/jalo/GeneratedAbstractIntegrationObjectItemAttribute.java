/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.jalo;

import de.hybris.platform.integrationservices.constants.IntegrationservicesConstants;
import de.hybris.platform.integrationservices.jalo.IntegrationObjectItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem AbstractIntegrationObjectItemAttribute}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedAbstractIntegrationObjectItemAttribute extends GenericItem
{
	/** Qualifier of the <code>AbstractIntegrationObjectItemAttribute.attributeName</code> attribute **/
	public static final String ATTRIBUTENAME = "attributeName";
	/** Qualifier of the <code>AbstractIntegrationObjectItemAttribute.autoCreate</code> attribute **/
	public static final String AUTOCREATE = "autoCreate";
	/** Qualifier of the <code>AbstractIntegrationObjectItemAttribute.returnIntegrationObjectItem</code> attribute **/
	public static final String RETURNINTEGRATIONOBJECTITEM = "returnIntegrationObjectItem";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ATTRIBUTENAME, AttributeMode.INITIAL);
		tmp.put(AUTOCREATE, AttributeMode.INITIAL);
		tmp.put(RETURNINTEGRATIONOBJECTITEM, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractIntegrationObjectItemAttribute.attributeName</code> attribute.
	 * @return the attributeName
	 */
	public String getAttributeName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ATTRIBUTENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractIntegrationObjectItemAttribute.attributeName</code> attribute.
	 * @return the attributeName
	 */
	public String getAttributeName()
	{
		return getAttributeName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractIntegrationObjectItemAttribute.attributeName</code> attribute. 
	 * @param value the attributeName
	 */
	public void setAttributeName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ATTRIBUTENAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractIntegrationObjectItemAttribute.attributeName</code> attribute. 
	 * @param value the attributeName
	 */
	public void setAttributeName(final String value)
	{
		setAttributeName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractIntegrationObjectItemAttribute.autoCreate</code> attribute.
	 * @return the autoCreate
	 */
	public Boolean isAutoCreate(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, AUTOCREATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractIntegrationObjectItemAttribute.autoCreate</code> attribute.
	 * @return the autoCreate
	 */
	public Boolean isAutoCreate()
	{
		return isAutoCreate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractIntegrationObjectItemAttribute.autoCreate</code> attribute. 
	 * @return the autoCreate
	 */
	public boolean isAutoCreateAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isAutoCreate( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractIntegrationObjectItemAttribute.autoCreate</code> attribute. 
	 * @return the autoCreate
	 */
	public boolean isAutoCreateAsPrimitive()
	{
		return isAutoCreateAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractIntegrationObjectItemAttribute.autoCreate</code> attribute. 
	 * @param value the autoCreate
	 */
	public void setAutoCreate(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, AUTOCREATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractIntegrationObjectItemAttribute.autoCreate</code> attribute. 
	 * @param value the autoCreate
	 */
	public void setAutoCreate(final Boolean value)
	{
		setAutoCreate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractIntegrationObjectItemAttribute.autoCreate</code> attribute. 
	 * @param value the autoCreate
	 */
	public void setAutoCreate(final SessionContext ctx, final boolean value)
	{
		setAutoCreate( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractIntegrationObjectItemAttribute.autoCreate</code> attribute. 
	 * @param value the autoCreate
	 */
	public void setAutoCreate(final boolean value)
	{
		setAutoCreate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractIntegrationObjectItemAttribute.returnIntegrationObjectItem</code> attribute.
	 * @return the returnIntegrationObjectItem
	 */
	public IntegrationObjectItem getReturnIntegrationObjectItem(final SessionContext ctx)
	{
		return (IntegrationObjectItem)getProperty( ctx, RETURNINTEGRATIONOBJECTITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractIntegrationObjectItemAttribute.returnIntegrationObjectItem</code> attribute.
	 * @return the returnIntegrationObjectItem
	 */
	public IntegrationObjectItem getReturnIntegrationObjectItem()
	{
		return getReturnIntegrationObjectItem( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractIntegrationObjectItemAttribute.returnIntegrationObjectItem</code> attribute. 
	 * @param value the returnIntegrationObjectItem
	 */
	public void setReturnIntegrationObjectItem(final SessionContext ctx, final IntegrationObjectItem value)
	{
		setProperty(ctx, RETURNINTEGRATIONOBJECTITEM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractIntegrationObjectItemAttribute.returnIntegrationObjectItem</code> attribute. 
	 * @param value the returnIntegrationObjectItem
	 */
	public void setReturnIntegrationObjectItem(final IntegrationObjectItem value)
	{
		setReturnIntegrationObjectItem( getSession().getSessionContext(), value );
	}
	
}
