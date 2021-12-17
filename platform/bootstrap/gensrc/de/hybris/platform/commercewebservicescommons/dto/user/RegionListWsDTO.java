/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.user;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.user.RegionWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * List of Regions
 */
@ApiModel(value="RegionList", description="List of Regions")
public  class RegionListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** This is the list of Region fields that should be returned in the response body<br/><br/><i>Generated property</i> for <code>RegionListWsDTO.regions</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="regions", value="This is the list of Region fields that should be returned in the response body") 	
	private List<RegionWsDTO> regions;
	
	public RegionListWsDTO()
	{
		// default constructor
	}
	
	public void setRegions(final List<RegionWsDTO> regions)
	{
		this.regions = regions;
	}

	public List<RegionWsDTO> getRegions() 
	{
		return regions;
	}
	

}