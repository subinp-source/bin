/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.store;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Time
 */
@ApiModel(value="Time", description="Representation of a Time")
public  class TimeWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Hour part of the time data<br/><br/><i>Generated property</i> for <code>TimeWsDTO.hour</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="hour", value="Hour part of the time data") 	
	private Byte hour;

	/** Minute part of the time data<br/><br/><i>Generated property</i> for <code>TimeWsDTO.minute</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="minute", value="Minute part of the time data") 	
	private Byte minute;

	/** Formatted hour<br/><br/><i>Generated property</i> for <code>TimeWsDTO.formattedHour</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="formattedHour", value="Formatted hour") 	
	private String formattedHour;
	
	public TimeWsDTO()
	{
		// default constructor
	}
	
	public void setHour(final Byte hour)
	{
		this.hour = hour;
	}

	public Byte getHour() 
	{
		return hour;
	}
	
	public void setMinute(final Byte minute)
	{
		this.minute = minute;
	}

	public Byte getMinute() 
	{
		return minute;
	}
	
	public void setFormattedHour(final String formattedHour)
	{
		this.formattedHour = formattedHour;
	}

	public String getFormattedHour() 
	{
		return formattedHour;
	}
	

}