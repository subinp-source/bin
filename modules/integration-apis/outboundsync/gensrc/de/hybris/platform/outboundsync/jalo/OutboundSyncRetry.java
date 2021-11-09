/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.outboundsync.jalo.OutboundChannelConfiguration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type OutboundSyncRetry.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class OutboundSyncRetry extends GenericItem
{
	/** Qualifier of the <code>OutboundSyncRetry.itemPk</code> attribute **/
	public static final String ITEMPK = "itemPk";
	/** Qualifier of the <code>OutboundSyncRetry.channel</code> attribute **/
	public static final String CHANNEL = "channel";
	/** Qualifier of the <code>OutboundSyncRetry.syncAttempts</code> attribute **/
	public static final String SYNCATTEMPTS = "syncAttempts";
	/** Qualifier of the <code>OutboundSyncRetry.remainingSyncAttempts</code> attribute **/
	public static final String REMAININGSYNCATTEMPTS = "remainingSyncAttempts";
	/** Qualifier of the <code>OutboundSyncRetry.reachedMaxRetries</code> attribute **/
	public static final String REACHEDMAXRETRIES = "reachedMaxRetries";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ITEMPK, AttributeMode.INITIAL);
		tmp.put(CHANNEL, AttributeMode.INITIAL);
		tmp.put(SYNCATTEMPTS, AttributeMode.INITIAL);
		tmp.put(REMAININGSYNCATTEMPTS, AttributeMode.INITIAL);
		tmp.put(REACHEDMAXRETRIES, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.channel</code> attribute.
	 * @return the channel - Channel with the configuration including the Integration Object and the Destination for the synchronization
	 */
	public OutboundChannelConfiguration getChannel(final SessionContext ctx)
	{
		return (OutboundChannelConfiguration)getProperty( ctx, "channel".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.channel</code> attribute.
	 * @return the channel - Channel with the configuration including the Integration Object and the Destination for the synchronization
	 */
	public OutboundChannelConfiguration getChannel()
	{
		return getChannel( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.channel</code> attribute. 
	 * @param value the channel - Channel with the configuration including the Integration Object and the Destination for the synchronization
	 */
	public void setChannel(final SessionContext ctx, final OutboundChannelConfiguration value)
	{
		setProperty(ctx, "channel".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.channel</code> attribute. 
	 * @param value the channel - Channel with the configuration including the Integration Object and the Destination for the synchronization
	 */
	public void setChannel(final OutboundChannelConfiguration value)
	{
		setChannel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.itemPk</code> attribute.
	 * @return the itemPk
	 */
	public Long getItemPk(final SessionContext ctx)
	{
		return (Long)getProperty( ctx, "itemPk".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.itemPk</code> attribute.
	 * @return the itemPk
	 */
	public Long getItemPk()
	{
		return getItemPk( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.itemPk</code> attribute. 
	 * @return the itemPk
	 */
	public long getItemPkAsPrimitive(final SessionContext ctx)
	{
		Long value = getItemPk( ctx );
		return value != null ? value.longValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.itemPk</code> attribute. 
	 * @return the itemPk
	 */
	public long getItemPkAsPrimitive()
	{
		return getItemPkAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.itemPk</code> attribute. 
	 * @param value the itemPk
	 */
	public void setItemPk(final SessionContext ctx, final Long value)
	{
		setProperty(ctx, "itemPk".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.itemPk</code> attribute. 
	 * @param value the itemPk
	 */
	public void setItemPk(final Long value)
	{
		setItemPk( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.itemPk</code> attribute. 
	 * @param value the itemPk
	 */
	public void setItemPk(final SessionContext ctx, final long value)
	{
		setItemPk( ctx,Long.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.itemPk</code> attribute. 
	 * @param value the itemPk
	 */
	public void setItemPk(final long value)
	{
		setItemPk( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.reachedMaxRetries</code> attribute.
	 * @return the reachedMaxRetries - Indicates this retry record has reached the maximum number of retries. Use remainingSyncAttempts after deprecation.
	 */
	public Boolean isReachedMaxRetries(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "reachedMaxRetries".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.reachedMaxRetries</code> attribute.
	 * @return the reachedMaxRetries - Indicates this retry record has reached the maximum number of retries. Use remainingSyncAttempts after deprecation.
	 */
	public Boolean isReachedMaxRetries()
	{
		return isReachedMaxRetries( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.reachedMaxRetries</code> attribute. 
	 * @return the reachedMaxRetries - Indicates this retry record has reached the maximum number of retries. Use remainingSyncAttempts after deprecation.
	 */
	public boolean isReachedMaxRetriesAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isReachedMaxRetries( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.reachedMaxRetries</code> attribute. 
	 * @return the reachedMaxRetries - Indicates this retry record has reached the maximum number of retries. Use remainingSyncAttempts after deprecation.
	 */
	public boolean isReachedMaxRetriesAsPrimitive()
	{
		return isReachedMaxRetriesAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.reachedMaxRetries</code> attribute. 
	 * @param value the reachedMaxRetries - Indicates this retry record has reached the maximum number of retries. Use remainingSyncAttempts after deprecation.
	 */
	public void setReachedMaxRetries(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "reachedMaxRetries".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.reachedMaxRetries</code> attribute. 
	 * @param value the reachedMaxRetries - Indicates this retry record has reached the maximum number of retries. Use remainingSyncAttempts after deprecation.
	 */
	public void setReachedMaxRetries(final Boolean value)
	{
		setReachedMaxRetries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.reachedMaxRetries</code> attribute. 
	 * @param value the reachedMaxRetries - Indicates this retry record has reached the maximum number of retries. Use remainingSyncAttempts after deprecation.
	 */
	public void setReachedMaxRetries(final SessionContext ctx, final boolean value)
	{
		setReachedMaxRetries( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.reachedMaxRetries</code> attribute. 
	 * @param value the reachedMaxRetries - Indicates this retry record has reached the maximum number of retries. Use remainingSyncAttempts after deprecation.
	 */
	public void setReachedMaxRetries(final boolean value)
	{
		setReachedMaxRetries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.remainingSyncAttempts</code> attribute.
	 * @return the remainingSyncAttempts - Number of outbound synchronization attempts remaining before giving up synchronization of the corresponding item.
	 * 						If 0 or negative, no more synchronization attempts will be made.
	 */
	public Integer getRemainingSyncAttempts(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "remainingSyncAttempts".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.remainingSyncAttempts</code> attribute.
	 * @return the remainingSyncAttempts - Number of outbound synchronization attempts remaining before giving up synchronization of the corresponding item.
	 * 						If 0 or negative, no more synchronization attempts will be made.
	 */
	public Integer getRemainingSyncAttempts()
	{
		return getRemainingSyncAttempts( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.remainingSyncAttempts</code> attribute. 
	 * @return the remainingSyncAttempts - Number of outbound synchronization attempts remaining before giving up synchronization of the corresponding item.
	 * 						If 0 or negative, no more synchronization attempts will be made.
	 */
	public int getRemainingSyncAttemptsAsPrimitive(final SessionContext ctx)
	{
		Integer value = getRemainingSyncAttempts( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.remainingSyncAttempts</code> attribute. 
	 * @return the remainingSyncAttempts - Number of outbound synchronization attempts remaining before giving up synchronization of the corresponding item.
	 * 						If 0 or negative, no more synchronization attempts will be made.
	 */
	public int getRemainingSyncAttemptsAsPrimitive()
	{
		return getRemainingSyncAttemptsAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.remainingSyncAttempts</code> attribute. 
	 * @param value the remainingSyncAttempts - Number of outbound synchronization attempts remaining before giving up synchronization of the corresponding item.
	 * 						If 0 or negative, no more synchronization attempts will be made.
	 */
	public void setRemainingSyncAttempts(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "remainingSyncAttempts".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.remainingSyncAttempts</code> attribute. 
	 * @param value the remainingSyncAttempts - Number of outbound synchronization attempts remaining before giving up synchronization of the corresponding item.
	 * 						If 0 or negative, no more synchronization attempts will be made.
	 */
	public void setRemainingSyncAttempts(final Integer value)
	{
		setRemainingSyncAttempts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.remainingSyncAttempts</code> attribute. 
	 * @param value the remainingSyncAttempts - Number of outbound synchronization attempts remaining before giving up synchronization of the corresponding item.
	 * 						If 0 or negative, no more synchronization attempts will be made.
	 */
	public void setRemainingSyncAttempts(final SessionContext ctx, final int value)
	{
		setRemainingSyncAttempts( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.remainingSyncAttempts</code> attribute. 
	 * @param value the remainingSyncAttempts - Number of outbound synchronization attempts remaining before giving up synchronization of the corresponding item.
	 * 						If 0 or negative, no more synchronization attempts will be made.
	 */
	public void setRemainingSyncAttempts(final int value)
	{
		setRemainingSyncAttempts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.syncAttempts</code> attribute.
	 * @return the syncAttempts - Number of attempts to synchronize, that will allow us to keep track of the max number of attempts
	 * 						that we want to perform. This attempts number includes the original attempt as well as all subsequent retries.
	 * 						For example, when first item synchronization attempt fails, a new retry entity will be created and the
	 * 						{@code syncAttempts} are set to 1 although no retries have been attempted yet.
	 */
	public Integer getSyncAttempts(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "syncAttempts".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.syncAttempts</code> attribute.
	 * @return the syncAttempts - Number of attempts to synchronize, that will allow us to keep track of the max number of attempts
	 * 						that we want to perform. This attempts number includes the original attempt as well as all subsequent retries.
	 * 						For example, when first item synchronization attempt fails, a new retry entity will be created and the
	 * 						{@code syncAttempts} are set to 1 although no retries have been attempted yet.
	 */
	public Integer getSyncAttempts()
	{
		return getSyncAttempts( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.syncAttempts</code> attribute. 
	 * @return the syncAttempts - Number of attempts to synchronize, that will allow us to keep track of the max number of attempts
	 * 						that we want to perform. This attempts number includes the original attempt as well as all subsequent retries.
	 * 						For example, when first item synchronization attempt fails, a new retry entity will be created and the
	 * 						{@code syncAttempts} are set to 1 although no retries have been attempted yet.
	 */
	public int getSyncAttemptsAsPrimitive(final SessionContext ctx)
	{
		Integer value = getSyncAttempts( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncRetry.syncAttempts</code> attribute. 
	 * @return the syncAttempts - Number of attempts to synchronize, that will allow us to keep track of the max number of attempts
	 * 						that we want to perform. This attempts number includes the original attempt as well as all subsequent retries.
	 * 						For example, when first item synchronization attempt fails, a new retry entity will be created and the
	 * 						{@code syncAttempts} are set to 1 although no retries have been attempted yet.
	 */
	public int getSyncAttemptsAsPrimitive()
	{
		return getSyncAttemptsAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.syncAttempts</code> attribute. 
	 * @param value the syncAttempts - Number of attempts to synchronize, that will allow us to keep track of the max number of attempts
	 * 						that we want to perform. This attempts number includes the original attempt as well as all subsequent retries.
	 * 						For example, when first item synchronization attempt fails, a new retry entity will be created and the
	 * 						{@code syncAttempts} are set to 1 although no retries have been attempted yet.
	 */
	public void setSyncAttempts(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "syncAttempts".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.syncAttempts</code> attribute. 
	 * @param value the syncAttempts - Number of attempts to synchronize, that will allow us to keep track of the max number of attempts
	 * 						that we want to perform. This attempts number includes the original attempt as well as all subsequent retries.
	 * 						For example, when first item synchronization attempt fails, a new retry entity will be created and the
	 * 						{@code syncAttempts} are set to 1 although no retries have been attempted yet.
	 */
	public void setSyncAttempts(final Integer value)
	{
		setSyncAttempts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.syncAttempts</code> attribute. 
	 * @param value the syncAttempts - Number of attempts to synchronize, that will allow us to keep track of the max number of attempts
	 * 						that we want to perform. This attempts number includes the original attempt as well as all subsequent retries.
	 * 						For example, when first item synchronization attempt fails, a new retry entity will be created and the
	 * 						{@code syncAttempts} are set to 1 although no retries have been attempted yet.
	 */
	public void setSyncAttempts(final SessionContext ctx, final int value)
	{
		setSyncAttempts( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncRetry.syncAttempts</code> attribute. 
	 * @param value the syncAttempts - Number of attempts to synchronize, that will allow us to keep track of the max number of attempts
	 * 						that we want to perform. This attempts number includes the original attempt as well as all subsequent retries.
	 * 						For example, when first item synchronization attempt fails, a new retry entity will be created and the
	 * 						{@code syncAttempts} are set to 1 although no retries have been attempted yet.
	 */
	public void setSyncAttempts(final int value)
	{
		setSyncAttempts( getSession().getSessionContext(), value );
	}
	
}
