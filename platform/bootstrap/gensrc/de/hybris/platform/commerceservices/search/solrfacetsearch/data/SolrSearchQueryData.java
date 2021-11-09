/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import java.io.Serializable;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchFilterQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import java.util.List;


import java.util.Objects;
/**
 * POJO representing a Search query.
 */
public  class SolrSearchQueryData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SolrSearchQueryData.freeTextSearch</code> property defined at extension <code>commerceservices</code>. */
		
	private String freeTextSearch;

	/** <i>Generated property</i> for <code>SolrSearchQueryData.categoryCode</code> property defined at extension <code>commerceservices</code>. */
		
	private String categoryCode;

	/** <i>Generated property</i> for <code>SolrSearchQueryData.filterTerms</code> property defined at extension <code>commerceservices</code>. */
		
	private List<SolrSearchQueryTermData> filterTerms;

	/** <i>Generated property</i> for <code>SolrSearchQueryData.filterQueries</code> property defined at extension <code>commerceservices</code>. */
		
	private List<SolrSearchFilterQueryData> filterQueries;

	/** <i>Generated property</i> for <code>SolrSearchQueryData.sort</code> property defined at extension <code>commerceservices</code>. */
		
	private String sort;

	/** <i>Generated property</i> for <code>SolrSearchQueryData.searchQueryContext</code> property defined at extension <code>commerceservices</code>. */
		
	private SearchQueryContext searchQueryContext;
	
	public SolrSearchQueryData()
	{
		// default constructor
	}
	
	public void setFreeTextSearch(final String freeTextSearch)
	{
		this.freeTextSearch = freeTextSearch;
	}

	public String getFreeTextSearch() 
	{
		return freeTextSearch;
	}
	
	public void setCategoryCode(final String categoryCode)
	{
		this.categoryCode = categoryCode;
	}

	public String getCategoryCode() 
	{
		return categoryCode;
	}
	
	public void setFilterTerms(final List<SolrSearchQueryTermData> filterTerms)
	{
		this.filterTerms = filterTerms;
	}

	public List<SolrSearchQueryTermData> getFilterTerms() 
	{
		return filterTerms;
	}
	
	public void setFilterQueries(final List<SolrSearchFilterQueryData> filterQueries)
	{
		this.filterQueries = filterQueries;
	}

	public List<SolrSearchFilterQueryData> getFilterQueries() 
	{
		return filterQueries;
	}
	
	public void setSort(final String sort)
	{
		this.sort = sort;
	}

	public String getSort() 
	{
		return sort;
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