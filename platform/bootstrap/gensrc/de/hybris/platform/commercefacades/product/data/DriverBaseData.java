/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;


import java.util.Objects;
public  class DriverBaseData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>DriverBaseData.nameOfDriver</code> property defined at extension <code>trainingfacades</code>. */
		
	private String nameOfDriver;

	/** <i>Generated property</i> for <code>DriverBaseData.experience</code> property defined at extension <code>trainingfacades</code>. */
		
	private String experience;

	/** <i>Generated property</i> for <code>DriverBaseData.driverId</code> property defined at extension <code>trainingfacades</code>. */
		
	private String driverId;
	
	public DriverBaseData()
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