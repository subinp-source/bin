/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.filter;

import de.hybris.platform.integrationservices.search.OrderExpression;

import java.util.List;

import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;

/**
 * Acts as a delegate to visiting the {@link OrderByExpression}.
 * The intended usage of this interface is to call the visit method from the
 * {@link org.apache.olingo.odata2.api.uri.expression.ExpressionVisitor#visitOrderByExpression(OrderByExpression, String, List<Object>)}
 */
public interface OrderByExpressionVisitor
{
	/**
	 * Visit the {@link OrderByExpression}
	 *
	 * @param expression The visited orderby expression node
	 * @param expressionString The $orderby expression string used to build the orderby expression tree
	 * @param orders The result of visiting the orders of the orderby expression
	 * @return The resulting {@link List<OrderExpression>} from visiting the OrderByExpression
	 */
	List<OrderExpression> visit(OrderByExpression expression, String expressionString, List<Object> orders);
}



