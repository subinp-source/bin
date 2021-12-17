/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.store;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.store.TimeWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * List of OpeningDay
 */
@ApiModel(value="OpeningDay", description="List of OpeningDay")
public  class OpeningDayWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Starting time of opening day<br/><br/><i>Generated property</i> for <code>OpeningDayWsDTO.openingTime</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="openingTime", value="Starting time of opening day") 	
	private TimeWsDTO openingTime;

	/** Closing time of opening day<br/><br/><i>Generated property</i> for <code>OpeningDayWsDTO.closingTime</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="closingTime", value="Closing time of opening day") 	
	private TimeWsDTO closingTime;
	
	public OpeningDayWsDTO()
	{
		// default constructor
	}
	
	public void setOpeningTime(final TimeWsDTO openingTime)
	{
		this.openingTime = openingTime;
	}

	public TimeWsDTO getOpeningTime() 
	{
		return openingTime;
	}
	
	public void setClosingTime(final TimeWsDTO closingTime)
	{
		this.closingTime = closingTime;
	}

	public TimeWsDTO getClosingTime() 
	{
		return closingTime;
	}
	

}