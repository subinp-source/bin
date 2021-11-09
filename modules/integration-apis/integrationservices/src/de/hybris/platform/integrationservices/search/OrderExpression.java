/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search;

/**
 * Represents the OData order by attribute, e.g., for the query option "$orderby=price asc" the orderBy = price and orderType = asc.
 */
public final class OrderExpression
{
	private final String orderBy;
	private final OrderBySorting orderBySorting;

	public OrderExpression(final String orderBy, final OrderBySorting orderBySorting)
	{
		this.orderBy = orderBy;
		this.orderBySorting = orderBySorting;
	}

	public String getOrderBy()
	{
		return orderBy;
	}

	public OrderBySorting getOrderBySorting()
	{
		return orderBySorting;
	}

}
