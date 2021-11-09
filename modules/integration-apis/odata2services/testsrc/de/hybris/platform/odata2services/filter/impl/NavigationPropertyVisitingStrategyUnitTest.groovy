/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.filter.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.search.ItemSearchResult
import de.hybris.platform.integrationservices.search.ItemSearchService
import de.hybris.platform.integrationservices.search.NavigationPropertyWhereClauseCondition
import de.hybris.platform.integrationservices.search.NoResultWhereClauseCondition
import de.hybris.platform.integrationservices.search.SimplePropertyWhereClauseCondition
import de.hybris.platform.integrationservices.search.WhereClauseConditions
import de.hybris.platform.odata2services.filter.FilterProcessingException
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequestFactory
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.uri.expression.BinaryExpression
import org.apache.olingo.odata2.api.uri.expression.BinaryOperator
import org.apache.olingo.odata2.api.uri.expression.MemberExpression
import org.apache.olingo.odata2.api.uri.expression.PropertyExpression
import org.junit.Test
import org.springframework.core.convert.converter.Converter
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class NavigationPropertyVisitingStrategyUnitTest extends Specification {
    private static final def OPERATOR_EQ = BinaryOperator.EQ
    private static final def OPERATOR_NE = BinaryOperator.NE
    private static final def OPERATOR_LT = BinaryOperator.LT
    private static final def OPERATOR_LE = BinaryOperator.LE
    private static final def OPERATOR_GT = BinaryOperator.GT
    private static final def OPERATOR_GE = BinaryOperator.GE
    private static final def OPERATOR_AND = BinaryOperator.AND
    private static final def OPERATOR_OR = BinaryOperator.OR

    private static final def PROPERTY_ACCESS = BinaryOperator.PROPERTY_ACCESS

    @Shared
    def context = Stub(ODataContext)

    def itemLookupRequestFactory = Stub(ItemLookupRequestFactory)
    def itemSearchService = Stub(ItemSearchService)
    def operatorConverter = Stub(Converter) {
        convert(OPERATOR_EQ) >> "="
        convert(OPERATOR_NE) >> "<>"
        convert(OPERATOR_LT) >> "<"
        convert(OPERATOR_LE) >> "<="
        convert(OPERATOR_GT) >> ">"
        convert(OPERATOR_GE) >> ">="
        convert(OPERATOR_AND) >> "AND"
        convert(PROPERTY_ACCESS) >> "PROPERTY_ACCESS"
    }
    def strategy = new NavigationPropertyVisitingStrategy(
            context: context,
            itemLookupRequestFactory: itemLookupRequestFactory,
            itemSearchService: itemSearchService,
            operatorConverter: operatorConverter)

    @Test
    def "empty condition is returned if supported operator is not '='"() {
        expect:
        strategy.visit(null, PROPERTY_ACCESS, null, null).conditions.empty
    }

    @Test
    @Unroll
    def "strategy isApplicable() == #applicable when left operand is of type #leftOpType, left is of type #leftType, and property is #property"() {
        given:
        def operatorDoesNotMatter = null
        def rightResultDoesNotMatter = null

        expect:
        strategy.isApplicable(expression, operatorDoesNotMatter, left, rightResultDoesNotMatter) == applicable

        where:
        expression                                           | leftOpType         | left                     | leftType             | property         | applicable
        binaryExpression(MemberExpression, "version")        | "MemberExpression" | Stub(EdmEntitySet)       | "EdmEntitySet"       | "version"        | true
        binaryExpression(MemberExpression, "version")        | "MemberExpression" | Stub(PropertyExpression) | "PropertyExpression" | "version"        | false
        binaryExpression(MemberExpression, "integrationKey") | "MemberExpression" | Stub(EdmEntitySet)       | "EdmEntitySet"       | "integrationKey" | false
        binaryExpression(BinaryExpression, "version")        | "BinaryExpression" | Stub(EdmEntitySet)       | "EdmEntitySet"       | "version"        | false
    }

    @Test
    @Unroll
    def "NO_RESULT where clause condition is returned when no items found during visit."() {
        given:
        def attributeName = 'version'
        def attributeValue = 'Does not matter'
        def entitySet = Stub EdmEntitySet
        and:
        def request = Stub(ItemLookupRequest)
        itemLookupRequestFactory.createWithFilter(context, entitySet, asFilter(attributeName, attributeValue)) >> request
        and:
        itemSearchService.findItems(request) >> Stub(ItemSearchResult)
        and:
        def expression = binaryExpression(MemberExpression, attributeName)

        expect:
        strategy.visit(expression, operator, entitySet, null).conditions == result

        where:
        operator    | result
        OPERATOR_EQ | [new NoResultWhereClauseCondition()]
        OPERATOR_LE | [new NoResultWhereClauseCondition()]
        OPERATOR_LT | [new NoResultWhereClauseCondition()]
        OPERATOR_GE | [new NoResultWhereClauseCondition()]
        OPERATOR_GT | [new NoResultWhereClauseCondition()]
        OPERATOR_NE | [new NoResultWhereClauseCondition()]
    }

    @Test
    def "An empty list is returned when operator is not supported."() {
        given:
        def attributeName = 'version'
        def entitySet = Stub EdmEntitySet
        def expression = binaryExpression(MemberExpression, attributeName)

        expect:
        strategy.visit(expression, OPERATOR_AND, entitySet, null).conditions == []
    }

    @Test
    def "where clause condition is returned when items are found during visit"() {
        given:
        def attribute = 'version'
        def attributeValue = 'Staged'
        def entitySet = Stub EdmEntitySet
        and:
        def request = Stub ItemLookupRequest
        itemLookupRequestFactory.createWithFilter(context, entitySet, asFilter(attribute, attributeValue)) >> request
        and:
        itemSearchService.findItems(request) >> Stub(ItemSearchResult) {
            getTotalCount() >> Optional.of(2)
            getItems() >> [itemModel(1234), itemModel(5678)]
        }
        and:
        def expression = binaryExpression(MemberExpression, attribute)

        when:
        def result = strategy.visit(expression, OPERATOR_EQ, entitySet, attributeValue)

        then:
        result.conditions == [new NavigationPropertyWhereClauseCondition('catalogVersion', "1234,5678")]
    }

    @Test
    def "exception is thrown while visiting"() {
        given:
        def expression = binaryExpression(MemberExpression, 'version')
        def left = Stub EdmEntitySet
        itemLookupRequestFactory.createWithFilter(context, left, _ as WhereClauseConditions) >> {
            throw Stub(EdmException)
        }

        when:
        strategy.visit(expression, OPERATOR_EQ, left, null)

        then:
        thrown FilterProcessingException
    }

    def binaryExpression(Class operandType, property) {
        Stub(BinaryExpression) {
            getLeftOperand() >> Stub(operandType) {
                getProperty() >> Stub(PropertyExpression) {
                    getUriLiteral() >> property
                }
                getPath() >> Stub(PropertyExpression) {
                    getUriLiteral() >> "catalogVersion"
                }
            }
        }
    }

    def itemModel(long pk) {
        Stub(ItemModel) {
            getPk() >> PK.fromLong(pk)
        }
    }

    private static WhereClauseConditions asFilter(String attr, String value) {
        new WhereClauseConditions(SimplePropertyWhereClauseCondition.eq(attr, value))
    }
}
