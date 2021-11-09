/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.outboundsync.constants.OutboundsyncConstants;
import de.hybris.platform.outboundsync.jalo.OutboundChannelConfiguration;
import de.hybris.platform.outboundsync.jalo.OutboundSyncCronJob;
import de.hybris.platform.outboundsync.jalo.OutboundSyncJob;
import de.hybris.platform.outboundsync.jalo.OutboundSyncRetry;
import de.hybris.platform.outboundsync.jalo.OutboundSyncStreamConfiguration;
import de.hybris.platform.outboundsync.jalo.OutboundSyncStreamConfigurationContainer;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>OutboundsyncManager</code>.
 */
@SuppressWarnings({"unused","cast"})
@SLDSafe
public class OutboundsyncManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public OutboundChannelConfiguration createOutboundChannelConfiguration(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("OutboundChannelConfiguration");
			return (OutboundChannelConfiguration)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating OutboundChannelConfiguration : "+e.getMessage(), 0 );
		}
	}
	
	public OutboundChannelConfiguration createOutboundChannelConfiguration(final Map attributeValues)
	{
		return createOutboundChannelConfiguration( getSession().getSessionContext(), attributeValues );
	}
	
	public OutboundSyncCronJob createOutboundSyncCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("OutboundSyncCronJob");
			return (OutboundSyncCronJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating OutboundSyncCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public OutboundSyncCronJob createOutboundSyncCronJob(final Map attributeValues)
	{
		return createOutboundSyncCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	public OutboundSyncJob createOutboundSyncJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("OutboundSyncJob");
			return (OutboundSyncJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating OutboundSyncJob : "+e.getMessage(), 0 );
		}
	}
	
	public OutboundSyncJob createOutboundSyncJob(final Map attributeValues)
	{
		return createOutboundSyncJob( getSession().getSessionContext(), attributeValues );
	}
	
	public OutboundSyncRetry createOutboundSyncRetry(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("OutboundSyncRetry");
			return (OutboundSyncRetry)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating OutboundSyncRetry : "+e.getMessage(), 0 );
		}
	}
	
	public OutboundSyncRetry createOutboundSyncRetry(final Map attributeValues)
	{
		return createOutboundSyncRetry( getSession().getSessionContext(), attributeValues );
	}
	
	public OutboundSyncStreamConfiguration createOutboundSyncStreamConfiguration(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("OutboundSyncStreamConfiguration");
			return (OutboundSyncStreamConfiguration)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating OutboundSyncStreamConfiguration : "+e.getMessage(), 0 );
		}
	}
	
	public OutboundSyncStreamConfiguration createOutboundSyncStreamConfiguration(final Map attributeValues)
	{
		return createOutboundSyncStreamConfiguration( getSession().getSessionContext(), attributeValues );
	}
	
	public OutboundSyncStreamConfigurationContainer createOutboundSyncStreamConfigurationContainer(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("OutboundSyncStreamConfigurationContainer");
			return (OutboundSyncStreamConfigurationContainer)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating OutboundSyncStreamConfigurationContainer : "+e.getMessage(), 0 );
		}
	}
	
	public OutboundSyncStreamConfigurationContainer createOutboundSyncStreamConfigurationContainer(final Map attributeValues)
	{
		return createOutboundSyncStreamConfigurationContainer( getSession().getSessionContext(), attributeValues );
	}
	
	public static final OutboundsyncManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (OutboundsyncManager) em.getExtension(OutboundsyncConstants.EXTENSIONNAME);
	}
	
	@Override
	public String getName()
	{
		return OutboundsyncConstants.EXTENSIONNAME;
	}
	
}
