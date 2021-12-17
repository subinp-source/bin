/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.model;

import com.hybris.merchandising.model.MerchPropertyModel;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.List;

/**
 * Generated model class for type MerchProductDirectoryConfig first defined at extension merchandisingservices.
 */
@SuppressWarnings("all")
public class MerchProductDirectoryConfigModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MerchProductDirectoryConfig";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.indexedType</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String INDEXEDTYPE = "indexedType";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.enabled</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String ENABLED = "enabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.defaultLanguage</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String DEFAULTLANGUAGE = "defaultLanguage";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.currency</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.baseImageUrl</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String BASEIMAGEURL = "baseImageUrl";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.baseCatalogPageUrl</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String BASECATALOGPAGEURL = "baseCatalogPageUrl";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.rollUpStrategy</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String ROLLUPSTRATEGY = "rollUpStrategy";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.rollUpStrategyField</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String ROLLUPSTRATEGYFIELD = "rollUpStrategyField";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.cdsIdentifier</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String CDSIDENTIFIER = "cdsIdentifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.displayName</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String DISPLAYNAME = "displayName";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.merchProperties</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String MERCHPROPERTIES = "merchProperties";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.baseSites</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String BASESITES = "baseSites";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchProductDirectoryConfig.merchCatalogVersions</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String MERCHCATALOGVERSIONS = "merchCatalogVersions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MerchProductDirectoryConfigModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MerchProductDirectoryConfigModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseSites initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 * @param _currency initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 * @param _defaultLanguage initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 * @param _displayName initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 * @param _indexedType initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 * @param _rollUpStrategy initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 * @param _rollUpStrategyField initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public MerchProductDirectoryConfigModel(final List<BaseSiteModel> _baseSites, final CurrencyModel _currency, final LanguageModel _defaultLanguage, final String _displayName, final SolrIndexedTypeModel _indexedType, final String _rollUpStrategy, final String _rollUpStrategyField)
	{
		super();
		setBaseSites(_baseSites);
		setCurrency(_currency);
		setDefaultLanguage(_defaultLanguage);
		setDisplayName(_displayName);
		setIndexedType(_indexedType);
		setRollUpStrategy(_rollUpStrategy);
		setRollUpStrategyField(_rollUpStrategyField);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseSites initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 * @param _currency initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 * @param _defaultLanguage initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 * @param _displayName initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 * @param _indexedType initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _rollUpStrategy initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 * @param _rollUpStrategyField initial attribute declared by type <code>MerchProductDirectoryConfig</code> at extension <code>merchandisingservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public MerchProductDirectoryConfigModel(final List<BaseSiteModel> _baseSites, final CurrencyModel _currency, final LanguageModel _defaultLanguage, final String _displayName, final SolrIndexedTypeModel _indexedType, final ItemModel _owner, final String _rollUpStrategy, final String _rollUpStrategyField)
	{
		super();
		setBaseSites(_baseSites);
		setCurrency(_currency);
		setDefaultLanguage(_defaultLanguage);
		setDisplayName(_displayName);
		setIndexedType(_indexedType);
		setOwner(_owner);
		setRollUpStrategy(_rollUpStrategy);
		setRollUpStrategyField(_rollUpStrategyField);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.baseCatalogPageUrl</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the baseCatalogPageUrl
	 */
	@Accessor(qualifier = "baseCatalogPageUrl", type = Accessor.Type.GETTER)
	public String getBaseCatalogPageUrl()
	{
		return getPersistenceContext().getPropertyValue(BASECATALOGPAGEURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.baseImageUrl</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the baseImageUrl
	 */
	@Accessor(qualifier = "baseImageUrl", type = Accessor.Type.GETTER)
	public String getBaseImageUrl()
	{
		return getPersistenceContext().getPropertyValue(BASEIMAGEURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.baseSites</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the baseSites
	 */
	@Accessor(qualifier = "baseSites", type = Accessor.Type.GETTER)
	public List<BaseSiteModel> getBaseSites()
	{
		return getPersistenceContext().getPropertyValue(BASESITES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.cdsIdentifier</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the cdsIdentifier
	 */
	@Accessor(qualifier = "cdsIdentifier", type = Accessor.Type.GETTER)
	public String getCdsIdentifier()
	{
		return getPersistenceContext().getPropertyValue(CDSIDENTIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.currency</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.defaultLanguage</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the defaultLanguage
	 */
	@Accessor(qualifier = "defaultLanguage", type = Accessor.Type.GETTER)
	public LanguageModel getDefaultLanguage()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTLANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.displayName</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the displayName
	 */
	@Accessor(qualifier = "displayName", type = Accessor.Type.GETTER)
	public String getDisplayName()
	{
		return getPersistenceContext().getPropertyValue(DISPLAYNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.indexedType</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the indexedType - Indexed type
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
	public SolrIndexedTypeModel getIndexedType()
	{
		return getPersistenceContext().getPropertyValue(INDEXEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.merchCatalogVersions</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the merchCatalogVersions
	 */
	@Accessor(qualifier = "merchCatalogVersions", type = Accessor.Type.GETTER)
	public List<CatalogVersionModel> getMerchCatalogVersions()
	{
		return getPersistenceContext().getPropertyValue(MERCHCATALOGVERSIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.merchProperties</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the merchProperties
	 */
	@Accessor(qualifier = "merchProperties", type = Accessor.Type.GETTER)
	public List<MerchPropertyModel> getMerchProperties()
	{
		return getPersistenceContext().getPropertyValue(MERCHPROPERTIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.rollUpStrategy</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the rollUpStrategy - Roll Up Strategy
	 */
	@Accessor(qualifier = "rollUpStrategy", type = Accessor.Type.GETTER)
	public String getRollUpStrategy()
	{
		return getPersistenceContext().getPropertyValue(ROLLUPSTRATEGY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.rollUpStrategyField</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the rollUpStrategyField - Roll Up Strategy Field
	 */
	@Accessor(qualifier = "rollUpStrategyField", type = Accessor.Type.GETTER)
	public String getRollUpStrategyField()
	{
		return getPersistenceContext().getPropertyValue(ROLLUPSTRATEGYFIELD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.enabled</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the enabled
	 */
	@Accessor(qualifier = "enabled", type = Accessor.Type.GETTER)
	public boolean isEnabled()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ENABLED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchProductDirectoryConfig.baseCatalogPageUrl</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the baseCatalogPageUrl
	 */
	@Accessor(qualifier = "baseCatalogPageUrl", type = Accessor.Type.SETTER)
	public void setBaseCatalogPageUrl(final String value)
	{
		getPersistenceContext().setPropertyValue(BASECATALOGPAGEURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchProductDirectoryConfig.baseImageUrl</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the baseImageUrl
	 */
	@Accessor(qualifier = "baseImageUrl", type = Accessor.Type.SETTER)
	public void setBaseImageUrl(final String value)
	{
		getPersistenceContext().setPropertyValue(BASEIMAGEURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchProductDirectoryConfig.baseSites</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the baseSites
	 */
	@Accessor(qualifier = "baseSites", type = Accessor.Type.SETTER)
	public void setBaseSites(final List<BaseSiteModel> value)
	{
		getPersistenceContext().setPropertyValue(BASESITES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchProductDirectoryConfig.cdsIdentifier</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the cdsIdentifier
	 */
	@Accessor(qualifier = "cdsIdentifier", type = Accessor.Type.SETTER)
	public void setCdsIdentifier(final String value)
	{
		getPersistenceContext().setPropertyValue(CDSIDENTIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchProductDirectoryConfig.currency</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchProductDirectoryConfig.defaultLanguage</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the defaultLanguage
	 */
	@Accessor(qualifier = "defaultLanguage", type = Accessor.Type.SETTER)
	public void setDefaultLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTLANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchProductDirectoryConfig.displayName</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the displayName
	 */
	@Accessor(qualifier = "displayName", type = Accessor.Type.SETTER)
	public void setDisplayName(final String value)
	{
		getPersistenceContext().setPropertyValue(DISPLAYNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchProductDirectoryConfig.enabled</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the enabled
	 */
	@Accessor(qualifier = "enabled", type = Accessor.Type.SETTER)
	public void setEnabled(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ENABLED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MerchProductDirectoryConfig.indexedType</code> attribute defined at extension <code>merchandisingservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the indexedType - Indexed type
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
	public void setIndexedType(final SolrIndexedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXEDTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchProductDirectoryConfig.merchCatalogVersions</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the merchCatalogVersions
	 */
	@Accessor(qualifier = "merchCatalogVersions", type = Accessor.Type.SETTER)
	public void setMerchCatalogVersions(final List<CatalogVersionModel> value)
	{
		getPersistenceContext().setPropertyValue(MERCHCATALOGVERSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchProductDirectoryConfig.merchProperties</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the merchProperties
	 */
	@Accessor(qualifier = "merchProperties", type = Accessor.Type.SETTER)
	public void setMerchProperties(final List<MerchPropertyModel> value)
	{
		getPersistenceContext().setPropertyValue(MERCHPROPERTIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchProductDirectoryConfig.rollUpStrategy</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the rollUpStrategy - Roll Up Strategy
	 */
	@Accessor(qualifier = "rollUpStrategy", type = Accessor.Type.SETTER)
	public void setRollUpStrategy(final String value)
	{
		getPersistenceContext().setPropertyValue(ROLLUPSTRATEGY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchProductDirectoryConfig.rollUpStrategyField</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the rollUpStrategyField - Roll Up Strategy Field
	 */
	@Accessor(qualifier = "rollUpStrategyField", type = Accessor.Type.SETTER)
	public void setRollUpStrategyField(final String value)
	{
		getPersistenceContext().setPropertyValue(ROLLUPSTRATEGYFIELD, value);
	}
	
}
