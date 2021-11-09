/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.user.data;

import java.io.Serializable;


import java.util.Objects;
public  class RegionData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>RegionData.isocode</code> property defined at extension <code>commercefacades</code>. */
		
	private String isocode;

	/** <i>Generated property</i> for <code>RegionData.isocodeShort</code> property defined at extension <code>commercefacades</code>. */
		
	private String isocodeShort;

	/** <i>Generated property</i> for <code>RegionData.countryIso</code> property defined at extension <code>commercefacades</code>. */
		
	private String countryIso;

	/** <i>Generated property</i> for <code>RegionData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;
	
	public RegionData()
	{
		// default constructor
	}
	
	public void setIsocode(final String isocode)
	{
		this.isocode = isocode;
	}

	public String getIsocode() 
	{
		return isocode;
	}
	
	public void setIsocodeShort(final String isocodeShort)
	{
		this.isocodeShort = isocodeShort;
	}

	public String getIsocodeShort() 
	{
		return isocodeShort;
	}
	
	public void setCountryIso(final String countryIso)
	{
		this.countryIso = countryIso;
	}

	public String getCountryIso() 
	{
		return countryIso;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	

}