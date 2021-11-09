/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.jalo;

import de.hybris.platform.apiregistryservices.constants.ApiregistryservicesConstants;
import de.hybris.platform.apiregistryservices.jalo.BasicCredential;
import de.hybris.platform.apiregistryservices.jalo.ConsumedCertificateCredential;
import de.hybris.platform.apiregistryservices.jalo.ConsumedDestination;
import de.hybris.platform.apiregistryservices.jalo.ConsumedOAuthCredential;
import de.hybris.platform.apiregistryservices.jalo.DestinationTarget;
import de.hybris.platform.apiregistryservices.jalo.DestinationTargetCronJob;
import de.hybris.platform.apiregistryservices.jalo.Endpoint;
import de.hybris.platform.apiregistryservices.jalo.EventExportDeadLetter;
import de.hybris.platform.apiregistryservices.jalo.ExposedDestination;
import de.hybris.platform.apiregistryservices.jalo.ExposedOAuthCredential;
import de.hybris.platform.apiregistryservices.jalo.ProcessEventConfiguration;
import de.hybris.platform.apiregistryservices.jalo.constraints.EventMappingConstraint;
import de.hybris.platform.apiregistryservices.jalo.events.EventConfiguration;
import de.hybris.platform.apiregistryservices.jalo.events.EventPropertyConfiguration;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.webservicescommons.jalo.OAuthClientDetails;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>ApiregistryservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedApiregistryservicesManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("oAuthUrl", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.webservicescommons.jalo.OAuthClientDetails", Collections.unmodifiableMap(tmp));
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
	
	public BasicCredential createBasicCredential(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.BASICCREDENTIAL );
			return (BasicCredential)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating BasicCredential : "+e.getMessage(), 0 );
		}
	}
	
	public BasicCredential createBasicCredential(final Map attributeValues)
	{
		return createBasicCredential( getSession().getSessionContext(), attributeValues );
	}
	
	public ConsumedCertificateCredential createConsumedCertificateCredential(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.CONSUMEDCERTIFICATECREDENTIAL );
			return (ConsumedCertificateCredential)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ConsumedCertificateCredential : "+e.getMessage(), 0 );
		}
	}
	
	public ConsumedCertificateCredential createConsumedCertificateCredential(final Map attributeValues)
	{
		return createConsumedCertificateCredential( getSession().getSessionContext(), attributeValues );
	}
	
	public ConsumedDestination createConsumedDestination(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.CONSUMEDDESTINATION );
			return (ConsumedDestination)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ConsumedDestination : "+e.getMessage(), 0 );
		}
	}
	
	public ConsumedDestination createConsumedDestination(final Map attributeValues)
	{
		return createConsumedDestination( getSession().getSessionContext(), attributeValues );
	}
	
	public ConsumedOAuthCredential createConsumedOAuthCredential(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.CONSUMEDOAUTHCREDENTIAL );
			return (ConsumedOAuthCredential)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ConsumedOAuthCredential : "+e.getMessage(), 0 );
		}
	}
	
	public ConsumedOAuthCredential createConsumedOAuthCredential(final Map attributeValues)
	{
		return createConsumedOAuthCredential( getSession().getSessionContext(), attributeValues );
	}
	
	public DestinationTarget createDestinationTarget(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.DESTINATIONTARGET );
			return (DestinationTarget)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating DestinationTarget : "+e.getMessage(), 0 );
		}
	}
	
	public DestinationTarget createDestinationTarget(final Map attributeValues)
	{
		return createDestinationTarget( getSession().getSessionContext(), attributeValues );
	}
	
	public DestinationTargetCronJob createDestinationTargetCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.DESTINATIONTARGETCRONJOB );
			return (DestinationTargetCronJob)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating DestinationTargetCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public DestinationTargetCronJob createDestinationTargetCronJob(final Map attributeValues)
	{
		return createDestinationTargetCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	public Endpoint createEndpoint(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.ENDPOINT );
			return (Endpoint)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating Endpoint : "+e.getMessage(), 0 );
		}
	}
	
	public Endpoint createEndpoint(final Map attributeValues)
	{
		return createEndpoint( getSession().getSessionContext(), attributeValues );
	}
	
	public EventConfiguration createEventConfiguration(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.EVENTCONFIGURATION );
			return (EventConfiguration)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating EventConfiguration : "+e.getMessage(), 0 );
		}
	}
	
	public EventConfiguration createEventConfiguration(final Map attributeValues)
	{
		return createEventConfiguration( getSession().getSessionContext(), attributeValues );
	}
	
	public EventExportDeadLetter createEventExportDeadLetter(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.EVENTEXPORTDEADLETTER );
			return (EventExportDeadLetter)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating EventExportDeadLetter : "+e.getMessage(), 0 );
		}
	}
	
	public EventExportDeadLetter createEventExportDeadLetter(final Map attributeValues)
	{
		return createEventExportDeadLetter( getSession().getSessionContext(), attributeValues );
	}
	
	public EventMappingConstraint createEventMappingConstraint(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.EVENTMAPPINGCONSTRAINT );
			return (EventMappingConstraint)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating EventMappingConstraint : "+e.getMessage(), 0 );
		}
	}
	
	public EventMappingConstraint createEventMappingConstraint(final Map attributeValues)
	{
		return createEventMappingConstraint( getSession().getSessionContext(), attributeValues );
	}
	
	public EventPropertyConfiguration createEventPropertyConfiguration(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.EVENTPROPERTYCONFIGURATION );
			return (EventPropertyConfiguration)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating EventPropertyConfiguration : "+e.getMessage(), 0 );
		}
	}
	
	public EventPropertyConfiguration createEventPropertyConfiguration(final Map attributeValues)
	{
		return createEventPropertyConfiguration( getSession().getSessionContext(), attributeValues );
	}
	
	public ExposedDestination createExposedDestination(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.EXPOSEDDESTINATION );
			return (ExposedDestination)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ExposedDestination : "+e.getMessage(), 0 );
		}
	}
	
	public ExposedDestination createExposedDestination(final Map attributeValues)
	{
		return createExposedDestination( getSession().getSessionContext(), attributeValues );
	}
	
	public ExposedOAuthCredential createExposedOAuthCredential(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.EXPOSEDOAUTHCREDENTIAL );
			return (ExposedOAuthCredential)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ExposedOAuthCredential : "+e.getMessage(), 0 );
		}
	}
	
	public ExposedOAuthCredential createExposedOAuthCredential(final Map attributeValues)
	{
		return createExposedOAuthCredential( getSession().getSessionContext(), attributeValues );
	}
	
	public ProcessEventConfiguration createProcessEventConfiguration(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ApiregistryservicesConstants.TC.PROCESSEVENTCONFIGURATION );
			return (ProcessEventConfiguration)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProcessEventConfiguration : "+e.getMessage(), 0 );
		}
	}
	
	public ProcessEventConfiguration createProcessEventConfiguration(final Map attributeValues)
	{
		return createProcessEventConfiguration( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return ApiregistryservicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.oAuthUrl</code> attribute.
	 * @return the oAuthUrl - URL for accessing oauth
	 */
	public String getOAuthUrl(final SessionContext ctx, final OAuthClientDetails item)
	{
		return (String)item.getProperty( ctx, ApiregistryservicesConstants.Attributes.OAuthClientDetails.OAUTHURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OAuthClientDetails.oAuthUrl</code> attribute.
	 * @return the oAuthUrl - URL for accessing oauth
	 */
	public String getOAuthUrl(final OAuthClientDetails item)
	{
		return getOAuthUrl( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OAuthClientDetails.oAuthUrl</code> attribute. 
	 * @param value the oAuthUrl - URL for accessing oauth
	 */
	public void setOAuthUrl(final SessionContext ctx, final OAuthClientDetails item, final String value)
	{
		item.setProperty(ctx, ApiregistryservicesConstants.Attributes.OAuthClientDetails.OAUTHURL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OAuthClientDetails.oAuthUrl</code> attribute. 
	 * @param value the oAuthUrl - URL for accessing oauth
	 */
	public void setOAuthUrl(final OAuthClientDetails item, final String value)
	{
		setOAuthUrl( getSession().getSessionContext(), item, value );
	}
	
}
