/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.filter.impl;

import de.hybris.platform.integrationservices.constants.IntegrationservicesConstants;
import de.hybris.platform.integrationservices.search.OrderBySorting;
import de.hybris.platform.integrationservices.search.OrderExpression;
import de.hybris.platform.odata2services.filter.OrderByExpressionVisitor;
import de.hybris.platform.odata2services.filter.OrderByIntegrationKeyNotSupportedException;
import de.hybris.platform.odata2services.filter.OrderByNestedAttributeNotSupportedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;


/**
 * Default implementation of the {@link OrderByExpressionVisitor}
 */
public class DefaultOrderByExpressionVisitor implements OrderByExpressionVisitor
{

	@Override
	public List<OrderExpression> visit(final OrderByExpression expression,
	                                   final String expressionString,
	                                   final List<Object> orders)
	{
		if (expression.getUriLiteral().contains("/"))
		{
			throw new OrderByNestedAttributeNotSupportedException(expression.getUriLiteral());
		}

		if (expression.getUriLiteral().contains(IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME))
		{
			throw new OrderByIntegrationKeyNotSupportedException();
		}

		final List<OrderExpression> orderExpressions = new ArrayList<>();

		expression.getOrders().forEach(orderExpression -> orderExpressions.add(
				new OrderExpression(orderExpression.getExpression().getUriLiteral(),
						OrderBySorting.valueOf(orderExpression.getSortOrder().toString().toUpperCase(Locale.ENGLISH)))));

		return orderExpressions;
	}

}
