/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
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
 * Response body fields which will be returned while fetching the list of countries. The DTO is in XML or .json format
 */
@ApiModel(value="Country", description="Response body fields which will be returned while fetching the list of countries. The DTO is in XML or .json format")
public  class CountryWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Country code in iso format<br/><br/><i>Generated property</i> for <code>CountryWsDTO.isocode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="isocode", value="Country code in iso format") 	
	private String isocode;

	/** Name of the country<br/><br/><i>Generated property</i> for <code>CountryWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the country") 	
	private String name;
	
	public CountryWsDTO()
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
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	

}