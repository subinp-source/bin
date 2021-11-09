/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import de.hybris.platform.searchservices.util.JsonUtils;


public class SnGroupRequest
{
	private String expression;
	private Integer limit;

	public String getExpression()
	{
		return expression;
	}

	public void setExpression(final String expression)
	{
		this.expression = expression;
	}

	public Integer getLimit()
	{
		return limit;
	}

	public void setLimit(final Integer limit)
	{
		this.limit = limit;
	}

	@Override
	public String toString()
	{
		return JsonUtils.toJson(this);
	}
}
