/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.data;

import java.io.Serializable;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;


import java.util.Objects;
public  class SnSearchResultConverterData<R>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SnSearchResultConverterData<R>.indexConfiguration</code> property defined at extension <code>commerceservices</code>. */
		
	private SnIndexConfiguration indexConfiguration;

	/** <i>Generated property</i> for <code>SnSearchResultConverterData<R>.indexType</code> property defined at extension <code>commerceservices</code>. */
		
	private SnIndexType indexType;

	/** <i>Generated property</i> for <code>SnSearchResultConverterData<R>.searchQuery</code> property defined at extension <code>commerceservices</code>. */
		
	private SolrSearchQueryData searchQuery;

	/** <i>Generated property</i> for <code>SnSearchResultConverterData<R>.snSearchResult</code> property defined at extension <code>commerceservices</code>. */
		
	private R snSearchResult;
	
	public SnSearchResultConverterData()
	{
		// default constructor
	}
	
	public void setIndexConfiguration(final SnIndexConfiguration indexConfiguration)
	{
		this.indexConfiguration = indexConfiguration;
	}

	public SnIndexConfiguration getIndexConfiguration() 
	{
		return indexConfiguration;
	}
	
	public void setIndexType(final SnIndexType indexType)
	{
		this.indexType = indexType;
	}

	public SnIndexType getIndexType() 
	{
		return indexType;
	}
	
	public void setSearchQuery(final SolrSearchQueryData searchQuery)
	{
		this.searchQuery = searchQuery;
	}

	public SolrSearchQueryData getSearchQuery() 
	{
		return searchQuery;
	}
	
	public void setSnSearchResult(final R snSearchResult)
	{
		this.snSearchResult = snSearchResult;
	}

	public R getSnSearchResult() 
	{
		return snSearchResult;
	}
	

}