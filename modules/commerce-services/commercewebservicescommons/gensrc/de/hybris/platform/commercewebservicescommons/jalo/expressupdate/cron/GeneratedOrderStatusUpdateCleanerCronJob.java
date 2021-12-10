/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.jalo.expressupdate.cron;

import de.hybris.platform.commercewebservicescommons.constants.CommercewebservicescommonsConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commercewebservicescommons.jalo.expressupdate.cron.OrderStatusUpdateCleanerCronJob OrderStatusUpdateCleanerCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedOrderStatusUpdateCleanerCronJob extends CronJob
{
	/** Qualifier of the <code>OrderStatusUpdateCleanerCronJob.queueTimeLimit</code> attribute **/
	public static final String QUEUETIMELIMIT = "queueTimeLimit";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(QUEUETIMELIMIT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderStatusUpdateCleanerCronJob.queueTimeLimit</code> attribute.
	 * @return the queueTimeLimit - Only elements older than specified value (in minutes) will be removed from the queue
	 */
	public Integer getQueueTimeLimit(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, QUEUETIMELIMIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderStatusUpdateCleanerCronJob.queueTimeLimit</code> attribute.
	 * @return the queueTimeLimit - Only elements older than specified value (in minutes) will be removed from the queue
	 */
	public Integer getQueueTimeLimit()
	{
		return getQueueTimeLimit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderStatusUpdateCleanerCronJob.queueTimeLimit</code> attribute. 
	 * @return the queueTimeLimit - Only elements older than specified value (in minutes) will be removed from the queue
	 */
	public int getQueueTimeLimitAsPrimitive(final SessionContext ctx)
	{
		Integer value = getQueueTimeLimit( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderStatusUpdateCleanerCronJob.queueTimeLimit</code> attribute. 
	 * @return the queueTimeLimit - Only elements older than specified value (in minutes) will be removed from the queue
	 */
	public int getQueueTimeLimitAsPrimitive()
	{
		return getQueueTimeLimitAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OrderStatusUpdateCleanerCronJob.queueTimeLimit</code> attribute. 
	 * @param value the queueTimeLimit - Only elements older than specified value (in minutes) will be removed from the queue
	 */
	public void setQueueTimeLimit(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, QUEUETIMELIMIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OrderStatusUpdateCleanerCronJob.queueTimeLimit</code> attribute. 
	 * @param value the queueTimeLimit - Only elements older than specified value (in minutes) will be removed from the queue
	 */
	public void setQueueTimeLimit(final Integer value)
	{
		setQueueTimeLimit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OrderStatusUpdateCleanerCronJob.queueTimeLimit</code> attribute. 
	 * @param value the queueTimeLimit - Only elements older than specified value (in minutes) will be removed from the queue
	 */
	public void setQueueTimeLimit(final SessionContext ctx, final int value)
	{
		setQueueTimeLimit( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OrderStatusUpdateCleanerCronJob.queueTimeLimit</code> attribute. 
	 * @param value the queueTimeLimit - Only elements older than specified value (in minutes) will be removed from the queue
	 */
	public void setQueueTimeLimit(final int value)
	{
		setQueueTimeLimit( getSession().getSessionContext(), value );
	}
	
}
