/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.data;


public class SnLessThanQuery extends AbstractSnExpressionAndValueQuery
{
	public static final String TYPE = "lt";

	@Override
	public String getType()
	{
		return TYPE;
	}
}
