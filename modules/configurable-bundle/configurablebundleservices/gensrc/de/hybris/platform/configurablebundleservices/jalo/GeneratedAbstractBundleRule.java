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
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.configurablebundleservices.jalo.AbstractBundleRule AbstractBundleRule}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedAbstractBundleRule extends GenericItem
{
	/** Qualifier of the <code>AbstractBundleRule.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>AbstractBundleRule.catalogVersion</code> attribute **/
	public static final String CATALOGVERSION = "catalogVersion";
	/** Qualifier of the <code>AbstractBundleRule.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>AbstractBundleRule.ruleType</code> attribute **/
	public static final String RULETYPE = "ruleType";
	/** Qualifier of the <code>AbstractBundleRule.conditionalProducts</code> attribute **/
	public static final String CONDITIONALPRODUCTS = "conditionalProducts";
	/** Relation ordering override parameter constants for AbstractBundleRulesConditionalProductsRelation from ((configurablebundleservices))*/
	protected static String ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_SRC_ORDERED = "relation.AbstractBundleRulesConditionalProductsRelation.source.ordered";
	protected static String ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_TGT_ORDERED = "relation.AbstractBundleRulesConditionalProductsRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for AbstractBundleRulesConditionalProductsRelation from ((configurablebundleservices))*/
	protected static String ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_MARKMODIFIED = "relation.AbstractBundleRulesConditionalProductsRelation.markmodified";
	/** Qualifier of the <code>AbstractBundleRule.targetProducts</code> attribute **/
	public static final String TARGETPRODUCTS = "targetProducts";
	/** Relation ordering override parameter constants for AbstractBundleRulesTargetProductsRelation from ((configurablebundleservices))*/
	protected static String ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_SRC_ORDERED = "relation.AbstractBundleRulesTargetProductsRelation.source.ordered";
	protected static String ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_TGT_ORDERED = "relation.AbstractBundleRulesTargetProductsRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for AbstractBundleRulesTargetProductsRelation from ((configurablebundleservices))*/
	protected static String ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_MARKMODIFIED = "relation.AbstractBundleRulesTargetProductsRelation.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(CATALOGVERSION, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(RULETYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version
	 */
	public CatalogVersion getCatalogVersion(final SessionContext ctx)
	{
		return (CatalogVersion)getProperty( ctx, CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version
	 */
	public CatalogVersion getCatalogVersion()
	{
		return getCatalogVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractBundleRule.catalogVersion</code> attribute. 
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
	 * <i>Generated method</i> - Setter of the <code>AbstractBundleRule.catalogVersion</code> attribute. 
	 * @param value the catalogVersion - Catalog Version
	 */
	protected void setCatalogVersion(final CatalogVersion value)
	{
		setCatalogVersion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.conditionalProducts</code> attribute.
	 * @return the conditionalProducts
	 */
	public Collection<Product> getConditionalProducts(final SessionContext ctx)
	{
		final List<Product> items = getLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION,
			"Product",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.conditionalProducts</code> attribute.
	 * @return the conditionalProducts
	 */
	public Collection<Product> getConditionalProducts()
	{
		return getConditionalProducts( getSession().getSessionContext() );
	}
	
	public long getConditionalProductsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION,
			"Product",
			null
		);
	}
	
	public long getConditionalProductsCount()
	{
		return getConditionalProductsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractBundleRule.conditionalProducts</code> attribute. 
	 * @param value the conditionalProducts
	 */
	public void setConditionalProducts(final SessionContext ctx, final Collection<Product> value)
	{
		setLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractBundleRule.conditionalProducts</code> attribute. 
	 * @param value the conditionalProducts
	 */
	public void setConditionalProducts(final Collection<Product> value)
	{
		setConditionalProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to conditionalProducts. 
	 * @param value the item to add to conditionalProducts
	 */
	public void addToConditionalProducts(final SessionContext ctx, final Product value)
	{
		addLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to conditionalProducts. 
	 * @param value the item to add to conditionalProducts
	 */
	public void addToConditionalProducts(final Product value)
	{
		addToConditionalProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from conditionalProducts. 
	 * @param value the item to remove from conditionalProducts
	 */
	public void removeFromConditionalProducts(final SessionContext ctx, final Product value)
	{
		removeLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from conditionalProducts. 
	 * @param value the item to remove from conditionalProducts
	 */
	public void removeFromConditionalProducts(final Product value)
	{
		removeFromConditionalProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractBundleRule.id</code> attribute. 
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
	 * <i>Generated method</i> - Setter of the <code>AbstractBundleRule.id</code> attribute. 
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
			return Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESCONDITIONALPRODUCTSRELATION_MARKMODIFIED);
		}
		ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Product");
		if(relationSecondEnd1.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.name</code> attribute.
	 * @return the name - Name of the bundle rule
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.name</code> attribute.
	 * @return the name - Name of the bundle rule
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractBundleRule.name</code> attribute. 
	 * @param value the name - Name of the bundle rule
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractBundleRule.name</code> attribute. 
	 * @param value the name - Name of the bundle rule
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.ruleType</code> attribute.
	 * @return the ruleType
	 */
	public EnumerationValue getRuleType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, RULETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.ruleType</code> attribute.
	 * @return the ruleType
	 */
	public EnumerationValue getRuleType()
	{
		return getRuleType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractBundleRule.ruleType</code> attribute. 
	 * @param value the ruleType
	 */
	public void setRuleType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, RULETYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractBundleRule.ruleType</code> attribute. 
	 * @param value the ruleType
	 */
	public void setRuleType(final EnumerationValue value)
	{
		setRuleType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.targetProducts</code> attribute.
	 * @return the targetProducts
	 */
	public Collection<Product> getTargetProducts(final SessionContext ctx)
	{
		final List<Product> items = getLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION,
			"Product",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.targetProducts</code> attribute.
	 * @return the targetProducts
	 */
	public Collection<Product> getTargetProducts()
	{
		return getTargetProducts( getSession().getSessionContext() );
	}
	
	public long getTargetProductsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION,
			"Product",
			null
		);
	}
	
	public long getTargetProductsCount()
	{
		return getTargetProductsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractBundleRule.targetProducts</code> attribute. 
	 * @param value the targetProducts
	 */
	public void setTargetProducts(final SessionContext ctx, final Collection<Product> value)
	{
		setLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractBundleRule.targetProducts</code> attribute. 
	 * @param value the targetProducts
	 */
	public void setTargetProducts(final Collection<Product> value)
	{
		setTargetProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to targetProducts. 
	 * @param value the item to add to targetProducts
	 */
	public void addToTargetProducts(final SessionContext ctx, final Product value)
	{
		addLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to targetProducts. 
	 * @param value the item to add to targetProducts
	 */
	public void addToTargetProducts(final Product value)
	{
		addToTargetProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from targetProducts. 
	 * @param value the item to remove from targetProducts
	 */
	public void removeFromTargetProducts(final SessionContext ctx, final Product value)
	{
		removeLinkedItems( 
			ctx,
			true,
			ConfigurableBundleServicesConstants.Relations.ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ABSTRACTBUNDLERULESTARGETPRODUCTSRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from targetProducts. 
	 * @param value the item to remove from targetProducts
	 */
	public void removeFromTargetProducts(final Product value)
	{
		removeFromTargetProducts( getSession().getSessionContext(), value );
	}
	
}
