/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.data;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;


import java.util.Objects;
public  class AsSearchQueryData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AsSearchQueryData.query</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String query;

	/** <i>Generated property</i> for <code>AsSearchQueryData.activePage</code> property defined at extension <code>adaptivesearch</code>. */
		
	private int activePage;

	/** <i>Generated property</i> for <code>AsSearchQueryData.pageSize</code> property defined at extension <code>adaptivesearch</code>. */
		
	private int pageSize;

	/** <i>Generated property</i> for <code>AsSearchQueryData.facetValues</code> property defined at extension <code>adaptivesearch</code>. */
		
	private Map<String,Set<String>> facetValues;

	/** <i>Generated property</i> for <code>AsSearchQueryData.sort</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String sort;
	
	public AsSearchQueryData()
	{
		// default constructor
	}
	
	public void setQuery(final String query)
	{
		this.query = query;
	}

	public String getQuery() 
	{
		return query;
	}
	
	public void setActivePage(final int activePage)
	{
		this.activePage = activePage;
	}

	public int getActivePage() 
	{
		return activePage;
	}
	
	public void setPageSize(final int pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getPageSize() 
	{
		return pageSize;
	}
	
	public void setFacetValues(final Map<String,Set<String>> facetValues)
	{
		this.facetValues = facetValues;
	}

	public Map<String,Set<String>> getFacetValues() 
	{
		return facetValues;
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