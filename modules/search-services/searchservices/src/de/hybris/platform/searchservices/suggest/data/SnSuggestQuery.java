/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.data;

import de.hybris.platform.searchservices.util.JsonUtils;


/**
 * Represents a suggest query.
 */
public class SnSuggestQuery
{
	private String query;
	private Integer limit;

	public String getQuery()
	{
		return query;
	}

	public void setQuery(final String query)
	{
		this.query = query;
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
