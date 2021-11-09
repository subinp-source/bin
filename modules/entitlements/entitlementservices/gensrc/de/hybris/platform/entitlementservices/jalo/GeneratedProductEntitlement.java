/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.entitlementservices.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.entitlementservices.constants.EntitlementservicesConstants;
import de.hybris.platform.entitlementservices.jalo.Entitlement;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.entitlementservices.jalo.ProductEntitlement ProductEntitlement}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedProductEntitlement extends GenericItem
{
	/** Qualifier of the <code>ProductEntitlement.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>ProductEntitlement.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>ProductEntitlement.catalogVersion</code> attribute **/
	public static final String CATALOGVERSION = "catalogVersion";
	/** Qualifier of the <code>ProductEntitlement.timeUnit</code> attribute **/
	public static final String TIMEUNIT = "timeUnit";
	/** Qualifier of the <code>ProductEntitlement.timeUnitStart</code> attribute **/
	public static final String TIMEUNITSTART = "timeUnitStart";
	/** Qualifier of the <code>ProductEntitlement.timeUnitDuration</code> attribute **/
	public static final String TIMEUNITDURATION = "timeUnitDuration";
	/** Qualifier of the <code>ProductEntitlement.conditionString</code> attribute **/
	public static final String CONDITIONSTRING = "conditionString";
	/** Qualifier of the <code>ProductEntitlement.conditionPath</code> attribute **/
	public static final String CONDITIONPATH = "conditionPath";
	/** Qualifier of the <code>ProductEntitlement.conditionGeo</code> attribute **/
	public static final String CONDITIONGEO = "conditionGeo";
	/** Qualifier of the <code>ProductEntitlement.quantity</code> attribute **/
	public static final String QUANTITY = "quantity";
	/** Qualifier of the <code>ProductEntitlement.subscriptionProduct</code> attribute **/
	public static final String SUBSCRIPTIONPRODUCT = "subscriptionProduct";
	/** Qualifier of the <code>ProductEntitlement.entitlement</code> attribute **/
	public static final String ENTITLEMENT = "entitlement";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n SUBSCRIPTIONPRODUCT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedProductEntitlement> SUBSCRIPTIONPRODUCTHANDLER = new BidirectionalOneToManyHandler<GeneratedProductEntitlement>(
	EntitlementservicesConstants.TC.PRODUCTENTITLEMENT,
	false,
	"subscriptionProduct",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n ENTITLEMENT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedProductEntitlement> ENTITLEMENTHANDLER = new BidirectionalOneToManyHandler<GeneratedProductEntitlement>(
	EntitlementservicesConstants.TC.PRODUCTENTITLEMENT,
	false,
	"entitlement",
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
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(CATALOGVERSION, AttributeMode.INITIAL);
		tmp.put(TIMEUNIT, AttributeMode.INITIAL);
		tmp.put(TIMEUNITSTART, AttributeMode.INITIAL);
		tmp.put(TIMEUNITDURATION, AttributeMode.INITIAL);
		tmp.put(CONDITIONSTRING, AttributeMode.INITIAL);
		tmp.put(CONDITIONPATH, AttributeMode.INITIAL);
		tmp.put(CONDITIONGEO, AttributeMode.INITIAL);
		tmp.put(QUANTITY, AttributeMode.INITIAL);
		tmp.put(SUBSCRIPTIONPRODUCT, AttributeMode.INITIAL);
		tmp.put(ENTITLEMENT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version
	 */
	public CatalogVersion getCatalogVersion(final SessionContext ctx)
	{
		return (CatalogVersion)getProperty( ctx, CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version
	 */
	public CatalogVersion getCatalogVersion()
	{
		return getCatalogVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.catalogVersion</code> attribute. 
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
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.catalogVersion</code> attribute. 
	 * @param value the catalogVersion - Catalog Version
	 */
	protected void setCatalogVersion(final CatalogVersion value)
	{
		setCatalogVersion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.conditionGeo</code> attribute.
	 * @return the conditionGeo - Geo Condition
	 */
	public Collection<String> getConditionGeo(final SessionContext ctx)
	{
		Collection<String> coll = (Collection<String>)getProperty( ctx, CONDITIONGEO);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.conditionGeo</code> attribute.
	 * @return the conditionGeo - Geo Condition
	 */
	public Collection<String> getConditionGeo()
	{
		return getConditionGeo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.conditionGeo</code> attribute. 
	 * @param value the conditionGeo - Geo Condition
	 */
	public void setConditionGeo(final SessionContext ctx, final Collection<String> value)
	{
		setProperty(ctx, CONDITIONGEO,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.conditionGeo</code> attribute. 
	 * @param value the conditionGeo - Geo Condition
	 */
	public void setConditionGeo(final Collection<String> value)
	{
		setConditionGeo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.conditionPath</code> attribute.
	 * @return the conditionPath - Path Condition
	 */
	public String getConditionPath(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CONDITIONPATH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.conditionPath</code> attribute.
	 * @return the conditionPath - Path Condition
	 */
	public String getConditionPath()
	{
		return getConditionPath( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.conditionPath</code> attribute. 
	 * @param value the conditionPath - Path Condition
	 */
	public void setConditionPath(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CONDITIONPATH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.conditionPath</code> attribute. 
	 * @param value the conditionPath - Path Condition
	 */
	public void setConditionPath(final String value)
	{
		setConditionPath( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.conditionString</code> attribute.
	 * @return the conditionString - String Condition
	 */
	public String getConditionString(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CONDITIONSTRING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.conditionString</code> attribute.
	 * @return the conditionString - String Condition
	 */
	public String getConditionString()
	{
		return getConditionString( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.conditionString</code> attribute. 
	 * @param value the conditionString - String Condition
	 */
	public void setConditionString(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CONDITIONSTRING,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.conditionString</code> attribute. 
	 * @param value the conditionString - String Condition
	 */
	public void setConditionString(final String value)
	{
		setConditionString( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		SUBSCRIPTIONPRODUCTHANDLER.newInstance(ctx, allAttributes);
		ENTITLEMENTHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.description</code> attribute.
	 * @return the description - Description
	 */
	public String getDescription(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedProductEntitlement.getDescription requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.description</code> attribute.
	 * @return the description - Description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.description</code> attribute. 
	 * @return the localized description - Description
	 */
	public Map<Language,String> getAllDescription(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,DESCRIPTION,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.description</code> attribute. 
	 * @return the localized description - Description
	 */
	public Map<Language,String> getAllDescription()
	{
		return getAllDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.description</code> attribute. 
	 * @param value the description - Description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedProductEntitlement.setDescription requires a session language", 0 );
		}
		setLocalizedProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.description</code> attribute. 
	 * @param value the description - Description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.description</code> attribute. 
	 * @param value the description - Description
	 */
	public void setAllDescription(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.description</code> attribute. 
	 * @param value the description - Description
	 */
	public void setAllDescription(final Map<Language,String> value)
	{
		setAllDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.entitlement</code> attribute.
	 * @return the entitlement
	 */
	public Entitlement getEntitlement(final SessionContext ctx)
	{
		return (Entitlement)getProperty( ctx, ENTITLEMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.entitlement</code> attribute.
	 * @return the entitlement
	 */
	public Entitlement getEntitlement()
	{
		return getEntitlement( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.entitlement</code> attribute. 
	 * @param value the entitlement
	 */
	public void setEntitlement(final SessionContext ctx, final Entitlement value)
	{
		ENTITLEMENTHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.entitlement</code> attribute. 
	 * @param value the entitlement
	 */
	public void setEntitlement(final Entitlement value)
	{
		setEntitlement( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.id</code> attribute. 
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
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.id</code> attribute. 
	 * @param value the id - Identifier
	 */
	protected void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.quantity</code> attribute.
	 * @return the quantity - Quantity
	 */
	public Integer getQuantity(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, QUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.quantity</code> attribute.
	 * @return the quantity - Quantity
	 */
	public Integer getQuantity()
	{
		return getQuantity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.quantity</code> attribute. 
	 * @return the quantity - Quantity
	 */
	public int getQuantityAsPrimitive(final SessionContext ctx)
	{
		Integer value = getQuantity( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.quantity</code> attribute. 
	 * @return the quantity - Quantity
	 */
	public int getQuantityAsPrimitive()
	{
		return getQuantityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.quantity</code> attribute. 
	 * @param value the quantity - Quantity
	 */
	public void setQuantity(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, QUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.quantity</code> attribute. 
	 * @param value the quantity - Quantity
	 */
	public void setQuantity(final Integer value)
	{
		setQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.quantity</code> attribute. 
	 * @param value the quantity - Quantity
	 */
	public void setQuantity(final SessionContext ctx, final int value)
	{
		setQuantity( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.quantity</code> attribute. 
	 * @param value the quantity - Quantity
	 */
	public void setQuantity(final int value)
	{
		setQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.subscriptionProduct</code> attribute.
	 * @return the subscriptionProduct
	 */
	public Product getSubscriptionProduct(final SessionContext ctx)
	{
		return (Product)getProperty( ctx, SUBSCRIPTIONPRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.subscriptionProduct</code> attribute.
	 * @return the subscriptionProduct
	 */
	public Product getSubscriptionProduct()
	{
		return getSubscriptionProduct( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.subscriptionProduct</code> attribute. 
	 * @param value the subscriptionProduct
	 */
	public void setSubscriptionProduct(final SessionContext ctx, final Product value)
	{
		SUBSCRIPTIONPRODUCTHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.subscriptionProduct</code> attribute. 
	 * @param value the subscriptionProduct
	 */
	public void setSubscriptionProduct(final Product value)
	{
		setSubscriptionProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.timeUnit</code> attribute.
	 * @return the timeUnit - Time Unit
	 */
	public EnumerationValue getTimeUnit(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, TIMEUNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.timeUnit</code> attribute.
	 * @return the timeUnit - Time Unit
	 */
	public EnumerationValue getTimeUnit()
	{
		return getTimeUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.timeUnit</code> attribute. 
	 * @param value the timeUnit - Time Unit
	 */
	public void setTimeUnit(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, TIMEUNIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.timeUnit</code> attribute. 
	 * @param value the timeUnit - Time Unit
	 */
	public void setTimeUnit(final EnumerationValue value)
	{
		setTimeUnit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.timeUnitDuration</code> attribute.
	 * @return the timeUnitDuration - Duration
	 */
	public Integer getTimeUnitDuration(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, TIMEUNITDURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.timeUnitDuration</code> attribute.
	 * @return the timeUnitDuration - Duration
	 */
	public Integer getTimeUnitDuration()
	{
		return getTimeUnitDuration( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.timeUnitDuration</code> attribute. 
	 * @return the timeUnitDuration - Duration
	 */
	public int getTimeUnitDurationAsPrimitive(final SessionContext ctx)
	{
		Integer value = getTimeUnitDuration( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.timeUnitDuration</code> attribute. 
	 * @return the timeUnitDuration - Duration
	 */
	public int getTimeUnitDurationAsPrimitive()
	{
		return getTimeUnitDurationAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.timeUnitDuration</code> attribute. 
	 * @param value the timeUnitDuration - Duration
	 */
	public void setTimeUnitDuration(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, TIMEUNITDURATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.timeUnitDuration</code> attribute. 
	 * @param value the timeUnitDuration - Duration
	 */
	public void setTimeUnitDuration(final Integer value)
	{
		setTimeUnitDuration( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.timeUnitDuration</code> attribute. 
	 * @param value the timeUnitDuration - Duration
	 */
	public void setTimeUnitDuration(final SessionContext ctx, final int value)
	{
		setTimeUnitDuration( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.timeUnitDuration</code> attribute. 
	 * @param value the timeUnitDuration - Duration
	 */
	public void setTimeUnitDuration(final int value)
	{
		setTimeUnitDuration( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.timeUnitStart</code> attribute.
	 * @return the timeUnitStart - Start
	 */
	public Integer getTimeUnitStart(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, TIMEUNITSTART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.timeUnitStart</code> attribute.
	 * @return the timeUnitStart - Start
	 */
	public Integer getTimeUnitStart()
	{
		return getTimeUnitStart( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.timeUnitStart</code> attribute. 
	 * @return the timeUnitStart - Start
	 */
	public int getTimeUnitStartAsPrimitive(final SessionContext ctx)
	{
		Integer value = getTimeUnitStart( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductEntitlement.timeUnitStart</code> attribute. 
	 * @return the timeUnitStart - Start
	 */
	public int getTimeUnitStartAsPrimitive()
	{
		return getTimeUnitStartAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.timeUnitStart</code> attribute. 
	 * @param value the timeUnitStart - Start
	 */
	public void setTimeUnitStart(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, TIMEUNITSTART,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.timeUnitStart</code> attribute. 
	 * @param value the timeUnitStart - Start
	 */
	public void setTimeUnitStart(final Integer value)
	{
		setTimeUnitStart( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.timeUnitStart</code> attribute. 
	 * @param value the timeUnitStart - Start
	 */
	public void setTimeUnitStart(final SessionContext ctx, final int value)
	{
		setTimeUnitStart( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductEntitlement.timeUnitStart</code> attribute. 
	 * @param value the timeUnitStart - Start
	 */
	public void setTimeUnitStart(final int value)
	{
		setTimeUnitStart( getSession().getSessionContext(), value );
	}
	
}
