/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.data;

import de.hybris.platform.adaptivesearch.data.AbstractAsItemConfiguration;
import de.hybris.platform.adaptivesearch.enums.AsSortOrder;


import java.util.Objects;
public  class AsSortExpression extends AbstractAsItemConfiguration 
{

 

	/** <i>Generated property</i> for <code>AsSortExpression.expression</code> property defined at extension <code>adaptivesearch</code>. */
		
	private String expression;

	/** <i>Generated property</i> for <code>AsSortExpression.order</code> property defined at extension <code>adaptivesearch</code>. */
		
	private AsSortOrder order;
	
	public AsSortExpression()
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
	
	public void setOrder(final AsSortOrder order)
	{
		this.order = order;
	}

	public AsSortOrder getOrder() 
	{
		return order;
	}
	

}