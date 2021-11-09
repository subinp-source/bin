/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.core.servicelayer.data;

import java.io.Serializable;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SortData;
import java.util.List;


import java.util.Objects;
/**
 * POJO that includes all necessary data for making a paginated and sorted search and returning response from that request.
 */
public  class SearchPageData<RESULT>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SearchPageData<RESULT>.results</code> property defined at extension <code>core</code>. */
		
	private List<RESULT> results;

	/** <i>Generated property</i> for <code>SearchPageData<RESULT>.sorts</code> property defined at extension <code>core</code>. */
		
	private List<SortData> sorts;

	/** <i>Generated property</i> for <code>SearchPageData<RESULT>.pagination</code> property defined at extension <code>core</code>. */
		
	private PaginationData pagination;
	
	public SearchPageData()
	{
		// default constructor
	}
	
	public void setResults(final List<RESULT> results)
	{
		this.results = results;
	}

	public List<RESULT> getResults() 
	{
		return results;
	}
	
	public void setSorts(final List<SortData> sorts)
	{
		this.sorts = sorts;
	}

	public List<SortData> getSorts() 
	{
		return sorts;
	}
	
	public void setPagination(final PaginationData pagination)
	{
		this.pagination = pagination;
	}

	public PaginationData getPagination() 
	{
		return pagination;
	}
	

}