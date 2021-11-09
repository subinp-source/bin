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
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import de.hybris.platform.subscriptionservices.jalo.ChargeEntry;
import de.hybris.platform.subscriptionservices.jalo.SubscriptionPricePlan;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.RecurringChargeEntry RecurringChargeEntry}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedRecurringChargeEntry extends ChargeEntry
{
	/** Qualifier of the <code>RecurringChargeEntry.cycleStart</code> attribute **/
	public static final String CYCLESTART = "cycleStart";
	/** Qualifier of the <code>RecurringChargeEntry.cycleEnd</code> attribute **/
	public static final String CYCLEEND = "cycleEnd";
	/** Qualifier of the <code>RecurringChargeEntry.subscriptionPricePlanRecurring</code> attribute **/
	public static final String SUBSCRIPTIONPRICEPLANRECURRING = "subscriptionPricePlanRecurring";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n SUBSCRIPTIONPRICEPLANRECURRING's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedRecurringChargeEntry> SUBSCRIPTIONPRICEPLANRECURRINGHANDLER = new BidirectionalOneToManyHandler<GeneratedRecurringChargeEntry>(
	SubscriptionservicesConstants.TC.RECURRINGCHARGEENTRY,
	false,
	"subscriptionPricePlanRecurring",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(ChargeEntry.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CYCLESTART, AttributeMode.INITIAL);
		tmp.put(CYCLEEND, AttributeMode.INITIAL);
		tmp.put(SUBSCRIPTIONPRICEPLANRECURRING, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		SUBSCRIPTIONPRICEPLANRECURRINGHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RecurringChargeEntry.cycleEnd</code> attribute.
	 * @return the cycleEnd - CycleEnd
	 */
	public Integer getCycleEnd(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CYCLEEND);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RecurringChargeEntry.cycleEnd</code> attribute.
	 * @return the cycleEnd - CycleEnd
	 */
	public Integer getCycleEnd()
	{
		return getCycleEnd( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RecurringChargeEntry.cycleEnd</code> attribute. 
	 * @return the cycleEnd - CycleEnd
	 */
	public int getCycleEndAsPrimitive(final SessionContext ctx)
	{
		Integer value = getCycleEnd( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RecurringChargeEntry.cycleEnd</code> attribute. 
	 * @return the cycleEnd - CycleEnd
	 */
	public int getCycleEndAsPrimitive()
	{
		return getCycleEndAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RecurringChargeEntry.cycleEnd</code> attribute. 
	 * @param value the cycleEnd - CycleEnd
	 */
	public void setCycleEnd(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CYCLEEND,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RecurringChargeEntry.cycleEnd</code> attribute. 
	 * @param value the cycleEnd - CycleEnd
	 */
	public void setCycleEnd(final Integer value)
	{
		setCycleEnd( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RecurringChargeEntry.cycleEnd</code> attribute. 
	 * @param value the cycleEnd - CycleEnd
	 */
	public void setCycleEnd(final SessionContext ctx, final int value)
	{
		setCycleEnd( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RecurringChargeEntry.cycleEnd</code> attribute. 
	 * @param value the cycleEnd - CycleEnd
	 */
	public void setCycleEnd(final int value)
	{
		setCycleEnd( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RecurringChargeEntry.cycleStart</code> attribute.
	 * @return the cycleStart - CycleStart
	 */
	public Integer getCycleStart(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CYCLESTART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RecurringChargeEntry.cycleStart</code> attribute.
	 * @return the cycleStart - CycleStart
	 */
	public Integer getCycleStart()
	{
		return getCycleStart( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RecurringChargeEntry.cycleStart</code> attribute. 
	 * @return the cycleStart - CycleStart
	 */
	public int getCycleStartAsPrimitive(final SessionContext ctx)
	{
		Integer value = getCycleStart( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RecurringChargeEntry.cycleStart</code> attribute. 
	 * @return the cycleStart - CycleStart
	 */
	public int getCycleStartAsPrimitive()
	{
		return getCycleStartAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RecurringChargeEntry.cycleStart</code> attribute. 
	 * @param value the cycleStart - CycleStart
	 */
	public void setCycleStart(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CYCLESTART,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RecurringChargeEntry.cycleStart</code> attribute. 
	 * @param value the cycleStart - CycleStart
	 */
	public void setCycleStart(final Integer value)
	{
		setCycleStart( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RecurringChargeEntry.cycleStart</code> attribute. 
	 * @param value the cycleStart - CycleStart
	 */
	public void setCycleStart(final SessionContext ctx, final int value)
	{
		setCycleStart( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RecurringChargeEntry.cycleStart</code> attribute. 
	 * @param value the cycleStart - CycleStart
	 */
	public void setCycleStart(final int value)
	{
		setCycleStart( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RecurringChargeEntry.subscriptionPricePlanRecurring</code> attribute.
	 * @return the subscriptionPricePlanRecurring
	 */
	public SubscriptionPricePlan getSubscriptionPricePlanRecurring(final SessionContext ctx)
	{
		return (SubscriptionPricePlan)getProperty( ctx, SUBSCRIPTIONPRICEPLANRECURRING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RecurringChargeEntry.subscriptionPricePlanRecurring</code> attribute.
	 * @return the subscriptionPricePlanRecurring
	 */
	public SubscriptionPricePlan getSubscriptionPricePlanRecurring()
	{
		return getSubscriptionPricePlanRecurring( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RecurringChargeEntry.subscriptionPricePlanRecurring</code> attribute. 
	 * @param value the subscriptionPricePlanRecurring
	 */
	public void setSubscriptionPricePlanRecurring(final SessionContext ctx, final SubscriptionPricePlan value)
	{
		SUBSCRIPTIONPRICEPLANRECURRINGHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RecurringChargeEntry.subscriptionPricePlanRecurring</code> attribute. 
	 * @param value the subscriptionPricePlanRecurring
	 */
	public void setSubscriptionPricePlanRecurring(final SubscriptionPricePlan value)
	{
		setSubscriptionPricePlanRecurring( getSession().getSessionContext(), value );
	}
	
}
