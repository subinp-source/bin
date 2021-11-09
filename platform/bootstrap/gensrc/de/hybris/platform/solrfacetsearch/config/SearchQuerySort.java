/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;


import java.util.Objects;
public  class SearchQuerySort  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SearchQuerySort.field</code> property defined at extension <code>solrfacetsearch</code>. */
		
	private String field;

	/** <i>Generated property</i> for <code>SearchQuerySort.ascending</code> property defined at extension <code>solrfacetsearch</code>. */
		
	private boolean ascending;
	
	public SearchQuerySort()
	{
		// default constructor
	}
	
	public void setField(final String field)
	{
		this.field = field;
	}

	public String getField() 
	{
		return field;
	}
	
	public void setAscending(final boolean ascending)
	{
		this.ascending = ascending;
	}

	public boolean isAscending() 
	{
		return ascending;
	}
	

}