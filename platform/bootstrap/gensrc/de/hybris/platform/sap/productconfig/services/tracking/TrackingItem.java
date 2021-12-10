/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:32:59 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.tracking;

import java.io.Serializable;
import  java.lang.String;
import de.hybris.platform.sap.productconfig.services.tracking.TrackingItemKey;
import java.util.Map;


import java.util.Objects;
public  class TrackingItem  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Object with key data<br/><br/><i>Generated property</i> for <code>TrackingItem.trackingItemKey</code> property defined at extension <code>sapproductconfigservices</code>. */
		
	private TrackingItemKey trackingItemKey;

	/** Additional attributes that specify the action<br/><br/><i>Generated property</i> for <code>TrackingItem.parameters</code> property defined at extension <code>sapproductconfigservices</code>. */
		
	private Map<String,String> parameters;
	
	public TrackingItem()
	{
		// default constructor
	}
	
	public void setTrackingItemKey(final TrackingItemKey trackingItemKey)
	{
		this.trackingItemKey = trackingItemKey;
	}

	public TrackingItemKey getTrackingItemKey() 
	{
		return trackingItemKey;
	}
	
	public void setParameters(final Map<String,String> parameters)
	{
		this.parameters = parameters;
	}

	public Map<String,String> getParameters() 
	{
		return parameters;
	}
	

}