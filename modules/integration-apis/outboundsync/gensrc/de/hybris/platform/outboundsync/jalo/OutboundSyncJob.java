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
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.outboundsync.jalo.OutboundSyncStreamConfigurationContainer;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type OutboundSyncJob.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class OutboundSyncJob extends ServicelayerJob
{
	/** Qualifier of the <code>OutboundSyncJob.streamConfigurationContainer</code> attribute **/
	public static final String STREAMCONFIGURATIONCONTAINER = "streamConfigurationContainer";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(ServicelayerJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(STREAMCONFIGURATIONCONTAINER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncJob.streamConfigurationContainer</code> attribute.
	 * @return the streamConfigurationContainer
	 */
	public OutboundSyncStreamConfigurationContainer getStreamConfigurationContainer(final SessionContext ctx)
	{
		return (OutboundSyncStreamConfigurationContainer)getProperty( ctx, "streamConfigurationContainer".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundSyncJob.streamConfigurationContainer</code> attribute.
	 * @return the streamConfigurationContainer
	 */
	public OutboundSyncStreamConfigurationContainer getStreamConfigurationContainer()
	{
		return getStreamConfigurationContainer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncJob.streamConfigurationContainer</code> attribute. 
	 * @param value the streamConfigurationContainer
	 */
	protected void setStreamConfigurationContainer(final SessionContext ctx, final OutboundSyncStreamConfigurationContainer value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+"streamConfigurationContainer".intern()+"' is not changeable", 0 );
		}
		setProperty(ctx, "streamConfigurationContainer".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundSyncJob.streamConfigurationContainer</code> attribute. 
	 * @param value the streamConfigurationContainer
	 */
	protected void setStreamConfigurationContainer(final OutboundSyncStreamConfigurationContainer value)
	{
		setStreamConfigurationContainer( getSession().getSessionContext(), value );
	}
	
}
