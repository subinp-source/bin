/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import de.hybris.platform.searchservices.util.JsonUtils;


public abstract class AbstractSnFacetFilter
{
	private String facetId;

	public abstract String getType();

	public String getFacetId()
	{
		return facetId;
	}

	public void setFacetId(final String facetId)
	{
		this.facetId = facetId;
	}

	@Override
	public String toString()
	{
		return JsonUtils.toJson(this);
	}
}
