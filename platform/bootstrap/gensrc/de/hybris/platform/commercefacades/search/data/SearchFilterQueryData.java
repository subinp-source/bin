/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.search.data;

import java.io.Serializable;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.FilterQueryOperator;
import java.util.Set;


import java.util.Objects;
public  class SearchFilterQueryData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SearchFilterQueryData.key</code> property defined at extension <code>commercefacades</code>. */
		
	private String key;

	/** <i>Generated property</i> for <code>SearchFilterQueryData.operator</code> property defined at extension <code>commercefacades</code>. */
		
	private FilterQueryOperator operator;

	/** <i>Generated property</i> for <code>SearchFilterQueryData.values</code> property defined at extension <code>commercefacades</code>. */
		
	private Set<String> values;
	
	public SearchFilterQueryData()
	{
		// default constructor
	}
	
	public void setKey(final String key)
	{
		this.key = key;
	}

	public String getKey() 
	{
		return key;
	}
	
	public void setOperator(final FilterQueryOperator operator)
	{
		this.operator = operator;
	}

	public FilterQueryOperator getOperator() 
	{
		return operator;
	}
	
	public void setValues(final Set<String> values)
	{
		this.values = values;
	}

	public Set<String> getValues() 
	{
		return values;
	}
	

}