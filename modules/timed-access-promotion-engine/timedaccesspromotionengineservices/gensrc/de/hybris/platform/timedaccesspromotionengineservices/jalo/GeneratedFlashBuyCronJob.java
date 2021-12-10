/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionengineservices.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.timedaccesspromotionengineservices.constants.TimedaccesspromotionengineservicesConstants;
import de.hybris.platform.timedaccesspromotionengineservices.jalo.FlashBuyCoupon;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cronjob.jalo.CronJob FlashBuyCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedFlashBuyCronJob extends CronJob
{
	/** Qualifier of the <code>FlashBuyCronJob.flashBuyCoupon</code> attribute **/
	public static final String FLASHBUYCOUPON = "flashBuyCoupon";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(FLASHBUYCOUPON, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCronJob.flashBuyCoupon</code> attribute.
	 * @return the flashBuyCoupon
	 */
	public FlashBuyCoupon getFlashBuyCoupon(final SessionContext ctx)
	{
		return (FlashBuyCoupon)getProperty( ctx, FLASHBUYCOUPON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCronJob.flashBuyCoupon</code> attribute.
	 * @return the flashBuyCoupon
	 */
	public FlashBuyCoupon getFlashBuyCoupon()
	{
		return getFlashBuyCoupon( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCronJob.flashBuyCoupon</code> attribute. 
	 * @param value the flashBuyCoupon
	 */
	public void setFlashBuyCoupon(final SessionContext ctx, final FlashBuyCoupon value)
	{
		setProperty(ctx, FLASHBUYCOUPON,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCronJob.flashBuyCoupon</code> attribute. 
	 * @param value the flashBuyCoupon
	 */
	public void setFlashBuyCoupon(final FlashBuyCoupon value)
	{
		setFlashBuyCoupon( getSession().getSessionContext(), value );
	}
	
}
