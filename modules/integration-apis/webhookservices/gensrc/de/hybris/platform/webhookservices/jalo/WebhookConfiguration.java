/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webhookservices.jalo;

import de.hybris.platform.apiregistryservices.jalo.ConsumedDestination;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.integrationservices.jalo.IntegrationObject;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type WebhookConfiguration.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WebhookConfiguration extends GenericItem
{
	/** Qualifier of the <code>WebhookConfiguration.eventType</code> attribute **/
	public static final String EVENTTYPE = "eventType";
	/** Qualifier of the <code>WebhookConfiguration.integrationObject</code> attribute **/
	public static final String INTEGRATIONOBJECT = "integrationObject";
	/** Qualifier of the <code>WebhookConfiguration.destination</code> attribute **/
	public static final String DESTINATION = "destination";
	/** Qualifier of the <code>WebhookConfiguration.filterLocation</code> attribute **/
	public static final String FILTERLOCATION = "filterLocation";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(EVENTTYPE, AttributeMode.INITIAL);
		tmp.put(INTEGRATIONOBJECT, AttributeMode.INITIAL);
		tmp.put(DESTINATION, AttributeMode.INITIAL);
		tmp.put(FILTERLOCATION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebhookConfiguration.destination</code> attribute.
	 * @return the destination - The destination this WebhookConfiguration is to send the payload
	 */
	public ConsumedDestination getDestination(final SessionContext ctx)
	{
		return (ConsumedDestination)getProperty( ctx, "destination".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebhookConfiguration.destination</code> attribute.
	 * @return the destination - The destination this WebhookConfiguration is to send the payload
	 */
	public ConsumedDestination getDestination()
	{
		return getDestination( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebhookConfiguration.destination</code> attribute. 
	 * @param value the destination - The destination this WebhookConfiguration is to send the payload
	 */
	public void setDestination(final SessionContext ctx, final ConsumedDestination value)
	{
		setProperty(ctx, "destination".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebhookConfiguration.destination</code> attribute. 
	 * @param value the destination - The destination this WebhookConfiguration is to send the payload
	 */
	public void setDestination(final ConsumedDestination value)
	{
		setDestination( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebhookConfiguration.eventType</code> attribute.
	 * @return the eventType - The event this WebhookConfiguration responds to
	 */
	public String getEventType(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "eventType".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebhookConfiguration.eventType</code> attribute.
	 * @return the eventType - The event this WebhookConfiguration responds to
	 */
	public String getEventType()
	{
		return getEventType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebhookConfiguration.eventType</code> attribute. 
	 * @param value the eventType - The event this WebhookConfiguration responds to
	 */
	public void setEventType(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "eventType".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebhookConfiguration.eventType</code> attribute. 
	 * @param value the eventType - The event this WebhookConfiguration responds to
	 */
	public void setEventType(final String value)
	{
		setEventType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebhookConfiguration.filterLocation</code> attribute.
	 * @return the filterLocation - Specifies the URI of the filter logic. For example, script://orderFilter.
	 */
	public String getFilterLocation(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "filterLocation".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebhookConfiguration.filterLocation</code> attribute.
	 * @return the filterLocation - Specifies the URI of the filter logic. For example, script://orderFilter.
	 */
	public String getFilterLocation()
	{
		return getFilterLocation( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebhookConfiguration.filterLocation</code> attribute. 
	 * @param value the filterLocation - Specifies the URI of the filter logic. For example, script://orderFilter.
	 */
	public void setFilterLocation(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "filterLocation".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebhookConfiguration.filterLocation</code> attribute. 
	 * @param value the filterLocation - Specifies the URI of the filter logic. For example, script://orderFilter.
	 */
	public void setFilterLocation(final String value)
	{
		setFilterLocation( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebhookConfiguration.integrationObject</code> attribute.
	 * @return the integrationObject - The IntegrationObject this WebhookConfiguration is to use when sending the payload
	 */
	public IntegrationObject getIntegrationObject(final SessionContext ctx)
	{
		return (IntegrationObject)getProperty( ctx, "integrationObject".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebhookConfiguration.integrationObject</code> attribute.
	 * @return the integrationObject - The IntegrationObject this WebhookConfiguration is to use when sending the payload
	 */
	public IntegrationObject getIntegrationObject()
	{
		return getIntegrationObject( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebhookConfiguration.integrationObject</code> attribute. 
	 * @param value the integrationObject - The IntegrationObject this WebhookConfiguration is to use when sending the payload
	 */
	public void setIntegrationObject(final SessionContext ctx, final IntegrationObject value)
	{
		setProperty(ctx, "integrationObject".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebhookConfiguration.integrationObject</code> attribute. 
	 * @param value the integrationObject - The IntegrationObject this WebhookConfiguration is to use when sending the payload
	 */
	public void setIntegrationObject(final IntegrationObject value)
	{
		setIntegrationObject( getSession().getSessionContext(), value );
	}
	
}
