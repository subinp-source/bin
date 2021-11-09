/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.data;

import de.hybris.platform.adaptivesearch.data.AbstractAsItemConfiguration;


import java.util.Objects;
public  class AsGroup extends AbstractAsItemConfiguration 
{

 

	/** <i>Generated property</i> for <code>AsGroup.expression</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String expression;

	/** <i>Generated property</i> for <code>AsGroup.limit</code> property defined at extension <code>adaptivesearch</code>. */
		
	private Integer limit;
	
	public AsGroup()
	{
		// default constructor
	}
	
	public void setExpression(final String expression)
	{
		this.expression = expression;
	}

	public String getExpression() 
	{
		return expression;
	}
	
	public void setLimit(final Integer limit)
	{
		this.limit = limit;
	}

	public Integer getLimit() 
	{
		return limit;
	}
	

}