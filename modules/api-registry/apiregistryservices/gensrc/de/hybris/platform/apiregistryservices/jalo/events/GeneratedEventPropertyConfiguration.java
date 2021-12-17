/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.jalo.events;

import de.hybris.platform.apiregistryservices.constants.ApiregistryservicesConstants;
import de.hybris.platform.apiregistryservices.jalo.events.EventConfiguration;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.apiregistryservices.jalo.events.EventPropertyConfiguration EventPropertyConfiguration}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedEventPropertyConfiguration extends GenericItem
{
	/** Qualifier of the <code>EventPropertyConfiguration.propertyName</code> attribute **/
	public static final String PROPERTYNAME = "propertyName";
	/** Qualifier of the <code>EventPropertyConfiguration.propertyMapping</code> attribute **/
	public static final String PROPERTYMAPPING = "propertyMapping";
	/** Qualifier of the <code>EventPropertyConfiguration.type</code> attribute **/
	public static final String TYPE = "type";
	/** Qualifier of the <code>EventPropertyConfiguration.title</code> attribute **/
	public static final String TITLE = "title";
	/** Qualifier of the <code>EventPropertyConfiguration.required</code> attribute **/
	public static final String REQUIRED = "required";
	/** Qualifier of the <code>EventPropertyConfiguration.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>EventPropertyConfiguration.examples</code> attribute **/
	public static final String EXAMPLES = "examples";
	/** Qualifier of the <code>EventPropertyConfiguration.eventConfigurationPOS</code> attribute **/
	public static final String EVENTCONFIGURATIONPOS = "eventConfigurationPOS";
	/** Qualifier of the <code>EventPropertyConfiguration.eventConfiguration</code> attribute **/
	public static final String EVENTCONFIGURATION = "eventConfiguration";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n EVENTCONFIGURATION's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedEventPropertyConfiguration> EVENTCONFIGURATIONHANDLER = new BidirectionalOneToManyHandler<GeneratedEventPropertyConfiguration>(
	ApiregistryservicesConstants.TC.EVENTPROPERTYCONFIGURATION,
	false,
	"eventConfiguration",
	"eventConfigurationPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(PROPERTYNAME, AttributeMode.INITIAL);
		tmp.put(PROPERTYMAPPING, AttributeMode.INITIAL);
		tmp.put(TYPE, AttributeMode.INITIAL);
		tmp.put(TITLE, AttributeMode.INITIAL);
		tmp.put(REQUIRED, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(EXAMPLES, AttributeMode.INITIAL);
		tmp.put(EVENTCONFIGURATIONPOS, AttributeMode.INITIAL);
		tmp.put(EVENTCONFIGURATION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		EVENTCONFIGURATIONHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.description</code> attribute.
	 * @return the description - Human-readable event property description
	 */
	public String getDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.description</code> attribute.
	 * @return the description - Human-readable event property description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.description</code> attribute. 
	 * @param value the description - Human-readable event property description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.description</code> attribute. 
	 * @param value the description - Human-readable event property description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.eventConfiguration</code> attribute.
	 * @return the eventConfiguration - Event Configuration
	 */
	public EventConfiguration getEventConfiguration(final SessionContext ctx)
	{
		return (EventConfiguration)getProperty( ctx, EVENTCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.eventConfiguration</code> attribute.
	 * @return the eventConfiguration - Event Configuration
	 */
	public EventConfiguration getEventConfiguration()
	{
		return getEventConfiguration( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.eventConfiguration</code> attribute. 
	 * @param value the eventConfiguration - Event Configuration
	 */
	protected void setEventConfiguration(final SessionContext ctx, final EventConfiguration value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+EVENTCONFIGURATION+"' is not changeable", 0 );
		}
		EVENTCONFIGURATIONHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.eventConfiguration</code> attribute. 
	 * @param value the eventConfiguration - Event Configuration
	 */
	protected void setEventConfiguration(final EventConfiguration value)
	{
		setEventConfiguration( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.eventConfigurationPOS</code> attribute.
	 * @return the eventConfigurationPOS
	 */
	 Integer getEventConfigurationPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, EVENTCONFIGURATIONPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.eventConfigurationPOS</code> attribute.
	 * @return the eventConfigurationPOS
	 */
	 Integer getEventConfigurationPOS()
	{
		return getEventConfigurationPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.eventConfigurationPOS</code> attribute. 
	 * @return the eventConfigurationPOS
	 */
	 int getEventConfigurationPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getEventConfigurationPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.eventConfigurationPOS</code> attribute. 
	 * @return the eventConfigurationPOS
	 */
	 int getEventConfigurationPOSAsPrimitive()
	{
		return getEventConfigurationPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.eventConfigurationPOS</code> attribute. 
	 * @param value the eventConfigurationPOS
	 */
	 void setEventConfigurationPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, EVENTCONFIGURATIONPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.eventConfigurationPOS</code> attribute. 
	 * @param value the eventConfigurationPOS
	 */
	 void setEventConfigurationPOS(final Integer value)
	{
		setEventConfigurationPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.eventConfigurationPOS</code> attribute. 
	 * @param value the eventConfigurationPOS
	 */
	 void setEventConfigurationPOS(final SessionContext ctx, final int value)
	{
		setEventConfigurationPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.eventConfigurationPOS</code> attribute. 
	 * @param value the eventConfigurationPOS
	 */
	 void setEventConfigurationPOS(final int value)
	{
		setEventConfigurationPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.examples</code> attribute.
	 * @return the examples - Extracting result example
	 */
	public Map<String,String> getAllExamples(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, EXAMPLES);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.examples</code> attribute.
	 * @return the examples - Extracting result example
	 */
	public Map<String,String> getAllExamples()
	{
		return getAllExamples( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.examples</code> attribute. 
	 * @param value the examples - Extracting result example
	 */
	public void setAllExamples(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, EXAMPLES,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.examples</code> attribute. 
	 * @param value the examples - Extracting result example
	 */
	public void setAllExamples(final Map<String,String> value)
	{
		setAllExamples( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.propertyMapping</code> attribute.
	 * @return the propertyMapping - Extracting Path
	 */
	public String getPropertyMapping(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PROPERTYMAPPING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.propertyMapping</code> attribute.
	 * @return the propertyMapping - Extracting Path
	 */
	public String getPropertyMapping()
	{
		return getPropertyMapping( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.propertyMapping</code> attribute. 
	 * @param value the propertyMapping - Extracting Path
	 */
	public void setPropertyMapping(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PROPERTYMAPPING,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.propertyMapping</code> attribute. 
	 * @param value the propertyMapping - Extracting Path
	 */
	public void setPropertyMapping(final String value)
	{
		setPropertyMapping( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.propertyName</code> attribute.
	 * @return the propertyName - Target property Id
	 */
	public String getPropertyName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PROPERTYNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.propertyName</code> attribute.
	 * @return the propertyName - Target property Id
	 */
	public String getPropertyName()
	{
		return getPropertyName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.propertyName</code> attribute. 
	 * @param value the propertyName - Target property Id
	 */
	protected void setPropertyName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+PROPERTYNAME+"' is not changeable", 0 );
		}
		setProperty(ctx, PROPERTYNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.propertyName</code> attribute. 
	 * @param value the propertyName - Target property Id
	 */
	protected void setPropertyName(final String value)
	{
		setPropertyName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.required</code> attribute.
	 * @return the required - Required Flag
	 */
	public Boolean isRequired(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, REQUIRED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.required</code> attribute.
	 * @return the required - Required Flag
	 */
	public Boolean isRequired()
	{
		return isRequired( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.required</code> attribute. 
	 * @return the required - Required Flag
	 */
	public boolean isRequiredAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isRequired( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.required</code> attribute. 
	 * @return the required - Required Flag
	 */
	public boolean isRequiredAsPrimitive()
	{
		return isRequiredAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.required</code> attribute. 
	 * @param value the required - Required Flag
	 */
	public void setRequired(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, REQUIRED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.required</code> attribute. 
	 * @param value the required - Required Flag
	 */
	public void setRequired(final Boolean value)
	{
		setRequired( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.required</code> attribute. 
	 * @param value the required - Required Flag
	 */
	public void setRequired(final SessionContext ctx, final boolean value)
	{
		setRequired( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.required</code> attribute. 
	 * @param value the required - Required Flag
	 */
	public void setRequired(final boolean value)
	{
		setRequired( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.title</code> attribute.
	 * @return the title - Human-readable Title
	 */
	public String getTitle(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.title</code> attribute.
	 * @return the title - Human-readable Title
	 */
	public String getTitle()
	{
		return getTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.title</code> attribute. 
	 * @param value the title - Human-readable Title
	 */
	public void setTitle(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.title</code> attribute. 
	 * @param value the title - Human-readable Title
	 */
	public void setTitle(final String value)
	{
		setTitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.type</code> attribute.
	 * @return the type - Result Type
	 */
	public String getType(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.type</code> attribute.
	 * @return the type - Result Type
	 */
	public String getType()
	{
		return getType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.type</code> attribute. 
	 * @param value the type - Result Type
	 */
	public void setType(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventPropertyConfiguration.type</code> attribute. 
	 * @param value the type - Result Type
	 */
	public void setType(final String value)
	{
		setType( getSession().getSessionContext(), value );
	}
	
}
