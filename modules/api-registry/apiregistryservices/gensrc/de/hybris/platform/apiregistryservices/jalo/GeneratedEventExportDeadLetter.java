/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.jalo;

import de.hybris.platform.apiregistryservices.constants.ApiregistryservicesConstants;
import de.hybris.platform.apiregistryservices.jalo.DestinationTarget;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.apiregistryservices.jalo.EventExportDeadLetter EventExportDeadLetter}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedEventExportDeadLetter extends GenericItem
{
	/** Qualifier of the <code>EventExportDeadLetter.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>EventExportDeadLetter.eventType</code> attribute **/
	public static final String EVENTTYPE = "eventType";
	/** Qualifier of the <code>EventExportDeadLetter.destinationTarget</code> attribute **/
	public static final String DESTINATIONTARGET = "destinationTarget";
	/** Qualifier of the <code>EventExportDeadLetter.destinationChannel</code> attribute **/
	public static final String DESTINATIONCHANNEL = "destinationChannel";
	/** Qualifier of the <code>EventExportDeadLetter.timestamp</code> attribute **/
	public static final String TIMESTAMP = "timestamp";
	/** Qualifier of the <code>EventExportDeadLetter.payload</code> attribute **/
	public static final String PAYLOAD = "payload";
	/** Qualifier of the <code>EventExportDeadLetter.error</code> attribute **/
	public static final String ERROR = "error";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(EVENTTYPE, AttributeMode.INITIAL);
		tmp.put(DESTINATIONTARGET, AttributeMode.INITIAL);
		tmp.put(DESTINATIONCHANNEL, AttributeMode.INITIAL);
		tmp.put(TIMESTAMP, AttributeMode.INITIAL);
		tmp.put(PAYLOAD, AttributeMode.INITIAL);
		tmp.put(ERROR, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.destinationChannel</code> attribute.
	 * @return the destinationChannel - Destination Channel
	 */
	public EnumerationValue getDestinationChannel(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, DESTINATIONCHANNEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.destinationChannel</code> attribute.
	 * @return the destinationChannel - Destination Channel
	 */
	public EnumerationValue getDestinationChannel()
	{
		return getDestinationChannel( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.destinationChannel</code> attribute. 
	 * @param value the destinationChannel - Destination Channel
	 */
	public void setDestinationChannel(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, DESTINATIONCHANNEL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.destinationChannel</code> attribute. 
	 * @param value the destinationChannel - Destination Channel
	 */
	public void setDestinationChannel(final EnumerationValue value)
	{
		setDestinationChannel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.destinationTarget</code> attribute.
	 * @return the destinationTarget - Destination Target
	 */
	public DestinationTarget getDestinationTarget(final SessionContext ctx)
	{
		return (DestinationTarget)getProperty( ctx, DESTINATIONTARGET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.destinationTarget</code> attribute.
	 * @return the destinationTarget - Destination Target
	 */
	public DestinationTarget getDestinationTarget()
	{
		return getDestinationTarget( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.destinationTarget</code> attribute. 
	 * @param value the destinationTarget - Destination Target
	 */
	public void setDestinationTarget(final SessionContext ctx, final DestinationTarget value)
	{
		setProperty(ctx, DESTINATIONTARGET,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.destinationTarget</code> attribute. 
	 * @param value the destinationTarget - Destination Target
	 */
	public void setDestinationTarget(final DestinationTarget value)
	{
		setDestinationTarget( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.error</code> attribute.
	 * @return the error - Response Error
	 */
	public String getError(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ERROR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.error</code> attribute.
	 * @return the error - Response Error
	 */
	public String getError()
	{
		return getError( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.error</code> attribute. 
	 * @param value the error - Response Error
	 */
	public void setError(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ERROR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.error</code> attribute. 
	 * @param value the error - Response Error
	 */
	public void setError(final String value)
	{
		setError( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.eventType</code> attribute.
	 * @return the eventType - Event Type
	 */
	public String getEventType(final SessionContext ctx)
	{
		return (String)getProperty( ctx, EVENTTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.eventType</code> attribute.
	 * @return the eventType - Event Type
	 */
	public String getEventType()
	{
		return getEventType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.eventType</code> attribute. 
	 * @param value the eventType - Event Type
	 */
	public void setEventType(final SessionContext ctx, final String value)
	{
		setProperty(ctx, EVENTTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.eventType</code> attribute. 
	 * @param value the eventType - Event Type
	 */
	public void setEventType(final String value)
	{
		setEventType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.id</code> attribute.
	 * @return the id - Unique id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.id</code> attribute.
	 * @return the id - Unique id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.id</code> attribute. 
	 * @param value the id - Unique id
	 */
	protected void setId(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+ID+"' is not changeable", 0 );
		}
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.id</code> attribute. 
	 * @param value the id - Unique id
	 */
	protected void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.payload</code> attribute.
	 * @return the payload - Json Payload
	 */
	public String getPayload(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PAYLOAD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.payload</code> attribute.
	 * @return the payload - Json Payload
	 */
	public String getPayload()
	{
		return getPayload( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.payload</code> attribute. 
	 * @param value the payload - Json Payload
	 */
	public void setPayload(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PAYLOAD,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.payload</code> attribute. 
	 * @param value the payload - Json Payload
	 */
	public void setPayload(final String value)
	{
		setPayload( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.timestamp</code> attribute.
	 * @return the timestamp - Event Send Time
	 */
	public Date getTimestamp(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, TIMESTAMP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventExportDeadLetter.timestamp</code> attribute.
	 * @return the timestamp - Event Send Time
	 */
	public Date getTimestamp()
	{
		return getTimestamp( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.timestamp</code> attribute. 
	 * @param value the timestamp - Event Send Time
	 */
	public void setTimestamp(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, TIMESTAMP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventExportDeadLetter.timestamp</code> attribute. 
	 * @param value the timestamp - Event Send Time
	 */
	public void setTimestamp(final Date value)
	{
		setTimestamp( getSession().getSessionContext(), value );
	}
	
}
