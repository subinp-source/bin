/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import java.util.List;


public class SnPromotedHitsRankRule extends AbstractSnRankRule
{
	public static final String TYPE = "promotedHits";

	private List<SnPromotedHit> hits;

	@Override
	public String getType()
	{
		return TYPE;
	}

	public List<SnPromotedHit> getHits()
	{
		return hits;
	}

	public void setHits(final List<SnPromotedHit> hits)
	{
		this.hits = hits;
	}
}
