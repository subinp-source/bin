/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.jalo;

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
 * Generated class for type OutboundChannelConfiguration.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class OutboundChannelConfiguration extends GenericItem
{
	/** Qualifier of the <code>OutboundChannelConfiguration.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>OutboundChannelConfiguration.integrationObject</code> attribute **/
	public static final String INTEGRATIONOBJECT = "integrationObject";
	/** Qualifier of the <code>OutboundChannelConfiguration.destination</code> attribute **/
	public static final String DESTINATION = "destination";
	/** Qualifier of the <code>OutboundChannelConfiguration.autoGenerate</code> attribute **/
	public static final String AUTOGENERATE = "autoGenerate";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(INTEGRATIONOBJECT, AttributeMode.INITIAL);
		tmp.put(DESTINATION, AttributeMode.INITIAL);
		tmp.put(AUTOGENERATE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundChannelConfiguration.autoGenerate</code> attribute.
	 * @return the autoGenerate - Identifies whether or not additional outboundsync configuration will automatically generate when this
	 * 						{@code OutboundChannelConfiguration} is created.
	 */
	public Boolean isAutoGenerate(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "autoGenerate".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundChannelConfiguration.autoGenerate</code> attribute.
	 * @return the autoGenerate - Identifies whether or not additional outboundsync configuration will automatically generate when this
	 * 						{@code OutboundChannelConfiguration} is created.
	 */
	public Boolean isAutoGenerate()
	{
		return isAutoGenerate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundChannelConfiguration.autoGenerate</code> attribute. 
	 * @return the autoGenerate - Identifies whether or not additional outboundsync configuration will automatically generate when this
	 * 						{@code OutboundChannelConfiguration} is created.
	 */
	public boolean isAutoGenerateAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isAutoGenerate( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundChannelConfiguration.autoGenerate</code> attribute. 
	 * @return the autoGenerate - Identifies whether or not additional outboundsync configuration will automatically generate when this
	 * 						{@code OutboundChannelConfiguration} is created.
	 */
	public boolean isAutoGenerateAsPrimitive()
	{
		return isAutoGenerateAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundChannelConfiguration.autoGenerate</code> attribute. 
	 * @param value the autoGenerate - Identifies whether or not additional outboundsync configuration will automatically generate when this
	 * 						{@code OutboundChannelConfiguration} is created.
	 */
	public void setAutoGenerate(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "autoGenerate".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundChannelConfiguration.autoGenerate</code> attribute. 
	 * @param value the autoGenerate - Identifies whether or not additional outboundsync configuration will automatically generate when this
	 * 						{@code OutboundChannelConfiguration} is created.
	 */
	public void setAutoGenerate(final Boolean value)
	{
		setAutoGenerate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundChannelConfiguration.autoGenerate</code> attribute. 
	 * @param value the autoGenerate - Identifies whether or not additional outboundsync configuration will automatically generate when this
	 * 						{@code OutboundChannelConfiguration} is created.
	 */
	public void setAutoGenerate(final SessionContext ctx, final boolean value)
	{
		setAutoGenerate( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundChannelConfiguration.autoGenerate</code> attribute. 
	 * @param value the autoGenerate - Identifies whether or not additional outboundsync configuration will automatically generate when this
	 * 						{@code OutboundChannelConfiguration} is created.
	 */
	public void setAutoGenerate(final boolean value)
	{
		setAutoGenerate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundChannelConfiguration.code</code> attribute.
	 * @return the code - The unique value that represents this outbound scenario
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundChannelConfiguration.code</code> attribute.
	 * @return the code - The unique value that represents this outbound scenario
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundChannelConfiguration.code</code> attribute. 
	 * @param value the code - The unique value that represents this outbound scenario
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundChannelConfiguration.code</code> attribute. 
	 * @param value the code - The unique value that represents this outbound scenario
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundChannelConfiguration.destination</code> attribute.
	 * @return the destination - Destination where the Integration Object will be sent for this outbound scenario
	 */
	public ConsumedDestination getDestination(final SessionContext ctx)
	{
		return (ConsumedDestination)getProperty( ctx, "destination".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundChannelConfiguration.destination</code> attribute.
	 * @return the destination - Destination where the Integration Object will be sent for this outbound scenario
	 */
	public ConsumedDestination getDestination()
	{
		return getDestination( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundChannelConfiguration.destination</code> attribute. 
	 * @param value the destination - Destination where the Integration Object will be sent for this outbound scenario
	 */
	public void setDestination(final SessionContext ctx, final ConsumedDestination value)
	{
		setProperty(ctx, "destination".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundChannelConfiguration.destination</code> attribute. 
	 * @param value the destination - Destination where the Integration Object will be sent for this outbound scenario
	 */
	public void setDestination(final ConsumedDestination value)
	{
		setDestination( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundChannelConfiguration.integrationObject</code> attribute.
	 * @return the integrationObject - Integration Object to be sent for this outbound scenario
	 */
	public IntegrationObject getIntegrationObject(final SessionContext ctx)
	{
		return (IntegrationObject)getProperty( ctx, "integrationObject".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OutboundChannelConfiguration.integrationObject</code> attribute.
	 * @return the integrationObject - Integration Object to be sent for this outbound scenario
	 */
	public IntegrationObject getIntegrationObject()
	{
		return getIntegrationObject( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundChannelConfiguration.integrationObject</code> attribute. 
	 * @param value the integrationObject - Integration Object to be sent for this outbound scenario
	 */
	public void setIntegrationObject(final SessionContext ctx, final IntegrationObject value)
	{
		setProperty(ctx, "integrationObject".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OutboundChannelConfiguration.integrationObject</code> attribute. 
	 * @param value the integrationObject - Integration Object to be sent for this outbound scenario
	 */
	public void setIntegrationObject(final IntegrationObject value)
	{
		setIntegrationObject( getSession().getSessionContext(), value );
	}
	
}
