/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.search.pagedata;

import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PageableWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a search results pagination
 *
 * @deprecated Use de.hybris.platform.webservicescommons.dto.PaginationWsDTO instead
 */
@ApiModel(value="deprecatedPagination", description="Representation of a search results pagination")
@Deprecated(since = "6.5", forRemoval = true)
public  class PaginationWsDTO extends PageableWsDTO 
{

 

	/** The total number of pages. This is the number of pages, each of pageSize, required to display the totalResults.
            <br/><br/><i>Generated property</i> for <code>PaginationWsDTO.totalPages</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="totalPages", value="The total number of pages. This is the number of pages, each of pageSize, required to display the totalResults.") 	
	private Integer totalPages;

	/** The total number of matched results across all pages<br/><br/><i>Generated property</i> for <code>PaginationWsDTO.totalResults</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="totalResults", value="The total number of matched results across all pages") 	
	private Long totalResults;
	
	public PaginationWsDTO()
	{
		// default constructor
	}
	
	public void setTotalPages(final Integer totalPages)
	{
		this.totalPages = totalPages;
	}

	public Integer getTotalPages() 
	{
		return totalPages;
	}
	
	public void setTotalResults(final Long totalResults)
	{
		this.totalResults = totalResults;
	}

	public Long getTotalResults() 
	{
		return totalResults;
	}
	

}