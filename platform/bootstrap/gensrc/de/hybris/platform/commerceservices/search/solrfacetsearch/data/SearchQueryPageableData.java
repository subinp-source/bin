/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import java.io.Serializable;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;


import java.util.Objects;
public  class SearchQueryPageableData<STATE>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SearchQueryPageableData<STATE>.searchQueryData</code> property defined at extension <code>commerceservices</code>. */
		
	private STATE searchQueryData;

	/** <i>Generated property</i> for <code>SearchQueryPageableData<STATE>.pageableData</code> property defined at extension <code>commerceservices</code>. */
		
	private PageableData pageableData;
	
	public SearchQueryPageableData()
	{
		// default constructor
	}
	
	public void setSearchQueryData(final STATE searchQueryData)
	{
		this.searchQueryData = searchQueryData;
	}

	public STATE getSearchQueryData() 
	{
		return searchQueryData;
	}
	
	public void setPageableData(final PageableData pageableData)
	{
		this.pageableData = pageableData;
	}

	public PageableData getPageableData() 
	{
		return pageableData;
	}
	

}