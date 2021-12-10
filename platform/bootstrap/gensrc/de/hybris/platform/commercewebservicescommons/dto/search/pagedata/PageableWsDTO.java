/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:32:59 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.search.pagedata;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a search query pagination
 *
 * @deprecated Use de.hybris.platform.webservicescommons.dto.PaginationWsDTO instead
 */
@ApiModel(value="Pageable", description="Representation of a search query pagination")
@Deprecated(since = "6.5", forRemoval = true)
public  class PageableWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** The number of results per page. A page may have less results if there are less than a full page of results, only on the last page in the results<br/><br/><i>Generated property</i> for <code>PageableWsDTO.pageSize</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="pageSize", value="The number of results per page. A page may have less results if there are less than a full page of results, only on the last page in the results") 	
	private Integer pageSize;

	/** The current page number. The first page is number zero (0), the second page is number one (1), and so on<br/><br/><i>Generated property</i> for <code>PageableWsDTO.currentPage</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="currentPage", value="The current page number. The first page is number zero (0), the second page is number one (1), and so on") 	
	private Integer currentPage;

	/** The selected sort code<br/><br/><i>Generated property</i> for <code>PageableWsDTO.sort</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="sort", value="The selected sort code") 	
	private String sort;
	
	public PageableWsDTO()
	{
		// default constructor
	}
	
	public void setPageSize(final Integer pageSize)
	{
		this.pageSize = pageSize;
	}

	public Integer getPageSize() 
	{
		return pageSize;
	}
	
	public void setCurrentPage(final Integer currentPage)
	{
		this.currentPage = currentPage;
	}

	public Integer getCurrentPage() 
	{
		return currentPage;
	}
	
	public void setSort(final String sort)
	{
		this.sort = sort;
	}

	public String getSort() 
	{
		return sort;
	}
	

}