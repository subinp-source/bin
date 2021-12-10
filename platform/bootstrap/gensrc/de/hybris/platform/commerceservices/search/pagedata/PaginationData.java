/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.pagedata;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;


import java.util.Objects;
/**
 * POJO representation of search results pagination.
 *
 * @deprecated use de.hybris.platform.core.servicelayer.data.PaginationData instead
 */
@Deprecated(since = "6.5", forRemoval = true)
public  class PaginationData extends PageableData 
{

 

	/** The total number of pages. This is the number of pages, each of pageSize, required to display the totalNumberOfResults.<br/><br/><i>Generated property</i> for <code>PaginationData.numberOfPages</code> property defined at extension <code>commerceservices</code>. */
		
	private int numberOfPages;

	/** The total number of matched results across all pages.<br/><br/><i>Generated property</i> for <code>PaginationData.totalNumberOfResults</code> property defined at extension <code>commerceservices</code>. */
		
	private long totalNumberOfResults;
	
	public PaginationData()
	{
		// default constructor
	}
	
	public void setNumberOfPages(final int numberOfPages)
	{
		this.numberOfPages = numberOfPages;
	}

	public int getNumberOfPages() 
	{
		return numberOfPages;
	}
	
	public void setTotalNumberOfResults(final long totalNumberOfResults)
	{
		this.totalNumberOfResults = totalNumberOfResults;
	}

	public long getTotalNumberOfResults() 
	{
		return totalNumberOfResults;
	}
	

}