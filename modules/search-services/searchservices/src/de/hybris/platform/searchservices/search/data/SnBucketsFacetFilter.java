/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import java.util.List;


public class SnBucketsFacetFilter extends AbstractSnFacetFilter
{
	public static final String TYPE = "buckets";

	private List<String> bucketIds;

	@Override
	public String getType()
	{
		return TYPE;
	}

	public List<String> getBucketIds()
	{
		return bucketIds;
	}

	public void setBucketIds(final List<String> bucketIds)
	{
		this.bucketIds = bucketIds;
	}
}
