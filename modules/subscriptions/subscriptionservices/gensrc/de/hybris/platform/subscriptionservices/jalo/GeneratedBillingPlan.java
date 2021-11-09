/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import de.hybris.platform.subscriptionservices.jalo.BillingFrequency;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.BillingPlan BillingPlan}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedBillingPlan extends GenericItem
{
	/** Qualifier of the <code>BillingPlan.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>BillingPlan.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>BillingPlan.billingCycleDay</code> attribute **/
	public static final String BILLINGCYCLEDAY = "billingCycleDay";
	/** Qualifier of the <code>BillingPlan.billingCycleType</code> attribute **/
	public static final String BILLINGCYCLETYPE = "billingCycleType";
	/** Qualifier of the <code>BillingPlan.billingFrequency</code> attribute **/
	public static final String BILLINGFREQUENCY = "billingFrequency";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(BILLINGCYCLEDAY, AttributeMode.INITIAL);
		tmp.put(BILLINGCYCLETYPE, AttributeMode.INITIAL);
		tmp.put(BILLINGFREQUENCY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.billingCycleDay</code> attribute.
	 * @return the billingCycleDay - Billing Cycle Day
	 */
	public Integer getBillingCycleDay(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, BILLINGCYCLEDAY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.billingCycleDay</code> attribute.
	 * @return the billingCycleDay - Billing Cycle Day
	 */
	public Integer getBillingCycleDay()
	{
		return getBillingCycleDay( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.billingCycleDay</code> attribute. 
	 * @return the billingCycleDay - Billing Cycle Day
	 */
	public int getBillingCycleDayAsPrimitive(final SessionContext ctx)
	{
		Integer value = getBillingCycleDay( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.billingCycleDay</code> attribute. 
	 * @return the billingCycleDay - Billing Cycle Day
	 */
	public int getBillingCycleDayAsPrimitive()
	{
		return getBillingCycleDayAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.billingCycleDay</code> attribute. 
	 * @param value the billingCycleDay - Billing Cycle Day
	 */
	public void setBillingCycleDay(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, BILLINGCYCLEDAY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.billingCycleDay</code> attribute. 
	 * @param value the billingCycleDay - Billing Cycle Day
	 */
	public void setBillingCycleDay(final Integer value)
	{
		setBillingCycleDay( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.billingCycleDay</code> attribute. 
	 * @param value the billingCycleDay - Billing Cycle Day
	 */
	public void setBillingCycleDay(final SessionContext ctx, final int value)
	{
		setBillingCycleDay( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.billingCycleDay</code> attribute. 
	 * @param value the billingCycleDay - Billing Cycle Day
	 */
	public void setBillingCycleDay(final int value)
	{
		setBillingCycleDay( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.billingCycleType</code> attribute.
	 * @return the billingCycleType - Billing Cycle Type
	 */
	public EnumerationValue getBillingCycleType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, BILLINGCYCLETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.billingCycleType</code> attribute.
	 * @return the billingCycleType - Billing Cycle Type
	 */
	public EnumerationValue getBillingCycleType()
	{
		return getBillingCycleType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.billingCycleType</code> attribute. 
	 * @param value the billingCycleType - Billing Cycle Type
	 */
	public void setBillingCycleType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, BILLINGCYCLETYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.billingCycleType</code> attribute. 
	 * @param value the billingCycleType - Billing Cycle Type
	 */
	public void setBillingCycleType(final EnumerationValue value)
	{
		setBillingCycleType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.billingFrequency</code> attribute.
	 * @return the billingFrequency - Billing Frequency
	 */
	public BillingFrequency getBillingFrequency(final SessionContext ctx)
	{
		return (BillingFrequency)getProperty( ctx, BILLINGFREQUENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.billingFrequency</code> attribute.
	 * @return the billingFrequency - Billing Frequency
	 */
	public BillingFrequency getBillingFrequency()
	{
		return getBillingFrequency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.billingFrequency</code> attribute. 
	 * @param value the billingFrequency - Billing Frequency
	 */
	public void setBillingFrequency(final SessionContext ctx, final BillingFrequency value)
	{
		setProperty(ctx, BILLINGFREQUENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.billingFrequency</code> attribute. 
	 * @param value the billingFrequency - Billing Frequency
	 */
	public void setBillingFrequency(final BillingFrequency value)
	{
		setBillingFrequency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.id</code> attribute. 
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
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.id</code> attribute. 
	 * @param value the id - Identifier
	 */
	protected void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedBillingPlan.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.name</code> attribute. 
	 * @return the localized name - Name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BillingPlan.name</code> attribute. 
	 * @return the localized name - Name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.name</code> attribute. 
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
			throw new JaloInvalidParameterException("GeneratedBillingPlan.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BillingPlan.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
}
