/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;


public class SnQueryFunctionRankRule extends AbstractSnRankRule
{
	public static final String TYPE = "queryFunction";

	private AbstractSnQuery query;
	private Float weight;

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

	public Float getWeight()
	{
		return weight;
	}

	public void setWeight(final Float weight)
	{
		this.weight = weight;
	}
}
