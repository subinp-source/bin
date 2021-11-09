/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

public class SnRangeQuery extends AbstractSnExpressionQuery
{
	public static final String TYPE = "range";

	private Object from;
	private Boolean includeFrom;
	private Object to;
	private Boolean includeTo;

	@Override
	public String getType()
	{
		return TYPE;
	}

	public Object getFrom()
	{
		return from;
	}

	public void setFrom(final Object from)
	{
		this.from = from;
	}

	public Boolean getIncludeFrom()
	{
		return includeFrom;
	}

	public void setIncludeFrom(final Boolean includeFrom)
	{
		this.includeFrom = includeFrom;
	}

	public Object getTo()
	{
		return to;
	}

	public void setTo(final Object to)
	{
		this.to = to;
	}

	public Boolean getIncludeTo()
	{
		return includeTo;
	}

	public void setIncludeTo(final Boolean includeTo)
	{
		this.includeTo = includeTo;
	}
}
