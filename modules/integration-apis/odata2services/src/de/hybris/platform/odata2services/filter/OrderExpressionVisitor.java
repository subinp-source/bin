/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.filter;

import org.apache.olingo.odata2.api.uri.expression.OrderExpression;
import org.apache.olingo.odata2.api.uri.expression.SortOrder;

/**
 * Acts as a delegate to visiting the {@link OrderExpression}.
 * The intended usage of this interface is to call the visit method from the
 * {@link org.apache.olingo.odata2.api.uri.expression.ExpressionVisitor#visitOrder(OrderExpression, Object, SortOrder)}
 */
public interface OrderExpressionVisitor
{
	/**
	 * Visit the {@link OrderExpression}
	 *
	 * @param expression The visited order expression node
	 * @param filterResult The result of visiting the filter expression contained in the order
	 * @param sortOrder The sort order
	 * @return The visited order expression node
	 */
	Object visit(OrderExpression expression, Object filterResult, SortOrder sortOrder);

}


