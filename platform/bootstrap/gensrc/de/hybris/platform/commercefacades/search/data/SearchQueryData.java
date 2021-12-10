/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.search.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.search.data.SearchFilterQueryData;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import java.util.List;


import java.util.Objects;
public  class SearchQueryData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SearchQueryData.value</code> property defined at extension <code>commercefacades</code>. */
		
	private String value;

	/** <i>Generated property</i> for <code>SearchQueryData.filterQueries</code> property defined at extension <code>commercefacades</code>. */
		
	private List<SearchFilterQueryData> filterQueries;

	/** <i>Generated property</i> for <code>SearchQueryData.searchQueryContext</code> property defined at extension <code>commercefacades</code>. */
		
	private SearchQueryContext searchQueryContext;
	
	public SearchQueryData()
	{
		// default constructor
	}
	
	public void setValue(final String value)
	{
		this.value = value;
	}

	public String getValue() 
	{
		return value;
	}
	
	public void setFilterQueries(final List<SearchFilterQueryData> filterQueries)
	{
		this.filterQueries = filterQueries;
	}

	public List<SearchFilterQueryData> getFilterQueries() 
	{
		return filterQueries;
	}
	
	public void setSearchQueryContext(final SearchQueryContext searchQueryContext)
	{
		this.searchQueryContext = searchQueryContext;
	}

	public SearchQueryContext getSearchQueryContext() 
	{
		return searchQueryContext;
	}
	

}