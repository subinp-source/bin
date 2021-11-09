/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;

import de.hybris.platform.searchservices.util.JsonUtils;


/**
 * Rules for changing the score/rank of search hits.
 */
public abstract class AbstractSnRankRule
{
	public abstract String getType();

	@Override
	public String toString()
	{
		return JsonUtils.toJson(this);
	}
}
