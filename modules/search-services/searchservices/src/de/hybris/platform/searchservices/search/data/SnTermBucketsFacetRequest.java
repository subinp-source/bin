/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import java.util.List;


public class SnTermBucketsFacetRequest extends AbstractSnBucketsFacetRequest
{
	public static final String TYPE = "termBuckets";

	private Integer topBucketsSize;
	private List<String> promotedBucketIds;
	private List<String> excludedBucketIds;
	private SnBucketsSort sort;

	@Override
	public String getType()
	{
		return TYPE;
	}

	public Integer getTopBucketsSize()
	{
		return topBucketsSize;
	}

	public void setTopBucketsSize(final Integer topBucketsSize)
	{
		this.topBucketsSize = topBucketsSize;
	}

	public List<String> getPromotedBucketIds()
	{
		return promotedBucketIds;
	}

	public void setPromotedBucketIds(final List<String> promotedBucketIds)
	{
		this.promotedBucketIds = promotedBucketIds;
	}

	public List<String> getExcludedBucketIds()
	{
		return excludedBucketIds;
	}

	public void setExcludedBucketIds(final List<String> excludedBucketIds)
	{
		this.excludedBucketIds = excludedBucketIds;
	}

	public SnBucketsSort getSort()
	{
		return sort;
	}

	public void setSort(final SnBucketsSort sort)
	{
		this.sort = sort;
	}
}
