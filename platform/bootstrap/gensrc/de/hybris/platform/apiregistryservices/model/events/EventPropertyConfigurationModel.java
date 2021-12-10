/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.model.events;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Map;

/**
 * Generated model class for type EventPropertyConfiguration first defined at extension apiregistryservices.
 * <p>
 * Exporting configuration for single event property.
 */
@SuppressWarnings("all")
public class EventPropertyConfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "EventPropertyConfiguration";
	
	/**<i>Generated relation code constant for relation <code>EventConfiguration2EventPropertyConfiguration</code> defining source attribute <code>eventConfiguration</code> in extension <code>apiregistryservices</code>.</i>*/
	public static final String _EVENTCONFIGURATION2EVENTPROPERTYCONFIGURATION = "EventConfiguration2EventPropertyConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>EventPropertyConfiguration.propertyName</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String PROPERTYNAME = "propertyName";
	
	/** <i>Generated constant</i> - Attribute key of <code>EventPropertyConfiguration.propertyMapping</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String PROPERTYMAPPING = "propertyMapping";
	
	/** <i>Generated constant</i> - Attribute key of <code>EventPropertyConfiguration.type</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>EventPropertyConfiguration.title</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>EventPropertyConfiguration.required</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String REQUIRED = "required";
	
	/** <i>Generated constant</i> - Attribute key of <code>EventPropertyConfiguration.description</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>EventPropertyConfiguration.examples</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String EXAMPLES = "examples";
	
	/** <i>Generated constant</i> - Attribute key of <code>EventPropertyConfiguration.eventConfigurationPOS</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String EVENTCONFIGURATIONPOS = "eventConfigurationPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>EventPropertyConfiguration.eventConfiguration</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String EVENTCONFIGURATION = "eventConfiguration";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public EventPropertyConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public EventPropertyConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _eventConfiguration initial attribute declared by type <code>EventPropertyConfiguration</code> at extension <code>apiregistryservices</code>
	 * @param _propertyMapping initial attribute declared by type <code>EventPropertyConfiguration</code> at extension <code>apiregistryservices</code>
	 * @param _propertyName initial attribute declared by type <code>EventPropertyConfiguration</code> at extension <code>apiregistryservices</code>
	 * @param _title initial attribute declared by type <code>EventPropertyConfiguration</code> at extension <code>apiregistryservices</code>
	 * @param _type initial attribute declared by type <code>EventPropertyConfiguration</code> at extension <code>apiregistryservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public EventPropertyConfigurationModel(final EventConfigurationModel _eventConfiguration, final String _propertyMapping, final String _propertyName, final String _title, final String _type)
	{
		super();
		setEventConfiguration(_eventConfiguration);
		setPropertyMapping(_propertyMapping);
		setPropertyName(_propertyName);
		setTitle(_title);
		setType(_type);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _eventConfiguration initial attribute declared by type <code>EventPropertyConfiguration</code> at extension <code>apiregistryservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _propertyMapping initial attribute declared by type <code>EventPropertyConfiguration</code> at extension <code>apiregistryservices</code>
	 * @param _propertyName initial attribute declared by type <code>EventPropertyConfiguration</code> at extension <code>apiregistryservices</code>
	 * @param _title initial attribute declared by type <code>EventPropertyConfiguration</code> at extension <code>apiregistryservices</code>
	 * @param _type initial attribute declared by type <code>EventPropertyConfiguration</code> at extension <code>apiregistryservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public EventPropertyConfigurationModel(final EventConfigurationModel _eventConfiguration, final ItemModel _owner, final String _propertyMapping, final String _propertyName, final String _title, final String _type)
	{
		super();
		setEventConfiguration(_eventConfiguration);
		setOwner(_owner);
		setPropertyMapping(_propertyMapping);
		setPropertyName(_propertyName);
		setTitle(_title);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.description</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the description - Human-readable event property description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.eventConfiguration</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the eventConfiguration - Event Configuration
	 */
	@Accessor(qualifier = "eventConfiguration", type = Accessor.Type.GETTER)
	public EventConfigurationModel getEventConfiguration()
	{
		return getPersistenceContext().getPropertyValue(EVENTCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.examples</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the examples - Extracting result example
	 */
	@Accessor(qualifier = "examples", type = Accessor.Type.GETTER)
	public Map<String,String> getExamples()
	{
		return getPersistenceContext().getPropertyValue(EXAMPLES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.propertyMapping</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the propertyMapping - Extracting Path
	 */
	@Accessor(qualifier = "propertyMapping", type = Accessor.Type.GETTER)
	public String getPropertyMapping()
	{
		return getPersistenceContext().getPropertyValue(PROPERTYMAPPING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.propertyName</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the propertyName - Target property Id
	 */
	@Accessor(qualifier = "propertyName", type = Accessor.Type.GETTER)
	public String getPropertyName()
	{
		return getPersistenceContext().getPropertyValue(PROPERTYNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.title</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the title - Human-readable Title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getPersistenceContext().getPropertyValue(TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.type</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the type - Result Type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public String getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventPropertyConfiguration.required</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the required - Required Flag
	 */
	@Accessor(qualifier = "required", type = Accessor.Type.GETTER)
	public boolean isRequired()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(REQUIRED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EventPropertyConfiguration.description</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the description - Human-readable event property description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>EventPropertyConfiguration.eventConfiguration</code> attribute defined at extension <code>apiregistryservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the eventConfiguration - Event Configuration
	 */
	@Accessor(qualifier = "eventConfiguration", type = Accessor.Type.SETTER)
	public void setEventConfiguration(final EventConfigurationModel value)
	{
		getPersistenceContext().setPropertyValue(EVENTCONFIGURATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EventPropertyConfiguration.examples</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the examples - Extracting result example
	 */
	@Accessor(qualifier = "examples", type = Accessor.Type.SETTER)
	public void setExamples(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(EXAMPLES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EventPropertyConfiguration.propertyMapping</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the propertyMapping - Extracting Path
	 */
	@Accessor(qualifier = "propertyMapping", type = Accessor.Type.SETTER)
	public void setPropertyMapping(final String value)
	{
		getPersistenceContext().setPropertyValue(PROPERTYMAPPING, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>EventPropertyConfiguration.propertyName</code> attribute defined at extension <code>apiregistryservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the propertyName - Target property Id
	 */
	@Accessor(qualifier = "propertyName", type = Accessor.Type.SETTER)
	public void setPropertyName(final String value)
	{
		getPersistenceContext().setPropertyValue(PROPERTYNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EventPropertyConfiguration.required</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the required - Required Flag
	 */
	@Accessor(qualifier = "required", type = Accessor.Type.SETTER)
	public void setRequired(final boolean value)
	{
		getPersistenceContext().setPropertyValue(REQUIRED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EventPropertyConfiguration.title</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the title - Human-readable Title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		getPersistenceContext().setPropertyValue(TITLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EventPropertyConfiguration.type</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the type - Result Type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final String value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
}
