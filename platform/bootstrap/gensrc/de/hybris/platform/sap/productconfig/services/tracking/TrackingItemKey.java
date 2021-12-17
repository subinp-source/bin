/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.tracking;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.services.tracking.EventType;
import java.time.LocalDateTime;


import java.util.Objects;
public  class TrackingItemKey  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Name of the action that is described within this item<br/><br/><i>Generated property</i> for <code>TrackingItemKey.eventType</code> property defined at extension <code>sapproductconfigservices</code>. */
		
	private EventType eventType;

	/** Time at which the action occurred<br/><br/><i>Generated property</i> for <code>TrackingItemKey.timestamp</code> property defined at extension <code>sapproductconfigservices</code>. */
		
	private LocalDateTime timestamp;

	/** User session id<br/><br/><i>Generated property</i> for <code>TrackingItemKey.sessionId</code> property defined at extension <code>sapproductconfigservices</code>. */
		
	private String sessionId;

	/** Configuration session id<br/><br/><i>Generated property</i> for <code>TrackingItemKey.configId</code> property defined at extension <code>sapproductconfigservices</code>. */
		
	private String configId;
	
	public TrackingItemKey()
	{
		// default constructor
	}
	
	public void setEventType(final EventType eventType)
	{
		this.eventType = eventType;
	}

	public EventType getEventType() 
	{
		return eventType;
	}
	
	public void setTimestamp(final LocalDateTime timestamp)
	{
		this.timestamp = timestamp;
	}

	public LocalDateTime getTimestamp() 
	{
		return timestamp;
	}
	
	public void setSessionId(final String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getSessionId() 
	{
		return sessionId;
	}
	
	public void setConfigId(final String configId)
	{
		this.configId = configId;
	}

	public String getConfigId() 
	{
		return configId;
	}
	

}