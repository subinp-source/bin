/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.filter.impl;

import de.hybris.platform.odata2services.filter.OrderExpressionVisitor;

import org.apache.olingo.odata2.api.uri.expression.OrderExpression;
import org.apache.olingo.odata2.api.uri.expression.SortOrder;

/**
 * Default implementation of the {@link OrderExpressionVisitor}
 */
public class DefaultOrderExpressionVisitor implements OrderExpressionVisitor
{
	@Override
	public Object visit(final OrderExpression expression, final Object filterResult, final SortOrder sortOrder)
	{
		return expression;
	}
}
