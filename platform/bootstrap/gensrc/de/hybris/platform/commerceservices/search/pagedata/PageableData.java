/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.pagedata;

import java.io.Serializable;


import java.util.Objects;
/**
 * POJO representation of search query pagination.
 *
 * @deprecated use de.hybris.platform.core.servicelayer.data.PaginationData instead
 */
@Deprecated(since = "6.5", forRemoval = true)
public  class PageableData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** The number of results per page. A page may have less results if there are less than a full page of results, only on the last page in the results.<br/><br/><i>Generated property</i> for <code>PageableData.pageSize</code> property defined at extension <code>commerceservices</code>. */
		
	private int pageSize;

	/** The current page number. The first page is number zero (0), the second page is number one (1), and so on.<br/><br/><i>Generated property</i> for <code>PageableData.currentPage</code> property defined at extension <code>commerceservices</code>. */
		
	private int currentPage;

	/** The selected sort code.<br/><br/><i>Generated property</i> for <code>PageableData.sort</code> property defined at extension <code>commerceservices</code>. */
		
	private String sort;
	
	public PageableData()
	{
		// default constructor
	}
	
	public void setPageSize(final int pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getPageSize() 
	{
		return pageSize;
	}
	
	public void setCurrentPage(final int currentPage)
	{
		this.currentPage = currentPage;
	}

	public int getCurrentPage() 
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