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
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import de.hybris.platform.subscriptionservices.jalo.UsageCharge;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.PerUnitUsageCharge PerUnitUsageCharge}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedPerUnitUsageCharge extends UsageCharge
{
	/** Qualifier of the <code>PerUnitUsageCharge.usageChargeType</code> attribute **/
	public static final String USAGECHARGETYPE = "usageChargeType";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(UsageCharge.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(USAGECHARGETYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PerUnitUsageCharge.usageChargeType</code> attribute.
	 * @return the usageChargeType - Usage Charge Type
	 */
	public EnumerationValue getUsageChargeType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, USAGECHARGETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PerUnitUsageCharge.usageChargeType</code> attribute.
	 * @return the usageChargeType - Usage Charge Type
	 */
	public EnumerationValue getUsageChargeType()
	{
		return getUsageChargeType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PerUnitUsageCharge.usageChargeType</code> attribute. 
	 * @param value the usageChargeType - Usage Charge Type
	 */
	public void setUsageChargeType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, USAGECHARGETYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PerUnitUsageCharge.usageChargeType</code> attribute. 
	 * @param value the usageChargeType - Usage Charge Type
	 */
	public void setUsageChargeType(final EnumerationValue value)
	{
		setUsageChargeType( getSession().getSessionContext(), value );
	}
	
}
