/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import java.util.List;


public class SnAndQuery extends AbstractSnQuery
{
	public static final String TYPE = "and";

	private List<AbstractSnQuery> queries;

	@Override
	public String getType()
	{
		return TYPE;
	}

	public List<AbstractSnQuery> getQueries()
	{
		return queries;
	}

	public void setQueries(final List<AbstractSnQuery> queries)
	{
		this.queries = queries;
	}
}
