/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MerchSynchronizationConfig first defined at extension merchandisingservices.
 */
@SuppressWarnings("all")
public class MerchSynchronizationConfigModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MerchSynchronizationConfig";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchSynchronizationConfig.baseSite</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String BASESITE = "baseSite";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchSynchronizationConfig.catalog</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String CATALOG = "catalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchSynchronizationConfig.catalogVersion</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchSynchronizationConfig.enabled</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String ENABLED = "enabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchSynchronizationConfig.baseCategoryUrl</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String BASECATEGORYURL = "baseCategoryUrl";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MerchSynchronizationConfigModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MerchSynchronizationConfigModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseSite initial attribute declared by type <code>MerchSynchronizationConfig</code> at extension <code>merchandisingservices</code>
	 * @param _catalog initial attribute declared by type <code>MerchSynchronizationConfig</code> at extension <code>merchandisingservices</code>
	 * @param _catalogVersion initial attribute declared by type <code>MerchSynchronizationConfig</code> at extension <code>merchandisingservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public MerchSynchronizationConfigModel(final BaseSiteModel _baseSite, final CatalogModel _catalog, final CatalogVersionModel _catalogVersion)
	{
		super();
		setBaseSite(_baseSite);
		setCatalog(_catalog);
		setCatalogVersion(_catalogVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseSite initial attribute declared by type <code>MerchSynchronizationConfig</code> at extension <code>merchandisingservices</code>
	 * @param _catalog initial attribute declared by type <code>MerchSynchronizationConfig</code> at extension <code>merchandisingservices</code>
	 * @param _catalogVersion initial attribute declared by type <code>MerchSynchronizationConfig</code> at extension <code>merchandisingservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public MerchSynchronizationConfigModel(final BaseSiteModel _baseSite, final CatalogModel _catalog, final CatalogVersionModel _catalogVersion, final ItemModel _owner)
	{
		super();
		setBaseSite(_baseSite);
		setCatalog(_catalog);
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.baseCategoryUrl</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the baseCategoryUrl
	 */
	@Accessor(qualifier = "baseCategoryUrl", type = Accessor.Type.GETTER)
	public String getBaseCategoryUrl()
	{
		return getPersistenceContext().getPropertyValue(BASECATEGORYURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.baseSite</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the baseSite - Base site to synchronize
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
	public BaseSiteModel getBaseSite()
	{
		return getPersistenceContext().getPropertyValue(BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.catalog</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the catalog - Catalog to synchronize
	 */
	@Accessor(qualifier = "catalog", type = Accessor.Type.GETTER)
	public CatalogModel getCatalog()
	{
		return getPersistenceContext().getPropertyValue(CATALOG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.catalogVersion</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the catalogVersion - Catalog Version to synchronize
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchSynchronizationConfig.enabled</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the enabled
	 */
	@Accessor(qualifier = "enabled", type = Accessor.Type.GETTER)
	public boolean isEnabled()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ENABLED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchSynchronizationConfig.baseCategoryUrl</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the baseCategoryUrl
	 */
	@Accessor(qualifier = "baseCategoryUrl", type = Accessor.Type.SETTER)
	public void setBaseCategoryUrl(final String value)
	{
		getPersistenceContext().setPropertyValue(BASECATEGORYURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchSynchronizationConfig.baseSite</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the baseSite - Base site to synchronize
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
	public void setBaseSite(final BaseSiteModel value)
	{
		getPersistenceContext().setPropertyValue(BASESITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchSynchronizationConfig.catalog</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the catalog - Catalog to synchronize
	 */
	@Accessor(qualifier = "catalog", type = Accessor.Type.SETTER)
	public void setCatalog(final CatalogModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchSynchronizationConfig.catalogVersion</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the catalogVersion - Catalog Version to synchronize
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchSynchronizationConfig.enabled</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the enabled
	 */
	@Accessor(qualifier = "enabled", type = Accessor.Type.SETTER)
	public void setEnabled(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ENABLED, toObject(value));
	}
	
}
