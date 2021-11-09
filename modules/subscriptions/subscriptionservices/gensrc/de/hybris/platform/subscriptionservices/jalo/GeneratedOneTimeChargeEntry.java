/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.jalo;

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
import de.hybris.platform.subscriptionservices.jalo.BillingEvent;
import de.hybris.platform.subscriptionservices.jalo.ChargeEntry;
import de.hybris.platform.subscriptionservices.jalo.SubscriptionPricePlan;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.OneTimeChargeEntry OneTimeChargeEntry}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedOneTimeChargeEntry extends ChargeEntry
{
	/** Qualifier of the <code>OneTimeChargeEntry.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>OneTimeChargeEntry.billingEvent</code> attribute **/
	public static final String BILLINGEVENT = "billingEvent";
	/** Qualifier of the <code>OneTimeChargeEntry.subscriptionPricePlanOneTime</code> attribute **/
	public static final String SUBSCRIPTIONPRICEPLANONETIME = "subscriptionPricePlanOneTime";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n SUBSCRIPTIONPRICEPLANONETIME's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedOneTimeChargeEntry> SUBSCRIPTIONPRICEPLANONETIMEHANDLER = new BidirectionalOneToManyHandler<GeneratedOneTimeChargeEntry>(
	SubscriptionservicesConstants.TC.ONETIMECHARGEENTRY,
	false,
	"subscriptionPricePlanOneTime",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(ChargeEntry.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(BILLINGEVENT, AttributeMode.INITIAL);
		tmp.put(SUBSCRIPTIONPRICEPLANONETIME, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OneTimeChargeEntry.billingEvent</code> attribute.
	 * @return the billingEvent - Billing Event
	 */
	public BillingEvent getBillingEvent(final SessionContext ctx)
	{
		return (BillingEvent)getProperty( ctx, BILLINGEVENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OneTimeChargeEntry.billingEvent</code> attribute.
	 * @return the billingEvent - Billing Event
	 */
	public BillingEvent getBillingEvent()
	{
		return getBillingEvent( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OneTimeChargeEntry.billingEvent</code> attribute. 
	 * @param value the billingEvent - Billing Event
	 */
	public void setBillingEvent(final SessionContext ctx, final BillingEvent value)
	{
		setProperty(ctx, BILLINGEVENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OneTimeChargeEntry.billingEvent</code> attribute. 
	 * @param value the billingEvent - Billing Event
	 */
	public void setBillingEvent(final BillingEvent value)
	{
		setBillingEvent( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		SUBSCRIPTIONPRICEPLANONETIMEHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OneTimeChargeEntry.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedOneTimeChargeEntry.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OneTimeChargeEntry.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OneTimeChargeEntry.name</code> attribute. 
	 * @return the localized name - Name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OneTimeChargeEntry.name</code> attribute. 
	 * @return the localized name - Name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OneTimeChargeEntry.name</code> attribute. 
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
			throw new JaloInvalidParameterException("GeneratedOneTimeChargeEntry.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OneTimeChargeEntry.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OneTimeChargeEntry.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OneTimeChargeEntry.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OneTimeChargeEntry.subscriptionPricePlanOneTime</code> attribute.
	 * @return the subscriptionPricePlanOneTime
	 */
	public SubscriptionPricePlan getSubscriptionPricePlanOneTime(final SessionContext ctx)
	{
		return (SubscriptionPricePlan)getProperty( ctx, SUBSCRIPTIONPRICEPLANONETIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OneTimeChargeEntry.subscriptionPricePlanOneTime</code> attribute.
	 * @return the subscriptionPricePlanOneTime
	 */
	public SubscriptionPricePlan getSubscriptionPricePlanOneTime()
	{
		return getSubscriptionPricePlanOneTime( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OneTimeChargeEntry.subscriptionPricePlanOneTime</code> attribute. 
	 * @param value the subscriptionPricePlanOneTime
	 */
	public void setSubscriptionPricePlanOneTime(final SessionContext ctx, final SubscriptionPricePlan value)
	{
		SUBSCRIPTIONPRICEPLANONETIMEHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OneTimeChargeEntry.subscriptionPricePlanOneTime</code> attribute. 
	 * @param value the subscriptionPricePlanOneTime
	 */
	public void setSubscriptionPricePlanOneTime(final SubscriptionPricePlan value)
	{
		setSubscriptionPricePlanOneTime( getSession().getSessionContext(), value );
	}
	
}
