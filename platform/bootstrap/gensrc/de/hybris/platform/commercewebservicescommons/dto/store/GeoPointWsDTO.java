/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.store;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a GeoPoint
 */
@ApiModel(value="GeoPoint", description="Representation of a GeoPoint")
public  class GeoPointWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Geopoint latitude<br/><br/><i>Generated property</i> for <code>GeoPointWsDTO.latitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="latitude", value="Geopoint latitude") 	
	private Double latitude;

	/** Geopoint longitude<br/><br/><i>Generated property</i> for <code>GeoPointWsDTO.longitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="longitude", value="Geopoint longitude") 	
	private Double longitude;
	
	public GeoPointWsDTO()
	{
		// default constructor
	}
	
	public void setLatitude(final Double latitude)
	{
		this.latitude = latitude;
	}

	public Double getLatitude() 
	{
		return latitude;
	}
	
	public void setLongitude(final Double longitude)
	{
		this.longitude = longitude;
	}

	public Double getLongitude() 
	{
		return longitude;
	}
	

}