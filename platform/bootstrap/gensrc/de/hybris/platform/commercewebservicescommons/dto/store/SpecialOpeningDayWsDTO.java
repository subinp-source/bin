/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.store;

import de.hybris.platform.commercewebservicescommons.dto.store.OpeningDayWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;


import java.util.Objects;
/**
 * Representation of a special opening day
 */
@ApiModel(value="SpecialOpeningDay", description="Representation of a special opening day")
public  class SpecialOpeningDayWsDTO extends OpeningDayWsDTO 
{

 

	/** Date of special opening day<br/><br/><i>Generated property</i> for <code>SpecialOpeningDayWsDTO.date</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="date", value="Date of special opening day") 	
	private Date date;

	/** Text representation of the date of special opening day<br/><br/><i>Generated property</i> for <code>SpecialOpeningDayWsDTO.formattedDate</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="formattedDate", value="Text representation of the date of special opening day") 	
	private String formattedDate;

	/** Flag stating if special opening day is closed<br/><br/><i>Generated property</i> for <code>SpecialOpeningDayWsDTO.closed</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="closed", value="Flag stating if special opening day is closed") 	
	private Boolean closed;

	/** Name of the special opening day event<br/><br/><i>Generated property</i> for <code>SpecialOpeningDayWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the special opening day event") 	
	private String name;

	/** Comment field<br/><br/><i>Generated property</i> for <code>SpecialOpeningDayWsDTO.comment</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="comment", value="Comment field") 	
	private String comment;
	
	public SpecialOpeningDayWsDTO()
	{
		// default constructor
	}
	
	public void setDate(final Date date)
	{
		this.date = date;
	}

	public Date getDate() 
	{
		return date;
	}
	
	public void setFormattedDate(final String formattedDate)
	{
		this.formattedDate = formattedDate;
	}

	public String getFormattedDate() 
	{
		return formattedDate;
	}
	
	public void setClosed(final Boolean closed)
	{
		this.closed = closed;
	}

	public Boolean getClosed() 
	{
		return closed;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setComment(final String comment)
	{
		this.comment = comment;
	}

	public String getComment() 
	{
		return comment;
	}
	

}