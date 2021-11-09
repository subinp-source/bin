/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.user;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Response body fields which will be returned while fetching the list of country's regions.
 */
@ApiModel(value="Region", description="Response body fields which will be returned while fetching the list of country's regions.")
public  class RegionWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Country and Region code in iso format<br/><br/><i>Generated property</i> for <code>RegionWsDTO.isocode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="isocode", value="Country and Region code in iso format") 	
	private String isocode;

	/** Region code in short iso form<br/><br/><i>Generated property</i> for <code>RegionWsDTO.isocodeShort</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="isocodeShort", value="Region code in short iso form") 	
	private String isocodeShort;

	/** Country code in iso format<br/><br/><i>Generated property</i> for <code>RegionWsDTO.countryIso</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="countryIso", value="Country code in iso format") 	
	private String countryIso;

	/** Name of the region<br/><br/><i>Generated property</i> for <code>RegionWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the region") 	
	private String name;
	
	public RegionWsDTO()
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