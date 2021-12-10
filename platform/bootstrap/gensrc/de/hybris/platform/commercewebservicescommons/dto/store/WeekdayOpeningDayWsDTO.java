/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:32:59 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.store;

import de.hybris.platform.commercewebservicescommons.dto.store.OpeningDayWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Weekday Opening Day
 */
@ApiModel(value="WeekdayOpeningDay", description="Representation of a Weekday Opening Day")
public  class WeekdayOpeningDayWsDTO extends OpeningDayWsDTO 
{

 

	/** Text representation of week day opening day<br/><br/><i>Generated property</i> for <code>WeekdayOpeningDayWsDTO.weekDay</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="weekDay", value="Text representation of week day opening day") 	
	private String weekDay;

	/** Flag stating if weekday opening day is closed<br/><br/><i>Generated property</i> for <code>WeekdayOpeningDayWsDTO.closed</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="closed", value="Flag stating if weekday opening day is closed") 	
	private Boolean closed;
	
	public WeekdayOpeningDayWsDTO()
	{
		// default constructor
	}
	
	public void setWeekDay(final String weekDay)
	{
		this.weekDay = weekDay;
	}

	public String getWeekDay() 
	{
		return weekDay;
	}
	
	public void setClosed(final Boolean closed)
	{
		this.closed = closed;
	}

	public Boolean getClosed() 
	{
		return closed;
	}
	

}