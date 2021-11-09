/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.jalo;

import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogVersion;
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
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.AbstractSnIndexerCronJob;
import de.hybris.platform.searchservices.jalo.SnField;
import de.hybris.platform.searchservices.jalo.SnIndexConfiguration;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem SnIndexType}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSnIndexType extends GenericItem
{
	/** Qualifier of the <code>SnIndexType.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>SnIndexType.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>SnIndexType.itemComposedType</code> attribute **/
	public static final String ITEMCOMPOSEDTYPE = "itemComposedType";
	/** Qualifier of the <code>SnIndexType.identityProvider</code> attribute **/
	public static final String IDENTITYPROVIDER = "identityProvider";
	/** Qualifier of the <code>SnIndexType.identityProviderParameters</code> attribute **/
	public static final String IDENTITYPROVIDERPARAMETERS = "identityProviderParameters";
	/** Qualifier of the <code>SnIndexType.defaultValueProvider</code> attribute **/
	public static final String DEFAULTVALUEPROVIDER = "defaultValueProvider";
	/** Qualifier of the <code>SnIndexType.defaultValueProviderParameters</code> attribute **/
	public static final String DEFAULTVALUEPROVIDERPARAMETERS = "defaultValueProviderParameters";
	/** Qualifier of the <code>SnIndexType.listeners</code> attribute **/
	public static final String LISTENERS = "listeners";
	/** Qualifier of the <code>SnIndexType.indexConfigurationPOS</code> attribute **/
	public static final String INDEXCONFIGURATIONPOS = "indexConfigurationPOS";
	/** Qualifier of the <code>SnIndexType.indexConfiguration</code> attribute **/
	public static final String INDEXCONFIGURATION = "indexConfiguration";
	/** Qualifier of the <code>SnIndexType.fields</code> attribute **/
	public static final String FIELDS = "fields";
	/** Qualifier of the <code>SnIndexType.catalogs</code> attribute **/
	public static final String CATALOGS = "catalogs";
	/** Relation ordering override parameter constants for SnIndexType2Catalog from ((searchservices))*/
	protected static String SNINDEXTYPE2CATALOG_SRC_ORDERED = "relation.SnIndexType2Catalog.source.ordered";
	protected static String SNINDEXTYPE2CATALOG_TGT_ORDERED = "relation.SnIndexType2Catalog.target.ordered";
	/** Relation disable markmodifed parameter constants for SnIndexType2Catalog from ((searchservices))*/
	protected static String SNINDEXTYPE2CATALOG_MARKMODIFIED = "relation.SnIndexType2Catalog.markmodified";
	/** Qualifier of the <code>SnIndexType.catalogVersions</code> attribute **/
	public static final String CATALOGVERSIONS = "catalogVersions";
	/** Relation ordering override parameter constants for SnIndexType2CatalogVersion from ((searchservices))*/
	protected static String SNINDEXTYPE2CATALOGVERSION_SRC_ORDERED = "relation.SnIndexType2CatalogVersion.source.ordered";
	protected static String SNINDEXTYPE2CATALOGVERSION_TGT_ORDERED = "relation.SnIndexType2CatalogVersion.target.ordered";
	/** Relation disable markmodifed parameter constants for SnIndexType2CatalogVersion from ((searchservices))*/
	protected static String SNINDEXTYPE2CATALOGVERSION_MARKMODIFIED = "relation.SnIndexType2CatalogVersion.markmodified";
	/** Qualifier of the <code>SnIndexType.indexerCronJobs</code> attribute **/
	public static final String INDEXERCRONJOBS = "indexerCronJobs";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n INDEXCONFIGURATION's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedSnIndexType> INDEXCONFIGURATIONHANDLER = new BidirectionalOneToManyHandler<GeneratedSnIndexType>(
	SearchservicesConstants.TC.SNINDEXTYPE,
	false,
	"indexConfiguration",
	"indexConfigurationPOS",
	true,
	true,
	CollectionType.LIST
	);
	/**
	* {@link OneToManyHandler} for handling 1:n FIELDS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<SnField> FIELDSHANDLER = new OneToManyHandler<SnField>(
	SearchservicesConstants.TC.SNFIELD,
	true,
	"indexType",
	"indexTypePOS",
	true,
	true,
	CollectionType.LIST
	);
	/**
	* {@link OneToManyHandler} for handling 1:n INDEXERCRONJOBS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<AbstractSnIndexerCronJob> INDEXERCRONJOBSHANDLER = new OneToManyHandler<AbstractSnIndexerCronJob>(
	SearchservicesConstants.TC.ABSTRACTSNINDEXERCRONJOB,
	false,
	"indexType",
	"indexTypePOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(ITEMCOMPOSEDTYPE, AttributeMode.INITIAL);
		tmp.put(IDENTITYPROVIDER, AttributeMode.INITIAL);
		tmp.put(IDENTITYPROVIDERPARAMETERS, AttributeMode.INITIAL);
		tmp.put(DEFAULTVALUEPROVIDER, AttributeMode.INITIAL);
		tmp.put(DEFAULTVALUEPROVIDERPARAMETERS, AttributeMode.INITIAL);
		tmp.put(LISTENERS, AttributeMode.INITIAL);
		tmp.put(INDEXCONFIGURATIONPOS, AttributeMode.INITIAL);
		tmp.put(INDEXCONFIGURATION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.catalogs</code> attribute.
	 * @return the catalogs
	 */
	public List<Catalog> getCatalogs(final SessionContext ctx)
	{
		final List<Catalog> items = getLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXTYPE2CATALOG,
			"Catalog",
			null,
			Utilities.getRelationOrderingOverride(SNINDEXTYPE2CATALOG_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.catalogs</code> attribute.
	 * @return the catalogs
	 */
	public List<Catalog> getCatalogs()
	{
		return getCatalogs( getSession().getSessionContext() );
	}
	
	public long getCatalogsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXTYPE2CATALOG,
			"Catalog",
			null
		);
	}
	
	public long getCatalogsCount()
	{
		return getCatalogsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.catalogs</code> attribute. 
	 * @param value the catalogs
	 */
	public void setCatalogs(final SessionContext ctx, final List<Catalog> value)
	{
		setLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXTYPE2CATALOG,
			null,
			value,
			Utilities.getRelationOrderingOverride(SNINDEXTYPE2CATALOG_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(SNINDEXTYPE2CATALOG_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.catalogs</code> attribute. 
	 * @param value the catalogs
	 */
	public void setCatalogs(final List<Catalog> value)
	{
		setCatalogs( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to catalogs. 
	 * @param value the item to add to catalogs
	 */
	public void addToCatalogs(final SessionContext ctx, final Catalog value)
	{
		addLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXTYPE2CATALOG,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(SNINDEXTYPE2CATALOG_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(SNINDEXTYPE2CATALOG_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to catalogs. 
	 * @param value the item to add to catalogs
	 */
	public void addToCatalogs(final Catalog value)
	{
		addToCatalogs( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from catalogs. 
	 * @param value the item to remove from catalogs
	 */
	public void removeFromCatalogs(final SessionContext ctx, final Catalog value)
	{
		removeLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXTYPE2CATALOG,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(SNINDEXTYPE2CATALOG_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(SNINDEXTYPE2CATALOG_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from catalogs. 
	 * @param value the item to remove from catalogs
	 */
	public void removeFromCatalogs(final Catalog value)
	{
		removeFromCatalogs( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.catalogVersions</code> attribute.
	 * @return the catalogVersions
	 */
	public List<CatalogVersion> getCatalogVersions(final SessionContext ctx)
	{
		final List<CatalogVersion> items = getLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXTYPE2CATALOGVERSION,
			"CatalogVersion",
			null,
			Utilities.getRelationOrderingOverride(SNINDEXTYPE2CATALOGVERSION_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.catalogVersions</code> attribute.
	 * @return the catalogVersions
	 */
	public List<CatalogVersion> getCatalogVersions()
	{
		return getCatalogVersions( getSession().getSessionContext() );
	}
	
	public long getCatalogVersionsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXTYPE2CATALOGVERSION,
			"CatalogVersion",
			null
		);
	}
	
	public long getCatalogVersionsCount()
	{
		return getCatalogVersionsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.catalogVersions</code> attribute. 
	 * @param value the catalogVersions
	 */
	public void setCatalogVersions(final SessionContext ctx, final List<CatalogVersion> value)
	{
		setLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXTYPE2CATALOGVERSION,
			null,
			value,
			Utilities.getRelationOrderingOverride(SNINDEXTYPE2CATALOGVERSION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(SNINDEXTYPE2CATALOGVERSION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.catalogVersions</code> attribute. 
	 * @param value the catalogVersions
	 */
	public void setCatalogVersions(final List<CatalogVersion> value)
	{
		setCatalogVersions( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to catalogVersions. 
	 * @param value the item to add to catalogVersions
	 */
	public void addToCatalogVersions(final SessionContext ctx, final CatalogVersion value)
	{
		addLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXTYPE2CATALOGVERSION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(SNINDEXTYPE2CATALOGVERSION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(SNINDEXTYPE2CATALOGVERSION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to catalogVersions. 
	 * @param value the item to add to catalogVersions
	 */
	public void addToCatalogVersions(final CatalogVersion value)
	{
		addToCatalogVersions( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from catalogVersions. 
	 * @param value the item to remove from catalogVersions
	 */
	public void removeFromCatalogVersions(final SessionContext ctx, final CatalogVersion value)
	{
		removeLinkedItems( 
			ctx,
			true,
			SearchservicesConstants.Relations.SNINDEXTYPE2CATALOGVERSION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(SNINDEXTYPE2CATALOGVERSION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(SNINDEXTYPE2CATALOGVERSION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from catalogVersions. 
	 * @param value the item to remove from catalogVersions
	 */
	public void removeFromCatalogVersions(final CatalogVersion value)
	{
		removeFromCatalogVersions( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		INDEXCONFIGURATIONHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.defaultValueProvider</code> attribute.
	 * @return the defaultValueProvider
	 */
	public String getDefaultValueProvider(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DEFAULTVALUEPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.defaultValueProvider</code> attribute.
	 * @return the defaultValueProvider
	 */
	public String getDefaultValueProvider()
	{
		return getDefaultValueProvider( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.defaultValueProvider</code> attribute. 
	 * @param value the defaultValueProvider
	 */
	public void setDefaultValueProvider(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DEFAULTVALUEPROVIDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.defaultValueProvider</code> attribute. 
	 * @param value the defaultValueProvider
	 */
	public void setDefaultValueProvider(final String value)
	{
		setDefaultValueProvider( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.defaultValueProviderParameters</code> attribute.
	 * @return the defaultValueProviderParameters
	 */
	public Map<String,String> getAllDefaultValueProviderParameters(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, DEFAULTVALUEPROVIDERPARAMETERS);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.defaultValueProviderParameters</code> attribute.
	 * @return the defaultValueProviderParameters
	 */
	public Map<String,String> getAllDefaultValueProviderParameters()
	{
		return getAllDefaultValueProviderParameters( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.defaultValueProviderParameters</code> attribute. 
	 * @param value the defaultValueProviderParameters
	 */
	public void setAllDefaultValueProviderParameters(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, DEFAULTVALUEPROVIDERPARAMETERS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.defaultValueProviderParameters</code> attribute. 
	 * @param value the defaultValueProviderParameters
	 */
	public void setAllDefaultValueProviderParameters(final Map<String,String> value)
	{
		setAllDefaultValueProviderParameters( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.fields</code> attribute.
	 * @return the fields
	 */
	public List<SnField> getFields(final SessionContext ctx)
	{
		return (List<SnField>)FIELDSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.fields</code> attribute.
	 * @return the fields
	 */
	public List<SnField> getFields()
	{
		return getFields( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.fields</code> attribute. 
	 * @param value the fields
	 */
	public void setFields(final SessionContext ctx, final List<SnField> value)
	{
		FIELDSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.fields</code> attribute. 
	 * @param value the fields
	 */
	public void setFields(final List<SnField> value)
	{
		setFields( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to fields. 
	 * @param value the item to add to fields
	 */
	public void addToFields(final SessionContext ctx, final SnField value)
	{
		FIELDSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to fields. 
	 * @param value the item to add to fields
	 */
	public void addToFields(final SnField value)
	{
		addToFields( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from fields. 
	 * @param value the item to remove from fields
	 */
	public void removeFromFields(final SessionContext ctx, final SnField value)
	{
		FIELDSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from fields. 
	 * @param value the item to remove from fields
	 */
	public void removeFromFields(final SnField value)
	{
		removeFromFields( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.id</code> attribute.
	 * @return the id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.id</code> attribute.
	 * @return the id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.identityProvider</code> attribute.
	 * @return the identityProvider
	 */
	public String getIdentityProvider(final SessionContext ctx)
	{
		return (String)getProperty( ctx, IDENTITYPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.identityProvider</code> attribute.
	 * @return the identityProvider
	 */
	public String getIdentityProvider()
	{
		return getIdentityProvider( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.identityProvider</code> attribute. 
	 * @param value the identityProvider
	 */
	public void setIdentityProvider(final SessionContext ctx, final String value)
	{
		setProperty(ctx, IDENTITYPROVIDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.identityProvider</code> attribute. 
	 * @param value the identityProvider
	 */
	public void setIdentityProvider(final String value)
	{
		setIdentityProvider( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.identityProviderParameters</code> attribute.
	 * @return the identityProviderParameters
	 */
	public Map<String,String> getAllIdentityProviderParameters(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, IDENTITYPROVIDERPARAMETERS);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.identityProviderParameters</code> attribute.
	 * @return the identityProviderParameters
	 */
	public Map<String,String> getAllIdentityProviderParameters()
	{
		return getAllIdentityProviderParameters( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.identityProviderParameters</code> attribute. 
	 * @param value the identityProviderParameters
	 */
	public void setAllIdentityProviderParameters(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, IDENTITYPROVIDERPARAMETERS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.identityProviderParameters</code> attribute. 
	 * @param value the identityProviderParameters
	 */
	public void setAllIdentityProviderParameters(final Map<String,String> value)
	{
		setAllIdentityProviderParameters( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.indexConfiguration</code> attribute.
	 * @return the indexConfiguration
	 */
	public SnIndexConfiguration getIndexConfiguration(final SessionContext ctx)
	{
		return (SnIndexConfiguration)getProperty( ctx, INDEXCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.indexConfiguration</code> attribute.
	 * @return the indexConfiguration
	 */
	public SnIndexConfiguration getIndexConfiguration()
	{
		return getIndexConfiguration( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.indexConfiguration</code> attribute. 
	 * @param value the indexConfiguration
	 */
	public void setIndexConfiguration(final SessionContext ctx, final SnIndexConfiguration value)
	{
		INDEXCONFIGURATIONHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.indexConfiguration</code> attribute. 
	 * @param value the indexConfiguration
	 */
	public void setIndexConfiguration(final SnIndexConfiguration value)
	{
		setIndexConfiguration( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.indexConfigurationPOS</code> attribute.
	 * @return the indexConfigurationPOS
	 */
	 Integer getIndexConfigurationPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, INDEXCONFIGURATIONPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.indexConfigurationPOS</code> attribute.
	 * @return the indexConfigurationPOS
	 */
	 Integer getIndexConfigurationPOS()
	{
		return getIndexConfigurationPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.indexConfigurationPOS</code> attribute. 
	 * @return the indexConfigurationPOS
	 */
	 int getIndexConfigurationPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getIndexConfigurationPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.indexConfigurationPOS</code> attribute. 
	 * @return the indexConfigurationPOS
	 */
	 int getIndexConfigurationPOSAsPrimitive()
	{
		return getIndexConfigurationPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.indexConfigurationPOS</code> attribute. 
	 * @param value the indexConfigurationPOS
	 */
	 void setIndexConfigurationPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, INDEXCONFIGURATIONPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.indexConfigurationPOS</code> attribute. 
	 * @param value the indexConfigurationPOS
	 */
	 void setIndexConfigurationPOS(final Integer value)
	{
		setIndexConfigurationPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.indexConfigurationPOS</code> attribute. 
	 * @param value the indexConfigurationPOS
	 */
	 void setIndexConfigurationPOS(final SessionContext ctx, final int value)
	{
		setIndexConfigurationPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.indexConfigurationPOS</code> attribute. 
	 * @param value the indexConfigurationPOS
	 */
	 void setIndexConfigurationPOS(final int value)
	{
		setIndexConfigurationPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.indexerCronJobs</code> attribute.
	 * @return the indexerCronJobs
	 */
	public List<AbstractSnIndexerCronJob> getIndexerCronJobs(final SessionContext ctx)
	{
		return (List<AbstractSnIndexerCronJob>)INDEXERCRONJOBSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.indexerCronJobs</code> attribute.
	 * @return the indexerCronJobs
	 */
	public List<AbstractSnIndexerCronJob> getIndexerCronJobs()
	{
		return getIndexerCronJobs( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.indexerCronJobs</code> attribute. 
	 * @param value the indexerCronJobs
	 */
	public void setIndexerCronJobs(final SessionContext ctx, final List<AbstractSnIndexerCronJob> value)
	{
		INDEXERCRONJOBSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.indexerCronJobs</code> attribute. 
	 * @param value the indexerCronJobs
	 */
	public void setIndexerCronJobs(final List<AbstractSnIndexerCronJob> value)
	{
		setIndexerCronJobs( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to indexerCronJobs. 
	 * @param value the item to add to indexerCronJobs
	 */
	public void addToIndexerCronJobs(final SessionContext ctx, final AbstractSnIndexerCronJob value)
	{
		INDEXERCRONJOBSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to indexerCronJobs. 
	 * @param value the item to add to indexerCronJobs
	 */
	public void addToIndexerCronJobs(final AbstractSnIndexerCronJob value)
	{
		addToIndexerCronJobs( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from indexerCronJobs. 
	 * @param value the item to remove from indexerCronJobs
	 */
	public void removeFromIndexerCronJobs(final SessionContext ctx, final AbstractSnIndexerCronJob value)
	{
		INDEXERCRONJOBSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from indexerCronJobs. 
	 * @param value the item to remove from indexerCronJobs
	 */
	public void removeFromIndexerCronJobs(final AbstractSnIndexerCronJob value)
	{
		removeFromIndexerCronJobs( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Catalog");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(SNINDEXTYPE2CATALOG_MARKMODIFIED);
		}
		ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("CatalogVersion");
		if(relationSecondEnd1.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(SNINDEXTYPE2CATALOGVERSION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.itemComposedType</code> attribute.
	 * @return the itemComposedType
	 */
	public ComposedType getItemComposedType(final SessionContext ctx)
	{
		return (ComposedType)getProperty( ctx, ITEMCOMPOSEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.itemComposedType</code> attribute.
	 * @return the itemComposedType
	 */
	public ComposedType getItemComposedType()
	{
		return getItemComposedType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.itemComposedType</code> attribute. 
	 * @param value the itemComposedType
	 */
	public void setItemComposedType(final SessionContext ctx, final ComposedType value)
	{
		setProperty(ctx, ITEMCOMPOSEDTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.itemComposedType</code> attribute. 
	 * @param value the itemComposedType
	 */
	public void setItemComposedType(final ComposedType value)
	{
		setItemComposedType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.listeners</code> attribute.
	 * @return the listeners
	 */
	public List<String> getListeners(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, LISTENERS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.listeners</code> attribute.
	 * @return the listeners
	 */
	public List<String> getListeners()
	{
		return getListeners( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.listeners</code> attribute. 
	 * @param value the listeners
	 */
	public void setListeners(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, LISTENERS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.listeners</code> attribute. 
	 * @param value the listeners
	 */
	public void setListeners(final List<String> value)
	{
		setListeners( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSnIndexType.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexType.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSnIndexType.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexType.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
}
