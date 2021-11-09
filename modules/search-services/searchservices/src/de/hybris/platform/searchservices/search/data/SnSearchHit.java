/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import de.hybris.platform.searchservices.util.JsonUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class SnSearchHit
{
	private String id;
	private Float score;
	private Map<String, Object> fields;
	private Set<String> tags;
	private List<SnSearchHit> innerHits;

	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public Float getScore()
	{
		return score;
	}

	public void setScore(final Float score)
	{
		this.score = score;
	}

	public Map<String, Object> getFields()
	{
		return fields;
	}

	public void setFields(final Map<String, Object> fields)
	{
		this.fields = fields;
	}

	public Set<String> getTags()
	{
		return tags;
	}

	public void setTags(final Set<String> tags)
	{
		this.tags = tags;
	}

	public List<SnSearchHit> getInnerHits()
	{
		return innerHits;
	}

	public void setInnerHits(final List<SnSearchHit> innerHits)
	{
		this.innerHits = innerHits;
	}

	@Override
	public String toString()
	{
		return JsonUtils.toJson(this);
	}
}
