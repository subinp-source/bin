/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import java.util.List;


public class SnRangeBucketsFacetRequest extends AbstractSnBucketsFacetRequest
{
	public static final String TYPE = "rangeBuckets";

	private Boolean includeFrom;
	private Boolean includeTo;
	private List<SnRangeBucketRequest> buckets;

	@Override
	public String getType()
	{
		return TYPE;
	}

	public Boolean getIncludeFrom()
	{
		return includeFrom;
	}

	public void setIncludeFrom(final Boolean includeFrom)
	{
		this.includeFrom = includeFrom;
	}

	public Boolean getIncludeTo()
	{
		return includeTo;
	}

	public void setIncludeTo(final Boolean includeTo)
	{
		this.includeTo = includeTo;
	}

	public List<SnRangeBucketRequest> getBuckets()
	{
		return buckets;
	}

	public void setBuckets(final List<SnRangeBucketRequest> buckets)
	{
		this.buckets = buckets;
	}
}
