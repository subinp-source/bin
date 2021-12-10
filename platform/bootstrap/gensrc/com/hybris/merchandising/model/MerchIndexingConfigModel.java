/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.model;

import com.hybris.merchandising.model.MerchImagePropertyModel;
import com.hybris.merchandising.model.MerchPropertyModel;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.List;

/**
 * Generated model class for type MerchIndexingConfig first defined at extension merchandisingservices.
 */
@SuppressWarnings("all")
public class MerchIndexingConfigModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MerchIndexingConfig";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchIndexingConfig.indexedType</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String INDEXEDTYPE = "indexedType";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchIndexingConfig.enabled</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String ENABLED = "enabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchIndexingConfig.language</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchIndexingConfig.currency</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchIndexingConfig.baseImageUrl</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String BASEIMAGEURL = "baseImageUrl";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchIndexingConfig.baseProductPageUrl</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String BASEPRODUCTPAGEURL = "baseProductPageUrl";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchIndexingConfig.merchProperties</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String MERCHPROPERTIES = "merchProperties";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchIndexingConfig.merchImageProperties</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String MERCHIMAGEPROPERTIES = "merchImageProperties";
	
	/** <i>Generated constant</i> - Attribute key of <code>MerchIndexingConfig.merchCatalogVersions</code> attribute defined at extension <code>merchandisingservices</code>. */
	public static final String MERCHCATALOGVERSIONS = "merchCatalogVersions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MerchIndexingConfigModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MerchIndexingConfigModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>MerchIndexingConfig</code> at extension <code>merchandisingservices</code>
	 * @param _indexedType initial attribute declared by type <code>MerchIndexingConfig</code> at extension <code>merchandisingservices</code>
	 * @param _language initial attribute declared by type <code>MerchIndexingConfig</code> at extension <code>merchandisingservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public MerchIndexingConfigModel(final CurrencyModel _currency, final SolrIndexedTypeModel _indexedType, final LanguageModel _language)
	{
		super();
		setCurrency(_currency);
		setIndexedType(_indexedType);
		setLanguage(_language);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>MerchIndexingConfig</code> at extension <code>merchandisingservices</code>
	 * @param _indexedType initial attribute declared by type <code>MerchIndexingConfig</code> at extension <code>merchandisingservices</code>
	 * @param _language initial attribute declared by type <code>MerchIndexingConfig</code> at extension <code>merchandisingservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public MerchIndexingConfigModel(final CurrencyModel _currency, final SolrIndexedTypeModel _indexedType, final LanguageModel _language, final ItemModel _owner)
	{
		super();
		setCurrency(_currency);
		setIndexedType(_indexedType);
		setLanguage(_language);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchIndexingConfig.baseImageUrl</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the baseImageUrl
	 */
	@Accessor(qualifier = "baseImageUrl", type = Accessor.Type.GETTER)
	public String getBaseImageUrl()
	{
		return getPersistenceContext().getPropertyValue(BASEIMAGEURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchIndexingConfig.baseProductPageUrl</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the baseProductPageUrl
	 */
	@Accessor(qualifier = "baseProductPageUrl", type = Accessor.Type.GETTER)
	public String getBaseProductPageUrl()
	{
		return getPersistenceContext().getPropertyValue(BASEPRODUCTPAGEURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchIndexingConfig.currency</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchIndexingConfig.indexedType</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the indexedType - Indexed type
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
	public SolrIndexedTypeModel getIndexedType()
	{
		return getPersistenceContext().getPropertyValue(INDEXEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchIndexingConfig.language</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchIndexingConfig.merchCatalogVersions</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the merchCatalogVersions
	 */
	@Accessor(qualifier = "merchCatalogVersions", type = Accessor.Type.GETTER)
	public List<CatalogVersionModel> getMerchCatalogVersions()
	{
		return getPersistenceContext().getPropertyValue(MERCHCATALOGVERSIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchIndexingConfig.merchImageProperties</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the merchImageProperties
	 */
	@Accessor(qualifier = "merchImageProperties", type = Accessor.Type.GETTER)
	public List<MerchImagePropertyModel> getMerchImageProperties()
	{
		return getPersistenceContext().getPropertyValue(MERCHIMAGEPROPERTIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchIndexingConfig.merchProperties</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the merchProperties
	 */
	@Accessor(qualifier = "merchProperties", type = Accessor.Type.GETTER)
	public List<MerchPropertyModel> getMerchProperties()
	{
		return getPersistenceContext().getPropertyValue(MERCHPROPERTIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchIndexingConfig.enabled</code> attribute defined at extension <code>merchandisingservices</code>. 
	 * @return the enabled
	 */
	@Accessor(qualifier = "enabled", type = Accessor.Type.GETTER)
	public boolean isEnabled()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ENABLED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchIndexingConfig.baseImageUrl</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the baseImageUrl
	 */
	@Accessor(qualifier = "baseImageUrl", type = Accessor.Type.SETTER)
	public void setBaseImageUrl(final String value)
	{
		getPersistenceContext().setPropertyValue(BASEIMAGEURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchIndexingConfig.baseProductPageUrl</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the baseProductPageUrl
	 */
	@Accessor(qualifier = "baseProductPageUrl", type = Accessor.Type.SETTER)
	public void setBaseProductPageUrl(final String value)
	{
		getPersistenceContext().setPropertyValue(BASEPRODUCTPAGEURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchIndexingConfig.currency</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchIndexingConfig.enabled</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the enabled
	 */
	@Accessor(qualifier = "enabled", type = Accessor.Type.SETTER)
	public void setEnabled(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ENABLED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MerchIndexingConfig.indexedType</code> attribute defined at extension <code>merchandisingservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the indexedType - Indexed type
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
	public void setIndexedType(final SolrIndexedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXEDTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchIndexingConfig.language</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchIndexingConfig.merchCatalogVersions</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the merchCatalogVersions
	 */
	@Accessor(qualifier = "merchCatalogVersions", type = Accessor.Type.SETTER)
	public void setMerchCatalogVersions(final List<CatalogVersionModel> value)
	{
		getPersistenceContext().setPropertyValue(MERCHCATALOGVERSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchIndexingConfig.merchImageProperties</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the merchImageProperties
	 */
	@Accessor(qualifier = "merchImageProperties", type = Accessor.Type.SETTER)
	public void setMerchImageProperties(final List<MerchImagePropertyModel> value)
	{
		getPersistenceContext().setPropertyValue(MERCHIMAGEPROPERTIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MerchIndexingConfig.merchProperties</code> attribute defined at extension <code>merchandisingservices</code>. 
	 *  
	 * @param value the merchProperties
	 */
	@Accessor(qualifier = "merchProperties", type = Accessor.Type.SETTER)
	public void setMerchProperties(final List<MerchPropertyModel> value)
	{
		getPersistenceContext().setPropertyValue(MERCHPROPERTIES, value);
	}
	
}
