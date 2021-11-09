/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.jalo;

import com.hybris.merchandising.constants.MerchandisingservicesConstants;
import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.hybris.merchandising.jalo.MerchSynchronizationConfig MerchSynchronizationConfig}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedMerchSynchronizationConfig extends GenericItem
{
	/** Qualifier of the <code>MerchSynchronizationConfig.baseSite</code> attribute **/
	public static final String BASESITE = "baseSite";
	/** Qualifier of the <code>MerchSynchronizationConfig.catalog</code> attribute **/
	public static final String CATALOG = "catalog";
	/** Qualifier of the <code>MerchSynchronizationConfig.catalogVersion</code> attribute **/
	public static final String CATALOGVERSION = "catalogVersion";
	/** Qualifier of the <code>MerchSynchronizationConfig.enabled</code> attribute **/
	public static final String ENABLED = "enabled";
	/** Qualifier of the <code>MerchSynchronizationConfig.baseCategoryUrl</code> attribute **/
	public static final String BASECATEGORYURL = "baseCategoryUrl";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(BASESITE, AttributeMode.INITIAL);
		tmp.put(CATALOG, AttributeMode.INITIAL);
		tmp.put(CATALOGVERSION, AttributeMode.INITIAL);
		tmp.put(ENABLED, AttributeMode.INITIAL);
		tmp.put(BASECATEGORYURL, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.baseCategoryUrl</code> attribute.
	 * @return the baseCategoryUrl
	 */
	public String getBaseCategoryUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BASECATEGORYURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.baseCategoryUrl</code> attribute.
	 * @return the baseCategoryUrl
	 */
	public String getBaseCategoryUrl()
	{
		return getBaseCategoryUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchSynchronizationConfig.baseCategoryUrl</code> attribute. 
	 * @param value the baseCategoryUrl
	 */
	public void setBaseCategoryUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BASECATEGORYURL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchSynchronizationConfig.baseCategoryUrl</code> attribute. 
	 * @param value the baseCategoryUrl
	 */
	public void setBaseCategoryUrl(final String value)
	{
		setBaseCategoryUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.baseSite</code> attribute.
	 * @return the baseSite - Base site to synchronize
	 */
	public BaseSite getBaseSite(final SessionContext ctx)
	{
		return (BaseSite)getProperty( ctx, BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.baseSite</code> attribute.
	 * @return the baseSite - Base site to synchronize
	 */
	public BaseSite getBaseSite()
	{
		return getBaseSite( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchSynchronizationConfig.baseSite</code> attribute. 
	 * @param value the baseSite - Base site to synchronize
	 */
	public void setBaseSite(final SessionContext ctx, final BaseSite value)
	{
		setProperty(ctx, BASESITE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchSynchronizationConfig.baseSite</code> attribute. 
	 * @param value the baseSite - Base site to synchronize
	 */
	public void setBaseSite(final BaseSite value)
	{
		setBaseSite( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.catalog</code> attribute.
	 * @return the catalog - Catalog to synchronize
	 */
	public Catalog getCatalog(final SessionContext ctx)
	{
		return (Catalog)getProperty( ctx, CATALOG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.catalog</code> attribute.
	 * @return the catalog - Catalog to synchronize
	 */
	public Catalog getCatalog()
	{
		return getCatalog( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchSynchronizationConfig.catalog</code> attribute. 
	 * @param value the catalog - Catalog to synchronize
	 */
	public void setCatalog(final SessionContext ctx, final Catalog value)
	{
		setProperty(ctx, CATALOG,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchSynchronizationConfig.catalog</code> attribute. 
	 * @param value the catalog - Catalog to synchronize
	 */
	public void setCatalog(final Catalog value)
	{
		setCatalog( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version to synchronize
	 */
	public CatalogVersion getCatalogVersion(final SessionContext ctx)
	{
		return (CatalogVersion)getProperty( ctx, CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version to synchronize
	 */
	public CatalogVersion getCatalogVersion()
	{
		return getCatalogVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchSynchronizationConfig.catalogVersion</code> attribute. 
	 * @param value the catalogVersion - Catalog Version to synchronize
	 */
	public void setCatalogVersion(final SessionContext ctx, final CatalogVersion value)
	{
		setProperty(ctx, CATALOGVERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchSynchronizationConfig.catalogVersion</code> attribute. 
	 * @param value the catalogVersion - Catalog Version to synchronize
	 */
	public void setCatalogVersion(final CatalogVersion value)
	{
		setCatalogVersion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.enabled</code> attribute.
	 * @return the enabled
	 */
	public Boolean isEnabled(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.enabled</code> attribute.
	 * @return the enabled
	 */
	public Boolean isEnabled()
	{
		return isEnabled( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.enabled</code> attribute. 
	 * @return the enabled
	 */
	public boolean isEnabledAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isEnabled( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.enabled</code> attribute. 
	 * @return the enabled
	 */
	public boolean isEnabledAsPrimitive()
	{
		return isEnabledAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchSynchronizationConfig.enabled</code> attribute. 
	 * @param value the enabled
	 */
	public void setEnabled(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ENABLED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchSynchronizationConfig.enabled</code> attribute. 
	 * @param value the enabled
	 */
	public void setEnabled(final Boolean value)
	{
		setEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchSynchronizationConfig.enabled</code> attribute. 
	 * @param value the enabled
	 */
	public void setEnabled(final SessionContext ctx, final boolean value)
	{
		setEnabled( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchSynchronizationConfig.enabled</code> attribute. 
	 * @param value the enabled
	 */
	public void setEnabled(final boolean value)
	{
		setEnabled( getSession().getSessionContext(), value );
	}
	
}
