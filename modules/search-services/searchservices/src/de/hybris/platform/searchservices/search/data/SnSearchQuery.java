/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import de.hybris.platform.searchservices.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a search query.
 */
public class SnSearchQuery
{
	private String query;
	private Integer offset;
	private Integer limit;
	private List<SnFilter> filters;
	private List<AbstractSnFacetRequest> facets;
	private List<AbstractSnFacetFilter> facetFilters;
	private List<AbstractSnRankRule> rankRules;
	private SnSort sort;
	private List<SnSort> availableSorts;
	private SnGroupRequest group;

	public String getQuery()
	{
		return query;
	}

	public void setQuery(final String query)
	{
		this.query = query;
	}

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

	public List<SnFilter> getFilters()
	{
		if (filters == null)
		{
			filters = new ArrayList<>();
		}

		return filters;
	}

	public void setFilters(final List<SnFilter> filters)
	{
		this.filters = filters;
	}

	public List<AbstractSnFacetRequest> getFacets()
	{
		if (facets == null)
		{
			facets = new ArrayList<>();
		}

		return facets;
	}

	public void setFacets(final List<AbstractSnFacetRequest> facets)
	{
		this.facets = facets;
	}

	public List<AbstractSnFacetFilter> getFacetFilters()
	{
		if (facetFilters == null)
		{
			facetFilters = new ArrayList<>();
		}

		return facetFilters;
	}

	public void setFacetFilters(final List<AbstractSnFacetFilter> facetFilters)
	{
		this.facetFilters = facetFilters;
	}

	public List<AbstractSnRankRule> getRankRules()
	{
		if (rankRules == null)
		{
			rankRules = new ArrayList<>();
		}

		return rankRules;
	}

	public void setRankRules(final List<AbstractSnRankRule> rankRules)
	{
		this.rankRules = rankRules;
	}

	public SnSort getSort()
	{
		return sort;
	}

	public void setSort(final SnSort sort)
	{
		this.sort = sort;
	}

	public List<SnSort> getAvailableSorts()
	{
		if (availableSorts == null)
		{
			availableSorts = new ArrayList<>();
		}

		return availableSorts;
	}

	public void setAvailableSorts(final List<SnSort> availableSorts)
	{
		this.availableSorts = availableSorts;
	}

	public SnGroupRequest getGroup()
	{
		return group;
	}

	public void setGroup(final SnGroupRequest group)
	{
		this.group = group;
	}

	@Override
	public String toString()
	{
		return JsonUtils.toJson(this);
	}
}
