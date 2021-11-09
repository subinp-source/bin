/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search

import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.core.model.type.RelationDescriptorModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ItemWhereClauseConditionUnitTest extends Specification {

    @Shared
    def attrName = "attrName"
    @Shared
    def attrValue = "attrVal"

    @Test
    @Unroll
    def "instance of ItemWhereClauseCondition with ItemModel parameter results in condition #actualCondition"() {
        given:
        def itemModel = Stub(IntegrationObjectItemModel) {
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> type
            }
        }

        when:
        ItemWhereClauseCondition whereCC = new ItemWhereClauseCondition(itemModel, createWhereClauseCondition(name, value, operator))

        then:
        with(whereCC) {
            attributeName == name
            attributeValue == value
            condition == actualCondition
            conjunctiveOperator == ConjunctiveOperator.UNKNOWN
        }

        where:
        type       | name     | value     | operator | actualCondition
        "ItemType" | attrName | attrValue | ""       | "{itemtype:$attrName} = $attrValue"
        ""         | ""       | ""        | null     | "{:} = "
        ""         | ""       | ""        | " IN "   | "{:} IN "
        ""         | ""       | ""        | " =  "   | "{:} = "
    }

    @Test
    @Unroll
    def "instance of ItemWhereClauseCondition with AttributeDescriptor and name parameters results in condition #actualCondition"() {
        given:
        def attributeDescriptor = Stub(RelationDescriptorModel) {
            getRelationName() >> relationName
        }

        when:
        ItemWhereClauseCondition whereCC = new ItemWhereClauseCondition(attributeDescriptor, name, createWhereClauseCondition(name, value, operator))

        then:
        with(whereCC) {
            attributeName == name
            attributeValue == value
            condition == actualCondition
            conjunctiveOperator == ConjunctiveOperator.UNKNOWN
        }

        where:
        relationName   | name     | value     | operator | actualCondition
        "RelationName" | attrName | attrValue | ""       | "{relationname:$attrName} = $attrValue"
        ""             | ""       | ""        | null     | "{:} = "
        ""             | ""       | ""        | " IN "   | "{:} IN "
        ""             | ""       | ""        | " =  "   | "{:} = "
    }

    def createWhereClauseCondition(String name, String val, String operator) {
        Stub(WhereClauseCondition) {
            getAttributeName() >> name
            getAttributeValue() >> val
            getCompareOperator() >> operator
            getConjunctiveOperator() >> ConjunctiveOperator.UNKNOWN
        }
    }
}
