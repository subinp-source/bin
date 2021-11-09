/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.jalo;

import de.hybris.platform.configurablebundleservices.constants.ConfigurableBundleServicesConstants;
import de.hybris.platform.configurablebundleservices.jalo.AbstractBundleRule;
import de.hybris.platform.configurablebundleservices.jalo.AbstractBundleRuleTargetProductsAssignedConstraint;
import de.hybris.platform.configurablebundleservices.jalo.BundleTemplate;
import de.hybris.platform.configurablebundleservices.jalo.BundleTemplateProductsAssignedConstraint;
import de.hybris.platform.configurablebundleservices.jalo.BundleTemplateStatus;
import de.hybris.platform.configurablebundleservices.jalo.ChangeProductPriceBundleRule;
import de.hybris.platform.configurablebundleservices.jalo.DisableProductBundleRule;
import de.hybris.platform.configurablebundleservices.jalo.PickExactlyNBundleSelectionCriteria;
import de.hybris.platform.configurablebundleservices.jalo.PickNToMBundleSelectionCriteria;
import de.hybris.platform.configurablebundleservices.jalo.components.BundleCarouselComponent;
import de.hybris.platform.constants.CoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type <code>ConfigurableBundleServicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedConfigurableBundleServicesManager extends Extension
{
	/** Relation ordering override parameter constants for AbstractBundleRulesConditionalProductsRelation from ((configurablebundleservices))*/
	protected static String ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_SRC_ORDERED = "relation.AbstractBundleRulesConditionalProductsRelation.source.ordered";
	protected static String ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_TGT_ORDERED = "relation.AbstractBundleRulesConditionalProductsRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for AbstractBundleRulesConditionalProductsRelation from ((configurablebundleservices))*/
	protected static String ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_MARKMODIFIED = "relation.AbstractBundleRulesConditionalProductsRelation.markmodified";
	/** Relation ordering override parameter constants for AbstractBundleRulesTargetProductsRelation from ((configurablebundleservices))*/
	protected static String ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_SRC_ORDERED = "relation.AbstractBundleRulesTargetProductsRelation.source.ordered";
	protected static String ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_TGT_ORDERED = "relation.AbstractBundleRulesTargetProductsRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for AbstractBundleRulesTargetProductsRelation from ((configurablebundleservices))*/
	protected static String ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_MARKMODIFIED = "relation.AbstractBundleRulesTargetProductsRelation.markmodified";
	/** Relation ordering override parameter constants for ProductsBundleTemplatesRelation from ((configurablebundleservices))*/
	protected static String PRODUCTSBUNDLETEMPLATESRELATION_SRC_ORDERED = "relation.ProductsBundleTemplatesRelation.source.ordered";
	protected static String PRODUCTSBUNDLETEMPLATESRELATION_TGT_ORDERED = "relation.ProductsBundleTemplatesRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for ProductsBundleTemplatesRelation from ((configurablebundleservices))*/
	protected static String PRODUCTSBUNDLETEMPLATESRELATION_MARKMODIFIED = "relation.ProductsBundleTemplatesRelation.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n LASTMODIFIEDENTRIES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<CartEntry> LASTMODIFIEDENTRIESRELATIONLASTMODIFIEDENTRIESHANDLER = new OneToManyHandler<CartEntry>(
	CoreConstants.TC.CARTENTRY,
	false,
	"lastModifiedMasterCart",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("soldIndividually", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.product.Product", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("lastModifiedMasterCart", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.CartEntry", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.bundleTemplates</code> attribute.
	 * @return the bundleTemplates
	 */
	public Collection<BundleTemplate> getBundleTemplates(final SessionContext ctx, final Product item)
	{
		final List<BundleTemplate> items = item.getLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.PRODUCTSBUNDLETEMPLATESRELATION,
			"BundleTemplate",
			null,
			false,
			Utilities.getRelationOrderingOverride(PRODUCTSBUNDLETEMPLATESRELATION_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.bundleTemplates</code> attribute.
	 * @return the bundleTemplates
	 */
	public Collection<BundleTemplate> getBundleTemplates(final Product item)
	{
		return getBundleTemplates( getSession().getSessionContext(), item );
	}
	
	public long getBundleTemplatesCount(final SessionContext ctx, final Product item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.PRODUCTSBUNDLETEMPLATESRELATION,
			"BundleTemplate",
			null
		);
	}
	
	public long getBundleTemplatesCount(final Product item)
	{
		return getBundleTemplatesCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.bundleTemplates</code> attribute. 
	 * @param value the bundleTemplates
	 */
	public void setBundleTemplates(final SessionContext ctx, final Product item, final Collection<BundleTemplate> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.PRODUCTSBUNDLETEMPLATESRELATION,
			null,
			value,
			false,
			Utilities.getRelationOrderingOverride(PRODUCTSBUNDLETEMPLATESRELATION_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(PRODUCTSBUNDLETEMPLATESRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.bundleTemplates</code> attribute. 
	 * @param value the bundleTemplates
	 */
	public void setBundleTemplates(final Product item, final Collection<BundleTemplate> value)
	{
		setBundleTemplates( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to bundleTemplates. 
	 * @param value the item to add to bundleTemplates
	 */
	public void addToBundleTemplates(final SessionContext ctx, final Product item, final BundleTemplate value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.PRODUCTSBUNDLETEMPLATESRELATION,
			null,
			Collections.singletonList(value),
			false,
			Utilities.getRelationOrderingOverride(PRODUCTSBUNDLETEMPLATESRELATION_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(PRODUCTSBUNDLETEMPLATESRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to bundleTemplates. 
	 * @param value the item to add to bundleTemplates
	 */
	public void addToBundleTemplates(final Product item, final BundleTemplate value)
	{
		addToBundleTemplates( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from bundleTemplates. 
	 * @param value the item to remove from bundleTemplates
	 */
	public void removeFromBundleTemplates(final SessionContext ctx, final Product item, final BundleTemplate value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.PRODUCTSBUNDLETEMPLATESRELATION,
			null,
			Collections.singletonList(value),
			false,
			Utilities.getRelationOrderingOverride(PRODUCTSBUNDLETEMPLATESRELATION_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(PRODUCTSBUNDLETEMPLATESRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from bundleTemplates. 
	 * @param value the item to remove from bundleTemplates
	 */
	public void removeFromBundleTemplates(final Product item, final BundleTemplate value)
	{
		removeFromBundleTemplates( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.conditionalBundleRules</code> attribute.
	 * @return the conditionalBundleRules
	 */
	public Collection<AbstractBundleRule> getConditionalBundleRules(final SessionContext ctx, final Product item)
	{
		final List<AbstractBundleRule> items = item.getLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION,
			"AbstractBundleRule",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.conditionalBundleRules</code> attribute.
	 * @return the conditionalBundleRules
	 */
	public Collection<AbstractBundleRule> getConditionalBundleRules(final Product item)
	{
		return getConditionalBundleRules( getSession().getSessionContext(), item );
	}
	
	public long getConditionalBundleRulesCount(final SessionContext ctx, final Product item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION,
			"AbstractBundleRule",
			null
		);
	}
	
	public long getConditionalBundleRulesCount(final Product item)
	{
		return getConditionalBundleRulesCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.conditionalBundleRules</code> attribute. 
	 * @param value the conditionalBundleRules
	 */
	public void setConditionalBundleRules(final SessionContext ctx, final Product item, final Collection<AbstractBundleRule> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.conditionalBundleRules</code> attribute. 
	 * @param value the conditionalBundleRules
	 */
	public void setConditionalBundleRules(final Product item, final Collection<AbstractBundleRule> value)
	{
		setConditionalBundleRules( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to conditionalBundleRules. 
	 * @param value the item to add to conditionalBundleRules
	 */
	public void addToConditionalBundleRules(final SessionContext ctx, final Product item, final AbstractBundleRule value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to conditionalBundleRules. 
	 * @param value the item to add to conditionalBundleRules
	 */
	public void addToConditionalBundleRules(final Product item, final AbstractBundleRule value)
	{
		addToConditionalBundleRules( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from conditionalBundleRules. 
	 * @param value the item to remove from conditionalBundleRules
	 */
	public void removeFromConditionalBundleRules(final SessionContext ctx, final Product item, final AbstractBundleRule value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from conditionalBundleRules. 
	 * @param value the item to remove from conditionalBundleRules
	 */
	public void removeFromConditionalBundleRules(final Product item, final AbstractBundleRule value)
	{
		removeFromConditionalBundleRules( getSession().getSessionContext(), item, value );
	}
	
	public AbstractBundleRuleTargetProductsAssignedConstraint createAbstractBundleRuleTargetProductsAssignedConstraint(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ConfigurableBundleServicesConstants.TC.ABSTRACTBUNDLERULETARGETPRODUCTSASSIGNEDCONSTRAINT );
			return (AbstractBundleRuleTargetProductsAssignedConstraint)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating AbstractBundleRuleTargetProductsAssignedConstraint : "+e.getMessage(), 0 );
		}
	}
	
	public AbstractBundleRuleTargetProductsAssignedConstraint createAbstractBundleRuleTargetProductsAssignedConstraint(final Map attributeValues)
	{
		return createAbstractBundleRuleTargetProductsAssignedConstraint( getSession().getSessionContext(), attributeValues );
	}
	
	public BundleCarouselComponent createBundleCarouselComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ConfigurableBundleServicesConstants.TC.BUNDLECAROUSELCOMPONENT );
			return (BundleCarouselComponent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating BundleCarouselComponent : "+e.getMessage(), 0 );
		}
	}
	
	public BundleCarouselComponent createBundleCarouselComponent(final Map attributeValues)
	{
		return createBundleCarouselComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public BundleTemplate createBundleTemplate(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ConfigurableBundleServicesConstants.TC.BUNDLETEMPLATE );
			return (BundleTemplate)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating BundleTemplate : "+e.getMessage(), 0 );
		}
	}
	
	public BundleTemplate createBundleTemplate(final Map attributeValues)
	{
		return createBundleTemplate( getSession().getSessionContext(), attributeValues );
	}
	
	public BundleTemplateProductsAssignedConstraint createBundleTemplateProductsAssignedConstraint(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ConfigurableBundleServicesConstants.TC.BUNDLETEMPLATEPRODUCTSASSIGNEDCONSTRAINT );
			return (BundleTemplateProductsAssignedConstraint)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating BundleTemplateProductsAssignedConstraint : "+e.getMessage(), 0 );
		}
	}
	
	public BundleTemplateProductsAssignedConstraint createBundleTemplateProductsAssignedConstraint(final Map attributeValues)
	{
		return createBundleTemplateProductsAssignedConstraint( getSession().getSessionContext(), attributeValues );
	}
	
	public BundleTemplateStatus createBundleTemplateStatus(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ConfigurableBundleServicesConstants.TC.BUNDLETEMPLATESTATUS );
			return (BundleTemplateStatus)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating BundleTemplateStatus : "+e.getMessage(), 0 );
		}
	}
	
	public BundleTemplateStatus createBundleTemplateStatus(final Map attributeValues)
	{
		return createBundleTemplateStatus( getSession().getSessionContext(), attributeValues );
	}
	
	public ChangeProductPriceBundleRule createChangeProductPriceBundleRule(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ConfigurableBundleServicesConstants.TC.CHANGEPRODUCTPRICEBUNDLERULE );
			return (ChangeProductPriceBundleRule)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ChangeProductPriceBundleRule : "+e.getMessage(), 0 );
		}
	}
	
	public ChangeProductPriceBundleRule createChangeProductPriceBundleRule(final Map attributeValues)
	{
		return createChangeProductPriceBundleRule( getSession().getSessionContext(), attributeValues );
	}
	
	public DisableProductBundleRule createDisableProductBundleRule(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ConfigurableBundleServicesConstants.TC.DISABLEPRODUCTBUNDLERULE );
			return (DisableProductBundleRule)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating DisableProductBundleRule : "+e.getMessage(), 0 );
		}
	}
	
	public DisableProductBundleRule createDisableProductBundleRule(final Map attributeValues)
	{
		return createDisableProductBundleRule( getSession().getSessionContext(), attributeValues );
	}
	
	public PickExactlyNBundleSelectionCriteria createPickExactlyNBundleSelectionCriteria(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ConfigurableBundleServicesConstants.TC.PICKEXACTLYNBUNDLESELECTIONCRITERIA );
			return (PickExactlyNBundleSelectionCriteria)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating PickExactlyNBundleSelectionCriteria : "+e.getMessage(), 0 );
		}
	}
	
	public PickExactlyNBundleSelectionCriteria createPickExactlyNBundleSelectionCriteria(final Map attributeValues)
	{
		return createPickExactlyNBundleSelectionCriteria( getSession().getSessionContext(), attributeValues );
	}
	
	public PickNToMBundleSelectionCriteria createPickNToMBundleSelectionCriteria(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ConfigurableBundleServicesConstants.TC.PICKNTOMBUNDLESELECTIONCRITERIA );
			return (PickNToMBundleSelectionCriteria)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating PickNToMBundleSelectionCriteria : "+e.getMessage(), 0 );
		}
	}
	
	public PickNToMBundleSelectionCriteria createPickNToMBundleSelectionCriteria(final Map attributeValues)
	{
		return createPickNToMBundleSelectionCriteria( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return ConfigurableBundleServicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.lastModifiedEntries</code> attribute.
	 * @return the lastModifiedEntries
	 */
	public Collection<CartEntry> getLastModifiedEntries(final SessionContext ctx, final Cart item)
	{
		return LASTMODIFIEDENTRIESRELATIONLASTMODIFIEDENTRIESHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.lastModifiedEntries</code> attribute.
	 * @return the lastModifiedEntries
	 */
	public Collection<CartEntry> getLastModifiedEntries(final Cart item)
	{
		return getLastModifiedEntries( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.lastModifiedEntries</code> attribute. 
	 * @param value the lastModifiedEntries
	 */
	public void setLastModifiedEntries(final SessionContext ctx, final Cart item, final Collection<CartEntry> value)
	{
		LASTMODIFIEDENTRIESRELATIONLASTMODIFIEDENTRIESHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.lastModifiedEntries</code> attribute. 
	 * @param value the lastModifiedEntries
	 */
	public void setLastModifiedEntries(final Cart item, final Collection<CartEntry> value)
	{
		setLastModifiedEntries( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to lastModifiedEntries. 
	 * @param value the item to add to lastModifiedEntries
	 */
	public void addToLastModifiedEntries(final SessionContext ctx, final Cart item, final CartEntry value)
	{
		LASTMODIFIEDENTRIESRELATIONLASTMODIFIEDENTRIESHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to lastModifiedEntries. 
	 * @param value the item to add to lastModifiedEntries
	 */
	public void addToLastModifiedEntries(final Cart item, final CartEntry value)
	{
		addToLastModifiedEntries( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from lastModifiedEntries. 
	 * @param value the item to remove from lastModifiedEntries
	 */
	public void removeFromLastModifiedEntries(final SessionContext ctx, final Cart item, final CartEntry value)
	{
		LASTMODIFIEDENTRIESRELATIONLASTMODIFIEDENTRIESHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from lastModifiedEntries. 
	 * @param value the item to remove from lastModifiedEntries
	 */
	public void removeFromLastModifiedEntries(final Cart item, final CartEntry value)
	{
		removeFromLastModifiedEntries( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CartEntry.lastModifiedMasterCart</code> attribute.
	 * @return the lastModifiedMasterCart
	 */
	public Cart getLastModifiedMasterCart(final SessionContext ctx, final CartEntry item)
	{
		return (Cart)item.getProperty( ctx, ConfigurableBundleServicesConstants.Attributes.CartEntry.LASTMODIFIEDMASTERCART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CartEntry.lastModifiedMasterCart</code> attribute.
	 * @return the lastModifiedMasterCart
	 */
	public Cart getLastModifiedMasterCart(final CartEntry item)
	{
		return getLastModifiedMasterCart( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CartEntry.lastModifiedMasterCart</code> attribute. 
	 * @param value the lastModifiedMasterCart
	 */
	public void setLastModifiedMasterCart(final SessionContext ctx, final CartEntry item, final Cart value)
	{
		item.setProperty(ctx, ConfigurableBundleServicesConstants.Attributes.CartEntry.LASTMODIFIEDMASTERCART,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CartEntry.lastModifiedMasterCart</code> attribute. 
	 * @param value the lastModifiedMasterCart
	 */
	public void setLastModifiedMasterCart(final CartEntry item, final Cart value)
	{
		setLastModifiedMasterCart( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.soldIndividually</code> attribute.
	 * @return the soldIndividually
	 */
	public Boolean isSoldIndividually(final SessionContext ctx, final Product item)
	{
		return (Boolean)item.getProperty( ctx, ConfigurableBundleServicesConstants.Attributes.Product.SOLDINDIVIDUALLY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.soldIndividually</code> attribute.
	 * @return the soldIndividually
	 */
	public Boolean isSoldIndividually(final Product item)
	{
		return isSoldIndividually( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.soldIndividually</code> attribute. 
	 * @return the soldIndividually
	 */
	public boolean isSoldIndividuallyAsPrimitive(final SessionContext ctx, final Product item)
	{
		Boolean value = isSoldIndividually( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.soldIndividually</code> attribute. 
	 * @return the soldIndividually
	 */
	public boolean isSoldIndividuallyAsPrimitive(final Product item)
	{
		return isSoldIndividuallyAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.soldIndividually</code> attribute. 
	 * @param value the soldIndividually
	 */
	public void setSoldIndividually(final SessionContext ctx, final Product item, final Boolean value)
	{
		item.setProperty(ctx, ConfigurableBundleServicesConstants.Attributes.Product.SOLDINDIVIDUALLY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.soldIndividually</code> attribute. 
	 * @param value the soldIndividually
	 */
	public void setSoldIndividually(final Product item, final Boolean value)
	{
		setSoldIndividually( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.soldIndividually</code> attribute. 
	 * @param value the soldIndividually
	 */
	public void setSoldIndividually(final SessionContext ctx, final Product item, final boolean value)
	{
		setSoldIndividually( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.soldIndividually</code> attribute. 
	 * @param value the soldIndividually
	 */
	public void setSoldIndividually(final Product item, final boolean value)
	{
		setSoldIndividually( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.targetBundleRules</code> attribute.
	 * @return the targetBundleRules
	 */
	public Collection<AbstractBundleRule> getTargetBundleRules(final SessionContext ctx, final Product item)
	{
		final List<AbstractBundleRule> items = item.getLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION,
			"AbstractBundleRule",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.targetBundleRules</code> attribute.
	 * @return the targetBundleRules
	 */
	public Collection<AbstractBundleRule> getTargetBundleRules(final Product item)
	{
		return getTargetBundleRules( getSession().getSessionContext(), item );
	}
	
	public long getTargetBundleRulesCount(final SessionContext ctx, final Product item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION,
			"AbstractBundleRule",
			null
		);
	}
	
	public long getTargetBundleRulesCount(final Product item)
	{
		return getTargetBundleRulesCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.targetBundleRules</code> attribute. 
	 * @param value the targetBundleRules
	 */
	public void setTargetBundleRules(final SessionContext ctx, final Product item, final Collection<AbstractBundleRule> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.targetBundleRules</code> attribute. 
	 * @param value the targetBundleRules
	 */
	public void setTargetBundleRules(final Product item, final Collection<AbstractBundleRule> value)
	{
		setTargetBundleRules( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to targetBundleRules. 
	 * @param value the item to add to targetBundleRules
	 */
	public void addToTargetBundleRules(final SessionContext ctx, final Product item, final AbstractBundleRule value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to targetBundleRules. 
	 * @param value the item to add to targetBundleRules
	 */
	public void addToTargetBundleRules(final Product item, final AbstractBundleRule value)
	{
		addToTargetBundleRules( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from targetBundleRules. 
	 * @param value the item to remove from targetBundleRules
	 */
	public void removeFromTargetBundleRules(final SessionContext ctx, final Product item, final AbstractBundleRule value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from targetBundleRules. 
	 * @param value the item to remove from targetBundleRules
	 */
	public void removeFromTargetBundleRules(final Product item, final AbstractBundleRule value)
	{
		removeFromTargetBundleRules( getSession().getSessionContext(), item, value );
	}
	
}
