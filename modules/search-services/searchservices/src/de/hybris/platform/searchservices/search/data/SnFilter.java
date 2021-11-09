/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import de.hybris.platform.searchservices.util.JsonUtils;


public class SnFilter
{
	private AbstractSnQuery query;

	public AbstractSnQuery getQuery()
	{
		return query;
	}

	public void setQuery(final AbstractSnQuery query)
	{
		this.query = query;
	}

	@Override
	public String toString()
	{
		return JsonUtils.toJson(this);
	}
}
