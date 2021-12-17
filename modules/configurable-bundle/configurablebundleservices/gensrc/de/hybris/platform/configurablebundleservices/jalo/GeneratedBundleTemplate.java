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
import de.hybris.platform.configurablebundleservices.jalo.BundleSelectionCriteria;
import de.hybris.platform.configurablebundleservices.jalo.BundleTemplate;
import de.hybris.platform.configurablebundleservices.jalo.BundleTemplateStatus;
import de.hybris.platform.configurablebundleservices.jalo.ChangeProductPriceBundleRule;
import de.hybris.platform.configurablebundleservices.jalo.DisableProductBundleRule;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.PartOfHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.configurablebundleservices.jalo.BundleTemplate BundleTemplate}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedBundleTemplate extends GenericItem
{
	/** Qualifier of the <code>BundleTemplate.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>BundleTemplate.version</code> attribute **/
	public static final String VERSION = "version";
	/** Qualifier of the <code>BundleTemplate.catalogVersion</code> attribute **/
	public static final String CATALOGVERSION = "catalogVersion";
	/** Qualifier of the <code>BundleTemplate.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>BundleTemplate.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>BundleTemplate.bundleSelectionCriteria</code> attribute **/
	public static final String BUNDLESELECTIONCRITERIA = "bundleSelectionCriteria";
	/** Qualifier of the <code>BundleTemplate.parentTemplatePOS</code> attribute **/
	public static final String PARENTTEMPLATEPOS = "parentTemplatePOS";
	/** Qualifier of the <code>BundleTemplate.parentTemplate</code> attribute **/
	public static final String PARENTTEMPLATE = "parentTemplate";
	/** Qualifier of the <code>BundleTemplate.childTemplates</code> attribute **/
	public static final String CHILDTEMPLATES = "childTemplates";
	/** Qualifier of the <code>BundleTemplate.products</code> attribute **/
	public static final String PRODUCTS = "products";
	/** Relation ordering override parameter constants for ProductsBundleTemplatesRelation from ((configurablebundleservices))*/
	protected static String PRODUCTSBUNDLETEMPLATESRELATION_SRC_ORDERED = "relation.ProductsBundleTemplatesRelation.source.ordered";
	protected static String PRODUCTSBUNDLETEMPLATESRELATION_TGT_ORDERED = "relation.ProductsBundleTemplatesRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for ProductsBundleTemplatesRelation from ((configurablebundleservices))*/
	protected static String PRODUCTSBUNDLETEMPLATESRELATION_MARKMODIFIED = "relation.ProductsBundleTemplatesRelation.markmodified";
	/** Qualifier of the <code>BundleTemplate.changeProductPriceBundleRules</code> attribute **/
	public static final String CHANGEPRODUCTPRICEBUNDLERULES = "changeProductPriceBundleRules";
	/** Qualifier of the <code>BundleTemplate.disableProductBundleRules</code> attribute **/
	public static final String DISABLEPRODUCTBUNDLERULES = "disableProductBundleRules";
	/** Qualifier of the <code>BundleTemplate.requiredBundleTemplates</code> attribute **/
	public static final String REQUIREDBUNDLETEMPLATES = "requiredBundleTemplates";
	/** Relation ordering override parameter constants for RequiredBundleTemplatesDependentBundleTemplatesRelation from ((configurablebundleservices))*/
	protected static String REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION_SRC_ORDERED = "relation.RequiredBundleTemplatesDependentBundleTemplatesRelation.source.ordered";
	protected static String REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION_TGT_ORDERED = "relation.RequiredBundleTemplatesDependentBundleTemplatesRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for RequiredBundleTemplatesDependentBundleTemplatesRelation from ((configurablebundleservices))*/
	protected static String REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION_MARKMODIFIED = "relation.RequiredBundleTemplatesDependentBundleTemplatesRelation.markmodified";
	/** Qualifier of the <code>BundleTemplate.dependentBundleTemplates</code> attribute **/
	public static final String DEPENDENTBUNDLETEMPLATES = "dependentBundleTemplates";
	/** Qualifier of the <code>BundleTemplate.status</code> attribute **/
	public static final String STATUS = "status";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n PARENTTEMPLATE's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedBundleTemplate> PARENTTEMPLATEHANDLER = new BidirectionalOneToManyHandler<GeneratedBundleTemplate>(
	ConfigurableBundleServicesConstants.TC.BUNDLETEMPLATE,
	false,
	"parentTemplate",
	"parentTemplatePOS",
	true,
	true,
	CollectionType.LIST
	);
	/**
	* {@link OneToManyHandler} for handling 1:n CHILDTEMPLATES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<BundleTemplate> CHILDTEMPLATESHANDLER = new OneToManyHandler<BundleTemplate>(
	ConfigurableBundleServicesConstants.TC.BUNDLETEMPLATE,
	false,
	"parentTemplate",
	"parentTemplatePOS",
	true,
	true,
	CollectionType.LIST
	);
	/**
	* {@link OneToManyHandler} for handling 1:n CHANGEPRODUCTPRICEBUNDLERULES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ChangeProductPriceBundleRule> CHANGEPRODUCTPRICEBUNDLERULESHANDLER = new OneToManyHandler<ChangeProductPriceBundleRule>(
	ConfigurableBundleServicesConstants.TC.CHANGEPRODUCTPRICEBUNDLERULE,
	true,
	"bundleTemplate",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n DISABLEPRODUCTBUNDLERULES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<DisableProductBundleRule> DISABLEPRODUCTBUNDLERULESHANDLER = new OneToManyHandler<DisableProductBundleRule>(
	ConfigurableBundleServicesConstants.TC.DISABLEPRODUCTBUNDLERULE,
	true,
	"bundleTemplate",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n STATUS's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedBundleTemplate> STATUSHANDLER = new BidirectionalOneToManyHandler<GeneratedBundleTemplate>(
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
		tmp.put(VERSION, AttributeMode.INITIAL);
		tmp.put(CATALOGVERSION, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(BUNDLESELECTIONCRITERIA, AttributeMode.INITIAL);
		tmp.put(PARENTTEMPLATEPOS, AttributeMode.INITIAL);
		tmp.put(PARENTTEMPLATE, AttributeMode.INITIAL);
		tmp.put(STATUS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.bundleSelectionCriteria</code> attribute.
	 * @return the bundleSelectionCriteria - Criteria how many of the bundle's assigned products must/can be selected'
	 */
	public BundleSelectionCriteria getBundleSelectionCriteria(final SessionContext ctx)
	{
		return (BundleSelectionCriteria)getProperty( ctx, BUNDLESELECTIONCRITERIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.bundleSelectionCriteria</code> attribute.
	 * @return the bundleSelectionCriteria - Criteria how many of the bundle's assigned products must/can be selected'
	 */
	public BundleSelectionCriteria getBundleSelectionCriteria()
	{
		return getBundleSelectionCriteria( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.bundleSelectionCriteria</code> attribute. 
	 * @param value the bundleSelectionCriteria - Criteria how many of the bundle's assigned products must/can be selected'
	 */
	public void setBundleSelectionCriteria(final SessionContext ctx, final BundleSelectionCriteria value)
	{
		new PartOfHandler<BundleSelectionCriteria>()
		{
			@Override
			protected BundleSelectionCriteria doGetValue(final SessionContext ctx)
			{
				return getBundleSelectionCriteria( ctx );
			}
			@Override
			protected void doSetValue(final SessionContext ctx, final BundleSelectionCriteria _value)
			{
				final BundleSelectionCriteria value = _value;
				setProperty(ctx, BUNDLESELECTIONCRITERIA,value);
			}
		}.setValue( ctx, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.bundleSelectionCriteria</code> attribute. 
	 * @param value the bundleSelectionCriteria - Criteria how many of the bundle's assigned products must/can be selected'
	 */
	public void setBundleSelectionCriteria(final BundleSelectionCriteria value)
	{
		setBundleSelectionCriteria( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version
	 */
	public CatalogVersion getCatalogVersion(final SessionContext ctx)
	{
		return (CatalogVersion)getProperty( ctx, CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version
	 */
	public CatalogVersion getCatalogVersion()
	{
		return getCatalogVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.catalogVersion</code> attribute. 
	 * @param value the catalogVersion - Catalog Version
	 */
	public void setCatalogVersion(final SessionContext ctx, final CatalogVersion value)
	{
		setProperty(ctx, CATALOGVERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.catalogVersion</code> attribute. 
	 * @param value the catalogVersion - Catalog Version
	 */
	public void setCatalogVersion(final CatalogVersion value)
	{
		setCatalogVersion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.changeProductPriceBundleRules</code> attribute.
	 * @return the changeProductPriceBundleRules
	 */
	public Collection<ChangeProductPriceBundleRule> getChangeProductPriceBundleRules(final SessionContext ctx)
	{
		return CHANGEPRODUCTPRICEBUNDLERULESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.changeProductPriceBundleRules</code> attribute.
	 * @return the changeProductPriceBundleRules
	 */
	public Collection<ChangeProductPriceBundleRule> getChangeProductPriceBundleRules()
	{
		return getChangeProductPriceBundleRules( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.changeProductPriceBundleRules</code> attribute. 
	 * @param value the changeProductPriceBundleRules
	 */
	public void setChangeProductPriceBundleRules(final SessionContext ctx, final Collection<ChangeProductPriceBundleRule> value)
	{
		CHANGEPRODUCTPRICEBUNDLERULESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.changeProductPriceBundleRules</code> attribute. 
	 * @param value the changeProductPriceBundleRules
	 */
	public void setChangeProductPriceBundleRules(final Collection<ChangeProductPriceBundleRule> value)
	{
		setChangeProductPriceBundleRules( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to changeProductPriceBundleRules. 
	 * @param value the item to add to changeProductPriceBundleRules
	 */
	public void addToChangeProductPriceBundleRules(final SessionContext ctx, final ChangeProductPriceBundleRule value)
	{
		CHANGEPRODUCTPRICEBUNDLERULESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to changeProductPriceBundleRules. 
	 * @param value the item to add to changeProductPriceBundleRules
	 */
	public void addToChangeProductPriceBundleRules(final ChangeProductPriceBundleRule value)
	{
		addToChangeProductPriceBundleRules( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from changeProductPriceBundleRules. 
	 * @param value the item to remove from changeProductPriceBundleRules
	 */
	public void removeFromChangeProductPriceBundleRules(final SessionContext ctx, final ChangeProductPriceBundleRule value)
	{
		CHANGEPRODUCTPRICEBUNDLERULESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from changeProductPriceBundleRules. 
	 * @param value the item to remove from changeProductPriceBundleRules
	 */
	public void removeFromChangeProductPriceBundleRules(final ChangeProductPriceBundleRule value)
	{
		removeFromChangeProductPriceBundleRules( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.childTemplates</code> attribute.
	 * @return the childTemplates
	 */
	public List<BundleTemplate> getChildTemplates(final SessionContext ctx)
	{
		return (List<BundleTemplate>)CHILDTEMPLATESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.childTemplates</code> attribute.
	 * @return the childTemplates
	 */
	public List<BundleTemplate> getChildTemplates()
	{
		return getChildTemplates( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.childTemplates</code> attribute. 
	 * @param value the childTemplates
	 */
	public void setChildTemplates(final SessionContext ctx, final List<BundleTemplate> value)
	{
		CHILDTEMPLATESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.childTemplates</code> attribute. 
	 * @param value the childTemplates
	 */
	public void setChildTemplates(final List<BundleTemplate> value)
	{
		setChildTemplates( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to childTemplates. 
	 * @param value the item to add to childTemplates
	 */
	public void addToChildTemplates(final SessionContext ctx, final BundleTemplate value)
	{
		CHILDTEMPLATESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to childTemplates. 
	 * @param value the item to add to childTemplates
	 */
	public void addToChildTemplates(final BundleTemplate value)
	{
		addToChildTemplates( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from childTemplates. 
	 * @param value the item to remove from childTemplates
	 */
	public void removeFromChildTemplates(final SessionContext ctx, final BundleTemplate value)
	{
		CHILDTEMPLATESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from childTemplates. 
	 * @param value the item to remove from childTemplates
	 */
	public void removeFromChildTemplates(final BundleTemplate value)
	{
		removeFromChildTemplates( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		PARENTTEMPLATEHANDLER.newInstance(ctx, allAttributes);
		STATUSHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.dependentBundleTemplates</code> attribute.
	 * @return the dependentBundleTemplates
	 */
	public Collection<BundleTemplate> getDependentBundleTemplates(final SessionContext ctx)
	{
		final List<BundleTemplate> items = getLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION,
			"BundleTemplate",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.dependentBundleTemplates</code> attribute.
	 * @return the dependentBundleTemplates
	 */
	public Collection<BundleTemplate> getDependentBundleTemplates()
	{
		return getDependentBundleTemplates( getSession().getSessionContext() );
	}
	
	public long getDependentBundleTemplatesCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION,
			"BundleTemplate",
			null
		);
	}
	
	public long getDependentBundleTemplatesCount()
	{
		return getDependentBundleTemplatesCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.dependentBundleTemplates</code> attribute. 
	 * @param value the dependentBundleTemplates
	 */
	public void setDependentBundleTemplates(final SessionContext ctx, final Collection<BundleTemplate> value)
	{
		setLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.dependentBundleTemplates</code> attribute. 
	 * @param value the dependentBundleTemplates
	 */
	public void setDependentBundleTemplates(final Collection<BundleTemplate> value)
	{
		setDependentBundleTemplates( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to dependentBundleTemplates. 
	 * @param value the item to add to dependentBundleTemplates
	 */
	public void addToDependentBundleTemplates(final SessionContext ctx, final BundleTemplate value)
	{
		addLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to dependentBundleTemplates. 
	 * @param value the item to add to dependentBundleTemplates
	 */
	public void addToDependentBundleTemplates(final BundleTemplate value)
	{
		addToDependentBundleTemplates( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from dependentBundleTemplates. 
	 * @param value the item to remove from dependentBundleTemplates
	 */
	public void removeFromDependentBundleTemplates(final SessionContext ctx, final BundleTemplate value)
	{
		removeLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from dependentBundleTemplates. 
	 * @param value the item to remove from dependentBundleTemplates
	 */
	public void removeFromDependentBundleTemplates(final BundleTemplate value)
	{
		removeFromDependentBundleTemplates( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.description</code> attribute.
	 * @return the description - Description of the bundle template
	 */
	public String getDescription(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBundleTemplate.getDescription requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.description</code> attribute.
	 * @return the description - Description of the bundle template
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.description</code> attribute. 
	 * @return the localized description - Description of the bundle template
	 */
	public Map<Language,String> getAllDescription(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,DESCRIPTION,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.description</code> attribute. 
	 * @return the localized description - Description of the bundle template
	 */
	public Map<Language,String> getAllDescription()
	{
		return getAllDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.description</code> attribute. 
	 * @param value the description - Description of the bundle template
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBundleTemplate.setDescription requires a session language", 0 );
		}
		setLocalizedProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.description</code> attribute. 
	 * @param value the description - Description of the bundle template
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.description</code> attribute. 
	 * @param value the description - Description of the bundle template
	 */
	public void setAllDescription(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.description</code> attribute. 
	 * @param value the description - Description of the bundle template
	 */
	public void setAllDescription(final Map<Language,String> value)
	{
		setAllDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.disableProductBundleRules</code> attribute.
	 * @return the disableProductBundleRules
	 */
	public Collection<DisableProductBundleRule> getDisableProductBundleRules(final SessionContext ctx)
	{
		return DISABLEPRODUCTBUNDLERULESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.disableProductBundleRules</code> attribute.
	 * @return the disableProductBundleRules
	 */
	public Collection<DisableProductBundleRule> getDisableProductBundleRules()
	{
		return getDisableProductBundleRules( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.disableProductBundleRules</code> attribute. 
	 * @param value the disableProductBundleRules
	 */
	public void setDisableProductBundleRules(final SessionContext ctx, final Collection<DisableProductBundleRule> value)
	{
		DISABLEPRODUCTBUNDLERULESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.disableProductBundleRules</code> attribute. 
	 * @param value the disableProductBundleRules
	 */
	public void setDisableProductBundleRules(final Collection<DisableProductBundleRule> value)
	{
		setDisableProductBundleRules( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to disableProductBundleRules. 
	 * @param value the item to add to disableProductBundleRules
	 */
	public void addToDisableProductBundleRules(final SessionContext ctx, final DisableProductBundleRule value)
	{
		DISABLEPRODUCTBUNDLERULESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to disableProductBundleRules. 
	 * @param value the item to add to disableProductBundleRules
	 */
	public void addToDisableProductBundleRules(final DisableProductBundleRule value)
	{
		addToDisableProductBundleRules( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from disableProductBundleRules. 
	 * @param value the item to remove from disableProductBundleRules
	 */
	public void removeFromDisableProductBundleRules(final SessionContext ctx, final DisableProductBundleRule value)
	{
		DISABLEPRODUCTBUNDLERULESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from disableProductBundleRules. 
	 * @param value the item to remove from disableProductBundleRules
	 */
	public void removeFromDisableProductBundleRules(final DisableProductBundleRule value)
	{
		removeFromDisableProductBundleRules( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.id</code> attribute. 
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
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.id</code> attribute. 
	 * @param value the id - Identifier
	 */
	protected void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Product");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(PRODUCTSBUNDLETEMPLATESRELATION_MARKMODIFIED);
		}
		ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("BundleTemplate");
		if(relationSecondEnd1.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.name</code> attribute.
	 * @return the name - Name of the bundle template
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBundleTemplate.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.name</code> attribute.
	 * @return the name - Name of the bundle template
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.name</code> attribute. 
	 * @return the localized name - Name of the bundle template
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.name</code> attribute. 
	 * @return the localized name - Name of the bundle template
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.name</code> attribute. 
	 * @param value the name - Name of the bundle template
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBundleTemplate.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.name</code> attribute. 
	 * @param value the name - Name of the bundle template
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.name</code> attribute. 
	 * @param value the name - Name of the bundle template
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.name</code> attribute. 
	 * @param value the name - Name of the bundle template
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.parentTemplate</code> attribute.
	 * @return the parentTemplate
	 */
	public BundleTemplate getParentTemplate(final SessionContext ctx)
	{
		return (BundleTemplate)getProperty( ctx, PARENTTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.parentTemplate</code> attribute.
	 * @return the parentTemplate
	 */
	public BundleTemplate getParentTemplate()
	{
		return getParentTemplate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.parentTemplate</code> attribute. 
	 * @param value the parentTemplate
	 */
	protected void setParentTemplate(final SessionContext ctx, final BundleTemplate value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+PARENTTEMPLATE+"' is not changeable", 0 );
		}
		PARENTTEMPLATEHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.parentTemplate</code> attribute. 
	 * @param value the parentTemplate
	 */
	protected void setParentTemplate(final BundleTemplate value)
	{
		setParentTemplate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.parentTemplatePOS</code> attribute.
	 * @return the parentTemplatePOS
	 */
	 Integer getParentTemplatePOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, PARENTTEMPLATEPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.parentTemplatePOS</code> attribute.
	 * @return the parentTemplatePOS
	 */
	 Integer getParentTemplatePOS()
	{
		return getParentTemplatePOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.parentTemplatePOS</code> attribute. 
	 * @return the parentTemplatePOS
	 */
	 int getParentTemplatePOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getParentTemplatePOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.parentTemplatePOS</code> attribute. 
	 * @return the parentTemplatePOS
	 */
	 int getParentTemplatePOSAsPrimitive()
	{
		return getParentTemplatePOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.parentTemplatePOS</code> attribute. 
	 * @param value the parentTemplatePOS
	 */
	 void setParentTemplatePOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, PARENTTEMPLATEPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.parentTemplatePOS</code> attribute. 
	 * @param value the parentTemplatePOS
	 */
	 void setParentTemplatePOS(final Integer value)
	{
		setParentTemplatePOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.parentTemplatePOS</code> attribute. 
	 * @param value the parentTemplatePOS
	 */
	 void setParentTemplatePOS(final SessionContext ctx, final int value)
	{
		setParentTemplatePOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.parentTemplatePOS</code> attribute. 
	 * @param value the parentTemplatePOS
	 */
	 void setParentTemplatePOS(final int value)
	{
		setParentTemplatePOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.products</code> attribute.
	 * @return the products
	 */
	public List<Product> getProducts(final SessionContext ctx)
	{
		final List<Product> items = getLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.PRODUCTSBUNDLETEMPLATESRELATION,
			"Product",
			null,
			false,
			Utilities.getRelationOrderingOverride(PRODUCTSBUNDLETEMPLATESRELATION_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.products</code> attribute.
	 * @return the products
	 */
	public List<Product> getProducts()
	{
		return getProducts( getSession().getSessionContext() );
	}
	
	public long getProductsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.PRODUCTSBUNDLETEMPLATESRELATION,
			"Product",
			null
		);
	}
	
	public long getProductsCount()
	{
		return getProductsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.products</code> attribute. 
	 * @param value the products
	 */
	public void setProducts(final SessionContext ctx, final List<Product> value)
	{
		setLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.PRODUCTSBUNDLETEMPLATESRELATION,
			null,
			value,
			false,
			Utilities.getRelationOrderingOverride(PRODUCTSBUNDLETEMPLATESRELATION_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(PRODUCTSBUNDLETEMPLATESRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.products</code> attribute. 
	 * @param value the products
	 */
	public void setProducts(final List<Product> value)
	{
		setProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to products. 
	 * @param value the item to add to products
	 */
	public void addToProducts(final SessionContext ctx, final Product value)
	{
		addLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.PRODUCTSBUNDLETEMPLATESRELATION,
			null,
			Collections.singletonList(value),
			false,
			Utilities.getRelationOrderingOverride(PRODUCTSBUNDLETEMPLATESRELATION_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(PRODUCTSBUNDLETEMPLATESRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to products. 
	 * @param value the item to add to products
	 */
	public void addToProducts(final Product value)
	{
		addToProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from products. 
	 * @param value the item to remove from products
	 */
	public void removeFromProducts(final SessionContext ctx, final Product value)
	{
		removeLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.PRODUCTSBUNDLETEMPLATESRELATION,
			null,
			Collections.singletonList(value),
			false,
			Utilities.getRelationOrderingOverride(PRODUCTSBUNDLETEMPLATESRELATION_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(PRODUCTSBUNDLETEMPLATESRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from products. 
	 * @param value the item to remove from products
	 */
	public void removeFromProducts(final Product value)
	{
		removeFromProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.requiredBundleTemplates</code> attribute.
	 * @return the requiredBundleTemplates
	 */
	public Collection<BundleTemplate> getRequiredBundleTemplates(final SessionContext ctx)
	{
		final List<BundleTemplate> items = getLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION,
			"BundleTemplate",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.requiredBundleTemplates</code> attribute.
	 * @return the requiredBundleTemplates
	 */
	public Collection<BundleTemplate> getRequiredBundleTemplates()
	{
		return getRequiredBundleTemplates( getSession().getSessionContext() );
	}
	
	public long getRequiredBundleTemplatesCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION,
			"BundleTemplate",
			null
		);
	}
	
	public long getRequiredBundleTemplatesCount()
	{
		return getRequiredBundleTemplatesCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.requiredBundleTemplates</code> attribute. 
	 * @param value the requiredBundleTemplates
	 */
	public void setRequiredBundleTemplates(final SessionContext ctx, final Collection<BundleTemplate> value)
	{
		setLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.requiredBundleTemplates</code> attribute. 
	 * @param value the requiredBundleTemplates
	 */
	public void setRequiredBundleTemplates(final Collection<BundleTemplate> value)
	{
		setRequiredBundleTemplates( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to requiredBundleTemplates. 
	 * @param value the item to add to requiredBundleTemplates
	 */
	public void addToRequiredBundleTemplates(final SessionContext ctx, final BundleTemplate value)
	{
		addLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to requiredBundleTemplates. 
	 * @param value the item to add to requiredBundleTemplates
	 */
	public void addToRequiredBundleTemplates(final BundleTemplate value)
	{
		addToRequiredBundleTemplates( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from requiredBundleTemplates. 
	 * @param value the item to remove from requiredBundleTemplates
	 */
	public void removeFromRequiredBundleTemplates(final SessionContext ctx, final BundleTemplate value)
	{
		removeLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from requiredBundleTemplates. 
	 * @param value the item to remove from requiredBundleTemplates
	 */
	public void removeFromRequiredBundleTemplates(final BundleTemplate value)
	{
		removeFromRequiredBundleTemplates( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.status</code> attribute.
	 * @return the status
	 */
	public BundleTemplateStatus getStatus(final SessionContext ctx)
	{
		return (BundleTemplateStatus)getProperty( ctx, STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.status</code> attribute.
	 * @return the status
	 */
	public BundleTemplateStatus getStatus()
	{
		return getStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final SessionContext ctx, final BundleTemplateStatus value)
	{
		STATUSHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final BundleTemplateStatus value)
	{
		setStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.version</code> attribute.
	 * @return the version - The version of the bundle template. Each clone of a BundleTemplate needs to have a different version.
	 */
	public String getVersion(final SessionContext ctx)
	{
		return (String)getProperty( ctx, VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.version</code> attribute.
	 * @return the version - The version of the bundle template. Each clone of a BundleTemplate needs to have a different version.
	 */
	public String getVersion()
	{
		return getVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.version</code> attribute. 
	 * @param value the version - The version of the bundle template. Each clone of a BundleTemplate needs to have a different version.
	 */
	public void setVersion(final SessionContext ctx, final String value)
	{
		setProperty(ctx, VERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BundleTemplate.version</code> attribute. 
	 * @param value the version - The version of the bundle template. Each clone of a BundleTemplate needs to have a different version.
	 */
	public void setVersion(final String value)
	{
		setVersion( getSession().getSessionContext(), value );
	}
	
}
