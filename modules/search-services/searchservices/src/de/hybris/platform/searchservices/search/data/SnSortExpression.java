/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import de.hybris.platform.searchservices.util.JsonUtils;


public class SnSortExpression
{
	private String expression;

	private Boolean ascending;

	public String getExpression()
	{
		return expression;
	}

	public void setExpression(final String expression)
	{
		this.expression = expression;
	}

	public Boolean getAscending()
	{
		return ascending;
	}

	public void setAscending(final Boolean ascending)
	{
		this.ascending = ascending;
	}

	@Override
	public String toString()
	{
		return JsonUtils.toJson(this);
	}
}
