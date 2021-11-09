/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.facetdata;

import java.io.Serializable;


import java.util.Objects;
/**
 * POJO representing a facet value.
 */
public  class FacetValueData<STATE>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>FacetValueData<STATE>.code</code> property defined at extension <code>commerceservices</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>FacetValueData<STATE>.name</code> property defined at extension <code>commerceservices</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>FacetValueData<STATE>.count</code> property defined at extension <code>commerceservices</code>. */
		
	private long count;

	/** <i>Generated property</i> for <code>FacetValueData<STATE>.query</code> property defined at extension <code>commerceservices</code>. */
		
	private STATE query;

	/** <i>Generated property</i> for <code>FacetValueData<STATE>.selected</code> property defined at extension <code>commerceservices</code>. */
		
	private boolean selected;
	
	public FacetValueData()
	{
		// default constructor
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setCount(final long count)
	{
		this.count = count;
	}

	public long getCount() 
	{
		return count;
	}
	
	public void setQuery(final STATE query)
	{
		this.query = query;
	}

	public STATE getQuery() 
	{
		return query;
	}
	
	public void setSelected(final boolean selected)
	{
		this.selected = selected;
	}

	public boolean isSelected() 
	{
		return selected;
	}
	

}