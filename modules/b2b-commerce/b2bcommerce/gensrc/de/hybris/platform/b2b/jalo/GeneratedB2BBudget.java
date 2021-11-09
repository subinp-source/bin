/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.jalo;

import de.hybris.platform.b2b.constants.B2BCommerceConstants;
import de.hybris.platform.b2b.jalo.B2BCostCenter;
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.util.Utilities;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BBudget B2BBudget}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BBudget extends GenericItem
{
	/** Qualifier of the <code>B2BBudget.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>B2BBudget.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>B2BBudget.budget</code> attribute **/
	public static final String BUDGET = "budget";
	/** Qualifier of the <code>B2BBudget.currency</code> attribute **/
	public static final String CURRENCY = "currency";
	/** Qualifier of the <code>B2BBudget.dateRange</code> attribute **/
	public static final String DATERANGE = "dateRange";
	/** Qualifier of the <code>B2BBudget.active</code> attribute **/
	public static final String ACTIVE = "active";
	/** Qualifier of the <code>B2BBudget.Unit</code> attribute **/
	public static final String UNIT = "Unit";
	/** Qualifier of the <code>B2BBudget.CostCenters</code> attribute **/
	public static final String COSTCENTERS = "CostCenters";
	/** Relation ordering override parameter constants for B2BBudgets2CostCenters from ((b2bcommerce))*/
	protected static String B2BBUDGETS2COSTCENTERS_SRC_ORDERED = "relation.B2BBudgets2CostCenters.source.ordered";
	protected static String B2BBUDGETS2COSTCENTERS_TGT_ORDERED = "relation.B2BBudgets2CostCenters.target.ordered";
	/** Relation disable markmodifed parameter constants for B2BBudgets2CostCenters from ((b2bcommerce))*/
	protected static String B2BBUDGETS2COSTCENTERS_MARKMODIFIED = "relation.B2BBudgets2CostCenters.markmodified";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n UNIT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BBudget> UNITHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BBudget>(
	B2BCommerceConstants.TC.B2BBUDGET,
	false,
	"Unit",
	null,
	false,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(BUDGET, AttributeMode.INITIAL);
		tmp.put(CURRENCY, AttributeMode.INITIAL);
		tmp.put(DATERANGE, AttributeMode.INITIAL);
		tmp.put(ACTIVE, AttributeMode.INITIAL);
		tmp.put(UNIT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.active</code> attribute.
	 * @return the active
	 */
	public Boolean isActive(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.active</code> attribute.
	 * @return the active
	 */
	public Boolean isActive()
	{
		return isActive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.active</code> attribute. 
	 * @return the active
	 */
	public boolean isActiveAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isActive( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.active</code> attribute. 
	 * @return the active
	 */
	public boolean isActiveAsPrimitive()
	{
		return isActiveAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ACTIVE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final Boolean value)
	{
		setActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final SessionContext ctx, final boolean value)
	{
		setActive( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.active</code> attribute. 
	 * @param value the active
	 */
	public void setActive(final boolean value)
	{
		setActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.budget</code> attribute.
	 * @return the budget
	 */
	public BigDecimal getBudget(final SessionContext ctx)
	{
		return (BigDecimal)getProperty( ctx, BUDGET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.budget</code> attribute.
	 * @return the budget
	 */
	public BigDecimal getBudget()
	{
		return getBudget( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.budget</code> attribute. 
	 * @param value the budget
	 */
	public void setBudget(final SessionContext ctx, final BigDecimal value)
	{
		setProperty(ctx, BUDGET,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.budget</code> attribute. 
	 * @param value the budget
	 */
	public void setBudget(final BigDecimal value)
	{
		setBudget( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.CostCenters</code> attribute.
	 * @return the CostCenters
	 */
	public Set<B2BCostCenter> getCostCenters(final SessionContext ctx)
	{
		final List<B2BCostCenter> items = getLinkedItems( 
			ctx,
			true,
			B2BCommerceConstants.Relations.B2BBUDGETS2COSTCENTERS,
			"B2BCostCenter",
			null,
			false,
			false
		);
		return new LinkedHashSet<B2BCostCenter>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.CostCenters</code> attribute.
	 * @return the CostCenters
	 */
	public Set<B2BCostCenter> getCostCenters()
	{
		return getCostCenters( getSession().getSessionContext() );
	}
	
	public long getCostCentersCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			B2BCommerceConstants.Relations.B2BBUDGETS2COSTCENTERS,
			"B2BCostCenter",
			null
		);
	}
	
	public long getCostCentersCount()
	{
		return getCostCentersCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.CostCenters</code> attribute. 
	 * @param value the CostCenters
	 */
	public void setCostCenters(final SessionContext ctx, final Set<B2BCostCenter> value)
	{
		setLinkedItems( 
			ctx,
			true,
			B2BCommerceConstants.Relations.B2BBUDGETS2COSTCENTERS,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(B2BBUDGETS2COSTCENTERS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.CostCenters</code> attribute. 
	 * @param value the CostCenters
	 */
	public void setCostCenters(final Set<B2BCostCenter> value)
	{
		setCostCenters( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to CostCenters. 
	 * @param value the item to add to CostCenters
	 */
	public void addToCostCenters(final SessionContext ctx, final B2BCostCenter value)
	{
		addLinkedItems( 
			ctx,
			true,
			B2BCommerceConstants.Relations.B2BBUDGETS2COSTCENTERS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(B2BBUDGETS2COSTCENTERS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to CostCenters. 
	 * @param value the item to add to CostCenters
	 */
	public void addToCostCenters(final B2BCostCenter value)
	{
		addToCostCenters( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from CostCenters. 
	 * @param value the item to remove from CostCenters
	 */
	public void removeFromCostCenters(final SessionContext ctx, final B2BCostCenter value)
	{
		removeLinkedItems( 
			ctx,
			true,
			B2BCommerceConstants.Relations.B2BBUDGETS2COSTCENTERS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(B2BBUDGETS2COSTCENTERS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from CostCenters. 
	 * @param value the item to remove from CostCenters
	 */
	public void removeFromCostCenters(final B2BCostCenter value)
	{
		removeFromCostCenters( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		UNITHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency(final SessionContext ctx)
	{
		return (Currency)getProperty( ctx, CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency()
	{
		return getCurrency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final SessionContext ctx, final Currency value)
	{
		setProperty(ctx, CURRENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final Currency value)
	{
		setCurrency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.dateRange</code> attribute.
	 * @return the dateRange - date range the budget is active
	 */
	public StandardDateRange getDateRange(final SessionContext ctx)
	{
		return (StandardDateRange)getProperty( ctx, DATERANGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.dateRange</code> attribute.
	 * @return the dateRange - date range the budget is active
	 */
	public StandardDateRange getDateRange()
	{
		return getDateRange( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.dateRange</code> attribute. 
	 * @param value the dateRange - date range the budget is active
	 */
	public void setDateRange(final SessionContext ctx, final StandardDateRange value)
	{
		setProperty(ctx, DATERANGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.dateRange</code> attribute. 
	 * @param value the dateRange - date range the budget is active
	 */
	public void setDateRange(final StandardDateRange value)
	{
		setDateRange( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("B2BCostCenter");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(B2BBUDGETS2COSTCENTERS_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedB2BBudget.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.name</code> attribute. 
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
			throw new JaloInvalidParameterException("GeneratedB2BBudget.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.Unit</code> attribute.
	 * @return the Unit
	 */
	public B2BUnit getUnit(final SessionContext ctx)
	{
		return (B2BUnit)getProperty( ctx, UNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BBudget.Unit</code> attribute.
	 * @return the Unit
	 */
	public B2BUnit getUnit()
	{
		return getUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.Unit</code> attribute. 
	 * @param value the Unit
	 */
	public void setUnit(final SessionContext ctx, final B2BUnit value)
	{
		UNITHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BBudget.Unit</code> attribute. 
	 * @param value the Unit
	 */
	public void setUnit(final B2BUnit value)
	{
		setUnit( getSession().getSessionContext(), value );
	}
	
}
