/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.training.facades.product.data;

import java.io.Serializable;


import java.util.Objects;
public  class VehicleBase  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>VehicleBase.vehicleDescription</code> property defined at extension <code>trainingfacades</code>. */
		
	private String vehicleDescription;
	
	public VehicleBase()
	{
		// default constructor
	}
	
	public void setVehicleDescription(final String vehicleDescription)
	{
		this.vehicleDescription = vehicleDescription;
	}

	public String getVehicleDescription() 
	{
		return vehicleDescription;
	}
	

}