/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;


public class SnRangeBucketRequest extends AbstractSnBucketRequest
{
	private Object from;
	private Object to;

	public Object getFrom()
	{
		return from;
	}

	public void setFrom(final Object from)
	{
		this.from = from;
	}

	public Object getTo()
	{
		return to;
	}

	public void setTo(final Object to)
	{
		this.to = to;
	}
}
