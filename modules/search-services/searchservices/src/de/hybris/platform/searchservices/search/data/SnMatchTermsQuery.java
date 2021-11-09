/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;


public class SnMatchTermsQuery extends AbstractSnExpressionAndValuesQuery
{
	public static final String TYPE = "matchTerms";

	private SnMatchType matchType;

	@Override
	public String getType()
	{
		return TYPE;
	}

	public SnMatchType getMatchType()
	{
		return matchType;
	}

	public void setMatchType(final SnMatchType matchType)
	{
		this.matchType = matchType;
	}
}
