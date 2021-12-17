/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicepromotionfacades.customer360;

import java.io.Serializable;


import java.util.Objects;
public  class CSACouponData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CSACouponData.code</code> property defined at extension <code>assistedservicepromotionfacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>CSACouponData.name</code> property defined at extension <code>assistedservicepromotionfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>CSACouponData.couponApplied</code> property defined at extension <code>assistedservicepromotionfacades</code>. */
		
	private Boolean couponApplied;
	
	public CSACouponData()
	{
		// default constructor
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setCouponApplied(final Boolean couponApplied)
	{
		this.couponApplied = couponApplied;
	}

	public Boolean getCouponApplied() 
	{
		return couponApplied;
	}
	

}