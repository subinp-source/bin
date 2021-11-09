/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import java.util.List;


public class SnTermBucketsFacetResponse extends AbstractSnBucketsFacetResponse
{
	public static final String TYPE = "termBuckets";

	private List<SnTermBucketResponse> topBuckets;
	private List<SnTermBucketResponse> buckets;
	private List<SnTermBucketResponse> selectedBuckets;

	@Override
	public String getType()
	{
		return TYPE;
	}

	public List<SnTermBucketResponse> getTopBuckets()
	{
		return topBuckets;
	}

	public void setTopBuckets(final List<SnTermBucketResponse> topBuckets)
	{
		this.topBuckets = topBuckets;
	}

	public List<SnTermBucketResponse> getBuckets()
	{
		return buckets;
	}

	public void setBuckets(final List<SnTermBucketResponse> buckets)
	{
		this.buckets = buckets;
	}

	public List<SnTermBucketResponse> getSelectedBuckets()
	{
		return selectedBuckets;
	}

	public void setSelectedBuckets(final List<SnTermBucketResponse> selectedBuckets)
	{
		this.selectedBuckets = selectedBuckets;
	}
}
