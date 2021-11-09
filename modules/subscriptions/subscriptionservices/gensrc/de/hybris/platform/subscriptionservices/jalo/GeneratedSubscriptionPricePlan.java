/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.jalo;

import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import de.hybris.platform.subscriptionservices.jalo.OneTimeChargeEntry;
import de.hybris.platform.subscriptionservices.jalo.RecurringChargeEntry;
import de.hybris.platform.subscriptionservices.jalo.UsageCharge;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.SubscriptionPricePlan SubscriptionPricePlan}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSubscriptionPricePlan extends PriceRow
{
	/** Qualifier of the <code>SubscriptionPricePlan.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>SubscriptionPricePlan.oneTimeChargeEntries</code> attribute **/
	public static final String ONETIMECHARGEENTRIES = "oneTimeChargeEntries";
	/** Qualifier of the <code>SubscriptionPricePlan.recurringChargeEntries</code> attribute **/
	public static final String RECURRINGCHARGEENTRIES = "recurringChargeEntries";
	/** Qualifier of the <code>SubscriptionPricePlan.usageCharges</code> attribute **/
	public static final String USAGECHARGES = "usageCharges";
	/**
	* {@link OneToManyHandler} for handling 1:n ONETIMECHARGEENTRIES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<OneTimeChargeEntry> ONETIMECHARGEENTRIESHANDLER = new OneToManyHandler<OneTimeChargeEntry>(
	SubscriptionservicesConstants.TC.ONETIMECHARGEENTRY,
	true,
	"subscriptionPricePlanOneTime",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n RECURRINGCHARGEENTRIES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<RecurringChargeEntry> RECURRINGCHARGEENTRIESHANDLER = new OneToManyHandler<RecurringChargeEntry>(
	SubscriptionservicesConstants.TC.RECURRINGCHARGEENTRY,
	true,
	"subscriptionPricePlanRecurring",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n USAGECHARGES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<UsageCharge> USAGECHARGESHANDLER = new OneToManyHandler<UsageCharge>(
	SubscriptionservicesConstants.TC.USAGECHARGE,
	true,
	"subscriptionPricePlanUsage",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(PriceRow.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(NAME, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionPricePlan.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSubscriptionPricePlan.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionPricePlan.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionPricePlan.name</code> attribute. 
	 * @return the localized name - Name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionPricePlan.name</code> attribute. 
	 * @return the localized name - Name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionPricePlan.name</code> attribute. 
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
			throw new JaloInvalidParameterException("GeneratedSubscriptionPricePlan.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionPricePlan.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionPricePlan.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionPricePlan.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionPricePlan.oneTimeChargeEntries</code> attribute.
	 * @return the oneTimeChargeEntries
	 */
	public Collection<OneTimeChargeEntry> getOneTimeChargeEntries(final SessionContext ctx)
	{
		return ONETIMECHARGEENTRIESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionPricePlan.oneTimeChargeEntries</code> attribute.
	 * @return the oneTimeChargeEntries
	 */
	public Collection<OneTimeChargeEntry> getOneTimeChargeEntries()
	{
		return getOneTimeChargeEntries( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionPricePlan.oneTimeChargeEntries</code> attribute. 
	 * @param value the oneTimeChargeEntries
	 */
	public void setOneTimeChargeEntries(final SessionContext ctx, final Collection<OneTimeChargeEntry> value)
	{
		ONETIMECHARGEENTRIESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionPricePlan.oneTimeChargeEntries</code> attribute. 
	 * @param value the oneTimeChargeEntries
	 */
	public void setOneTimeChargeEntries(final Collection<OneTimeChargeEntry> value)
	{
		setOneTimeChargeEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to oneTimeChargeEntries. 
	 * @param value the item to add to oneTimeChargeEntries
	 */
	public void addToOneTimeChargeEntries(final SessionContext ctx, final OneTimeChargeEntry value)
	{
		ONETIMECHARGEENTRIESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to oneTimeChargeEntries. 
	 * @param value the item to add to oneTimeChargeEntries
	 */
	public void addToOneTimeChargeEntries(final OneTimeChargeEntry value)
	{
		addToOneTimeChargeEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from oneTimeChargeEntries. 
	 * @param value the item to remove from oneTimeChargeEntries
	 */
	public void removeFromOneTimeChargeEntries(final SessionContext ctx, final OneTimeChargeEntry value)
	{
		ONETIMECHARGEENTRIESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from oneTimeChargeEntries. 
	 * @param value the item to remove from oneTimeChargeEntries
	 */
	public void removeFromOneTimeChargeEntries(final OneTimeChargeEntry value)
	{
		removeFromOneTimeChargeEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionPricePlan.recurringChargeEntries</code> attribute.
	 * @return the recurringChargeEntries
	 */
	public Collection<RecurringChargeEntry> getRecurringChargeEntries(final SessionContext ctx)
	{
		return RECURRINGCHARGEENTRIESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionPricePlan.recurringChargeEntries</code> attribute.
	 * @return the recurringChargeEntries
	 */
	public Collection<RecurringChargeEntry> getRecurringChargeEntries()
	{
		return getRecurringChargeEntries( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionPricePlan.recurringChargeEntries</code> attribute. 
	 * @param value the recurringChargeEntries
	 */
	public void setRecurringChargeEntries(final SessionContext ctx, final Collection<RecurringChargeEntry> value)
	{
		RECURRINGCHARGEENTRIESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionPricePlan.recurringChargeEntries</code> attribute. 
	 * @param value the recurringChargeEntries
	 */
	public void setRecurringChargeEntries(final Collection<RecurringChargeEntry> value)
	{
		setRecurringChargeEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to recurringChargeEntries. 
	 * @param value the item to add to recurringChargeEntries
	 */
	public void addToRecurringChargeEntries(final SessionContext ctx, final RecurringChargeEntry value)
	{
		RECURRINGCHARGEENTRIESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to recurringChargeEntries. 
	 * @param value the item to add to recurringChargeEntries
	 */
	public void addToRecurringChargeEntries(final RecurringChargeEntry value)
	{
		addToRecurringChargeEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from recurringChargeEntries. 
	 * @param value the item to remove from recurringChargeEntries
	 */
	public void removeFromRecurringChargeEntries(final SessionContext ctx, final RecurringChargeEntry value)
	{
		RECURRINGCHARGEENTRIESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from recurringChargeEntries. 
	 * @param value the item to remove from recurringChargeEntries
	 */
	public void removeFromRecurringChargeEntries(final RecurringChargeEntry value)
	{
		removeFromRecurringChargeEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionPricePlan.usageCharges</code> attribute.
	 * @return the usageCharges
	 */
	public Collection<UsageCharge> getUsageCharges(final SessionContext ctx)
	{
		return USAGECHARGESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionPricePlan.usageCharges</code> attribute.
	 * @return the usageCharges
	 */
	public Collection<UsageCharge> getUsageCharges()
	{
		return getUsageCharges( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionPricePlan.usageCharges</code> attribute. 
	 * @param value the usageCharges
	 */
	public void setUsageCharges(final SessionContext ctx, final Collection<UsageCharge> value)
	{
		USAGECHARGESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionPricePlan.usageCharges</code> attribute. 
	 * @param value the usageCharges
	 */
	public void setUsageCharges(final Collection<UsageCharge> value)
	{
		setUsageCharges( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to usageCharges. 
	 * @param value the item to add to usageCharges
	 */
	public void addToUsageCharges(final SessionContext ctx, final UsageCharge value)
	{
		USAGECHARGESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to usageCharges. 
	 * @param value the item to add to usageCharges
	 */
	public void addToUsageCharges(final UsageCharge value)
	{
		addToUsageCharges( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from usageCharges. 
	 * @param value the item to remove from usageCharges
	 */
	public void removeFromUsageCharges(final SessionContext ctx, final UsageCharge value)
	{
		USAGECHARGESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from usageCharges. 
	 * @param value the item to remove from usageCharges
	 */
	public void removeFromUsageCharges(final UsageCharge value)
	{
		removeFromUsageCharges( getSession().getSessionContext(), value );
	}
	
}
