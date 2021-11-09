/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;


public class SnNotQuery extends AbstractSnQuery
{
	public static final String TYPE = "not";

	private AbstractSnQuery query;

	@Override
	public String getType()
	{
		return TYPE;
	}

	public AbstractSnQuery getQuery()
	{
		return query;
	}

	public void setQuery(final AbstractSnQuery query)
	{
		this.query = query;
	}
}
