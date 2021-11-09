/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a status summary, an aggregated view on issues for a specific status or severity. These issues are attached to configurations of products or order entries
 */
@ApiModel(value="StatusSummary", description="Representation of a status summary, an aggregated view on issues for a specific status or severity. These issues are attached to configurations of products or order entries")
public  class StatusSummaryWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Status or severity indicator, can be one of ERROR, WARNING, INFO or SUCCESS<br/><br/><i>Generated property</i> for <code>StatusSummaryWsDTO.status</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="status", value="Status or severity indicator, can be one of ERROR, WARNING, INFO or SUCCESS", example="ERROR") 	
	private String status;

	/** Number of issues per status<br/><br/><i>Generated property</i> for <code>StatusSummaryWsDTO.numberOfIssues</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="numberOfIssues", value="Number of issues per status", example="3") 	
	private Integer numberOfIssues;
	
	public StatusSummaryWsDTO()
	{
		// default constructor
	}
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	
	public void setNumberOfIssues(final Integer numberOfIssues)
	{
		this.numberOfIssues = numberOfIssues;
	}

	public Integer getNumberOfIssues() 
	{
		return numberOfIssues;
	}
	

}