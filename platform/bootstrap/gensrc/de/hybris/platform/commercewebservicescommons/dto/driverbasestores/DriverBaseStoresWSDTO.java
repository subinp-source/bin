/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.driverbasestores;

import java.io.Serializable;


import java.util.Objects;
public  class DriverBaseStoresWSDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>DriverBaseStoresWSDTO.nameOfDriver</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private String nameOfDriver;

	/** <i>Generated property</i> for <code>DriverBaseStoresWSDTO.experience</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private String experience;

	/** <i>Generated property</i> for <code>DriverBaseStoresWSDTO.driverId</code> property defined at extension <code>Hybriswebservices</code>. */
		
	private String driverId;
	
	public DriverBaseStoresWSDTO()
	{
		// default constructor
	}
	
	public void setNameOfDriver(final String nameOfDriver)
	{
		this.nameOfDriver = nameOfDriver;
	}

	public String getNameOfDriver() 
	{
		return nameOfDriver;
	}
	
	public void setExperience(final String experience)
	{
		this.experience = experience;
	}

	public String getExperience() 
	{
		return experience;
	}
	
	public void setDriverId(final String driverId)
	{
		this.driverId = driverId;
	}

	public String getDriverId() 
	{
		return driverId;
	}
	

}