/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;


public class SnMatchQuery extends AbstractSnExpressionAndValueQuery
{
	public static final String TYPE = "match";

	@Override
	public String getType()
	{
		return TYPE;
	}
}
