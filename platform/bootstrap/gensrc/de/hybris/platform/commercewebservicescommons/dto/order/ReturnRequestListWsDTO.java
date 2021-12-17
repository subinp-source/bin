/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.ReturnRequestWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of an Order Return Request List
 */
@ApiModel(value="ReturnRequestList", description="Representation of an Order Return Request List")
public  class ReturnRequestListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** List of order return requests<br/><br/><i>Generated property</i> for <code>ReturnRequestListWsDTO.returnRequests</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="returnRequests", value="List of order return requests") 	
	private List<ReturnRequestWsDTO> returnRequests;

	/** List of sorts<br/><br/><i>Generated property</i> for <code>ReturnRequestListWsDTO.sorts</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="sorts", value="List of sorts") 	
	private List<SortWsDTO> sorts;

	/** Pagination items<br/><br/><i>Generated property</i> for <code>ReturnRequestListWsDTO.pagination</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="pagination", value="Pagination items") 	
	private PaginationWsDTO pagination;
	
	public ReturnRequestListWsDTO()
	{
		// default constructor
	}
	
	public void setReturnRequests(final List<ReturnRequestWsDTO> returnRequests)
	{
		this.returnRequests = returnRequests;
	}

	public List<ReturnRequestWsDTO> getReturnRequests() 
	{
		return returnRequests;
	}
	
	public void setSorts(final List<SortWsDTO> sorts)
	{
		this.sorts = sorts;
	}

	public List<SortWsDTO> getSorts() 
	{
		return sorts;
	}
	
	public void setPagination(final PaginationWsDTO pagination)
	{
		this.pagination = pagination;
	}

	public PaginationWsDTO getPagination() 
	{
		return pagination;
	}
	

}