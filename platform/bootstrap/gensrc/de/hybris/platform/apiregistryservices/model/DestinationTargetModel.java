/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.enums.RegistrationStatus;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type DestinationTarget first defined at extension apiregistryservices.
 * <p>
 * Configuration of target system (tenant).
 */
@SuppressWarnings("all")
public class DestinationTargetModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DestinationTarget";
	
	/** <i>Generated constant</i> - Attribute key of <code>DestinationTarget.id</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>DestinationTarget.destinationChannel</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String DESTINATIONCHANNEL = "destinationChannel";
	
	/** <i>Generated constant</i> - Attribute key of <code>DestinationTarget.template</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String TEMPLATE = "template";
	
	/** <i>Generated constant</i> - Attribute key of <code>DestinationTarget.registrationStatus</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String REGISTRATIONSTATUS = "registrationStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>DestinationTarget.registrationStatusInfo</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String REGISTRATIONSTATUSINFO = "registrationStatusInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>DestinationTarget.eventConfigurations</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String EVENTCONFIGURATIONS = "eventConfigurations";
	
	/** <i>Generated constant</i> - Attribute key of <code>DestinationTarget.destinations</code> attribute defined at extension <code>apiregistryservices</code>. */
	public static final String DESTINATIONS = "destinations";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DestinationTargetModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DestinationTargetModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>DestinationTarget</code> at extension <code>apiregistryservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public DestinationTargetModel(final String _id)
	{
		super();
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>DestinationTarget</code> at extension <code>apiregistryservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public DestinationTargetModel(final String _id, final ItemModel _owner)
	{
		super();
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DestinationTarget.destinationChannel</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the destinationChannel - Destination Channel
	 */
	@Accessor(qualifier = "destinationChannel", type = Accessor.Type.GETTER)
	public DestinationChannel getDestinationChannel()
	{
		return getPersistenceContext().getPropertyValue(DESTINATIONCHANNEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DestinationTarget.destinations</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the destinations - Destinations
	 */
	@Accessor(qualifier = "destinations", type = Accessor.Type.GETTER)
	public Collection<AbstractDestinationModel> getDestinations()
	{
		return getPersistenceContext().getPropertyValue(DESTINATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DestinationTarget.eventConfigurations</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the eventConfigurations - Event Configurations
	 */
	@Accessor(qualifier = "eventConfigurations", type = Accessor.Type.GETTER)
	public Collection<EventConfigurationModel> getEventConfigurations()
	{
		return getPersistenceContext().getPropertyValue(EVENTCONFIGURATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DestinationTarget.id</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the id - Unique Id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DestinationTarget.registrationStatus</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the registrationStatus - Registration status
	 */
	@Accessor(qualifier = "registrationStatus", type = Accessor.Type.GETTER)
	public RegistrationStatus getRegistrationStatus()
	{
		return getPersistenceContext().getPropertyValue(REGISTRATIONSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DestinationTarget.registrationStatusInfo</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the registrationStatusInfo - Registration status information
	 */
	@Accessor(qualifier = "registrationStatusInfo", type = Accessor.Type.GETTER)
	public String getRegistrationStatusInfo()
	{
		return getPersistenceContext().getPropertyValue(REGISTRATIONSTATUSINFO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DestinationTarget.template</code> attribute defined at extension <code>apiregistryservices</code>. 
	 * @return the template - Template Flag
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.GETTER)
	public Boolean getTemplate()
	{
		return getPersistenceContext().getPropertyValue(TEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DestinationTarget.destinationChannel</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the destinationChannel - Destination Channel
	 */
	@Accessor(qualifier = "destinationChannel", type = Accessor.Type.SETTER)
	public void setDestinationChannel(final DestinationChannel value)
	{
		getPersistenceContext().setPropertyValue(DESTINATIONCHANNEL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DestinationTarget.destinations</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the destinations - Destinations
	 */
	@Accessor(qualifier = "destinations", type = Accessor.Type.SETTER)
	public void setDestinations(final Collection<AbstractDestinationModel> value)
	{
		getPersistenceContext().setPropertyValue(DESTINATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DestinationTarget.eventConfigurations</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the eventConfigurations - Event Configurations
	 */
	@Accessor(qualifier = "eventConfigurations", type = Accessor.Type.SETTER)
	public void setEventConfigurations(final Collection<EventConfigurationModel> value)
	{
		getPersistenceContext().setPropertyValue(EVENTCONFIGURATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DestinationTarget.id</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the id - Unique Id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DestinationTarget.registrationStatus</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the registrationStatus - Registration status
	 */
	@Accessor(qualifier = "registrationStatus", type = Accessor.Type.SETTER)
	public void setRegistrationStatus(final RegistrationStatus value)
	{
		getPersistenceContext().setPropertyValue(REGISTRATIONSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DestinationTarget.registrationStatusInfo</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the registrationStatusInfo - Registration status information
	 */
	@Accessor(qualifier = "registrationStatusInfo", type = Accessor.Type.SETTER)
	public void setRegistrationStatusInfo(final String value)
	{
		getPersistenceContext().setPropertyValue(REGISTRATIONSTATUSINFO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DestinationTarget.template</code> attribute defined at extension <code>apiregistryservices</code>. 
	 *  
	 * @param value the template - Template Flag
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.SETTER)
	public void setTemplate(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(TEMPLATE, value);
	}
	
}
