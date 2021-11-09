/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;


public class SnNotEqualQuery extends AbstractSnExpressionAndValueQuery
{
	public static final String TYPE = "ne";

	@Override
	public String getType()
	{
		return TYPE;
	}
}
