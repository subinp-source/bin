/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.configurablebundleservices.constants.ConfigurableBundleServicesConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.configurablebundleservices.jalo.BundleSelectionCriteria BundleSelectionCriteria}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedBundleSelectionCriteria extends GenericItem
{
	/** Qualifier of the <code>BundleSelectionCriteria.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>BundleSelectionCriteria.catalogVersion</code> attribute **/
	public static final String CATALOGVERSION = "catalogVersion";
	/** Qualifier of the <code>BundleSelectionCriteria.starter</code> attribute **/
	public static final String STARTER = "starter";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(CATALOGVERSION, AttributeMode.INITIAL);
		tmp.put(STARTER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleSelectionCriteria.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version
	 */
	public CatalogVersion getCatalogVersion(final SessionContext ctx)
	{
		return (CatalogVersion)getProperty( ctx, CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleSelectionCriteria.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version
	 */
	public CatalogVersion getCatalogVersion()
	{
		return getCatalogVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleSelectionCriteria.catalogVersion</code> attribute. 
	 * @param value the catalogVersion - Catalog Version
	 */
	protected void setCatalogVersion(final SessionContext ctx, final CatalogVersion value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+CATALOGVERSION+"' is not changeable", 0 );
		}
		setProperty(ctx, CATALOGVERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleSelectionCriteria.catalogVersion</code> attribute. 
	 * @param value the catalogVersion - Catalog Version
	 */
	protected void setCatalogVersion(final CatalogVersion value)
	{
		setCatalogVersion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleSelectionCriteria.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleSelectionCriteria.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleSelectionCriteria.id</code> attribute. 
	 * @param value the id - Identifier
	 */
	protected void setId(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+ID+"' is not changeable", 0 );
		}
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleSelectionCriteria.id</code> attribute. 
	 * @param value the id - Identifier
	 */
	protected void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleSelectionCriteria.starter</code> attribute.
	 * @return the starter - Determines starter component for the product.
	 */
	public Boolean isStarter(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, STARTER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleSelectionCriteria.starter</code> attribute.
	 * @return the starter - Determines starter component for the product.
	 */
	public Boolean isStarter()
	{
		return isStarter( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleSelectionCriteria.starter</code> attribute. 
	 * @return the starter - Determines starter component for the product.
	 */
	public boolean isStarterAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isStarter( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleSelectionCriteria.starter</code> attribute. 
	 * @return the starter - Determines starter component for the product.
	 */
	public boolean isStarterAsPrimitive()
	{
		return isStarterAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleSelectionCriteria.starter</code> attribute. 
	 * @param value the starter - Determines starter component for the product.
	 */
	public void setStarter(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, STARTER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleSelectionCriteria.starter</code> attribute. 
	 * @param value the starter - Determines starter component for the product.
	 */
	public void setStarter(final Boolean value)
	{
		setStarter( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleSelectionCriteria.starter</code> attribute. 
	 * @param value the starter - Determines starter component for the product.
	 */
	public void setStarter(final SessionContext ctx, final boolean value)
	{
		setStarter( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleSelectionCriteria.starter</code> attribute. 
	 * @param value the starter - Determines starter component for the product.
	 */
	public void setStarter(final boolean value)
	{
		setStarter( getSession().getSessionContext(), value );
	}
	
}
