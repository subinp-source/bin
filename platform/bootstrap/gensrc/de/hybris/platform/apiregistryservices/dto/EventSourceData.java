/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.dto;

import java.io.Serializable;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


import java.util.Objects;
public  class EventSourceData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>EventSourceData.event</code> property defined at extension <code>apiregistryservices</code>. */
		
	private AbstractEvent event;

	/** <i>Generated property</i> for <code>EventSourceData.eventConfig</code> property defined at extension <code>apiregistryservices</code>. */
		
	private EventConfigurationModel eventConfig;
	
	public EventSourceData()
	{
		// default constructor
	}
	
	public void setEvent(final AbstractEvent event)
	{
		this.event = event;
	}

	public AbstractEvent getEvent() 
	{
		return event;
	}
	
	public void setEventConfig(final EventConfigurationModel eventConfig)
	{
		this.eventConfig = eventConfig;
	}

	public EventConfigurationModel getEventConfig() 
	{
		return eventConfig;
	}
	

}