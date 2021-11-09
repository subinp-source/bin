/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

public abstract class AbstractSnBucketsFacetResponse extends AbstractSnFacetResponse
{
	private SnFacetFilterMode filterMode;

	public SnFacetFilterMode getFilterMode()
	{
		return filterMode;
	}

	public void setFilterMode(final SnFacetFilterMode filterMode)
	{
		this.filterMode = filterMode;
	}
}
