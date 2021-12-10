/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.searchservices.model.AbstractSnIndexerCronJobModel;
import de.hybris.platform.searchservices.model.SnFieldModel;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Generated model class for type SnIndexType first defined at extension searchservices.
 */
@SuppressWarnings("all")
public class SnIndexTypeModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SnIndexType";
	
	/**<i>Generated relation code constant for relation <code>SnIndexConfiguration2IndexType</code> defining source attribute <code>indexConfiguration</code> in extension <code>searchservices</code>.</i>*/
	public static final String _SNINDEXCONFIGURATION2INDEXTYPE = "SnIndexConfiguration2IndexType";
	
	/**<i>Generated relation code constant for relation <code>SnBaseStore2IndexType</code> defining source attribute <code>stores</code> in extension <code>commerceservices</code>.</i>*/
	public static final String _SNBASESTORE2INDEXTYPE = "SnBaseStore2IndexType";
	
	/**<i>Generated relation code constant for relation <code>SnBaseSite2IndexType</code> defining source attribute <code>sites</code> in extension <code>commerceservices</code>.</i>*/
	public static final String _SNBASESITE2INDEXTYPE = "SnBaseSite2IndexType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.id</code> attribute defined at extension <code>searchservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.name</code> attribute defined at extension <code>searchservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.itemComposedType</code> attribute defined at extension <code>searchservices</code>. */
	public static final String ITEMCOMPOSEDTYPE = "itemComposedType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.identityProvider</code> attribute defined at extension <code>searchservices</code>. */
	public static final String IDENTITYPROVIDER = "identityProvider";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.identityProviderParameters</code> attribute defined at extension <code>searchservices</code>. */
	public static final String IDENTITYPROVIDERPARAMETERS = "identityProviderParameters";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.defaultValueProvider</code> attribute defined at extension <code>searchservices</code>. */
	public static final String DEFAULTVALUEPROVIDER = "defaultValueProvider";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.defaultValueProviderParameters</code> attribute defined at extension <code>searchservices</code>. */
	public static final String DEFAULTVALUEPROVIDERPARAMETERS = "defaultValueProviderParameters";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.listeners</code> attribute defined at extension <code>searchservices</code>. */
	public static final String LISTENERS = "listeners";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.indexConfigurationPOS</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INDEXCONFIGURATIONPOS = "indexConfigurationPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.indexConfiguration</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INDEXCONFIGURATION = "indexConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.fields</code> attribute defined at extension <code>searchservices</code>. */
	public static final String FIELDS = "fields";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.catalogs</code> attribute defined at extension <code>searchservices</code>. */
	public static final String CATALOGS = "catalogs";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.catalogVersions</code> attribute defined at extension <code>searchservices</code>. */
	public static final String CATALOGVERSIONS = "catalogVersions";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.indexerCronJobs</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INDEXERCRONJOBS = "indexerCronJobs";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.stores</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String STORES = "stores";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnIndexType.sites</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String SITES = "sites";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SnIndexTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SnIndexTypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>SnIndexType</code> at extension <code>searchservices</code>
	 * @param _itemComposedType initial attribute declared by type <code>SnIndexType</code> at extension <code>searchservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnIndexTypeModel(final String _id, final ComposedTypeModel _itemComposedType)
	{
		super();
		setId(_id);
		setItemComposedType(_itemComposedType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>SnIndexType</code> at extension <code>searchservices</code>
	 * @param _itemComposedType initial attribute declared by type <code>SnIndexType</code> at extension <code>searchservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnIndexTypeModel(final String _id, final ComposedTypeModel _itemComposedType, final ItemModel _owner)
	{
		super();
		setId(_id);
		setItemComposedType(_itemComposedType);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.catalogs</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the catalogs
	 */
	@Accessor(qualifier = "catalogs", type = Accessor.Type.GETTER)
	public List<CatalogModel> getCatalogs()
	{
		return getPersistenceContext().getPropertyValue(CATALOGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.catalogVersions</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the catalogVersions
	 */
	@Accessor(qualifier = "catalogVersions", type = Accessor.Type.GETTER)
	public List<CatalogVersionModel> getCatalogVersions()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.defaultValueProvider</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the defaultValueProvider
	 */
	@Accessor(qualifier = "defaultValueProvider", type = Accessor.Type.GETTER)
	public String getDefaultValueProvider()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTVALUEPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.defaultValueProviderParameters</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the defaultValueProviderParameters
	 */
	@Accessor(qualifier = "defaultValueProviderParameters", type = Accessor.Type.GETTER)
	public Map<String,String> getDefaultValueProviderParameters()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTVALUEPROVIDERPARAMETERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.fields</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the fields
	 */
	@Accessor(qualifier = "fields", type = Accessor.Type.GETTER)
	public List<SnFieldModel> getFields()
	{
		return getPersistenceContext().getPropertyValue(FIELDS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.id</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.identityProvider</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the identityProvider
	 */
	@Accessor(qualifier = "identityProvider", type = Accessor.Type.GETTER)
	public String getIdentityProvider()
	{
		return getPersistenceContext().getPropertyValue(IDENTITYPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.identityProviderParameters</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the identityProviderParameters
	 */
	@Accessor(qualifier = "identityProviderParameters", type = Accessor.Type.GETTER)
	public Map<String,String> getIdentityProviderParameters()
	{
		return getPersistenceContext().getPropertyValue(IDENTITYPROVIDERPARAMETERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.indexConfiguration</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the indexConfiguration
	 */
	@Accessor(qualifier = "indexConfiguration", type = Accessor.Type.GETTER)
	public SnIndexConfigurationModel getIndexConfiguration()
	{
		return getPersistenceContext().getPropertyValue(INDEXCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.indexerCronJobs</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the indexerCronJobs
	 */
	@Accessor(qualifier = "indexerCronJobs", type = Accessor.Type.GETTER)
	public List<AbstractSnIndexerCronJobModel> getIndexerCronJobs()
	{
		return getPersistenceContext().getPropertyValue(INDEXERCRONJOBS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.itemComposedType</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the itemComposedType
	 */
	@Accessor(qualifier = "itemComposedType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getItemComposedType()
	{
		return getPersistenceContext().getPropertyValue(ITEMCOMPOSEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.listeners</code> attribute defined at extension <code>searchservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the listeners
	 */
	@Accessor(qualifier = "listeners", type = Accessor.Type.GETTER)
	public List<String> getListeners()
	{
		return getPersistenceContext().getPropertyValue(LISTENERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.name</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.name</code> attribute defined at extension <code>searchservices</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.sites</code> attribute defined at extension <code>commerceservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the sites
	 */
	@Accessor(qualifier = "sites", type = Accessor.Type.GETTER)
	public List<BaseSiteModel> getSites()
	{
		return getPersistenceContext().getPropertyValue(SITES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.stores</code> attribute defined at extension <code>commerceservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the stores
	 */
	@Accessor(qualifier = "stores", type = Accessor.Type.GETTER)
	public List<BaseStoreModel> getStores()
	{
		return getPersistenceContext().getPropertyValue(STORES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.catalogs</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the catalogs
	 */
	@Accessor(qualifier = "catalogs", type = Accessor.Type.SETTER)
	public void setCatalogs(final List<CatalogModel> value)
	{
		getPersistenceContext().setPropertyValue(CATALOGS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.catalogVersions</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the catalogVersions
	 */
	@Accessor(qualifier = "catalogVersions", type = Accessor.Type.SETTER)
	public void setCatalogVersions(final List<CatalogVersionModel> value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.defaultValueProvider</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the defaultValueProvider
	 */
	@Accessor(qualifier = "defaultValueProvider", type = Accessor.Type.SETTER)
	public void setDefaultValueProvider(final String value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTVALUEPROVIDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.defaultValueProviderParameters</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the defaultValueProviderParameters
	 */
	@Accessor(qualifier = "defaultValueProviderParameters", type = Accessor.Type.SETTER)
	public void setDefaultValueProviderParameters(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTVALUEPROVIDERPARAMETERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.fields</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the fields
	 */
	@Accessor(qualifier = "fields", type = Accessor.Type.SETTER)
	public void setFields(final List<SnFieldModel> value)
	{
		getPersistenceContext().setPropertyValue(FIELDS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.id</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.identityProvider</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the identityProvider
	 */
	@Accessor(qualifier = "identityProvider", type = Accessor.Type.SETTER)
	public void setIdentityProvider(final String value)
	{
		getPersistenceContext().setPropertyValue(IDENTITYPROVIDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.identityProviderParameters</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the identityProviderParameters
	 */
	@Accessor(qualifier = "identityProviderParameters", type = Accessor.Type.SETTER)
	public void setIdentityProviderParameters(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(IDENTITYPROVIDERPARAMETERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.indexConfiguration</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the indexConfiguration
	 */
	@Accessor(qualifier = "indexConfiguration", type = Accessor.Type.SETTER)
	public void setIndexConfiguration(final SnIndexConfigurationModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXCONFIGURATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.indexerCronJobs</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the indexerCronJobs
	 */
	@Accessor(qualifier = "indexerCronJobs", type = Accessor.Type.SETTER)
	public void setIndexerCronJobs(final List<AbstractSnIndexerCronJobModel> value)
	{
		getPersistenceContext().setPropertyValue(INDEXERCRONJOBS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.itemComposedType</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the itemComposedType
	 */
	@Accessor(qualifier = "itemComposedType", type = Accessor.Type.SETTER)
	public void setItemComposedType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(ITEMCOMPOSEDTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.listeners</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the listeners
	 */
	@Accessor(qualifier = "listeners", type = Accessor.Type.SETTER)
	public void setListeners(final List<String> value)
	{
		getPersistenceContext().setPropertyValue(LISTENERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.name</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.name</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.sites</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the sites
	 */
	@Accessor(qualifier = "sites", type = Accessor.Type.SETTER)
	public void setSites(final List<BaseSiteModel> value)
	{
		getPersistenceContext().setPropertyValue(SITES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnIndexType.stores</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the stores
	 */
	@Accessor(qualifier = "stores", type = Accessor.Type.SETTER)
	public void setStores(final List<BaseStoreModel> value)
	{
		getPersistenceContext().setPropertyValue(STORES, value);
	}
	
}
