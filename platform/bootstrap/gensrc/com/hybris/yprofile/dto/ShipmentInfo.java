/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.dto;

import java.io.Serializable;


import java.util.Objects;
public  class ShipmentInfo  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ShipmentInfo.address</code> property defined at extension <code>profileservices</code>. */
		
	private Address address;

	/** <i>Generated property</i> for <code>ShipmentInfo.carrier</code> property defined at extension <code>profileservices</code>. */
		
	private String carrier;

	/** <i>Generated property</i> for <code>ShipmentInfo.trackingRef</code> property defined at extension <code>profileservices</code>. */
		
	private String trackingRef;

	/** <i>Generated property</i> for <code>ShipmentInfo.status</code> property defined at extension <code>profileservices</code>. */
		
	private String status;
	
	public ShipmentInfo()
	{
		// default constructor
	}
	
	public void setAddress(final Address address)
	{
		this.address = address;
	}

	public Address getAddress() 
	{
		return address;
	}
	
	public void setCarrier(final String carrier)
	{
		this.carrier = carrier;
	}

	public String getCarrier() 
	{
		return carrier;
	}
	
	public void setTrackingRef(final String trackingRef)
	{
		this.trackingRef = trackingRef;
	}

	public String getTrackingRef() 
	{
		return trackingRef;
	}
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	

}