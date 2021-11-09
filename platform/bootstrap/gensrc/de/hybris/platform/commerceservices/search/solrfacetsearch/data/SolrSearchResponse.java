/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import java.io.Serializable;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;


import java.util.Objects;
public  class SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SEARCH_RESULT_TYPE>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SEARCH_RESULT_TYPE>.request</code> property defined at extension <code>commerceservices</code>. */
		
	private SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE> request;

	/** <i>Generated property</i> for <code>SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SEARCH_RESULT_TYPE>.searchResult</code> property defined at extension <code>commerceservices</code>. */
		
	private SEARCH_RESULT_TYPE searchResult;
	
	public SolrSearchResponse()
	{
		// default constructor
	}
	
	public void setRequest(final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE> request)
	{
		this.request = request;
	}

	public SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE> getRequest() 
	{
		return request;
	}
	
	public void setSearchResult(final SEARCH_RESULT_TYPE searchResult)
	{
		this.searchResult = searchResult;
	}

	public SEARCH_RESULT_TYPE getSearchResult() 
	{
		return searchResult;
	}
	

}