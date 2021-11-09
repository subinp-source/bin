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
import de.hybris.platform.subscriptionservices.jalo.UsageCharge;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.UsageChargeEntry UsageChargeEntry}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedUsageChargeEntry extends ChargeEntry
{
	/** Qualifier of the <code>UsageChargeEntry.usageCharge</code> attribute **/
	public static final String USAGECHARGE = "usageCharge";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n USAGECHARGE's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedUsageChargeEntry> USAGECHARGEHANDLER = new BidirectionalOneToManyHandler<GeneratedUsageChargeEntry>(
	SubscriptionservicesConstants.TC.USAGECHARGEENTRY,
	false,
	"usageCharge",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(ChargeEntry.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(USAGECHARGE, AttributeMode.INITIAL);
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
		USAGECHARGEHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageChargeEntry.usageCharge</code> attribute.
	 * @return the usageCharge
	 */
	public UsageCharge getUsageCharge(final SessionContext ctx)
	{
		return (UsageCharge)getProperty( ctx, USAGECHARGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageChargeEntry.usageCharge</code> attribute.
	 * @return the usageCharge
	 */
	public UsageCharge getUsageCharge()
	{
		return getUsageCharge( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageChargeEntry.usageCharge</code> attribute. 
	 * @param value the usageCharge
	 */
	public void setUsageCharge(final SessionContext ctx, final UsageCharge value)
	{
		USAGECHARGEHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageChargeEntry.usageCharge</code> attribute. 
	 * @param value the usageCharge
	 */
	public void setUsageCharge(final UsageCharge value)
	{
		setUsageCharge( getSession().getSessionContext(), value );
	}
	
}
