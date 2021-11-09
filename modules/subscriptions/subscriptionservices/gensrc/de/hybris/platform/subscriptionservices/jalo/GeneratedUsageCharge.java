/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.jalo;

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
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import de.hybris.platform.subscriptionservices.jalo.SubscriptionPricePlan;
import de.hybris.platform.subscriptionservices.jalo.UsageChargeEntry;
import de.hybris.platform.subscriptionservices.jalo.UsageUnit;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.UsageCharge UsageCharge}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedUsageCharge extends GenericItem
{
	/** Qualifier of the <code>UsageCharge.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>UsageCharge.catalogVersion</code> attribute **/
	public static final String CATALOGVERSION = "catalogVersion";
	/** Qualifier of the <code>UsageCharge.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>UsageCharge.usageUnit</code> attribute **/
	public static final String USAGEUNIT = "usageUnit";
	/** Qualifier of the <code>UsageCharge.subscriptionPricePlanUsage</code> attribute **/
	public static final String SUBSCRIPTIONPRICEPLANUSAGE = "subscriptionPricePlanUsage";
	/** Qualifier of the <code>UsageCharge.usageChargeEntries</code> attribute **/
	public static final String USAGECHARGEENTRIES = "usageChargeEntries";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n SUBSCRIPTIONPRICEPLANUSAGE's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedUsageCharge> SUBSCRIPTIONPRICEPLANUSAGEHANDLER = new BidirectionalOneToManyHandler<GeneratedUsageCharge>(
	SubscriptionservicesConstants.TC.USAGECHARGE,
	false,
	"subscriptionPricePlanUsage",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n USAGECHARGEENTRIES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<UsageChargeEntry> USAGECHARGEENTRIESHANDLER = new OneToManyHandler<UsageChargeEntry>(
	SubscriptionservicesConstants.TC.USAGECHARGEENTRY,
	true,
	"usageCharge",
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
		tmp.put(CATALOGVERSION, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(USAGEUNIT, AttributeMode.INITIAL);
		tmp.put(SUBSCRIPTIONPRICEPLANUSAGE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version
	 */
	public CatalogVersion getCatalogVersion(final SessionContext ctx)
	{
		return (CatalogVersion)getProperty( ctx, CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.catalogVersion</code> attribute.
	 * @return the catalogVersion - Catalog Version
	 */
	public CatalogVersion getCatalogVersion()
	{
		return getCatalogVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.catalogVersion</code> attribute. 
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
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.catalogVersion</code> attribute. 
	 * @param value the catalogVersion - Catalog Version
	 */
	protected void setCatalogVersion(final CatalogVersion value)
	{
		setCatalogVersion( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		SUBSCRIPTIONPRICEPLANUSAGEHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.id</code> attribute. 
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
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.id</code> attribute. 
	 * @param value the id - Identifier
	 */
	protected void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedUsageCharge.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.name</code> attribute. 
	 * @return the localized name - Name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.name</code> attribute. 
	 * @return the localized name - Name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedUsageCharge.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.subscriptionPricePlanUsage</code> attribute.
	 * @return the subscriptionPricePlanUsage
	 */
	public SubscriptionPricePlan getSubscriptionPricePlanUsage(final SessionContext ctx)
	{
		return (SubscriptionPricePlan)getProperty( ctx, SUBSCRIPTIONPRICEPLANUSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.subscriptionPricePlanUsage</code> attribute.
	 * @return the subscriptionPricePlanUsage
	 */
	public SubscriptionPricePlan getSubscriptionPricePlanUsage()
	{
		return getSubscriptionPricePlanUsage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.subscriptionPricePlanUsage</code> attribute. 
	 * @param value the subscriptionPricePlanUsage
	 */
	public void setSubscriptionPricePlanUsage(final SessionContext ctx, final SubscriptionPricePlan value)
	{
		SUBSCRIPTIONPRICEPLANUSAGEHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.subscriptionPricePlanUsage</code> attribute. 
	 * @param value the subscriptionPricePlanUsage
	 */
	public void setSubscriptionPricePlanUsage(final SubscriptionPricePlan value)
	{
		setSubscriptionPricePlanUsage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.usageChargeEntries</code> attribute.
	 * @return the usageChargeEntries
	 */
	public Collection<UsageChargeEntry> getUsageChargeEntries(final SessionContext ctx)
	{
		return USAGECHARGEENTRIESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.usageChargeEntries</code> attribute.
	 * @return the usageChargeEntries
	 */
	public Collection<UsageChargeEntry> getUsageChargeEntries()
	{
		return getUsageChargeEntries( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.usageChargeEntries</code> attribute. 
	 * @param value the usageChargeEntries
	 */
	public void setUsageChargeEntries(final SessionContext ctx, final Collection<UsageChargeEntry> value)
	{
		USAGECHARGEENTRIESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.usageChargeEntries</code> attribute. 
	 * @param value the usageChargeEntries
	 */
	public void setUsageChargeEntries(final Collection<UsageChargeEntry> value)
	{
		setUsageChargeEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to usageChargeEntries. 
	 * @param value the item to add to usageChargeEntries
	 */
	public void addToUsageChargeEntries(final SessionContext ctx, final UsageChargeEntry value)
	{
		USAGECHARGEENTRIESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to usageChargeEntries. 
	 * @param value the item to add to usageChargeEntries
	 */
	public void addToUsageChargeEntries(final UsageChargeEntry value)
	{
		addToUsageChargeEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from usageChargeEntries. 
	 * @param value the item to remove from usageChargeEntries
	 */
	public void removeFromUsageChargeEntries(final SessionContext ctx, final UsageChargeEntry value)
	{
		USAGECHARGEENTRIESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from usageChargeEntries. 
	 * @param value the item to remove from usageChargeEntries
	 */
	public void removeFromUsageChargeEntries(final UsageChargeEntry value)
	{
		removeFromUsageChargeEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.usageUnit</code> attribute.
	 * @return the usageUnit - Usage Unit
	 */
	public UsageUnit getUsageUnit(final SessionContext ctx)
	{
		return (UsageUnit)getProperty( ctx, USAGEUNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageCharge.usageUnit</code> attribute.
	 * @return the usageUnit - Usage Unit
	 */
	public UsageUnit getUsageUnit()
	{
		return getUsageUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.usageUnit</code> attribute. 
	 * @param value the usageUnit - Usage Unit
	 */
	public void setUsageUnit(final SessionContext ctx, final UsageUnit value)
	{
		setProperty(ctx, USAGEUNIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageCharge.usageUnit</code> attribute. 
	 * @param value the usageUnit - Usage Unit
	 */
	public void setUsageUnit(final UsageUnit value)
	{
		setUsageUnit( getSession().getSessionContext(), value );
	}
	
}
