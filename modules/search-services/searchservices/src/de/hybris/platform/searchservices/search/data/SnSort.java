/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import de.hybris.platform.searchservices.util.JsonUtils;

import java.util.List;


public class SnSort
{
	private String id;
	private String name;
	private Boolean applyPromotedHits;
	private Boolean highlightPromotedHits;
	private List<SnSortExpression> expressions;

	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public Boolean getApplyPromotedHits()
	{
		return applyPromotedHits;
	}

	public void setApplyPromotedHits(final Boolean applyPromotedHits)
	{
		this.applyPromotedHits = applyPromotedHits;
	}

	public Boolean getHighlightPromotedHits()
	{
		return highlightPromotedHits;
	}

	public void setHighlightPromotedHits(final Boolean highlightPromotedHits)
	{
		this.highlightPromotedHits = highlightPromotedHits;
	}

	public List<SnSortExpression> getExpressions()
	{
		return expressions;
	}

	public void setExpressions(final List<SnSortExpression> expressions)
	{
		this.expressions = expressions;
	}

	@Override
	public String toString()
	{
		return JsonUtils.toJson(this);
	}
}
