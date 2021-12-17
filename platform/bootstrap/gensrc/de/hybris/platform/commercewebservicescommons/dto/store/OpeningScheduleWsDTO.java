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
import de.hybris.platform.commercewebservicescommons.dto.store.SpecialOpeningDayWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.WeekdayOpeningDayWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of an Opening schedule
 */
@ApiModel(value="OpeningSchedule", description="Representation of an Opening schedule")
public  class OpeningScheduleWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Name of the opening schedule<br/><br/><i>Generated property</i> for <code>OpeningScheduleWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the opening schedule") 	
	private String name;

	/** Code of the opening schedule<br/><br/><i>Generated property</i> for <code>OpeningScheduleWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="code", value="Code of the opening schedule") 	
	private String code;

	/** List of weekday opening days<br/><br/><i>Generated property</i> for <code>OpeningScheduleWsDTO.weekDayOpeningList</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="weekDayOpeningList", value="List of weekday opening days") 	
	private List<WeekdayOpeningDayWsDTO> weekDayOpeningList;

	/** List of special opening days<br/><br/><i>Generated property</i> for <code>OpeningScheduleWsDTO.specialDayOpeningList</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="specialDayOpeningList", value="List of special opening days") 	
	private List<SpecialOpeningDayWsDTO> specialDayOpeningList;
	
	public OpeningScheduleWsDTO()
	{
		// default constructor
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setWeekDayOpeningList(final List<WeekdayOpeningDayWsDTO> weekDayOpeningList)
	{
		this.weekDayOpeningList = weekDayOpeningList;
	}

	public List<WeekdayOpeningDayWsDTO> getWeekDayOpeningList() 
	{
		return weekDayOpeningList;
	}
	
	public void setSpecialDayOpeningList(final List<SpecialOpeningDayWsDTO> specialDayOpeningList)
	{
		this.specialDayOpeningList = specialDayOpeningList;
	}

	public List<SpecialOpeningDayWsDTO> getSpecialDayOpeningList() 
	{
		return specialDayOpeningList;
	}
	

}