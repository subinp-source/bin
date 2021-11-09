/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.store;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Point of Service List
 */
@ApiModel(value="PointOfServiceList", description="Representation of a Point of Service List")
public  class PointOfServiceListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** List of points of service<br/><br/><i>Generated property</i> for <code>PointOfServiceListWsDTO.pointOfServices</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="pointOfServices", value="List of points of service") 	
	private List<PointOfServiceWsDTO> pointOfServices;
	
	public PointOfServiceListWsDTO()
	{
		// default constructor
	}
	
	public void setPointOfServices(final List<PointOfServiceWsDTO> pointOfServices)
	{
		this.pointOfServices = pointOfServices;
	}

	public List<PointOfServiceWsDTO> getPointOfServices() 
	{
		return pointOfServices;
	}
	

}