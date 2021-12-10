/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.jalo.events;

import de.hybris.platform.apiregistryservices.constants.ApiregistryservicesConstants;
import de.hybris.platform.apiregistryservices.jalo.DestinationTarget;
import de.hybris.platform.apiregistryservices.jalo.events.EventPropertyConfiguration;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.apiregistryservices.jalo.events.EventConfiguration EventConfiguration}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedEventConfiguration extends GenericItem
{
	/** Qualifier of the <code>EventConfiguration.eventClass</code> attribute **/
	public static final String EVENTCLASS = "eventClass";
	/** Qualifier of the <code>EventConfiguration.version</code> attribute **/
	public static final String VERSION = "version";
	/** Qualifier of the <code>EventConfiguration.exportFlag</code> attribute **/
	public static final String EXPORTFLAG = "exportFlag";
	/** Qualifier of the <code>EventConfiguration.priority</code> attribute **/
	public static final String PRIORITY = "priority";
	/** Qualifier of the <code>EventConfiguration.exportName</code> attribute **/
	public static final String EXPORTNAME = "exportName";
	/** Qualifier of the <code>EventConfiguration.mappingType</code> attribute **/
	public static final String MAPPINGTYPE = "mappingType";
	/** Qualifier of the <code>EventConfiguration.converterBean</code> attribute **/
	public static final String CONVERTERBEAN = "converterBean";
	/** Qualifier of the <code>EventConfiguration.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>EventConfiguration.extensionName</code> attribute **/
	public static final String EXTENSIONNAME = "extensionName";
	/** Qualifier of the <code>EventConfiguration.eventPropertyConfigurations</code> attribute **/
	public static final String EVENTPROPERTYCONFIGURATIONS = "eventPropertyConfigurations";
	/** Qualifier of the <code>EventConfiguration.destinationTarget</code> attribute **/
	public static final String DESTINATIONTARGET = "destinationTarget";
	/**
	* {@link OneToManyHandler} for handling 1:n EVENTPROPERTYCONFIGURATIONS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<EventPropertyConfiguration> EVENTPROPERTYCONFIGURATIONSHANDLER = new OneToManyHandler<EventPropertyConfiguration>(
	ApiregistryservicesConstants.TC.EVENTPROPERTYCONFIGURATION,
	true,
	"eventConfiguration",
	"eventConfigurationPOS",
	true,
	true,
	CollectionType.LIST
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n DESTINATIONTARGET's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedEventConfiguration> DESTINATIONTARGETHANDLER = new BidirectionalOneToManyHandler<GeneratedEventConfiguration>(
	ApiregistryservicesConstants.TC.EVENTCONFIGURATION,
	false,
	"destinationTarget",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(EVENTCLASS, AttributeMode.INITIAL);
		tmp.put(VERSION, AttributeMode.INITIAL);
		tmp.put(EXPORTFLAG, AttributeMode.INITIAL);
		tmp.put(PRIORITY, AttributeMode.INITIAL);
		tmp.put(EXPORTNAME, AttributeMode.INITIAL);
		tmp.put(MAPPINGTYPE, AttributeMode.INITIAL);
		tmp.put(CONVERTERBEAN, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(EXTENSIONNAME, AttributeMode.INITIAL);
		tmp.put(DESTINATIONTARGET, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.converterBean</code> attribute.
	 * @return the converterBean - Spring Bean Name
	 */
	public String getConverterBean(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CONVERTERBEAN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.converterBean</code> attribute.
	 * @return the converterBean - Spring Bean Name
	 */
	public String getConverterBean()
	{
		return getConverterBean( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.converterBean</code> attribute. 
	 * @param value the converterBean - Spring Bean Name
	 */
	public void setConverterBean(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CONVERTERBEAN,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.converterBean</code> attribute. 
	 * @param value the converterBean - Spring Bean Name
	 */
	public void setConverterBean(final String value)
	{
		setConverterBean( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		DESTINATIONTARGETHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.description</code> attribute.
	 * @return the description - Human-readable Description
	 */
	public String getDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.description</code> attribute.
	 * @return the description - Human-readable Description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.description</code> attribute. 
	 * @param value the description - Human-readable Description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.description</code> attribute. 
	 * @param value the description - Human-readable Description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.destinationTarget</code> attribute.
	 * @return the destinationTarget - Destination Target
	 */
	public DestinationTarget getDestinationTarget(final SessionContext ctx)
	{
		return (DestinationTarget)getProperty( ctx, DESTINATIONTARGET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.destinationTarget</code> attribute.
	 * @return the destinationTarget - Destination Target
	 */
	public DestinationTarget getDestinationTarget()
	{
		return getDestinationTarget( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.destinationTarget</code> attribute. 
	 * @param value the destinationTarget - Destination Target
	 */
	protected void setDestinationTarget(final SessionContext ctx, final DestinationTarget value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+DESTINATIONTARGET+"' is not changeable", 0 );
		}
		DESTINATIONTARGETHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.destinationTarget</code> attribute. 
	 * @param value the destinationTarget - Destination Target
	 */
	protected void setDestinationTarget(final DestinationTarget value)
	{
		setDestinationTarget( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.eventClass</code> attribute.
	 * @return the eventClass - Full path of Class
	 */
	public String getEventClass(final SessionContext ctx)
	{
		return (String)getProperty( ctx, EVENTCLASS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.eventClass</code> attribute.
	 * @return the eventClass - Full path of Class
	 */
	public String getEventClass()
	{
		return getEventClass( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.eventClass</code> attribute. 
	 * @param value the eventClass - Full path of Class
	 */
	public void setEventClass(final SessionContext ctx, final String value)
	{
		setProperty(ctx, EVENTCLASS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.eventClass</code> attribute. 
	 * @param value the eventClass - Full path of Class
	 */
	public void setEventClass(final String value)
	{
		setEventClass( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.eventPropertyConfigurations</code> attribute.
	 * @return the eventPropertyConfigurations - Event Property Configurations
	 */
	public List<EventPropertyConfiguration> getEventPropertyConfigurations(final SessionContext ctx)
	{
		return (List<EventPropertyConfiguration>)EVENTPROPERTYCONFIGURATIONSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.eventPropertyConfigurations</code> attribute.
	 * @return the eventPropertyConfigurations - Event Property Configurations
	 */
	public List<EventPropertyConfiguration> getEventPropertyConfigurations()
	{
		return getEventPropertyConfigurations( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.eventPropertyConfigurations</code> attribute. 
	 * @param value the eventPropertyConfigurations - Event Property Configurations
	 */
	public void setEventPropertyConfigurations(final SessionContext ctx, final List<EventPropertyConfiguration> value)
	{
		EVENTPROPERTYCONFIGURATIONSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.eventPropertyConfigurations</code> attribute. 
	 * @param value the eventPropertyConfigurations - Event Property Configurations
	 */
	public void setEventPropertyConfigurations(final List<EventPropertyConfiguration> value)
	{
		setEventPropertyConfigurations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to eventPropertyConfigurations. 
	 * @param value the item to add to eventPropertyConfigurations - Event Property Configurations
	 */
	public void addToEventPropertyConfigurations(final SessionContext ctx, final EventPropertyConfiguration value)
	{
		EVENTPROPERTYCONFIGURATIONSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to eventPropertyConfigurations. 
	 * @param value the item to add to eventPropertyConfigurations - Event Property Configurations
	 */
	public void addToEventPropertyConfigurations(final EventPropertyConfiguration value)
	{
		addToEventPropertyConfigurations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from eventPropertyConfigurations. 
	 * @param value the item to remove from eventPropertyConfigurations - Event Property Configurations
	 */
	public void removeFromEventPropertyConfigurations(final SessionContext ctx, final EventPropertyConfiguration value)
	{
		EVENTPROPERTYCONFIGURATIONSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from eventPropertyConfigurations. 
	 * @param value the item to remove from eventPropertyConfigurations - Event Property Configurations
	 */
	public void removeFromEventPropertyConfigurations(final EventPropertyConfiguration value)
	{
		removeFromEventPropertyConfigurations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.exportFlag</code> attribute.
	 * @return the exportFlag - Export Flag. Indicates whether the event or its specification is exported to its target destination.
	 */
	public Boolean isExportFlag(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, EXPORTFLAG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.exportFlag</code> attribute.
	 * @return the exportFlag - Export Flag. Indicates whether the event or its specification is exported to its target destination.
	 */
	public Boolean isExportFlag()
	{
		return isExportFlag( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.exportFlag</code> attribute. 
	 * @return the exportFlag - Export Flag. Indicates whether the event or its specification is exported to its target destination.
	 */
	public boolean isExportFlagAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isExportFlag( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.exportFlag</code> attribute. 
	 * @return the exportFlag - Export Flag. Indicates whether the event or its specification is exported to its target destination.
	 */
	public boolean isExportFlagAsPrimitive()
	{
		return isExportFlagAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.exportFlag</code> attribute. 
	 * @param value the exportFlag - Export Flag. Indicates whether the event or its specification is exported to its target destination.
	 */
	public void setExportFlag(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, EXPORTFLAG,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.exportFlag</code> attribute. 
	 * @param value the exportFlag - Export Flag. Indicates whether the event or its specification is exported to its target destination.
	 */
	public void setExportFlag(final Boolean value)
	{
		setExportFlag( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.exportFlag</code> attribute. 
	 * @param value the exportFlag - Export Flag. Indicates whether the event or its specification is exported to its target destination.
	 */
	public void setExportFlag(final SessionContext ctx, final boolean value)
	{
		setExportFlag( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.exportFlag</code> attribute. 
	 * @param value the exportFlag - Export Flag. Indicates whether the event or its specification is exported to its target destination.
	 */
	public void setExportFlag(final boolean value)
	{
		setExportFlag( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.exportName</code> attribute.
	 * @return the exportName - Name of the event in the target system
	 */
	public String getExportName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, EXPORTNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.exportName</code> attribute.
	 * @return the exportName - Name of the event in the target system
	 */
	public String getExportName()
	{
		return getExportName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.exportName</code> attribute. 
	 * @param value the exportName - Name of the event in the target system
	 */
	public void setExportName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, EXPORTNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.exportName</code> attribute. 
	 * @param value the exportName - Name of the event in the target system
	 */
	public void setExportName(final String value)
	{
		setExportName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.extensionName</code> attribute.
	 * @return the extensionName - Name of the EC extension where the event class is defined
	 */
	public String getExtensionName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, EXTENSIONNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.extensionName</code> attribute.
	 * @return the extensionName - Name of the EC extension where the event class is defined
	 */
	public String getExtensionName()
	{
		return getExtensionName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.extensionName</code> attribute. 
	 * @param value the extensionName - Name of the EC extension where the event class is defined
	 */
	public void setExtensionName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, EXTENSIONNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.extensionName</code> attribute. 
	 * @param value the extensionName - Name of the EC extension where the event class is defined
	 */
	public void setExtensionName(final String value)
	{
		setExtensionName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.mappingType</code> attribute.
	 * @return the mappingType - Way of event data mapping
	 */
	public EnumerationValue getMappingType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, MAPPINGTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.mappingType</code> attribute.
	 * @return the mappingType - Way of event data mapping
	 */
	public EnumerationValue getMappingType()
	{
		return getMappingType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.mappingType</code> attribute. 
	 * @param value the mappingType - Way of event data mapping
	 */
	public void setMappingType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, MAPPINGTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.mappingType</code> attribute. 
	 * @param value the mappingType - Way of event data mapping
	 */
	public void setMappingType(final EnumerationValue value)
	{
		setMappingType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.priority</code> attribute.
	 * @return the priority - Export Priority
	 */
	public EnumerationValue getPriority(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.priority</code> attribute.
	 * @return the priority - Export Priority
	 */
	public EnumerationValue getPriority()
	{
		return getPriority( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.priority</code> attribute. 
	 * @param value the priority - Export Priority
	 */
	public void setPriority(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, PRIORITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.priority</code> attribute. 
	 * @param value the priority - Export Priority
	 */
	public void setPriority(final EnumerationValue value)
	{
		setPriority( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.version</code> attribute.
	 * @return the version - Event Configuration Version
	 */
	public Integer getVersion(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.version</code> attribute.
	 * @return the version - Event Configuration Version
	 */
	public Integer getVersion()
	{
		return getVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.version</code> attribute. 
	 * @return the version - Event Configuration Version
	 */
	public int getVersionAsPrimitive(final SessionContext ctx)
	{
		Integer value = getVersion( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventConfiguration.version</code> attribute. 
	 * @return the version - Event Configuration Version
	 */
	public int getVersionAsPrimitive()
	{
		return getVersionAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.version</code> attribute. 
	 * @param value the version - Event Configuration Version
	 */
	protected void setVersion(final SessionContext ctx, final Integer value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+VERSION+"' is not changeable", 0 );
		}
		setProperty(ctx, VERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.version</code> attribute. 
	 * @param value the version - Event Configuration Version
	 */
	protected void setVersion(final Integer value)
	{
		setVersion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.version</code> attribute. 
	 * @param value the version - Event Configuration Version
	 */
	protected void setVersion(final SessionContext ctx, final int value)
	{
		setVersion( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventConfiguration.version</code> attribute. 
	 * @param value the version - Event Configuration Version
	 */
	protected void setVersion(final int value)
	{
		setVersion( getSession().getSessionContext(), value );
	}
	
}
