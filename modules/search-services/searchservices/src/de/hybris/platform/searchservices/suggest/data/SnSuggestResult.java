/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.data;

import de.hybris.platform.searchservices.util.JsonUtils;

import java.util.List;


/**
 * Represents a suggest result.
 */
public class SnSuggestResult
{
	private Integer size;
	private List<SnSuggestHit> suggestHits;

	public Integer getSize()
	{
		return size;
	}

	public void setSize(final Integer size)
	{
		this.size = size;
	}

	public List<SnSuggestHit> getSuggestHits()
	{
		return suggestHits;
	}

	public void setSuggestHits(final List<SnSuggestHit> suggestHits)
	{
		this.suggestHits = suggestHits;
	}

	@Override
	public String toString()
	{
		return JsonUtils.toJson(this);
	}
}
