/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;


public abstract class AbstractSnExpressionQuery extends AbstractSnQuery
{
	private String expression;

	public String getExpression()
	{
		return expression;
	}

	public void setExpression(final String expression)
	{
		this.expression = expression;
	}
}
