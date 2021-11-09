/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.jalo;

import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import de.hybris.platform.subscriptionservices.jalo.UsageChargeEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.TierUsageChargeEntry TierUsageChargeEntry}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedTierUsageChargeEntry extends UsageChargeEntry
{
	/** Qualifier of the <code>TierUsageChargeEntry.tierStart</code> attribute **/
	public static final String TIERSTART = "tierStart";
	/** Qualifier of the <code>TierUsageChargeEntry.tierEnd</code> attribute **/
	public static final String TIEREND = "tierEnd";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(UsageChargeEntry.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(TIERSTART, AttributeMode.INITIAL);
		tmp.put(TIEREND, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TierUsageChargeEntry.tierEnd</code> attribute.
	 * @return the tierEnd - Tier End
	 */
	public Integer getTierEnd(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, TIEREND);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TierUsageChargeEntry.tierEnd</code> attribute.
	 * @return the tierEnd - Tier End
	 */
	public Integer getTierEnd()
	{
		return getTierEnd( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TierUsageChargeEntry.tierEnd</code> attribute. 
	 * @return the tierEnd - Tier End
	 */
	public int getTierEndAsPrimitive(final SessionContext ctx)
	{
		Integer value = getTierEnd( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TierUsageChargeEntry.tierEnd</code> attribute. 
	 * @return the tierEnd - Tier End
	 */
	public int getTierEndAsPrimitive()
	{
		return getTierEndAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TierUsageChargeEntry.tierEnd</code> attribute. 
	 * @param value the tierEnd - Tier End
	 */
	public void setTierEnd(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, TIEREND,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TierUsageChargeEntry.tierEnd</code> attribute. 
	 * @param value the tierEnd - Tier End
	 */
	public void setTierEnd(final Integer value)
	{
		setTierEnd( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TierUsageChargeEntry.tierEnd</code> attribute. 
	 * @param value the tierEnd - Tier End
	 */
	public void setTierEnd(final SessionContext ctx, final int value)
	{
		setTierEnd( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TierUsageChargeEntry.tierEnd</code> attribute. 
	 * @param value the tierEnd - Tier End
	 */
	public void setTierEnd(final int value)
	{
		setTierEnd( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TierUsageChargeEntry.tierStart</code> attribute.
	 * @return the tierStart - Tier Start
	 */
	public Integer getTierStart(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, TIERSTART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TierUsageChargeEntry.tierStart</code> attribute.
	 * @return the tierStart - Tier Start
	 */
	public Integer getTierStart()
	{
		return getTierStart( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TierUsageChargeEntry.tierStart</code> attribute. 
	 * @return the tierStart - Tier Start
	 */
	public int getTierStartAsPrimitive(final SessionContext ctx)
	{
		Integer value = getTierStart( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TierUsageChargeEntry.tierStart</code> attribute. 
	 * @return the tierStart - Tier Start
	 */
	public int getTierStartAsPrimitive()
	{
		return getTierStartAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TierUsageChargeEntry.tierStart</code> attribute. 
	 * @param value the tierStart - Tier Start
	 */
	public void setTierStart(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, TIERSTART,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TierUsageChargeEntry.tierStart</code> attribute. 
	 * @param value the tierStart - Tier Start
	 */
	public void setTierStart(final Integer value)
	{
		setTierStart( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TierUsageChargeEntry.tierStart</code> attribute. 
	 * @param value the tierStart - Tier Start
	 */
	public void setTierStart(final SessionContext ctx, final int value)
	{
		setTierStart( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TierUsageChargeEntry.tierStart</code> attribute. 
	 * @param value the tierStart - Tier Start
	 */
	public void setTierStart(final int value)
	{
		setTierStart( getSession().getSessionContext(), value );
	}
	
}
