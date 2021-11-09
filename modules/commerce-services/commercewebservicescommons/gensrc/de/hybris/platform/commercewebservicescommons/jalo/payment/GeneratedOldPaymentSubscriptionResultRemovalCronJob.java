/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.jalo.payment;

import de.hybris.platform.commercewebservicescommons.constants.CommercewebservicescommonsConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commercewebservicescommons.jalo.payment.OldPaymentSubscriptionResultRemovalCronJob OldPaymentSubscriptionResultRemovalCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedOldPaymentSubscriptionResultRemovalCronJob extends CronJob
{
	/** Qualifier of the <code>OldPaymentSubscriptionResultRemovalCronJob.age</code> attribute **/
	public static final String AGE = "age";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(AGE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OldPaymentSubscriptionResultRemovalCronJob.age</code> attribute.
	 * @return the age - After specified number of seconds payment subscription result will be cleaned up. Default is 24 hour.
	 */
	public Integer getAge(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, AGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OldPaymentSubscriptionResultRemovalCronJob.age</code> attribute.
	 * @return the age - After specified number of seconds payment subscription result will be cleaned up. Default is 24 hour.
	 */
	public Integer getAge()
	{
		return getAge( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OldPaymentSubscriptionResultRemovalCronJob.age</code> attribute. 
	 * @return the age - After specified number of seconds payment subscription result will be cleaned up. Default is 24 hour.
	 */
	public int getAgeAsPrimitive(final SessionContext ctx)
	{
		Integer value = getAge( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OldPaymentSubscriptionResultRemovalCronJob.age</code> attribute. 
	 * @return the age - After specified number of seconds payment subscription result will be cleaned up. Default is 24 hour.
	 */
	public int getAgeAsPrimitive()
	{
		return getAgeAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OldPaymentSubscriptionResultRemovalCronJob.age</code> attribute. 
	 * @param value the age - After specified number of seconds payment subscription result will be cleaned up. Default is 24 hour.
	 */
	public void setAge(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, AGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OldPaymentSubscriptionResultRemovalCronJob.age</code> attribute. 
	 * @param value the age - After specified number of seconds payment subscription result will be cleaned up. Default is 24 hour.
	 */
	public void setAge(final Integer value)
	{
		setAge( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OldPaymentSubscriptionResultRemovalCronJob.age</code> attribute. 
	 * @param value the age - After specified number of seconds payment subscription result will be cleaned up. Default is 24 hour.
	 */
	public void setAge(final SessionContext ctx, final int value)
	{
		setAge( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OldPaymentSubscriptionResultRemovalCronJob.age</code> attribute. 
	 * @param value the age - After specified number of seconds payment subscription result will be cleaned up. Default is 24 hour.
	 */
	public void setAge(final int value)
	{
		setAge( getSession().getSessionContext(), value );
	}
	
}
