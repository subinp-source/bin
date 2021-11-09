/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.filter.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.search.OrderBySorting
import de.hybris.platform.odata2services.filter.OrderByIntegrationKeyNotSupportedException
import de.hybris.platform.odata2services.filter.OrderByNestedAttributeNotSupportedException
import org.apache.olingo.odata2.api.uri.expression.SortOrder
import org.apache.olingo.odata2.core.uri.expression.OrderByExpressionImpl
import org.apache.olingo.odata2.core.uri.expression.OrderExpressionImpl
import org.junit.Test
import spock.lang.Specification

@UnitTest
class DefaultOrderByExpressionVisitorUnitTest extends Specification {

    def orderByExpressionVisitor = new DefaultOrderByExpressionVisitor()

    @Test
    def "order by expression visitor returns an ordered list of ordering attributes (e.g., orderBy and orderType)"() {
        given:
        def orderByPrice = Stub(OrderExpressionImpl) {
            getExpression() >> Stub(OrderExpressionImpl) {
                getUriLiteral() >> "price"
            }
            getSortOrder() >> SortOrder.asc
        }

        def orderByProductId = Stub(OrderExpressionImpl) {
            getExpression() >> Stub(OrderExpressionImpl) {
                getUriLiteral() >> "code"
            }
            getSortOrder() >> SortOrder.desc
        }

        def expression = Stub(OrderByExpressionImpl) {
            getOrders() >> [orderByPrice, orderByProductId]
        }

        when:
        def actualOrderExpressions = orderByExpressionVisitor.visit(expression, null, null)

        then:
        actualOrderExpressions.get(0).orderBy == "price"
        actualOrderExpressions.get(0).orderBySorting == OrderBySorting.ASC
        actualOrderExpressions.get(1).orderBy == "code"
        actualOrderExpressions.get(1).orderBySorting == OrderBySorting.DESC
    }

    @Test
    def "order by expression visitor throws an exception when ordering by nested attributes!"() {
        given:
        def expression = Stub(OrderByExpressionImpl) {
            getUriLiteral() >> "currency/isocode asc"
        }

        when:
        def actualOrderExpressions = orderByExpressionVisitor.visit(expression, null, null)

        then:
        OrderByNestedAttributeNotSupportedException ex = thrown()
        ex.message == "Ordering by nested attribute [currency/isocode asc] is not currently supported!"
    }

    @Test
    def "order by expression visitor throws an exception when ordering by integration key!"() {
        given:
        def expression = Stub(OrderByExpressionImpl) {
            getUriLiteral() >> "integrationKey asc"
        }

        when:
        def actualOrderExpressions = orderByExpressionVisitor.visit(expression, null, null)

        then:
        OrderByIntegrationKeyNotSupportedException ex = thrown()
        ex.message == "Ordering by integrationKey is not supported!"
    }

}
