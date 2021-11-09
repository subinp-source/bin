/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import de.hybris.platform.searchservices.util.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents a search result.
 */
public class SnSearchResult
{
	private Integer offset;
	private Integer limit;
	private Integer size;
	private Integer totalSize;
	private List<SnSearchHit> searchHits;
	private List<AbstractSnFacetResponse> facets;
	private SnNamedSort sort;
	private List<SnNamedSort> availableSorts;

	private final Map<String, Object> debug = new HashMap<>();

	public Integer getOffset()
	{
		return offset;
	}

	public void setOffset(final Integer offset)
	{
		this.offset = offset;
	}

	public Integer getLimit()
	{
		return limit;
	}

	public void setLimit(final Integer limit)
	{
		this.limit = limit;
	}

	public Integer getSize()
	{
		return size;
	}

	public void setSize(final Integer size)
	{
		this.size = size;
	}

	public Integer getTotalSize()
	{
		return totalSize;
	}

	public void setTotalSize(final Integer totalSize)
	{
		this.totalSize = totalSize;
	}

	public List<SnSearchHit> getSearchHits()
	{
		if (searchHits == null)
		{
			searchHits = new ArrayList<>();
		}

		return searchHits;
	}

	public void setSearchHits(final List<SnSearchHit> searchHits)
	{
		this.searchHits = searchHits;
	}

	public List<AbstractSnFacetResponse> getFacets()
	{
		if (facets == null)
		{
			facets = new ArrayList<>();
		}

		return facets;
	}

	public void setFacets(final List<AbstractSnFacetResponse> facets)
	{
		this.facets = facets;
	}

	public SnNamedSort getSort()
	{
		return sort;
	}

	public void setSort(final SnNamedSort sort)
	{
		this.sort = sort;
	}

	public List<SnNamedSort> getAvailableSorts()
	{
		if (availableSorts == null)
		{
			availableSorts = new ArrayList<>();
		}

		return availableSorts;
	}

	public void setAvailableSorts(final List<SnNamedSort> availableSorts)
	{
		this.availableSorts = availableSorts;
	}

	public Map<String, Object> getDebug()
	{
		return debug;
	}

	@Override
	public String toString()
	{
		return JsonUtils.toJson(this);
	}
}
