/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class SimplePropertyWhereClauseConditionUnitTest extends Specification {
    @Test
    @Unroll
    def "extract attribute #conditionAttrValue and value from condition"() {
        given:
        def whereCC = new SimplePropertyWhereClauseCondition(actualAttrName, actualCompareOperator, actualAttrValue)

        expect:
        with(whereCC) {
            attributeName == actualAttrName
            condition == "{$actualAttrName} $actualCompareOperator '$actualAttrValue'"
            attributeValue == conditionAttrValue
            conjunctiveOperator == ConjunctiveOperator.UNKNOWN
        }

        where:
        actualAttrName | actualCompareOperator | actualAttrValue | conditionAttrValue
        "attrName"     | "="                   | "attrValue"     | "'attrValue'"
        ""             | "IN"                  | ""              | "''"
    }

    @Test
    @Unroll
    def 'eq() creates = condition for an attribute value'() {
        given:
        def attributeName = 'attribute'
        def attributeValue = 'value'
        def condition = SimplePropertyWhereClauseCondition.eq(attributeName, attributeValue)

        expect:
        condition.attributeName == attributeName
        condition.compareOperator == '='
        condition.attributeValue == "'$attributeValue'"
        condition.conjunctiveOperator == ConjunctiveOperator.UNKNOWN
    }
}
