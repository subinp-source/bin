/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.jalo;

import com.hybris.merchandising.constants.MerchandisingservicesConstants;
import com.hybris.merchandising.jalo.MerchProperty;
import de.hybris.platform.basecommerce.constants.BasecommerceConstants;
import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedType;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link com.hybris.merchandising.jalo.MerchProductDirectoryConfig MerchProductDirectoryConfig}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedMerchProductDirectoryConfig extends GenericItem
{
	/** Qualifier of the <code>MerchProductDirectoryConfig.indexedType</code> attribute **/
	public static final String INDEXEDTYPE = "indexedType";
	/** Qualifier of the <code>MerchProductDirectoryConfig.enabled</code> attribute **/
	public static final String ENABLED = "enabled";
	/** Qualifier of the <code>MerchProductDirectoryConfig.defaultLanguage</code> attribute **/
	public static final String DEFAULTLANGUAGE = "defaultLanguage";
	/** Qualifier of the <code>MerchProductDirectoryConfig.currency</code> attribute **/
	public static final String CURRENCY = "currency";
	/** Qualifier of the <code>MerchProductDirectoryConfig.baseImageUrl</code> attribute **/
	public static final String BASEIMAGEURL = "baseImageUrl";
	/** Qualifier of the <code>MerchProductDirectoryConfig.baseCatalogPageUrl</code> attribute **/
	public static final String BASECATALOGPAGEURL = "baseCatalogPageUrl";
	/** Qualifier of the <code>MerchProductDirectoryConfig.rollUpStrategy</code> attribute **/
	public static final String ROLLUPSTRATEGY = "rollUpStrategy";
	/** Qualifier of the <code>MerchProductDirectoryConfig.rollUpStrategyField</code> attribute **/
	public static final String ROLLUPSTRATEGYFIELD = "rollUpStrategyField";
	/** Qualifier of the <code>MerchProductDirectoryConfig.cdsIdentifier</code> attribute **/
	public static final String CDSIDENTIFIER = "cdsIdentifier";
	/** Qualifier of the <code>MerchProductDirectoryConfig.displayName</code> attribute **/
	public static final String DISPLAYNAME = "displayName";
	/** Qualifier of the <code>MerchProductDirectoryConfig.merchProperties</code> attribute **/
	public static final String MERCHPROPERTIES = "merchProperties";
	/** Qualifier of the <code>MerchProductDirectoryConfig.baseSites</code> attribute **/
	public static final String BASESITES = "baseSites";
	/** Qualifier of the <code>MerchProductDirectoryConfig.merchCatalogVersions</code> attribute **/
	public static final String MERCHCATALOGVERSIONS = "merchCatalogVersions";
	/** Relation ordering override parameter constants for MerchProductDir2CatalogVersion from ((merchandisingservices))*/
	protected static String MERCHPRODUCTDIR2CATALOGVERSION_SRC_ORDERED = "relation.MerchProductDir2CatalogVersion.source.ordered";
	protected static String MERCHPRODUCTDIR2CATALOGVERSION_TGT_ORDERED = "relation.MerchProductDir2CatalogVersion.target.ordered";
	/** Relation disable markmodifed parameter constants for MerchProductDir2CatalogVersion from ((merchandisingservices))*/
	protected static String MERCHPRODUCTDIR2CATALOGVERSION_MARKMODIFIED = "relation.MerchProductDir2CatalogVersion.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n MERCHPROPERTIES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<MerchProperty> MERCHPROPERTIESHANDLER = new OneToManyHandler<MerchProperty>(
	MerchandisingservicesConstants.TC.MERCHPROPERTY,
	true,
	"merchProductDirectoryConfig",
	"merchProductDirectoryConfigPOS",
	true,
	true,
	CollectionType.LIST
	);
	/**
	* {@link OneToManyHandler} for handling 1:n BASESITES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<BaseSite> BASESITESHANDLER = new OneToManyHandler<BaseSite>(
	BasecommerceConstants.TC.BASESITE,
	false,
	"merchProductDirectoryConfig",
	"merchProductDirectoryConfigPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(INDEXEDTYPE, AttributeMode.INITIAL);
		tmp.put(ENABLED, AttributeMode.INITIAL);
		tmp.put(DEFAULTLANGUAGE, AttributeMode.INITIAL);
		tmp.put(CURRENCY, AttributeMode.INITIAL);
		tmp.put(BASEIMAGEURL, AttributeMode.INITIAL);
		tmp.put(BASECATALOGPAGEURL, AttributeMode.INITIAL);
		tmp.put(ROLLUPSTRATEGY, AttributeMode.INITIAL);
		tmp.put(ROLLUPSTRATEGYFIELD, AttributeMode.INITIAL);
		tmp.put(CDSIDENTIFIER, AttributeMode.INITIAL);
		tmp.put(DISPLAYNAME, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.baseCatalogPageUrl</code> attribute.
	 * @return the baseCatalogPageUrl
	 */
	public String getBaseCatalogPageUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BASECATALOGPAGEURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.baseCatalogPageUrl</code> attribute.
	 * @return the baseCatalogPageUrl
	 */
	public String getBaseCatalogPageUrl()
	{
		return getBaseCatalogPageUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.baseCatalogPageUrl</code> attribute. 
	 * @param value the baseCatalogPageUrl
	 */
	public void setBaseCatalogPageUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BASECATALOGPAGEURL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.baseCatalogPageUrl</code> attribute. 
	 * @param value the baseCatalogPageUrl
	 */
	public void setBaseCatalogPageUrl(final String value)
	{
		setBaseCatalogPageUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.baseImageUrl</code> attribute.
	 * @return the baseImageUrl
	 */
	public String getBaseImageUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BASEIMAGEURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.baseImageUrl</code> attribute.
	 * @return the baseImageUrl
	 */
	public String getBaseImageUrl()
	{
		return getBaseImageUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.baseImageUrl</code> attribute. 
	 * @param value the baseImageUrl
	 */
	public void setBaseImageUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BASEIMAGEURL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.baseImageUrl</code> attribute. 
	 * @param value the baseImageUrl
	 */
	public void setBaseImageUrl(final String value)
	{
		setBaseImageUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.baseSites</code> attribute.
	 * @return the baseSites
	 */
	public List<BaseSite> getBaseSites(final SessionContext ctx)
	{
		return (List<BaseSite>)BASESITESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.baseSites</code> attribute.
	 * @return the baseSites
	 */
	public List<BaseSite> getBaseSites()
	{
		return getBaseSites( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.baseSites</code> attribute. 
	 * @param value the baseSites
	 */
	public void setBaseSites(final SessionContext ctx, final List<BaseSite> value)
	{
		BASESITESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.baseSites</code> attribute. 
	 * @param value the baseSites
	 */
	public void setBaseSites(final List<BaseSite> value)
	{
		setBaseSites( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to baseSites. 
	 * @param value the item to add to baseSites
	 */
	public void addToBaseSites(final SessionContext ctx, final BaseSite value)
	{
		BASESITESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to baseSites. 
	 * @param value the item to add to baseSites
	 */
	public void addToBaseSites(final BaseSite value)
	{
		addToBaseSites( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from baseSites. 
	 * @param value the item to remove from baseSites
	 */
	public void removeFromBaseSites(final SessionContext ctx, final BaseSite value)
	{
		BASESITESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from baseSites. 
	 * @param value the item to remove from baseSites
	 */
	public void removeFromBaseSites(final BaseSite value)
	{
		removeFromBaseSites( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.cdsIdentifier</code> attribute.
	 * @return the cdsIdentifier
	 */
	public String getCdsIdentifier(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CDSIDENTIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.cdsIdentifier</code> attribute.
	 * @return the cdsIdentifier
	 */
	public String getCdsIdentifier()
	{
		return getCdsIdentifier( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.cdsIdentifier</code> attribute. 
	 * @param value the cdsIdentifier
	 */
	public void setCdsIdentifier(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CDSIDENTIFIER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.cdsIdentifier</code> attribute. 
	 * @param value the cdsIdentifier
	 */
	public void setCdsIdentifier(final String value)
	{
		setCdsIdentifier( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency(final SessionContext ctx)
	{
		return (Currency)getProperty( ctx, CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency()
	{
		return getCurrency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final SessionContext ctx, final Currency value)
	{
		setProperty(ctx, CURRENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final Currency value)
	{
		setCurrency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.defaultLanguage</code> attribute.
	 * @return the defaultLanguage
	 */
	public Language getDefaultLanguage(final SessionContext ctx)
	{
		return (Language)getProperty( ctx, DEFAULTLANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.defaultLanguage</code> attribute.
	 * @return the defaultLanguage
	 */
	public Language getDefaultLanguage()
	{
		return getDefaultLanguage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.defaultLanguage</code> attribute. 
	 * @param value the defaultLanguage
	 */
	public void setDefaultLanguage(final SessionContext ctx, final Language value)
	{
		setProperty(ctx, DEFAULTLANGUAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.defaultLanguage</code> attribute. 
	 * @param value the defaultLanguage
	 */
	public void setDefaultLanguage(final Language value)
	{
		setDefaultLanguage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.displayName</code> attribute.
	 * @return the displayName
	 */
	public String getDisplayName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DISPLAYNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.displayName</code> attribute.
	 * @return the displayName
	 */
	public String getDisplayName()
	{
		return getDisplayName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.displayName</code> attribute. 
	 * @param value the displayName
	 */
	public void setDisplayName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DISPLAYNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.displayName</code> attribute. 
	 * @param value the displayName
	 */
	public void setDisplayName(final String value)
	{
		setDisplayName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.enabled</code> attribute.
	 * @return the enabled
	 */
	public Boolean isEnabled(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.enabled</code> attribute.
	 * @return the enabled
	 */
	public Boolean isEnabled()
	{
		return isEnabled( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.enabled</code> attribute. 
	 * @return the enabled
	 */
	public boolean isEnabledAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isEnabled( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.enabled</code> attribute. 
	 * @return the enabled
	 */
	public boolean isEnabledAsPrimitive()
	{
		return isEnabledAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.enabled</code> attribute. 
	 * @param value the enabled
	 */
	public void setEnabled(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ENABLED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.enabled</code> attribute. 
	 * @param value the enabled
	 */
	public void setEnabled(final Boolean value)
	{
		setEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.enabled</code> attribute. 
	 * @param value the enabled
	 */
	public void setEnabled(final SessionContext ctx, final boolean value)
	{
		setEnabled( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.enabled</code> attribute. 
	 * @param value the enabled
	 */
	public void setEnabled(final boolean value)
	{
		setEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.indexedType</code> attribute.
	 * @return the indexedType - Indexed type
	 */
	public SolrIndexedType getIndexedType(final SessionContext ctx)
	{
		return (SolrIndexedType)getProperty( ctx, INDEXEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.indexedType</code> attribute.
	 * @return the indexedType - Indexed type
	 */
	public SolrIndexedType getIndexedType()
	{
		return getIndexedType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.indexedType</code> attribute. 
	 * @param value the indexedType - Indexed type
	 */
	protected void setIndexedType(final SessionContext ctx, final SolrIndexedType value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+INDEXEDTYPE+"' is not changeable", 0 );
		}
		setProperty(ctx, INDEXEDTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.indexedType</code> attribute. 
	 * @param value the indexedType - Indexed type
	 */
	protected void setIndexedType(final SolrIndexedType value)
	{
		setIndexedType( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CatalogVersion");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(MERCHPRODUCTDIR2CATALOGVERSION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.merchCatalogVersions</code> attribute.
	 * @return the merchCatalogVersions
	 */
	public List<CatalogVersion> getMerchCatalogVersions(final SessionContext ctx)
	{
		final List<CatalogVersion> items = getLinkedItems( 
			ctx,
			true,
			MerchandisingservicesConstants.Relations.MERCHPRODUCTDIR2CATALOGVERSION,
			"CatalogVersion",
			null,
			Utilities.getRelationOrderingOverride(MERCHPRODUCTDIR2CATALOGVERSION_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.merchCatalogVersions</code> attribute.
	 * @return the merchCatalogVersions
	 */
	public List<CatalogVersion> getMerchCatalogVersions()
	{
		return getMerchCatalogVersions( getSession().getSessionContext() );
	}
	
	public long getMerchCatalogVersionsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			MerchandisingservicesConstants.Relations.MERCHPRODUCTDIR2CATALOGVERSION,
			"CatalogVersion",
			null
		);
	}
	
	public long getMerchCatalogVersionsCount()
	{
		return getMerchCatalogVersionsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.merchCatalogVersions</code> attribute. 
	 * @param value the merchCatalogVersions
	 */
	public void setMerchCatalogVersions(final SessionContext ctx, final List<CatalogVersion> value)
	{
		setLinkedItems( 
			ctx,
			true,
			MerchandisingservicesConstants.Relations.MERCHPRODUCTDIR2CATALOGVERSION,
			null,
			value,
			Utilities.getRelationOrderingOverride(MERCHPRODUCTDIR2CATALOGVERSION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(MERCHPRODUCTDIR2CATALOGVERSION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.merchCatalogVersions</code> attribute. 
	 * @param value the merchCatalogVersions
	 */
	public void setMerchCatalogVersions(final List<CatalogVersion> value)
	{
		setMerchCatalogVersions( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to merchCatalogVersions. 
	 * @param value the item to add to merchCatalogVersions
	 */
	public void addToMerchCatalogVersions(final SessionContext ctx, final CatalogVersion value)
	{
		addLinkedItems( 
			ctx,
			true,
			MerchandisingservicesConstants.Relations.MERCHPRODUCTDIR2CATALOGVERSION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(MERCHPRODUCTDIR2CATALOGVERSION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(MERCHPRODUCTDIR2CATALOGVERSION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to merchCatalogVersions. 
	 * @param value the item to add to merchCatalogVersions
	 */
	public void addToMerchCatalogVersions(final CatalogVersion value)
	{
		addToMerchCatalogVersions( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from merchCatalogVersions. 
	 * @param value the item to remove from merchCatalogVersions
	 */
	public void removeFromMerchCatalogVersions(final SessionContext ctx, final CatalogVersion value)
	{
		removeLinkedItems( 
			ctx,
			true,
			MerchandisingservicesConstants.Relations.MERCHPRODUCTDIR2CATALOGVERSION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(MERCHPRODUCTDIR2CATALOGVERSION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(MERCHPRODUCTDIR2CATALOGVERSION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from merchCatalogVersions. 
	 * @param value the item to remove from merchCatalogVersions
	 */
	public void removeFromMerchCatalogVersions(final CatalogVersion value)
	{
		removeFromMerchCatalogVersions( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.merchProperties</code> attribute.
	 * @return the merchProperties
	 */
	public List<MerchProperty> getMerchProperties(final SessionContext ctx)
	{
		return (List<MerchProperty>)MERCHPROPERTIESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.merchProperties</code> attribute.
	 * @return the merchProperties
	 */
	public List<MerchProperty> getMerchProperties()
	{
		return getMerchProperties( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.merchProperties</code> attribute. 
	 * @param value the merchProperties
	 */
	public void setMerchProperties(final SessionContext ctx, final List<MerchProperty> value)
	{
		MERCHPROPERTIESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.merchProperties</code> attribute. 
	 * @param value the merchProperties
	 */
	public void setMerchProperties(final List<MerchProperty> value)
	{
		setMerchProperties( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to merchProperties. 
	 * @param value the item to add to merchProperties
	 */
	public void addToMerchProperties(final SessionContext ctx, final MerchProperty value)
	{
		MERCHPROPERTIESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to merchProperties. 
	 * @param value the item to add to merchProperties
	 */
	public void addToMerchProperties(final MerchProperty value)
	{
		addToMerchProperties( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from merchProperties. 
	 * @param value the item to remove from merchProperties
	 */
	public void removeFromMerchProperties(final SessionContext ctx, final MerchProperty value)
	{
		MERCHPROPERTIESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from merchProperties. 
	 * @param value the item to remove from merchProperties
	 */
	public void removeFromMerchProperties(final MerchProperty value)
	{
		removeFromMerchProperties( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.rollUpStrategy</code> attribute.
	 * @return the rollUpStrategy - Roll Up Strategy
	 */
	public String getRollUpStrategy(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ROLLUPSTRATEGY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.rollUpStrategy</code> attribute.
	 * @return the rollUpStrategy - Roll Up Strategy
	 */
	public String getRollUpStrategy()
	{
		return getRollUpStrategy( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.rollUpStrategy</code> attribute. 
	 * @param value the rollUpStrategy - Roll Up Strategy
	 */
	public void setRollUpStrategy(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ROLLUPSTRATEGY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.rollUpStrategy</code> attribute. 
	 * @param value the rollUpStrategy - Roll Up Strategy
	 */
	public void setRollUpStrategy(final String value)
	{
		setRollUpStrategy( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.rollUpStrategyField</code> attribute.
	 * @return the rollUpStrategyField - Roll Up Strategy Field
	 */
	public String getRollUpStrategyField(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ROLLUPSTRATEGYFIELD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MerchProductDirectoryConfig.rollUpStrategyField</code> attribute.
	 * @return the rollUpStrategyField - Roll Up Strategy Field
	 */
	public String getRollUpStrategyField()
	{
		return getRollUpStrategyField( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.rollUpStrategyField</code> attribute. 
	 * @param value the rollUpStrategyField - Roll Up Strategy Field
	 */
	public void setRollUpStrategyField(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ROLLUPSTRATEGYFIELD,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MerchProductDirectoryConfig.rollUpStrategyField</code> attribute. 
	 * @param value the rollUpStrategyField - Roll Up Strategy Field
	 */
	public void setRollUpStrategyField(final String value)
	{
		setRollUpStrategyField( getSession().getSessionContext(), value );
	}
	
}
