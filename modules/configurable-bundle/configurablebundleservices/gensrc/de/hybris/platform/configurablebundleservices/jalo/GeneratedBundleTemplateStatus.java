/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.configurablebundleservices.constants.ConfigurableBundleServicesConstants;
import de.hybris.platform.configurablebundleservices.jalo.BundleTemplate;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.configurablebundleservices.jalo.BundleTemplateStatus BundleTemplateStatus}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedBundleTemplateStatus extends GenericItem
{
	/** Qualifier of the <code>BundleTemplateStatus.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>BundleTemplateStatus.catalogVersion</code> attribute **/
	public static final String CATALOGVERSION = "catalogVersion";
	/** Qualifier of the <code>BundleTemplateStatus.status</code> attribute **/
	public static final String STATUS = "status";
	/** Qualifier of the <code>BundleTemplateStatus.bundleTemplates</code> attribute **/
	public static final String BUNDLETEMPLATES = "bundleTemplates";
	/**
	* {@link OneToManyHandler} for handling 1:n BUNDLETEMPLATES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<BundleTemplate> BUNDLETEMPLATESHANDLER = new OneToManyHandler<BundleTemplate>(
	ConfigurableBundleServicesConstants.TC.BUNDLETEMPLATE,
	false,
	"status",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(CATALOGVERSION, AttributeMode.INITIAL);
		tmp.put(STATUS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplateStatus.bundleTemplates</code> attribute.
	 * @return the bundleTemplates
	 */
	public Collection<BundleTemplate> getBundleTemplates(final SessionContext ctx)
	{
		return BUNDLETEMPLATESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplateStatus.bundleTemplates</code> attribute.
	 * @return the bundleTemplates
	 */
	public Collection<BundleTemplate> getBundleTemplates()
	{
		return getBundleTemplates( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplateStatus.bundleTemplates</code> attribute. 
	 * @param value the bundleTemplates
	 */
	public void setBundleTemplates(final SessionContext ctx, final Collection<BundleTemplate> value)
	{
		BUNDLETEMPLATESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplateStatus.bundleTemplates</code> attribute. 
	 * @param value the bundleTemplates
	 */
	public void setBundleTemplates(final Collection<BundleTemplate> value)
	{
		setBundleTemplates( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to bundleTemplates. 
	 * @param value the item to add to bundleTemplates
	 */
	public void addToBundleTemplates(final SessionContext ctx, final BundleTemplate value)
	{
		BUNDLETEMPLATESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to bundleTemplates. 
	 * @param value the item to add to bundleTemplates
	 */
	public void addToBundleTemplates(final BundleTemplate value)
	{
		addToBundleTemplates( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from bundleTemplates. 
	 * @param value the item to remove from bundleTemplates
	 */
	public void removeFromBundleTemplates(final SessionContext ctx, final BundleTemplate value)
	{
		BUNDLETEMPLATESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from bundleTemplates. 
	 * @param value the item to remove from bundleTemplates
	 */
	public void removeFromBundleTemplates(final BundleTemplate value)
	{
		removeFromBundleTemplates( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplateStatus.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version
	 */
	public CatalogVersion getCatalogVersion(final SessionContext ctx)
	{
		return (CatalogVersion)getProperty( ctx, CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplateStatus.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version
	 */
	public CatalogVersion getCatalogVersion()
	{
		return getCatalogVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplateStatus.catalogVersion</code> attribute. 
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
	 * <i>Generated method</i> - Setter of the <code>BundleTemplateStatus.catalogVersion</code> attribute. 
	 * @param value the catalogVersion - Catalog Version
	 */
	protected void setCatalogVersion(final CatalogVersion value)
	{
		setCatalogVersion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplateStatus.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplateStatus.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplateStatus.id</code> attribute. 
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
	 * <i>Generated method</i> - Setter of the <code>BundleTemplateStatus.id</code> attribute. 
	 * @param value the id - Identifier
	 */
	protected void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplateStatus.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplateStatus.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus()
	{
		return getStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplateStatus.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, STATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplateStatus.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final EnumerationValue value)
	{
		setStatus( getSession().getSessionContext(), value );
	}
	
}
