/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import java.util.List;


public class SnRangeBucketsFacetResponse extends AbstractSnBucketsFacetResponse
{
	public static final String TYPE = "rangeBuckets";

	private List<SnRangeBucketResponse> buckets;
	private List<SnRangeBucketResponse> selectedBuckets;

	@Override
	public String getType()
	{
		return TYPE;
	}

	public List<SnRangeBucketResponse> getBuckets()
	{
		return buckets;
	}

	public void setBuckets(final List<SnRangeBucketResponse> buckets)
	{
		this.buckets = buckets;
	}

	public List<SnRangeBucketResponse> getSelectedBuckets()
	{
		return selectedBuckets;
	}

	public void setSelectedBuckets(final List<SnRangeBucketResponse> selectedBuckets)
	{
		this.selectedBuckets = selectedBuckets;
	}
}
