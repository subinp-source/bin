/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.filter.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.search.ItemSearchService
import de.hybris.platform.integrationservices.search.NavigationPropertyWithIntegrationKeyWhereClauseCondition
import de.hybris.platform.integrationservices.search.NoResultWhereClauseCondition
import de.hybris.platform.odata2services.filter.FilterProcessingException
import de.hybris.platform.odata2services.filter.IntegrationKeyNestedFilteringNotSupportedException
import de.hybris.platform.odata2services.odata.integrationkey.IntegrationKeyToODataEntryGenerator
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequestFactory
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
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
class NavigationPropertyWithIntegrationKeyVisitingStrategyUnitTest extends Specification {
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
    def context = Stub ODataContext
    @Shared
    def entitySet = Stub EdmEntitySet
    @Shared
    def unitKeyExpression = expression(MemberExpression, "unit", "integrationKey")

    def integrationKeyConverter = Stub(IntegrationKeyToODataEntryGenerator)
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
        convert(PROPERTY_ACCESS) >> 'PROPERTY_ACCESS'
    }
    def strategy = new NavigationPropertyWithIntegrationKeyVisitingStrategy(
            integrationKeyConverter: integrationKeyConverter,
            itemLookupRequestFactory: itemLookupRequestFactory,
            itemSearchService: itemSearchService,
            context: context,
            operatorConverter: operatorConverter)

    @Test
    def "empty condition is returned if supported operator is not '='"() {
        expect:
        strategy.visit(null, PROPERTY_ACCESS, null, null).conditions.empty
    }

    @Test
    @Unroll
    def "strategy isApplicable() == #applicable when left operand is of type #leftOpType, left is of type #leftType, and property is #property"() {
        expect:
        strategy.isApplicable(expression, null, left, null) == applicable

        where:
        expression                                             | leftOpType         | left                     | leftType             | property         | applicable
        unitKeyExpression                                      | "MemberExpression" | Mock(PropertyExpression) | "PropertyExpression" | "integrationKey" | false
        expression(MemberExpression, "unit", "version")        | "MemberExpression" | entitySet                | "EdmEntitySet"       | "version"        | false
        expression(BinaryExpression, "unit", "integrationKey") | "BinaryExpression" | entitySet                | 'EdmEntitySet'       | 'integrationKey' | false
    }

    @Test
    @Unroll
    def "strategy isApplicable() throws exception when comparing integrationKey with operator #operator"() {
        when:
        def rightResultDoesNotMatter = null
        strategy.isApplicable(unitKeyExpression, operator, entitySet, rightResultDoesNotMatter)

        then:
        IntegrationKeyNestedFilteringNotSupportedException ex = thrown()
        ex.message == "For nested attribute filtering at someObject/IntegrationKey, logical operators other than 'eq' are not supported."

        where:
        operator << [null, OPERATOR_LE, OPERATOR_LT, OPERATOR_GE, OPERATOR_GT, OPERATOR_NE, OPERATOR_AND, OPERATOR_OR]

    }

    @Test
    def "exception thrown during integration key generation"() {
        given:
        integrationKeyConverter.generate(entitySet, _ as String) >> {
            throw new EdmException(EdmException.PROPERTYNOTFOUND)
        }

        when:
        strategy.visit(unitKeyExpression, OPERATOR_EQ, entitySet, "key|value")

        then:
        thrown FilterProcessingException
    }

    @Test
    def "no result condition during visit"() {
        given:
        def entry = Stub(ODataEntry)
        integrationKeyConverter.generate(entitySet, _ as String) >> entry
        and:
        def request = Stub(ItemLookupRequest)
        itemLookupRequestFactory.create(context, entitySet, entry, _ as String) >> request
        and:
        itemSearchService.findUniqueItem(request) >> Optional.empty()

        when:
        def conditions = strategy.visit(unitKeyExpression, OPERATOR_EQ, entitySet, "key|value")

        then:
        conditions == new NoResultWhereClauseCondition().toWhereClauseConditions()
    }

    @Test
    def "query constructed correctly during visit strategy"() {
        given:
        def entry = Stub(ODataEntry)
        integrationKeyConverter.generate(entitySet, _ as String) >> entry
        and:
        def request = Stub(ItemLookupRequest)
        itemLookupRequestFactory.create(context, entitySet, entry, _ as String) >> request
        and:
        def foundItem = Stub(ItemModel) { getPk() >> PK.fromLong(1234L) }
        itemSearchService.findUniqueItem(request) >> Optional.of(foundItem)

        when:
        def conditions = strategy.visit(unitKeyExpression, OPERATOR_EQ, entitySet, "key|value")

        then:
        conditions.conditions == [new NavigationPropertyWithIntegrationKeyWhereClauseCondition('unit', '1234')]
    }

    def expression(Class operandType, path, property) {
        Stub(BinaryExpression) {
            getLeftOperand() >> Stub(operandType) {
                getProperty() >> Stub(PropertyExpression) {
                    getUriLiteral() >> property
                }
                getPath() >> Stub(PropertyExpression) {
                    getUriLiteral() >> path
                }
            }
        }
    }
}
